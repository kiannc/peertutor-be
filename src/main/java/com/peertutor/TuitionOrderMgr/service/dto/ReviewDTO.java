package com.peertutor.TuitionOrderMgr.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.peertutor.Reviewmgr.model.Review} entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReviewDTO implements Serializable{
	private Long id;
	
	private Long tutorID;
	
	private Long tutionOrderID;
	
	private int rating;
	
	private String comment;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public Long getTutorID() {
		return tutorID;
	}
	public void setTutorID(Long tutorID) {
		this.tutorID = tutorID;
	}
	public Long getTutionOrderID() {
		return tutionOrderID;
	}
	public void setTutionOrderID(Long tutionOrderID) {
		this.tutionOrderID = tutionOrderID;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewDTO reviewDTO = (ReviewDTO) o;
        if (reviewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reviewDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
    @Override
    public String toString() {
        return "ReviewDTO{" +
                "tutorid=" + getTutorID() +
                ", rating=" + getRating() +
                ", comment=" + getComment()+
                "}";
    }
	
}
