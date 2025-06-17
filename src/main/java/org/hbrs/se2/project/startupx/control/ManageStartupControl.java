package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.StartUpException;
import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.mapper.BrancheMapper;
import org.hbrs.se2.project.startupx.mapper.StartupMapper;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.*;

/**
 * StartupControl ist für jegliche Interaktion zwischen Nutzer/Views und der Datenbankanbindung (durch
 * verschiedene Repositories) zuständig.
 * Durch diese Klasse lassen sich Startups erstellen, löschen, updaten und suchen
 *
 * @author Korbinian Gauglitz
 * @version 1.0
 */
@Controller
@Transactional
public class ManageStartupControl {

    private static final Logger logger = LoggerFactory.getLogger(ManageStartupControl.class);

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
                //TODO ggf. anpassen: Repository ersetzen durch einen "User/Studenten-Control"?
                studentList.add(studentRepository.findById(studentIds)
                        .orElseThrow(() -> new StartUpException("Student mit der ID " + studentIds + " nicht gefunden")));
            } catch (StartUpException e) {
                logger.warn(e.getMessage());
            }
        }

        Branche branche = getBrancheById(startupDTO.getBranche());

        List<Student> mitarbeiterList = new ArrayList<>();

        Startup startup = StartupMapper.mapToStartup(startupDTO, studentList, branche, mitarbeiterList);

        Startup savedStartup = startupRepository.save(startup);

        logger.info("Startup gegründet: {}", savedStartup);
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

    public StartupDTO findByID(Long id) {
        Optional<Startup> startup = startupRepository.findById(id);
        if (startup.isPresent()) {
            return StartupMapper.mapToStartupDto(startup.get());
        }
        logger.error("Startup mit ID {} nicht gefunden.", id);
        throw new StartUpException("Startup mit der ID " + id + " wurde nicht gefunden.");
    }


    public List<StartupDTO> findByHavingAnyStellenausschreibungJoin() {

        List<Startup> startupList = startupRepository.findByStellenausschreibungenIsNotEmpty();

        List<StartupDTO> startupDTOList = new ArrayList<>();

        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        logger.info("Startups mit mind. einer Stellenausschreibung erstellt.");
        return startupDTOList;
    }

    public List<StartupDTO> findByGruendungsdatum(LocalDate gruendungsdatum) {

        List<Startup> startupList = startupRepository.findByGruendungsdatum(gruendungsdatum);

        List<StartupDTO> startupDTOList = new ArrayList<>();

        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        return startupDTOList;
    }

    public List<StartupDTO> findTop5ByOrderByGruendungsdatumDesc() {
        List<Startup> startupList = startupRepository.findTop5ByOrderByGruendungsdatumDesc();

        List<StartupDTO> startupDTOList = new ArrayList<>();

        for(Startup startup : startupList) {
            startupDTOList.add(StartupMapper.mapToStartupDto(startup));
        }

        logger.info("Die fünf neusten Startups wurden aufgelistet.");
        return startupDTOList;
    }

    @Transactional
    public StartupDTO updateStartup(StartupDTO startupDTO) {

        Startup oldStartup = getStartupById(startupDTO.getId());

        Branche newBranche = getBrancheById(startupDTO.getBranche());

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
            logger.info("Startup gelöscht: {}", startupDTO);
            return true;
        } catch (Exception e) {
            logger.error("Fehler beim Löschen des Startups mit ID {}: {}", startupDTO.getId(), e.getMessage());
            throw new StartUpException("Startup konnte nicht gelöscht werden.", e);
        }
    }


    @Transactional
    public BrancheDTO createBranche(BrancheDTO brancheDTO) {

        Branche newBranche = BrancheMapper.mapToBranche(brancheDTO);

        Branche savedBranche = brancheRepository.save(newBranche);

        logger.info("Branche mit der Bezeichnung {} erstellt.", savedBranche.getStartups());
        return BrancheMapper.mapToBrancheDTO(savedBranche);
    }

    public Branche getBrancheById(Long id) {
        return brancheRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Branche mit ID {} nicht gefunden.", id);
                    return new StartUpException("Branche mit der " + id + " nicht gefunden.");
                });
    }

    public List<BrancheDTO> getBranches() {
        List<Branche> brancheList = brancheRepository.findAll();
        List<BrancheDTO> brancheDTOList = new ArrayList<>();
        for(Branche branche : brancheList) {
            brancheDTOList.add(BrancheMapper.mapToBrancheDTO(branche));
        }

        logger.info("Branchen zurückgegeben.");
        return brancheDTOList;
    }

    private Startup getStartupById(Long id) {
        return startupRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Startup mit ID {} nicht gefunden.", id);
                    return new StartUpException("Startup mit der ID " + id + " wurde nicht gefunden.");
                });
    }

    public String getBrancheNameById(Long id) {
        return brancheRepository.findById(id)
                .map(Branche::getBezeichnung)
                .orElse("Unbekannt");
    }

    @Transactional
    public void neuerMitarbeiter(Long studentId, Long startupId) throws Exception {

        Student student = studentRepository.findById(studentId).get();
        Startup startup = startupRepository.findById(startupId).get();

        if(student.getStartup() != null) {
            student.setStartup(startup);
            studentRepository.save(student);
        } else {
            throw new Exception("Student arbeitet schon bei einem Startup.");
        }

        if (startup.getMitarbeiterList() == null) {
            startup.setMitarbeiterList(new ArrayList<>());
        }
        startup.getMitarbeiterList().add(student);
        startup.setAnzahlMitarbeiter(startup.getAnzahlMitarbeiter()+1);
        startupRepository.save(startup);
    }

    @Transactional
    public void mitarbeiterEntfernen(Long studentId, Long startupId) {

        Student student = studentRepository.findById(studentId).get();
        Startup startup = startupRepository.findById(startupId).get();

        if(student.getStartup().equals(startup)) {
            student.setStartup(null);
        }
        studentRepository.save(student);
        startup.setAnzahlMitarbeiter(startup.getAnzahlMitarbeiter()-1);
        startupRepository.save(startup);
    }
}
