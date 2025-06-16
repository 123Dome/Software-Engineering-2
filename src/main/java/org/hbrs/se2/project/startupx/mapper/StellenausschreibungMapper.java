package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StellenausschreibungMapper {

    public static StellenausschreibungDTO toDTO(Stellenausschreibung stellenausschreibung) {
        if (stellenausschreibung == null) {
            return null;
        }

        Set<Long> skillIds = new LinkedHashSet<>();

        List<Long> bewerbungIds = new ArrayList<>();

        if (stellenausschreibung.getSkills() != null) {
            skillIds = stellenausschreibung.getSkills().stream()
                    .map(Skill::getId)
                    .collect(Collectors.toSet());;
        }

        if (stellenausschreibung.getBewerbungen() != null) {
            bewerbungIds = stellenausschreibung.getBewerbungen().stream()
                    .map(Bewerbung::getId)
                    .toList();
        }

        StellenausschreibungDTO stellenausschreibungDTO = StellenausschreibungDTO.builder()
                .id(stellenausschreibung.getId())
                .titel(stellenausschreibung.getTitel())
                .beschreibung(stellenausschreibung.getBeschreibung())
                .startup(stellenausschreibung.getStartup().getId())
                .skills(skillIds)
                .bewerbungen(bewerbungIds)
                .status(stellenausschreibung.getStatus())
                .build();

        return stellenausschreibungDTO;
    }
}
