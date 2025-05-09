package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentareDto;
import org.hbrs.se2.project.startupx.entities.Kommentare;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KommentarMapper {

    KommentarMapper INSTANCE = Mappers.getMapper(KommentarMapper.class);

    Kommentare mapToKommentare(KommentareDto kommentareDto);

    KommentareDto mapToKommentareDto(Kommentare kommentare);
}
