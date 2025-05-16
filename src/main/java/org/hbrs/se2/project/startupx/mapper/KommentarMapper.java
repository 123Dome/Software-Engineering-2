package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class KommentarMapper {

    public static KommentarDTO mapToKommentarDto    (Kommentar kommentar) {
        if (kommentar == null) {
            return null;
        }

        KommentarDTO kommentarDTO = KommentarDTO.builder()
                .id(kommentar.getId())
                .kommentar(kommentar.getKommentar())
                .erstellungsdatum(kommentar.getErstellungsdatum())
                .user(kommentar.getUser().getId())
                .bewertung(kommentar.getBewertung())
                .updated(kommentar.getUpdated())
                .build();

        return kommentarDTO;
    }
}
