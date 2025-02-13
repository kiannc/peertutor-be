package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.model.TutorCalendar;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.TutorCalendarReq;
import com.peertutor.TuitionOrderMgr.model.viewmodel.response.TutorCalendarRes;
import com.peertutor.TuitionOrderMgr.repository.TutorCalendarRepository;
import com.peertutor.TuitionOrderMgr.service.mapper.TutorCalendarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class TutorCalendarService {

    private static final Logger logger = LoggerFactory.getLogger(TutorCalendarService.class);
    private final TutorCalendarMapper tutorCalendarMapper;
    private TutorCalendarRepository tutorCalendarRepository;

    public TutorCalendarService(TutorCalendarRepository tutorCalendarRepository, TutorCalendarMapper tutorCalendarMapper) {
        this.tutorCalendarRepository = tutorCalendarRepository;
        this.tutorCalendarMapper = tutorCalendarMapper;
    }

    public List<TutorCalendar> addAvailableDate(TutorCalendarReq req) {
        Date currentDate = Date.valueOf(LocalDate.now());
        tutorCalendarRepository.deleteAllByTutorIdAndAvailableDateGreaterThan(req.tutorId, currentDate);
        tutorCalendarRepository.deleteAllByTutorIdAndAvailableDate(req.tutorId, currentDate);

        req.availableDates
                .stream()
                .filter(date -> date.after(currentDate) || date.equals(currentDate))
                .limit(50)
                .forEach(date -> {
                        TutorCalendar tutorCalendar = new TutorCalendar();
                        tutorCalendar.setTutorId(req.tutorId);
                        tutorCalendar.setAvailableDate(date);

                        try {
                            tutorCalendar = tutorCalendarRepository.save(tutorCalendar);
                        } catch (Exception e) {
                            logger.error("Add available time slot fail: " + e.getMessage());
                        }
                });

        return tutorCalendarRepository.findAllByTutorId(req.tutorId);
    }

    public TutorCalendarRes getTutorCalendar(Long tutorID) {
        List<TutorCalendar> tutorCalendar = tutorCalendarRepository.findAllByTutorId(tutorID);

        List<Date> dates = tutorCalendar.stream().map(c -> c.getAvailableDate()).collect(Collectors.toList());
        Collections.sort(dates);
        List<String> result = dates.stream().map(date -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }).collect(Collectors.toList());
        return new TutorCalendarRes(result);
    }

    @Transactional
    public void deleteAvailableDates(Long tutorID, String dates) {
        List<TutorCalendar> tutorCalendar = tutorCalendarRepository.findAllByTutorId(tutorID);

        List<Date> result = Arrays.asList(dates.replaceAll("[()\\[\\]]", "").split(",")).stream().map(
                date -> {
                    try {
                        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                        return new Date(utilDate.getTime());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());

        tutorCalendarRepository.deleteAllByTutorIdAndAvailableDateIn(tutorID, result);
    }
}
