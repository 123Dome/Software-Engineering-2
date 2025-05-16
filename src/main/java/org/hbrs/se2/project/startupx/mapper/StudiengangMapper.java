package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class StudiengangMapper {
//    StudiengangMapper INSTANCE = Mappers.getMapper(StudiengangMapper.class);

//    Studiengang mapToStudiengang(StudiengangDTO studiengangDTO);

    public StudiengangDTO mapToDto(Studiengang studiengang) {
        if (studiengang == null) return null;

        StudiengangDTO studiengangDTO = new StudiengangDTO();
        studiengangDTO.setId(studiengang.getId());
        studiengangDTO.setStudiengang(studiengang.getStudiengang());
        return studiengangDTO;
    }
}
