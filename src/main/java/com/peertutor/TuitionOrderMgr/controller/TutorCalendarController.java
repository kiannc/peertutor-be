package com.peertutor.TuitionOrderMgr.controller;

import com.peertutor.TuitionOrderMgr.model.TutorCalendar;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.TutorCalendarReq;
import com.peertutor.TuitionOrderMgr.model.viewmodel.response.TutorCalendarRes;
import com.peertutor.TuitionOrderMgr.repository.TutorCalendarRepository;
import com.peertutor.TuitionOrderMgr.service.AuthService;
import com.peertutor.TuitionOrderMgr.service.TutorCalendarService;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path="/tutor-calendar-mgr")
public class TutorCalendarController {

	@Autowired
	AppConfig appConfig;
	@Autowired
	private TutorCalendarRepository tutorCalendarRepository;// = new CustomerRepository();
	@Autowired
	private TutorCalendarService tutorCalendarService;
	@Autowired
	private AuthService authService;

	// do not remove this
	@GetMapping(path="/health")
	public @ResponseBody String healthCheck(){
		return "Ok 2";
	}

	@PostMapping(path = "/calendar")
	@Transactional
	public @ResponseBody ResponseEntity<List<TutorCalendar>> addAvailableDate(@RequestBody  @Valid TutorCalendarReq req) {
		if (req.availableDates == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}

		List<TutorCalendar> saveTutorCalendar;
		saveTutorCalendar = tutorCalendarService.addAvailableDate(req);

		if (saveTutorCalendar == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		return ResponseEntity.ok().body(saveTutorCalendar);
	}

	@GetMapping(path = "/calendar")
	public @ResponseBody ResponseEntity<TutorCalendarRes> getReview(
			@RequestParam(name = "name") String name,
			@RequestParam(name = "tutorId") Long tutorId) {
		TutorCalendarRes tutorCalendar = tutorCalendarService.getTutorCalendar(tutorId);
		return ResponseEntity.ok().body(tutorCalendar);
	}

	@DeleteMapping(path = "/calendar")
	@Transactional
	public @ResponseBody ResponseEntity<TutorCalendarRes> getReview(
			@RequestParam(name = "name") String name,
			@RequestParam(name = "tutorId") Long tutorId,
			@RequestParam(name = "dates") String dates) {
		tutorCalendarService.deleteAvailableDates(tutorId, dates);
		return ResponseEntity.ok().body(null);
	}
}
