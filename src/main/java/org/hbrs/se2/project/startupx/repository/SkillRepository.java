package org.hbrs.se2.project.startupx.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Long findSkillBySkillName(@Size(max = 255) @NotNull String skillName);
}
