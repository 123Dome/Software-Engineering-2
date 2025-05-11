package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.GruenderDTO;
import org.hbrs.se2.project.startupx.entities.Gruender;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GruenderMapper {
    GruenderMapper INSTANCE = Mappers.getMapper(GruenderMapper.class);

    Gruender mapToGruender(GruenderDTO gruenderDTO);

    GruenderDTO mapToGruenderDTO(Gruender gruender);
}
