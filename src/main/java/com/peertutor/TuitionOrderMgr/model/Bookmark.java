package com.peertutor.TuitionOrderMgr.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "bookmark")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tutor_id", nullable = false)
    private Long tutorID;

    @Column(name = "student_id", nullable = false)
    private Long studentID;

    @ManyToOne
    @JoinColumn(name = "tutor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Tutor tutor;

    public Long getTutorID() {
        return tutorID;
    }

    public void setTutorID(Long id) {
        this.tutorID = id;
    }

    public Long getStudentID() { return studentID; }

    public void setStudentID(Long id) {
        this.studentID = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bookmark bookmark = (Bookmark) o;
        if (bookmark.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookmark.getId());
    }

    @Override
    public String toString() {
        return "Bookmark {" +
                "id=" + id +
                ", Tutor ID='" + tutorID + '\'' +
                ", Student ID='" + studentID + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
}
