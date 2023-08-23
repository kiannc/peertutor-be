package com.peertutor.TuitionOrderMgr.model.viewmodel.response;

import com.peertutor.TuitionOrderMgr.service.dto.ReviewDTO;

public class ReviewRes {

	public Long id;

	public Long tutorID;

	public Long tutionOrderID;

	public int rating;

	public String comment;
    
	 public ReviewRes(ReviewDTO reviewDTO) {
		 	this.id = reviewDTO.getId();
		 	this.tutorID = reviewDTO.getTutorID();
		 	this.tutionOrderID = reviewDTO.getTutionOrderID();
		 	this.rating = reviewDTO.getRating();
		 	this.comment =reviewDTO.getComment();
	    }

}
