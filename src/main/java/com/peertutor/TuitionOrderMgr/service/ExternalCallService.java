package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.service.dto.StudentDTO;
import com.peertutor.TuitionOrderMgr.service.dto.TutorCriteria;
import com.peertutor.TuitionOrderMgr.service.dto.TutorDTO;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExternalCallService{

    @Autowired
    AppConfig appConfig;
    @Autowired
    TutorService tutorService;
    @Autowired
    StudentService studentService;
    @Autowired
    AuthService authService;

    public List<TutorDTO> getAllTutorName(String name, String sessionToken) {

        boolean result = authService.getAuthentication(name, sessionToken);
        if (!result) {
            return null;
        }

        TutorDTO tutorProfile = tutorService.getTutorProfileByAccountName(name);
        if (tutorProfile != null) {
            TutorCriteria criteria = new TutorCriteria(
                    Optional.ofNullable(tutorProfile.getDisplayName()),
                    Optional.ofNullable(tutorProfile.getSubjects()),
                    Optional.ofNullable(tutorProfile.getIntroduction()),
                    Optional.ofNullable(tutorProfile.getCertificates()));
            List<TutorDTO> tutorList = tutorService.getTutorByCriteria(criteria);
            return tutorList.stream().map(tutorDTO -> new TutorDTO(
                    tutorDTO.getId(),
                    tutorDTO.getAccountName(),
                    tutorDTO.getDisplayName(),
                    tutorDTO.getIntroduction(),
                    tutorDTO.getSubjects(),
                    tutorDTO.getCertificates()
            )).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<StudentDTO> getAllStudentName(String name, String sessionToken) {
        boolean result = authService.getAuthentication(name, sessionToken);
        if (!result) {
            return null;
        }

        return studentService.getAllStudents();
    }
}
