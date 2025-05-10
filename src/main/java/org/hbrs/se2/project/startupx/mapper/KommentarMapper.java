package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentareDTO;
import org.hbrs.se2.project.startupx.entities.Kommentare;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KommentarMapper {

    KommentarMapper INSTANCE = Mappers.getMapper(KommentarMapper.class);

    Kommentare mapToKommentare(KommentareDTO kommentareDto);

    KommentareDTO mapToKommentareDto(Kommentare kommentare);
}
