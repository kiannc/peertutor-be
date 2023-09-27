package com.peertutor.TuitionOrderMgr.model.viewmodel.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

public class TutorCalendarReq {
	@NotNull
    @NotEmpty
    public String name;
    public Long id;

	public Long tutorId;

	public List<Date> availableDates;
}
