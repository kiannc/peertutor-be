package com.peertutor.TuitionOrderMgr.model.viewmodel.request;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
public class ReviewReq {
	@NotNull
    @NotEmpty
    public String name;

    public Long id;

	public Long tutorID;

	public Long tutionOrderID;

	public int rating;

	public String comment;
}
