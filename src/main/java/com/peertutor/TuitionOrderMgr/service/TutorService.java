package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.model.Tutor;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.TutorProfileReq;
import com.peertutor.TuitionOrderMgr.model.viewmodel.response.TutorProfileRes;
import com.peertutor.TuitionOrderMgr.repository.TutorRepository;
import com.peertutor.TuitionOrderMgr.service.dto.BookmarkDTO;
import com.peertutor.TuitionOrderMgr.service.dto.TutorCriteria;
import com.peertutor.TuitionOrderMgr.service.dto.TutorDTO;
import com.peertutor.TuitionOrderMgr.service.mapper.TutorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TutorService {
    private static final Logger logger = LoggerFactory.getLogger(TutorService.class);
    @Autowired
    private final TutorMapper tutorMapper;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private TutorQueryService tutorQueryService;
    @Autowired
    private BookmarkService bookmarkService;

    public TutorService(TutorRepository tutorRepository, TutorMapper tutorMapper) {
        this.tutorRepository = tutorRepository;
        this.tutorMapper = tutorMapper;
    }

    public TutorDTO getTutorProfileByAccountName(String accountName) {
        Tutor tutor = tutorRepository.findByAccountName(accountName);

        if (tutor == null) {
            return null;
        }
        TutorDTO result = tutorMapper.toDto(tutor);

        return result;
    }

    public TutorDTO getTutorProfileById(Long id) {
        Optional<Tutor> tutor = tutorRepository.findById(id);

        if (!tutor.isPresent()) {
            return null;
        }
        TutorDTO result = tutorMapper.toDto(tutor.get());

        return result;
    }

    public TutorDTO createTutorProfile(TutorProfileReq req) {
        Tutor tutor = tutorRepository.findByAccountName(req.name);

        if (tutor == null) {
            tutor = new Tutor();
            tutor.setAccountName(req.name);
        }

        if (req.displayName != null && !req.displayName.trim().isEmpty()) {
            tutor.setDisplayName(req.displayName);
        } else {
            tutor.setDisplayName(req.name);
        }

        tutor.setIntroduction(req.introduction);
        tutor.setSubjects(req.subjects);
        tutor.setCertificates(req.certificates);

        try {
            tutor = tutorRepository.save(tutor);
        } catch (Exception e) {
            logger.error("Tutor Profile Creation Failed: " + e.getMessage());
            return null;
        }

        TutorDTO result = tutorMapper.toDto(tutor);

        return result;
    }

    public List<TutorDTO> getTutorByCriteria(TutorCriteria criteria) {
        List<TutorDTO> tutorList = tutorQueryService.findByCriteria(criteria);
        return tutorList;
    }
    public Page<TutorDTO> getTutorByCriteria(TutorCriteria criteria, Pageable pageable, Optional<Long> studentId) {
        Page<TutorDTO> tutorList = tutorQueryService.findByCriteria(criteria, pageable);

        if (studentId.isPresent()) {
            tutorList = tutorList.map(tutorDTO -> {
                BookmarkDTO isBookMarked = bookmarkService.getBookmark(studentId.get(), tutorDTO.getId());
                tutorDTO.setBookmarked(isBookMarked != null);
                return tutorDTO;
            });
        }

        return tutorList;
    }
}
