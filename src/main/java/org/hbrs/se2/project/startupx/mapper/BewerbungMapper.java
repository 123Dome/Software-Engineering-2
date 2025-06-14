package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
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
