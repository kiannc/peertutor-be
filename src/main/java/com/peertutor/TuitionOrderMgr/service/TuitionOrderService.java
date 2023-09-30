package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.exception.ExistingTuitionOrderException;
import com.peertutor.TuitionOrderMgr.model.TuitionOrder;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.TuitionOrderReq;
import com.peertutor.TuitionOrderMgr.repository.TuitionOrderRepository;
import com.peertutor.TuitionOrderMgr.service.dto.*;
import com.peertutor.TuitionOrderMgr.service.mapper.TuitionOrderMapper;
import io.github.jhipster.service.filter.IntegerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TuitionOrderService {

    private static final Logger logger = LoggerFactory.getLogger(TuitionOrder.class);
    @Autowired
    private final TuitionOrderMapper tuitionOrderMapper;
    @Autowired
    private TuitionOrderRepository tuitionOrderRepository;
    @Autowired
    private TuitionOrderQueryService tuitionOrderQueryService;
    @Autowired
    private TutorCalendarService tutorCalendarService;
    @Autowired
    private TutorService tutorService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ExternalCallService externalCallService;

    public TuitionOrderService(TuitionOrderRepository tuitionOrderRepository, TuitionOrderMapper tuitionOrderMapper) {
        this.tuitionOrderRepository = tuitionOrderRepository;
        this.tuitionOrderMapper = tuitionOrderMapper;
    }

    public List<TuitionOrderDTO> getAllTuitionOrder() {
        List<TuitionOrder> orders = tuitionOrderRepository.findAll();

        return orders.stream().map(tuitionOrderMapper::toDto).collect(Collectors.toList());

    }

    public Page<TuitionOrderDetailedDTO> getTuitionOrderDetailsByCriteria(TuitionOrderCriteria criteria, Pageable pageable) {
        Page<TuitionOrderDTO> page = tuitionOrderQueryService.findByCriteria(criteria, pageable);
        updatePastTuitionOrderStatus();
        Page<TuitionOrderDetailedDTO> result = page.map(order -> {
            TuitionOrderDetailedDTO newOrder = new TuitionOrderDetailedDTO();
            newOrder.setId(order.getId());
            newOrder.setTutorId(order.getTutorId());
            newOrder.setTutorName(tutorService.getTutorProfileById(order.getTutorId()).getDisplayName());
            newOrder.setStudentId(order.getStudentId());
            newOrder.setStudentName(studentService.getStudentProfileById(order.getStudentId()).getDisplayName());
            newOrder.setSelectedDates(order.getSelectedDates());
            newOrder.setStatus(order.getStatus());
            return newOrder;
        });

        return result;
    }

    public Page<TuitionOrderDTO> getTuitionOrderByCriteria(TuitionOrderCriteria criteria, Pageable pageable) {
        Page<TuitionOrderDTO> page = tuitionOrderQueryService.findByCriteria(criteria, pageable);
        return page;
    }

    public TuitionOrderDTO createTuitionOrder(TuitionOrderReq req) throws ExistingTuitionOrderException {
        TuitionOrder tuitionOrder = new TuitionOrder();

        if (req.id != null) {
            tuitionOrder = tuitionOrderRepository.findById(req.id).orElse(new TuitionOrder());
        } else {
            // Detecting Overlapping tuition Order
            updatePastTuitionOrderStatus();
            TuitionOrderCriteria criteria = new TuitionOrderCriteria(req.studentId, req.tutorId, 1);
            criteria.setStatus((IntegerFilter) new IntegerFilter().setIn(Arrays.asList(0, 1)));
            List<TuitionOrder> unfinishedTuitionOrders = tuitionOrderQueryService.findByCriteriaWithoutPage(criteria);
            if (!unfinishedTuitionOrders.isEmpty()) {
                throw new ExistingTuitionOrderException("You have an existing tuition order with the tutor");
            }

            // can only create tuition order with pending status
            tuitionOrder.setStatus(0);
        }

        if (req.status != null && req.id != null) {
            tuitionOrder.setStatus(req.status);
        }

        if (req.studentId != null) {
            tuitionOrder.setStudentId(req.studentId);
        }

        if (req.tutorId != null) {
            tuitionOrder.setTutorId(req.tutorId);
        }

        List<Date> availableDates = tutorCalendarService.getTutorCalendar(req.tutorId).availableDate;
        if (req.selectedDates != null) {
            Collections.sort(req.selectedDates);
            String selectedDates = String.join(";", req.selectedDates.toString());

            if (req.status != null && req.status != 2) {
                List<LocalDate> aDate = availableDates.stream()
                        .map(Date::toLocalDate)
                        .collect(Collectors.toList());
                List<LocalDate> sDate = req.selectedDates.stream()
                        .map(Date::toLocalDate)
                        .collect(Collectors.toList());

                if (!aDate.containsAll(sDate)) {
                    logger.info("Tutor is not available on selected dates");
                    return null;
                } else {
                    logger.info("Tutor is available on selected dates");
                }
            }

            tuitionOrder.setSelectedDates(selectedDates);
        }


        try {
            tuitionOrder = tuitionOrderRepository.save(tuitionOrder);
        } catch (Exception e) {
            logger.error("TuitionOrder Profile Creation Failed: " + e.getMessage());
            return null;
        } finally {
            if (req.id != null && req.status == 1 & req.selectedDates != null) {
                String selectedDates = String.join(";", req.selectedDates.toString());
                removeConflictTuitionOrder(req.selectedDates, req.tutorId);
                tutorCalendarService.deleteAvailableDates(req.tutorId, selectedDates);
            }
        }

        TuitionOrderDTO result = tuitionOrderMapper.toDto(tuitionOrder);

        return result;
    }

    public TuitionOrderDTO getTuitionOrderById(Long id) {
        Optional<TuitionOrder> tuitionOrder = tuitionOrderRepository.findById(id);

        if (!tuitionOrder.isPresent()) {
            return null;
        }
        TuitionOrderDTO result = tuitionOrderMapper.toDto(tuitionOrder.get());

        return result;
    }

    public void updatePastTuitionOrderStatus() {
        List<TuitionOrder> tuitionOrder = tuitionOrderRepository.findByStatus(1);
        tuitionOrder.stream().filter(order -> {
                    boolean containFutureDays = Arrays
                            .stream(
                                    order.getSelectedDates()
                                            .replaceAll("\\[", "")
                                            .replaceAll("\\]", "")
                                            .split(","))
                            .map(date -> LocalDate.parse(date.trim())).anyMatch(date -> {
                                LocalDate now = LocalDate.now();
                                boolean test = now.isAfter(date);
                                return now.isBefore(date) && !now.isEqual(date);
                            });
                    return !containFutureDays && order.getStatus() == 1;
                })
                .forEach(order -> {
                    try {
                        order.setStatus(3);
                        tuitionOrderRepository.save(order);
                    } catch (Exception e) {
                        logger.error("TuitionOrder Profile Update Failed: " + e.getMessage());
                    }
                });
    }

    public void removeConflictTuitionOrder(List<Date> selectedDates, Long tutorId) {
        List<String> dates = selectedDates.stream().map(date -> {
            return date.toString();
        }).collect(Collectors.toList());

        dates.forEach(date -> {
            List<TuitionOrder> orders =
                    tuitionOrderRepository.findBySelectedDatesContainingAndStatusAndTutorId(date, 0, tutorId);
            orders.forEach(order -> {
                try {
                    order.setStatus(2);
                    tuitionOrderRepository.save(order);
                } catch (Exception e) {
                    logger.error("TuitionOrder Profile Update Failed: " + e.getMessage());
                }
            });
        });
    }

}
