package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StellenausschreibungMapper {

    public static StellenausschreibungDTO toDTO(Stellenausschreibung stelle) {
        if (stelle == null) {
            return null;
        }

        StellenausschreibungDTO stellenausschreibungDTO = new StellenausschreibungDTO();
        stellenausschreibungDTO.setId(stelle.getId());
        stellenausschreibungDTO.setTitel(stelle.getTitel());
        stellenausschreibungDTO.setBeschreibung(stelle.getBeschreibung());
        stellenausschreibungDTO.setStartup(stelle.getStartup().getId());

        if (stelle.getSkills() != null) {
            List<Long> skillIds = stelle.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
            stellenausschreibungDTO.setSkills(skillIds);
        }

        if (stelle.getBewerbungen() != null) {
            List<Long> bewerbungIds = stelle.getBewerbungen().stream()
                    .map(Bewerbung::getId)
                    .toList();

            stellenausschreibungDTO.setBewerbungen(bewerbungIds);
        }
        return stellenausschreibungDTO;
    }
}
