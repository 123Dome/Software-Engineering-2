package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.StudiengangException;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.mapper.SkillMapper;
import org.hbrs.se2.project.startupx.mapper.StudiengangMapper;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.hbrs.se2.project.startupx.repository.StudiengangRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudiengangControl {

    private static final Logger logger = LoggerFactory.getLogger(StudiengangControl.class);

    @Autowired
    private StudiengangRepository studiengangRepository;

    @Autowired
    private SkillRepository skillRepository;

    public List<StudiengangDTO> getAll() {

        List<StudiengangDTO> studiengangDTOS = new ArrayList<>();
        for (Studiengang studiengang : studiengangRepository.findAll()) {
            studiengangDTOS.add(StudiengangMapper.mapToDto(studiengang));
        }

        return studiengangDTOS;
    }

    public StudiengangDTO getById(Long id) {
        Studiengang studiengang = studiengangRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Studiengang mit ID {} nicht gefunden.", id);
                    return new StudiengangException("Studiengang mit der ID " + id + " wurde nicht gefunden.");
                });

        return StudiengangMapper.mapToDto(studiengang);
    }

    public List<SkillDTO> findAllSkills() {
        List<SkillDTO> skillDTOS = new ArrayList<>();
        for (Skill skill : skillRepository.findAll()) {
            skillDTOS.add(SkillMapper.mapToSkillDto(skill));
        }
        return skillDTOS;
    }

}
