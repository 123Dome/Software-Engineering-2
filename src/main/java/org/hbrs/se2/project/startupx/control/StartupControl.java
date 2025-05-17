package org.hbrs.se2.project.startupx.control;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.mapper.StartupMapper;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * StartupControl ist für jegliche Interaktion zwischen Nutzer/Views und der Datenbankanbindung (durch
 * verschiedene Repositories) zuständig.
 * Durch diese Klasse lassen sich Startups erstellen, löschen, updaten und suchen
 *
 * @author Korbinian Gauglitz
 * @version 1.0
 */
@Controller
public class StartupControl {

    private static final Logger logger = LoggerFactory.getLogger(StartupControl.class);

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private BrancheRepository brancheRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public StartupDTO createStartup(StartupDTO startupDTO) {

        Set<Student> studentList = new LinkedHashSet<>();

        for(Long studentIds : startupDTO.getStudentenListe()) {
            try {
                studentList.add(studentRepository.findById(studentIds)
                        .orElseThrow(() -> new EntityNotFoundException("Student mit der ID " + studentIds + " nicht gefunden")));
            } catch (EntityNotFoundException e) {
                logger.warn("Student mit der ID " + studentIds + " nicht gefunden");
            }
        }

        Branche branche = new Branche();
        try {
             branche = brancheRepository.findById(startupDTO.getBranche())
                    .orElseThrow(() -> new EntityNotFoundException("Branche mit der ID " + startupDTO.getBranche() + " nicht gefunden"));
        } catch (EntityNotFoundException e) {
            logger.warn("Branche mit der ID " + startupDTO.getBranche() + " nicht gefunden");
        }

        Startup startup = StartupMapper.mapToStartup(startupDTO, studentList, branche);

        Startup savedStartup = startupRepository.save(startup);

        logger.info("Startup gegründet: " + savedStartup);
        return StartupMapper.mapToStartupDto(savedStartup);
    }

    public List<StartupDTO> findAll() {
        List<Startup> startupList = startupRepository.findAll();

        List<StartupDTO> startupDTOList = new ArrayList<>();
        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        logger.info("StartupDTOList erstellt.");
        return startupDTOList;
    }

    public List<StartupDTO> findByHavingAnyStellenausschreibungJoin() {

        List<Startup> startupList = startupRepository.findByHavingAnyStellenausschreibungJoin();

        List<StartupDTO> startupDTOList = new ArrayList<>();

        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        logger.info("Startups mit mind. einer Stellenausschreibung erstellt.");
        return startupDTOList;
    }

//    public List<StartupDTO> findByBranche(BrancheDTO brancheDTO) {
//
//        List<Startup> startupList = startupRepository.findByBranche_Id(brancheDTO.getId());
//
//        List<StartupDTO> startupDTOList = new ArrayList<>();
//        for(Startup startup : startupList) {
//            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
//        }
//
//        logger.info("Startups zur Branche " + brancheDTO.getBezeichnung() + " gefunden.");
//        return startupDTOList;
//    }
//
//    public List<StartupDTO> findByNameContaining(String nameContaining) {
//
//        List<Startup> startupList = startupRepository.findByNameContaining(nameContaining);
//
//        List<StartupDTO> startupDTOList = new ArrayList<>();
//
//        for(Startup startup : startupList) {
//            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
//        }
//
//        return startupDTOList;
//    }
//
//    public List<StartupDTO> findByGruendungsdatum(LocalDate gruendungsdatum) {
//
//        List<Startup> startupList = startupRepository.findByGruendungsdatum(gruendungsdatum);
//
//        List<StartupDTO> startupDTOList = new ArrayList<>();
//
//        for(Startup startup : startupList) {
//            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
//        }
//
//        return startupDTOList;
//    }

    @Transactional
    public StartupDTO updateStartup(StartupDTO startupDTO) {

        Startup oldStartup = startupRepository.findById(startupDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Startup " + startupDTO.getId() + " not found"));

        Branche newBranche = brancheRepository.findById(startupDTO.getBranche())
                .orElseThrow(() -> new EntityNotFoundException("Branche " + startupDTO.getBranche() + " not found"));

        oldStartup.setName(startupDTO.getName());
        oldStartup.setBranche(newBranche);
        oldStartup.setBeschreibung(startupDTO.getBeschreibung());
        oldStartup.setAnzahlMitarbeiter(startupDTO.getAnzahlMitarbeiter());

        Startup savedStartup = startupRepository.save(oldStartup);

        logger.info("Startup: " + oldStartup + " aktualisiert");
        return StartupMapper.mapToStartupDto(savedStartup);
    }

    public List<StartupDTO> getStartups(StudentDTO studentDTO) {

        List<Startup> startupList = startupRepository.findByStudentenListe_Id(studentDTO.getId());

        List<StartupDTO> startupDTOList = new ArrayList<>();

        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        logger.info("Startups zu User: " + studentDTO + " gefunden.");
        return startupDTOList;
    }

    @Transactional
    public boolean deleteStartup(StartupDTO startupDTO) {

        try {
            startupRepository.deleteById(startupDTO.getId());
            logger.info("Startup: " + startupDTO + " gelöscht");
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
