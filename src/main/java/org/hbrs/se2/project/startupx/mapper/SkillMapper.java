package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SkillMapper {
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    Skill mapToSkill(SkillDTO skillDTO);

    SkillDTO mapToSkillDto(Skill skill);
}
