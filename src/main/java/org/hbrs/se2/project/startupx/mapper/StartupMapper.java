package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StartupMapper {

    StartupMapper INSTANCE = Mappers.getMapper(StartupMapper.class);

    Startup mapToStartup(StartupDTO startupDto);

    StartupDTO mapToStartupDto(Startup startup);
}
