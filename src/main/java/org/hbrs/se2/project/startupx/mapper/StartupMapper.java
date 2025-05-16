package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StartupMapper {

    public static StartupDTO mapToStartupDto(Startup startup) {
        if (startup == null) {
            return null;
        }

        return new StartupDTO(
                startup.getId(),
                startup.getName(),
                startup.getBranche() != null ? startup.getBranche().getId() : null,
                startup.getBeschreibung(),
                startup.getGruendungsdatum(),
                startup.getAnzahlMitarbeiter(),
                startup.getKommentare() != null ? startup.getKommentare().stream().map(Kommentar::getId).collect(Collectors.toList()) : null,
                startup.getStellenausschreibungen() != null ? startup.getStellenausschreibungen().stream().map(Stellenausschreibung::getId).collect(Collectors.toList()) : null,
                startup.getStudentenListe() != null ? startup.getStudentenListe().stream().map(Student::getId).collect(Collectors.toSet()) : null
        );
    }
}
