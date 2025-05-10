package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StellenausschreibungDTO {
    private Integer id;
    private StartupDTO startupDTO;
    private String titel;
    private String beschreibung;
    private List<SkillDTO> skillDTOs = new ArrayList<>();
    private List<BewerbungDTO> bewerbungDTOs = new ArrayList<>();
}
