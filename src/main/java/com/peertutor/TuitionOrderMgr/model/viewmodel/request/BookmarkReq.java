package com.peertutor.TuitionOrderMgr.model.viewmodel.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BookmarkReq {
    @NotNull
    @NotEmpty
    public String name;

    @NotNull
    @NotEmpty
    public String sessionToken;

    public Long id;

    @NotNull
    public Long tutorID;

    @NotNull
    public Long studentID;
}