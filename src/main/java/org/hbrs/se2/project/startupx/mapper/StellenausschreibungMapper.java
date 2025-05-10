package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StellenausschreibungMapper {

    StellenausschreibungMapper INSTANCE = Mappers.getMapper(StellenausschreibungMapper.class);

    Stellenausschreibung mapToStellenausschreibung(StellenausschreibungDTO stellenausschreibungDto);

    StellenausschreibungDTO mapToStellenausschreibungDto(Stellenausschreibung stellenausschreibung);
}
