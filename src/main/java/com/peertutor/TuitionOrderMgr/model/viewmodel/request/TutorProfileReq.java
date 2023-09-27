package com.peertutor.TuitionOrderMgr.model.viewmodel.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TutorProfileReq {
    @NotNull
    @NotEmpty
    public String name;
    public String displayName;

    public String accountName;

    public String introduction;

    public String subjects;

    public String certificates;
}
