package com.peertutor.TuitionOrderMgr.controller;

import com.peertutor.TuitionOrderMgr.model.viewmodel.request.StudentProfileReq;
import com.peertutor.TuitionOrderMgr.model.viewmodel.response.StudentProfileRes;
import com.peertutor.TuitionOrderMgr.repository.StudentRepository;
import com.peertutor.TuitionOrderMgr.service.AuthService;
import com.peertutor.TuitionOrderMgr.service.StudentService;
import com.peertutor.TuitionOrderMgr.service.dto.StudentDTO;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/student-mgr")
public class StudentController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private StudentRepository studentRepository;// = new CustomerRepository();
    @Autowired
    private StudentService studentService;
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/health")
    public @ResponseBody String healthCheck() {
        return "Ok 3";
    }

    @PostMapping(path = "/student")
    public @ResponseBody ResponseEntity<StudentProfileRes> createStudentProfile(@RequestBody @Valid StudentProfileReq req) {
        boolean result = authService.getAuthentication(req.name, req.sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        StudentDTO savedUser;

        savedUser = studentService.createStudentProfile(req);

        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        StudentProfileRes res = new StudentProfileRes();
        res.displayName = savedUser.getDisplayName();
        res.introduction = savedUser.getIntroduction();
        res.subjects = savedUser.getSubjects();
        res.id = savedUser.getId();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping(path = "/student")
    public @ResponseBody ResponseEntity<StudentProfileRes> getStudentProfile(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "sessionToken") String sessionToken,
            @RequestParam(name = "accountName") Optional<String> accountName,
            @RequestParam(name = "id") Optional<Long> id
    ) {
        boolean result = authService.getAuthentication(name, sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        StudentDTO studentRetrieved = null;

        if (id.isPresent()) {
            studentRetrieved = studentService.getStudentProfileById(id.get());
        } else if (accountName.isPresent()) {
            studentRetrieved = studentService.getStudentProfileByAccountName(accountName.get());
        }

        if (studentRetrieved == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        StudentProfileRes res = new StudentProfileRes();
        res.displayName = studentRetrieved.getDisplayName();
        res.introduction = studentRetrieved.getIntroduction();
        res.subjects = studentRetrieved.getSubjects();
        res.id = studentRetrieved.getId();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping(path = "/students")
    public @ResponseBody ResponseEntity<List<StudentDTO>> getStudentProfile(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "sessionToken") String sessionToken,
            @RequestParam(name = "displayName") Optional<String> displayName,
            Pageable pageable) {
        boolean result = authService.getAuthentication(name, sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<StudentDTO> students = studentService.getAllStudents();

//        StudentProfileRes res = new StudentProfileRes();
//        res.displayName = studentRetrieved.getDisplayName();
//        res.introduction = studentRetrieved.getIntroduction();
//        res.subjects = studentRetrieved.getSubjects();
//        res.id = studentRetrieved.getId();
////
//        TutorCriteria criteria = new TutorCriteria(displayName, subjects, introduction, certificates);
//        Page<TutorDTO> page = tutorService.getTutorByCriteria(criteria, pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        List<TutorProfileRes> filteredTutors = page.getContent().stream().map(tutorDTO -> {
//            TutorProfileRes res = new TutorProfileRes();
//            res.displayName = tutorDTO.getDisplayName();
//            res.introduction = tutorDTO.getIntroduction();
//            res.subjects = tutorDTO.getSubjects();
//            res.certificates = tutorDTO.getCertificates();
//            res.id = tutorDTO.getId();
//
//            return res;Ï
//        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(students);
    }

}
