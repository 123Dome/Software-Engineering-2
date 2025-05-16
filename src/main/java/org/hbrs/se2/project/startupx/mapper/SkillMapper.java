package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillMapper {

    public static SkillDTO mapToSkillDto(Skill skill) {
        if (skill == null) return null;

        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(skill.getId());
        skillDTO.setSkillName(skill.getSkillName());
        if (skill.getStellenausschreibungen() != null) {
            List<Long> ausschreibungsIds = skill.getStellenausschreibungen().stream()
                    .map(Stellenausschreibung::getId)
                    .collect(Collectors.toList());
            skillDTO.setStellenausschreibungen(ausschreibungsIds);
        }
        if (skill.getStudents() != null) {
            Set<Long> studentIds = skill.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            skillDTO.setStudenten(studentIds);
        }
        return skillDTO;
    }
}
