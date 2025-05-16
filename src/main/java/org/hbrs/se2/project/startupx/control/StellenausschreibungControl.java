package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
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

    private static Logger logger = LoggerFactory.getLogger(StellenausschreibungControl.class);

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
            try {
                skillList.add(skillRepository.findById(skillId).get());
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        Startup startup = startupRepository.findById(stellenausschreibungDTO.getStartup()).get();

        List<Bewerbung> bewerbungList = new ArrayList<>();

        Stellenausschreibung neueStellenausschreibung = Stellenausschreibung.builder()
                .startup(startup)
                .titel(stellenausschreibungDTO.getTitel())
                .beschreibung(stellenausschreibungDTO.getBeschreibung())
                .bewerbungen(bewerbungList)
                .skills(skillList)
                .build();

        Stellenausschreibung savedStellenausschreibung = stellenausschreibungRepository.save(neueStellenausschreibung);

        logger.info(savedStellenausschreibung + " erstellt");
        return StellenausschreibungMapper.toDTO(savedStellenausschreibung);
    }

    public List<StellenausschreibungDTO> getAllStellenausschreibungByStartup(StartupDTO startupDTO) {

        List<Stellenausschreibung> stellenausschreibungList = stellenausschreibungRepository.findByStartup_Id(startupDTO.getId());

        List<StellenausschreibungDTO> stellenausschreibungDTOS = new ArrayList<>();

        for(Stellenausschreibung stellenausschreibung : stellenausschreibungList) {
            stellenausschreibungDTOS.add(StellenausschreibungMapper.toDTO(stellenausschreibung));
        }

        logger.info("Stellenausschreibung für : " + startupDTO.getName() + " erstellt.");
        return stellenausschreibungDTOS;
    }

    @Transactional
    public StellenausschreibungDTO updateStellenausschreibung(StellenausschreibungDTO stellenausschreibungDTO) {

        Stellenausschreibung alteStellenausschreibung = stellenausschreibungRepository.findById(stellenausschreibungDTO.getId()).get();

        List<Skill> skillList = new ArrayList<>();
        for (Long skillId : stellenausschreibungDTO.getSkills()) {
            skillList.add(skillRepository.findById(skillId).get());
        }

        alteStellenausschreibung.setSkills(skillList);
        alteStellenausschreibung.setBeschreibung(stellenausschreibungDTO.getBeschreibung());
        alteStellenausschreibung.setTitel(stellenausschreibungDTO.getTitel());

        Stellenausschreibung savedStellenausschreibung = stellenausschreibungRepository.save(alteStellenausschreibung);

        logger.info(savedStellenausschreibung + " aktualisiert");
        return StellenausschreibungMapper.toDTO(savedStellenausschreibung);
    }

    @Transactional
    public boolean deleteStellenausschreibung(StellenausschreibungDTO stellenausschreibungDTO) {

        try {
            stellenausschreibungRepository.deleteById(stellenausschreibungDTO.getId());
            logger.info("Stellenausschreibung: " + stellenausschreibungDTO.getId() + " gelöscht");
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
