package com.peertutor.TuitionOrderMgr.controller;

import com.peertutor.TuitionOrderMgr.repository.TutorRepository;
import com.peertutor.TuitionOrderMgr.service.AuthService;
import com.peertutor.TuitionOrderMgr.service.RecService;
import com.peertutor.TuitionOrderMgr.service.dto.TutorDTO;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/recommendation-mgr")
public class RecController {
    @Autowired
    AppConfig appConfig;
    @Autowired
    private TutorRepository tutorRepository;// = new CustomerRepository();
    @Autowired
    private RecService recService;
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/health")
    public @ResponseBody String healthCheck() {
        return "Ok 3";
    }

    @GetMapping(path = "/rec")
    public @ResponseBody ResponseEntity<List<TutorDTO>> getTutorProfile(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "sessionToken") String sessionToken,
            @RequestParam(name = "id") Long id
    ) {
        boolean result = authService.getAuthentication(name, sessionToken);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<TutorDTO> tutors = recService.getTutorRec(id);

        return ResponseEntity.ok().body(tutors);
    }
}
