package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KommentarMapper {
    KommentarMapper INSTANCE = Mappers.getMapper(KommentarMapper.class);

    Kommentar mapToKommentar(KommentarDTO kommentarDTO);

    KommentarDTO mapToKommentarDto(Kommentar kommentar);
}
