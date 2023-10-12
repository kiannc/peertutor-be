package com.peertutor.TuitionOrderMgr.model.viewmodel.response;

import java.sql.Date;
import java.util.List;


public class TutorCalendarRes {
	public List<String> availableDate;

	public TutorCalendarRes(List<String> availableDate) {
		this.availableDate = availableDate;
	}

	public TutorCalendarRes() {
	}

	@Override
	public String toString() {
		return "availableDate" + availableDate.toString();
	}
}
