package com.peertutor.TuitionOrderMgr.service;

import com.peertutor.TuitionOrderMgr.model.Review;
import com.peertutor.TuitionOrderMgr.model.viewmodel.request.ReviewReq;
import com.peertutor.TuitionOrderMgr.repository.ReviewRepository;

import com.peertutor.TuitionOrderMgr.service.dto.ReviewDTO;
import com.peertutor.TuitionOrderMgr.service.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
	
	@Autowired
	private final ReviewMapper reviewMapper;
	@Autowired
	private ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
		this.reviewRepository = reviewRepository;
		this.reviewMapper = reviewMapper;
	}

	public ReviewDTO addReview(ReviewReq req) {
		Review reviewMgr = reviewRepository.findBytutionOrderID(req.tutionOrderID);
		if(reviewMgr == null) {
			reviewMgr = new Review();
		}

		reviewMgr.setTutorID(req.id);
		reviewMgr.settutionOrderID(req.tutionOrderID);
		reviewMgr.setRating(req.rating);
		reviewMgr.setComment(req.comment);

		try {
			reviewMgr = reviewRepository.save(reviewMgr);
		} catch (Exception e) {
			logger.error("Add Review fail: " + e.getMessage());
			return null;
		}

		ReviewDTO result = reviewMapper.toDto(reviewMgr);

		return result;
	}
	public ReviewDTO updateReview(ReviewDTO reviewDTO) {
		Review reviewMgr = reviewRepository.findBytutionOrderID(reviewDTO.getTutionOrderID());
		if(reviewMgr == null) {
			reviewMgr = new Review();
		}

		reviewMgr.setRating(reviewDTO.getRating());
		reviewMgr.setComment(reviewDTO.getComment());

		try {
			reviewMgr = reviewRepository.save(reviewMgr);
		} catch (Exception e) {
			logger.error("Add Review fail: " + e.getMessage());
			return null;
		}

		ReviewDTO result= reviewMapper.toDto(reviewMgr);

		return result;
	}
	public ReviewDTO getAllReview(Long tutorID) {
		Review review = reviewRepository.findByTutorID(tutorID);

		if(review == null) {
			return null;
		}
		ReviewDTO result= reviewMapper.toDto(review);

		return result;
	}



}
