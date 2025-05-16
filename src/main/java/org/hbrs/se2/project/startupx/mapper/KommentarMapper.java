package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class KommentarMapper {

    public static KommentarDTO mapToKommentarDto    (Kommentar kommentar) {
        if (kommentar == null) return null;

        KommentarDTO kommentarDTO = new KommentarDTO();
        kommentarDTO.setId(kommentar.getId());
        kommentarDTO.setKommentar(kommentar.getKommentar());
        kommentarDTO.setErstellungsdatum(kommentar.getErstellungsdatum());
        kommentarDTO.setUser(kommentar.getUser().getId()); // assuming User is not null
        kommentarDTO.setBewertung(kommentar.getBewertung());
        kommentarDTO.setUpdated(kommentar.getUpdated());
        kommentarDTO.setUpdated(kommentar.getUpdated());

        return kommentarDTO;
    }
}
