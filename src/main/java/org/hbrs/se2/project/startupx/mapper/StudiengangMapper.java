package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudiengangMapper {
    StudiengangMapper INSTANCE = Mappers.getMapper(StudiengangMapper.class);

    Studiengang mapToStudiengang(StudiengangDTO studiengangDTO);

    StudiengangDTO mapToStudiengangDTO(Studiengang studiengang);
}
