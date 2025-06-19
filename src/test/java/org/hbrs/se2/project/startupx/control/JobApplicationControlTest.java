package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;
import org.hbrs.se2.project.startupx.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class JobApplicationControlTest {

    @Autowired
    JobApplicationControl jobApplicationControl;

    @Autowired
    StellenausschreibungControl stellenausschreibungControl;

    @Autowired
    StellenausschreibungRepository stellenausschreibungRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    private StudentRepository studentRepository;

    BewerbungDTO bewerbungDTO;             // Hat alles

    Long skillId;

    List<Stellenausschreibung> stellenausschreibungList;
    Stellenausschreibung stellenausschreibung;
    StellenausschreibungDTO stellenausschreibungDTO;

    @BeforeEach
    void setUp() {

        skillId = skillRepository.findById(1L).get().getId();

        stellenausschreibungDTO = StellenausschreibungDTO.builder()
                .id(1L).bewerbungen(null).beschreibung("TestAusschreibung").titel("Titel").skills(Collections.singleton(skillId)).startup(10L).status(Status.OFFEN).build();

    }

    @Test
    void applyForJob() {
        stellenausschreibungControl.createStellenausschreibung(stellenausschreibungDTO);

        stellenausschreibungList = stellenausschreibungRepository.findByStartup_IdAndStatus(10L,Status.OFFEN);

        for(Stellenausschreibung s : stellenausschreibungList) {
            if(s.getBeschreibung().equals("TestAusschreibung")) {
                stellenausschreibung = s;
            }
        }

        bewerbungDTO = BewerbungDTO.builder().id(1L).stellenausschreibungen(stellenausschreibung.getId()).status(BewerbungsStatus.OFFEN).student(2L).bewerbungsschreiben("JunitTest").build();

        assertTrue(jobApplicationControl.applyForJob(bewerbungDTO));

        List<BewerbungDTO> bewerbungBeiStellenanzeige = jobApplicationControl.getApplicationByStellenausschreibung(stellenausschreibung.getId());

        assertEquals(1,bewerbungBeiStellenanzeige.size());
    }
}