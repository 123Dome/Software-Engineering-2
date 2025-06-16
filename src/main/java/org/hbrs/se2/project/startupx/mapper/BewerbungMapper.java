package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
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
                .status(bewerbung.getStatus())
                .build();

        return bewerbungDTO;
    }

    public static Bewerbung toEntity(BewerbungDTO bewerbungDTO, Student student, Stellenausschreibung stellenausschreibung) {
        if (bewerbungDTO == null) {
            return null;
        }

        return Bewerbung.builder()
                .id(bewerbungDTO.getId())
                .student(student)
                .bewerbungsschreiben(bewerbungDTO.getBewerbungsschreiben())
                .stellenausschreibung(stellenausschreibung)
                .status(bewerbungDTO.getStatus())
                .build();
    }
}
