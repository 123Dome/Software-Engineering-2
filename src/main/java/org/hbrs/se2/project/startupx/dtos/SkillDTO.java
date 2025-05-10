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
public class SkillDTO {
    private Integer id;
    private String skillName;
    private List<StellenausschreibungDTO> stellenausschreibungenDTOs = new ArrayList<>();
    private List<StudentDTO> studentDTOs = new ArrayList<>();
}
