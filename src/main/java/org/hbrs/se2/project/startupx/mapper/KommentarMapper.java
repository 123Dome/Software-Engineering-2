package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class KommentarMapper {

    public static KommentarDTO mapToKommentarDto    (Kommentar kommentar) {
        if (kommentar == null) {
            return null;
        }

        return KommentarDTO.builder()
                .id(kommentar.getId())
                .kommentar(kommentar.getKommentar())
                .erstellungsdatum(kommentar.getErstellungsdatum())
                .user(kommentar.getUser().getId())
                .bewertung(kommentar.getBewertung())
                .updated(kommentar.getUpdated())
                .build();
    }

    public static Kommentar mapToKommentar(KommentarDTO kommentarDTO, User user, Startup startup) {

        return Kommentar.builder()
                .kommentar(kommentarDTO.getKommentar())
                .bewertung(kommentarDTO.getBewertung())
                .updated(kommentarDTO.getUpdated())
                .erstellungsdatum(kommentarDTO.getErstellungsdatum())
                .user(user)
                .startup(startup)
                .build();
    }
}
