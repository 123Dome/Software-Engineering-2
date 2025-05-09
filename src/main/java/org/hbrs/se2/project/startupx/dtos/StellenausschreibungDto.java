package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Startup;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StellenausschreibungDto {

    private Integer id;
    private Startup startup;
    private String titel;
    private String beschreibung;
    private List<Skill> skills = new ArrayList<>();
    private List<Bewerbung> bewerbungen = new ArrayList<>();
}
