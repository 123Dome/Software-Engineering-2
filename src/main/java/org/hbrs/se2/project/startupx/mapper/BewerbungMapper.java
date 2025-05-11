package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BewerbungMapper {
    BewerbungMapper INSTANCE = Mappers.getMapper(BewerbungMapper.class);

    Bewerbung mapToBewerbung(BewerbungDTO bewerbungDTO);

    BewerbungDTO mapToBewerbungDTO(Bewerbung bewerbung);
}
