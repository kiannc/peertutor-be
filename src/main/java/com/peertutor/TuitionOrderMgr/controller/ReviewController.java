package com.peertutor.TuitionOrderMgr.controller;

import com.peertutor.TuitionOrderMgr.model.viewmodel.response.ReviewRes;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.ReviewReq;
import com.peertutor.TuitionOrderMgr.repository.ReviewRepository;
import com.peertutor.TuitionOrderMgr.service.ReviewService;
import com.peertutor.TuitionOrderMgr.service.dto.ReviewDTO;
import com.peertutor.TuitionOrderMgr.util.AppConfig;
import com.peertutor.TuitionOrderMgr.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Controller
@RequestMapping(path="/review-mgr")
public class ReviewController {
	@Autowired
	AppConfig appConfig;
	@Autowired
	private ReviewRepository reviewRepository;// = new CustomerRepository();
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private AuthService authService;

	// do not remove this
	@GetMapping(path="/health")
	public @ResponseBody String healthCheck(){
		return "Ok 2";
	}
	@GetMapping(path="/public-api")
	public @ResponseBody String callPublicApi() {
		String endpoint = "https://api.publicapis.org/entries"; //url+":"+port;
		System.out.println("endpoint" + endpoint);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

		return response.toString();
	}

//	@GetMapping(path="/call-app-tuition-order-mgr")
//	public @ResponseBody String callAppTwo() {
//		String url = appConfig.getTuitionOrderMgr().get("url");
//		String port = appConfig.getTuitionOrderMgr().get("port");
//
//		String endpoint = url+"/" ;
//		System.out.println("endpoint" + endpoint);
//
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
//
//		return response.toString();
//	}
//	
//	  @GetMapping(path = "/call-app-student-mgr")
//	    public @ResponseBody String callAppTwo() {
//	        String url = appConfig.getStudentMgr().get("url");
//	        String port = appConfig.getStudentMgr().get("port");
//
//
//	        String endpoint = url + "/"; //":"+port + "/";
//	        System.out.println("endpoint" + endpoint);
//
//	        RestTemplate restTemplate = new RestTemplate();
//	        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
//
//	        return response.toString();
//	    }

	@PostMapping(path = "/review")
	public @ResponseBody ResponseEntity<ReviewRes> addNewReview(@RequestBody  @Valid ReviewReq req) {

		boolean result = authService.getAuthentication(req.name, req.sessionToken);
		if (!result) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		ReviewDTO saveReview;
		saveReview = reviewService.addReview(req);

		if (saveReview == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}

		ReviewRes res = new ReviewRes(saveReview);
		res.rating = saveReview.getRating();
		res.comment = saveReview.getComment();
		

		return ResponseEntity.ok().body(res);
	}
	@GetMapping(path = "/reviews")
	public @ResponseBody ResponseEntity<ReviewRes> getReview(@RequestBody  @Valid ReviewReq req) {

		boolean result = authService.getAuthentication(req.name, req.sessionToken);
		if (!result) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		ReviewDTO saveReview;
		saveReview = reviewService.getAllReview(req.tutionOrderID);

		if (saveReview == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}

		ReviewRes res = new ReviewRes(saveReview);
		res.rating = saveReview.getRating();
		res.comment = saveReview.getComment();
		
		return ResponseEntity.ok().body(res);
	}
}
