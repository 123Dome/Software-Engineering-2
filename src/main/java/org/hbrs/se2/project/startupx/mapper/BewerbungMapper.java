package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class BewerbungMapper {
//    BewerbungMapper INSTANCE = Mappers.getMapper(BewerbungMapper.class);
//
//    Bewerbung mapToBewerbung(BewerbungDTO bewerbungDTO);
//
//    BewerbungDTO mapToBewerbungDTO(Bewerbung bewerbung);

    public static BewerbungDTO toDTO(Bewerbung bewerbung) {

        if (bewerbung == null) {
            return null;
        }

        BewerbungDTO bewerbungDTO = new BewerbungDTO();
        bewerbungDTO.setId(bewerbung.getId());
        bewerbungDTO.setStudent(bewerbung.getStudent().getId());
        bewerbungDTO.setStellenausschreibungen(bewerbung.getStellenausschreibung().getId());
        bewerbungDTO.setBewerbungsschreiben(bewerbung.getBewerbungsschreiben());

        return bewerbungDTO;
    }
}
