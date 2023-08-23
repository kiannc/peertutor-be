package com.peertutor.TuitionOrderMgr.service.mapper;

import com.peertutor.TuitionOrderMgr.model.Review;
import com.peertutor.TuitionOrderMgr.service.dto.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO,Review>{
	Review toEntity(ReviewDTO reviewDTO);

	ReviewDTO toDto(Review review);

    default Review fromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}

