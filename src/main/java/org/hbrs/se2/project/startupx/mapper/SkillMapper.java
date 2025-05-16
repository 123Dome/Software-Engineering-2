package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillMapper {

    public static SkillDTO mapToSkillDto(Skill skill) {
        if (skill == null) {
            return null;
        }

        List<Long> ausschreibungsIds = new ArrayList<>();
        if (skill.getStellenausschreibungen() != null) {
            ausschreibungsIds= skill.getStellenausschreibungen().stream()
                    .map(Stellenausschreibung::getId)
                    .toList();
        }

        Set<Long> studentIds = new LinkedHashSet<>();

        if (skill.getStudents() != null) {
            studentIds = skill.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
        }

        SkillDTO skillDTO = SkillDTO.builder()
                .id(skill.getId())
                .skillName(skill.getSkillName())
                .stellenausschreibungen(ausschreibungsIds)
                .studenten(studentIds)
                .build();

        return skillDTO;
    }
}
