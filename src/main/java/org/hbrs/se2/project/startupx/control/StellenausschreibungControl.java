package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.StellenausschreibungException;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.mapper.StellenausschreibungMapper;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * StellenausschreibungControl ist für jegliche Kommunikation zwischen User/Views und der Datenbank zuständig.
 * Die Klasse enthält CRUD-Methode für Stellenausschreibungen.
 *
 * @author Korbinian Gauglitz
 * @version 1.0
 */
@Component
public class StellenausschreibungControl {

    private static final Logger logger = LoggerFactory.getLogger(StellenausschreibungControl.class);

    @Autowired
    StellenausschreibungRepository stellenausschreibungRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Transactional
    public StellenausschreibungDTO createStellenausschreibung(StellenausschreibungDTO stellenausschreibungDTO) {
        List<Skill> skillList = new ArrayList<>();

        for (Long skillId : stellenausschreibungDTO.getSkills()) {
            Skill skill = skillRepository.findById(skillId).orElse(null);
            if (skill != null) {
                skillList.add(skill);
            } else {
                logger.warn("Skill mit ID {} nicht gefunden. Wird ignoriert.", skillId);
            }
        }

        Startup startup = startupRepository.findById(stellenausschreibungDTO.getStartup())
                .orElseThrow(() -> {
                    logger.error("Startup mit ID {} nicht gefunden.", stellenausschreibungDTO.getStartup());
                    return new StellenausschreibungException("Startup nicht gefunden.");
                });

        List<Bewerbung> bewerbungList = new ArrayList<>();

        Stellenausschreibung neueStellenausschreibung = Stellenausschreibung.builder()
                .startup(startup)
                .titel(stellenausschreibungDTO.getTitel())
                .beschreibung(stellenausschreibungDTO.getBeschreibung())
                .bewerbungen(bewerbungList)
                .skills(skillList)
                .build();

        try {
            Stellenausschreibung saved = stellenausschreibungRepository.save(neueStellenausschreibung);
            logger.info("Stellenausschreibung erstellt: ID={}", saved.getId());
            return StellenausschreibungMapper.toDTO(saved);
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der Stellenausschreibung", e);
            throw new StellenausschreibungException("Stellenausschreibung konnte nicht gespeichert werden.", e);
        }
    }

    public List<StellenausschreibungDTO> getAllStellenausschreibungByStartup(StartupDTO startupDTO) {
        List<Stellenausschreibung> stellenausschreibungList = stellenausschreibungRepository.findByStartup_Id(startupDTO.getId());
        List<StellenausschreibungDTO> stellenausschreibungDTOS = new ArrayList<>();

        for(Stellenausschreibung stellenausschreibung : stellenausschreibungList) {
            stellenausschreibungDTOS.add(StellenausschreibungMapper.toDTO(stellenausschreibung));
        }

        logger.info("Alle Stellenausschreibungen für Startup {} geladen.", startupDTO.getName());
        return stellenausschreibungDTOS;
    }

    @Transactional
    public StellenausschreibungDTO updateStellenausschreibung(StellenausschreibungDTO dto) {
        Stellenausschreibung stelle = stellenausschreibungRepository.findById(dto.getId())
                .orElseThrow(() -> {
                    logger.error("Stellenausschreibung mit ID {} nicht gefunden.", dto.getId());
                    return new StellenausschreibungException("Stellenausschreibung nicht gefunden.");
                });

        List<Skill> skills = new ArrayList<>();
        for (Long skillId : dto.getSkills()) {
            Skill skill = skillRepository.findById(skillId).orElse(null);
            if (skill != null) {
                skills.add(skill);
            } else {
                logger.warn("Skill mit ID {} nicht gefunden. Wird ignoriert.", skillId);
            }
        }

        stelle.setSkills(skills);
        stelle.setBeschreibung(dto.getBeschreibung());
        stelle.setTitel(dto.getTitel());

        try {
            Stellenausschreibung saved = stellenausschreibungRepository.save(stelle);
            logger.info("Stellenausschreibung aktualisiert: ID={}", saved.getId());
            return StellenausschreibungMapper.toDTO(saved);
        } catch (Exception e) {
            logger.error("Fehler beim Aktualisieren der Stellenausschreibung", e);
            throw new StellenausschreibungException("Fehler beim Aktualisieren der Stellenausschreibung.", e);
        }
    }

    @Transactional
    public boolean deleteStellenausschreibung(StellenausschreibungDTO dto) {
        try {
            stellenausschreibungRepository.deleteById(dto.getId());
            logger.info("Stellenausschreibung gelöscht: ID={}", dto.getId());
            return true;
        } catch (Exception e) {
            logger.error("Fehler beim Löschen der Stellenausschreibung: {}", e.getMessage());
            throw new StellenausschreibungException("Stellenausschreibung konnte nicht gelöscht werden.", e);
        }
    }
}
