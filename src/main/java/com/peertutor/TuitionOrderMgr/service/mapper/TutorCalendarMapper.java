package com.peertutor.TuitionOrderMgr.service.mapper;

import com.peertutor.TuitionOrderMgr.model.TutorCalendar;
import com.peertutor.TuitionOrderMgr.service.dto.TutorCalendarMgrDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TutorCalendarMapper extends EntityMapper<TutorCalendarMgrDTO, TutorCalendar>{
	TutorCalendar toEntity(TutorCalendarMgrDTO tutorCalendarMgrDTO);

	TutorCalendarMgrDTO toDto(TutorCalendar tutorCalendar);

    default TutorCalendar fromId(Long id) {
        if (id == null) {
            return null;
        }
        TutorCalendar tutorCalendar = new TutorCalendar();
        tutorCalendar.setId(id);
        return tutorCalendar;
    }
}
