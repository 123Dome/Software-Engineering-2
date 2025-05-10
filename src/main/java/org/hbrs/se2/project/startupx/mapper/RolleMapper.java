package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.RolleDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RolleMapper {
    RolleMapper INSTANCE = Mappers.getMapper(RolleMapper.class);

    Rolle mapToRolle(RolleDTO rolleDTO);

    RolleDTO mapToRolleDTO(Rolle rolle);
}
