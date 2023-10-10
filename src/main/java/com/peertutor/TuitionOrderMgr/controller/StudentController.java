package com.peertutor.TuitionOrderMgr.controller;

import com.peertutor.TuitionOrderMgr.exception.ExistingTuitionOrderException;
import com.peertutor.TuitionOrderMgr.exception.InputValidationException;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.StudentProfileReq;
import com.peertutor.TuitionOrderMgr.model.viewmodel.response.StudentProfileRes;
import com.peertutor.TuitionOrderMgr.repository.StudentRepository;
import com.peertutor.TuitionOrderMgr.service.AuthService;
import com.peertutor.TuitionOrderMgr.service.StudentService;
import com.peertutor.TuitionOrderMgr.service.dto.StudentDTO;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @GetMapping(path = "/health")
    public @ResponseBody String healthCheck() {
        return "Ok 3";
    }

    @PostMapping(path = "/student")
    public @ResponseBody ResponseEntity<StudentProfileRes> createStudentProfile(@RequestBody @Valid StudentProfileReq req) throws ExistingTuitionOrderException, InputValidationException {
        StudentDTO savedUser;

        PolicyFactory sanitizer = Sanitizers.FORMATTING;
        sanitizer.sanitize(req.name);
        sanitizer.sanitize(req.displayName);
        sanitizer.sanitize(req.accountName);
        sanitizer.sanitize(req.introduction);
        sanitizer.sanitize(req.subjects);

        savedUser = studentService.createStudentProfile(req);

        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        StudentProfileRes res = null;

         res = new StudentProfileRes();
         if (req.displayName.length() > 20) {
             logger.error("Your display name must not exceed 20 characters");
             throw new InputValidationException("Your display name must not exceed 20 characters");
         } else {
             res.displayName = savedUser.getDisplayName();
         }
         if (req.introduction.length() > 255) {
             logger.error("Your introduction must not exceed 255 characters");
             throw new InputValidationException("Your introduction must not exceed 255 characters");
         } else {
             res.introduction = savedUser.getIntroduction();
         }
         res.subjects = savedUser.getSubjects();
         res.id = savedUser.getId();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping(path = "/student")
    public @ResponseBody ResponseEntity<StudentProfileRes> getStudentProfile(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "accountName") Optional<String> accountName,
            @RequestParam(name = "id") Optional<Long> id
    ) {
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
            @RequestParam(name = "displayName") Optional<String> displayName,
            Pageable pageable) {
        List<StudentDTO> students = studentService.getAllStudents();

        return ResponseEntity.ok().body(students);
    }
}
