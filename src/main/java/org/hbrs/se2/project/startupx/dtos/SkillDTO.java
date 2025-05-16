package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {
    private Long id;
    private String skillName;
    private List<Long> stellenausschreibungen = new ArrayList<>();
    private Set<Long> studenten = new LinkedHashSet<>();
}
