package org.hbrs.se2.project.startupx.control;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
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
@RequestMapping("/api/startup")
public class StartupControl {

    private static final Logger logger = LoggerFactory.getLogger(StartupControl.class);

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private BrancheRepository brancheRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    @PostMapping("/createStartup")
    public ResponseEntity<StartupDTO> createStartup(@RequestBody StartupDTO startupDTO) {

        Set<Student> studentList = new LinkedHashSet<>();

        for(Long studentIds : startupDTO.getStudentenListe()) {
            studentList.add(studentRepository.findById(studentIds)
                    .orElseThrow(() -> new EntityNotFoundException("Student mit der ID " + studentIds + " nicht gefunden")));
        }

        Branche branche = brancheRepository.findById(startupDTO.getBranche())
                .orElseThrow(() -> new EntityNotFoundException("Branche mit der ID " + startupDTO.getBranche() + " nicht gefunden"));

        Startup startup = Startup.builder()
                .name(startupDTO.getName())
                .branche(branche)
                .beschreibung(startupDTO.getBeschreibung())
                .gruendungsdatum(startupDTO.getGruendungsdatum())
                .anzahlMitarbeiter(startupDTO.getAnzahlMitarbeiter())
                .studentenListe(studentList)
                .build();

        Startup savedStartup = startupRepository.save(startup);

        return new ResponseEntity<>(StartupMapper.mapToStartupDto(savedStartup), HttpStatus.OK);
    }

//    @GetMapping("/getall")
//    public List<StartupDTO> findAll() {
//        List<Startup> startupList = startupRepository.findAll();
//
//        List<StartupDTO> startupDTOList = new ArrayList<>();
//        for(Startup startup : startupList) {
//            startupDTOList.add(StartupMapper.INSTANCE.mapToStartupDto(startup));
//        }
//
//        return startupDTOList;
//    }
//
//    public List<StartupDTO> findByBranche(BrancheDTO brancheDTO) {
//
//        List<Startup> startupList = startupRepository.findByBranche(BrancheMapper.INSTANCE.mapToBranche(brancheDTO));
//
//        List<StartupDTO> startupDTOList = new ArrayList<>();
//        for(Startup startup : startupList) {
//            startupDTOList.add(StartupMapper.INSTANCE.mapToStartupDto(startup));
//        }
//
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
//            startupDTOList.add(StartupMapper.INSTANCE.mapToStartupDto(startup));
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
//            startupDTOList.add(StartupMapper.INSTANCE.mapToStartupDto(startup));
//        }
//
//        return startupDTOList;
//    }
//
//    @Transactional
//    public StartupDTO updateStartup(StartupDTO startupDTO) {
//
//        Startup oldStartup = startupRepository.findById(startupDTO.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Startup " + startupDTO.getId() + " not found"));
//
//        Branche newBranche = brancheRepository.findById(startupDTO.getBranche())
//                .orElseThrow(() -> new EntityNotFoundException("Branche " + startupDTO.getBranche() + " not found"));
//
//
//        oldStartup.setName(startupDTO.getName());
//        oldStartup.setBranche(newBranche);
//        oldStartup.setBeschreibung(startupDTO.getBeschreibung());
//        oldStartup.setAnzahlMitarbeiter(startupDTO.getAnzahlMitarbeiter());
//
//        startupRepository.save(oldStartup);
//
//        return StartupMapper.INSTANCE.mapToStartupDto(oldStartup);
//    }
//
//    public Set<StartupDTO> getStartups(StudentDTO studentDTO) {
//
//        return studentDTO.getStartups();
//    }
}
