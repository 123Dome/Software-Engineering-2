package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDto;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StellenausschreibungMapper {

    StellenausschreibungMapper INSTANCE = Mappers.getMapper(StellenausschreibungMapper.class);

    Stellenausschreibung mapToStellenausschreibung(StellenausschreibungDto stellenausschreibungDto);

    StellenausschreibungDto mapToStellenausschreibungDto(Stellenausschreibung stellenausschreibung);
}
