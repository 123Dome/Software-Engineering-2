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

    public static BewerbungDTO toDTO(Bewerbung bewerbung) {
        if (bewerbung == null) {
            return null;
        }

        BewerbungDTO bewerbungDTO = BewerbungDTO.builder()
                .id(bewerbung.getId())
                .student(bewerbung.getStudent().getId())
                .stellenausschreibungen(bewerbung.getStellenausschreibung().getId())
                .bewerbungsschreiben(bewerbung.getBewerbungsschreiben())
                .build();

        return bewerbungDTO;
    }
}
