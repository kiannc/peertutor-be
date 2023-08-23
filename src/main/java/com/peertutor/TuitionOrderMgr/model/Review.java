package com.peertutor.TuitionOrderMgr.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tutor_id", nullable = false, unique = true)
    private Long tutorID;
    
    @Column(name = "tutionorder_id", nullable = false)
    private Long tutionOrderID;
    
    @Column(name = "rating", nullable = false, unique = true)
	private int rating;
    

	@Column(name = "comment", nullable = false, unique = true)
	private String comment;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public void settutionOrderID(Long tutionOrderID) {
		this.tutionOrderID = tutionOrderID;
	}
	public int getRating() {
		return rating;
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

        Review review = (Review) o;
        if (review.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), review.getId()) &&
                Objects.equals(getTutorID(), review.getTutorID()) &&
                Objects.equals(getRating(), review.getRating()) &&
                Objects.equals(getComment(), review.getComment());
    }
	@Override
    public String toString() {
        return "review{" +
                "id=" + id +
                '}';
    }
}
