package com.peertutor.TuitionOrderMgr.service.mapper;


import com.peertutor.TuitionOrderMgr.model.Tutor;
import com.peertutor.TuitionOrderMgr.service.dto.TutorDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link com.peertutor.TuitionOrderMgr.model.Tutor} and its DTO {@link TutorDTO}.
 */
@Mapper(componentModel = "spring")
public interface TutorMapper extends EntityMapper<TutorDTO, Tutor> {

    Tutor toEntity(TutorDTO accountDTO);

    TutorDTO toDto(Tutor account);

    default Tutor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tutor account = new Tutor();
        account.setId(id);
        return account;
    }
}
