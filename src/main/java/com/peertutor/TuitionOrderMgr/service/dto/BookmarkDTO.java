package com.peertutor.TuitionOrderMgr.service.dto;

import com.peertutor.TuitionOrderMgr.model.Bookmark;
import com.peertutor.TuitionOrderMgr.model.Tutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookmarkDTO implements Serializable {

    private Long id;
    private Long tutorID;
    private Long studentID;
    private Tutor tutor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bookmark {" +
                "id=" + id +
                ", Tutor ID='" + tutorID + '\'' +
                ", Student ID='" + studentID + '\'' +
                '}';
    }

}
