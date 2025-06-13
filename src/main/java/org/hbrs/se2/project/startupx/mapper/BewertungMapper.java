package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.entities.Bewertung;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;

public class BewertungMapper {

    public static BewertungDTO mapToBewertungDTO(Bewertung bewertung) {
        if(bewertung == null){
            return null;
        }

        return BewertungDTO.builder()
                .id(bewertung.getId())
                .user(bewertung.getUser().getId())
                .startup(bewertung.getStartup().getId())
                .bewertung(bewertung.getBewertung())
                .kommentar(bewertung.getKommentar())
                .erstellungsdatum(bewertung.getErstellungsdatum())
                .build();
    }

    public static Bewertung mapToBewertung(BewertungDTO bewertungDTO, User user, Startup startup) {
        if(bewertungDTO == null){
            return null;
        }

        return Bewertung.builder()
                .user(user)
                .startup(startup)
                .bewertung(bewertungDTO.getBewertung())
                .kommentar(bewertungDTO.getKommentar())
                .erstellungsdatum(bewertungDTO.getErstellungsdatum())
                .build();
    }
}
