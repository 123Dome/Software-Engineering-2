package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class StudiengangMapper {

    public StudiengangDTO mapToDto(Studiengang studiengang) {
        if (studiengang == null) {
            return null;
        }

        StudiengangDTO studiengangDTO = StudiengangDTO.builder()
                .id(studiengang.getId())
                .studiengang(studiengang.getStudiengang())
                .build();

        return studiengangDTO;
    }
}
