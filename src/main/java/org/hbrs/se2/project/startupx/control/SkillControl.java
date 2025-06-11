package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillControl {

    @Autowired
    private SkillRepository skillRepository;

    public Set<String> getAllSkills() {
        Set<String> skills = new HashSet<>();

        List<Skill> skillList = skillRepository.findAll();

        for (Skill skill : skillList) {
            skills.add(skill.getSkillName());
        }
        return skills;
    }

    public Set<Long> getAllSkillsBySkillName(Set<String> skillNames) {
        Set<Long> skills = new LinkedHashSet<>();
        for (String skillName : skillNames) {
            skills.add(skillRepository.findSkillBySkillName(skillName));
        }

        return skills;
    }
}
