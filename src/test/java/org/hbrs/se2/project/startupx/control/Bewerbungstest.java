package org.hbrs.se2.project.startupx.control;


import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class Bewerbungstest {

    @Autowired
    ManageStartupControl manageStartupControl;

    @Autowired
    JobApplicationControl jobApplicationControl;

    @Autowired
    StellenausschreibungControl stellenausschreibungControl;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    private StartupRepository startupRepository;

    @Test
    public void test() {
        Student student = studentRepository.findById(20L).get();

        StartupDTO startupDTO = manageStartupControl.findByID(28L);

        List<StellenausschreibungDTO> list = stellenausschreibungControl.getAllStellenausschreibungByStartup(startupDTO);

        BewerbungDTO bewerbungDTO = BewerbungDTO.builder()
                .bewerbungsschreiben("TestSchreiben")
                .stellenausschreibungen(list.get(0).getId())
                .student(student.getId())
                .build();

        jobApplicationControl.applyForJob(bewerbungDTO);

        List<BewerbungDTO> alle = jobApplicationControl.getApplicationsByStartup(28L);

        assertEquals(1, alle.size());

        Long id = alle.get(0).getId();

        jobApplicationControl.accept(id);

        StartupDTO startupDTO1 = manageStartupControl.findByID(28L);

        assertEquals(1, startupDTO1.getMitarbeiterList().size());

    }

}
