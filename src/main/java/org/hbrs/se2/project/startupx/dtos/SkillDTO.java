package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillDTO {
    private Long id;
    private String skillName;
    private List<Long> stellenausschreibungen = new ArrayList<>();
    private Set<Long> studenten = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillDTO skillDTO = (SkillDTO) o;
        return Objects.equals(id, skillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return skillName;
    }
}
