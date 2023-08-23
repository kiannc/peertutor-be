package com.peertutor.TuitionOrderMgr.model.viewmodel.response;

import com.peertutor.TuitionOrderMgr.service.dto.BookmarkDTO;

public class BookmarkRes {

    public Long id;
    public Long tutorID;
    public Long studentID;

    public BookmarkRes(BookmarkDTO bookmarkDto){
        this.id = bookmarkDto.getId();
        this.tutorID = bookmarkDto.getTutorID();
        this.studentID = bookmarkDto.getStudentID();
    }
}
