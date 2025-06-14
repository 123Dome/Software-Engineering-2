package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StartupMapper {

    public static StartupDTO mapToStartupDto(Startup startup) {
        if (startup == null) {
            return null;
        }

        List<Long> bewertungIDs = new ArrayList<>();
        Set<Long> studentIDs = new LinkedHashSet<>();
        List<Long> stellenausschreibungIDs = new ArrayList<>();

        if (startup.getBewertungen() != null) {
            bewertungIDs = startup.getBewertungen().stream()
                    .map(Bewertung::getId)
                    .toList();
        }

        if (startup.getStudentenListe() != null) {
            studentIDs = startup.getStudentenListe().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
        }

        if (startup.getStellenausschreibungen() != null) {
            stellenausschreibungIDs = startup.getStellenausschreibungen().stream()
                    .map(Stellenausschreibung::getId)
                    .toList();
        }

        return StartupDTO.builder()
                .id(startup.getId())
                .name(startup.getName())
                .beschreibung(startup.getBeschreibung())
                .branche(startup.getBranche().getId())
                .gruendungsdatum(startup.getGruendungsdatum())
                .anzahlMitarbeiter(startup.getAnzahlMitarbeiter())
                .stellenausschreibungen(stellenausschreibungIDs)
                .studentenListe(studentIDs)
                .bewertungen(bewertungIDs)
                .build();
    }

    public static Startup mapToStartup(StartupDTO startupDTO, Set<Student> studentList, Branche branche) {

        return Startup.builder()
                .name(startupDTO.getName())
                .branche(branche)
                .beschreibung(startupDTO.getBeschreibung())
                .gruendungsdatum(startupDTO.getGruendungsdatum())
                .anzahlMitarbeiter(startupDTO.getAnzahlMitarbeiter())
                .studentenListe(studentList)
                .build();
    }
}
