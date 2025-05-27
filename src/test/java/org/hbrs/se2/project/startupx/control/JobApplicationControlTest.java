package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.repository.BewerbungRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class JobApplicationControlTest {
    @Autowired
    JobApplicationControl control;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    BewerbungRepository bewerbungRepository;

    //Set.Up Bewerbung
    BewerbungDTO short_bewerbung;       // Zu Kurz
    BewerbungDTO bewerbung;             // Hat alles

    Optional<Student> wrapper_student;
    Student student;

    Optional<Stellenausschreibung> wrapper_ausschreibung;
    Stellenausschreibung auschreibung;

    @Autowired
    private StellenausschreibungRepository stellenausschreibungRepository;

    @BeforeEach
    void setUp() {
        short_bewerbung = new BewerbungDTO();
        bewerbung = new BewerbungDTO();


        //Fehlerhafte DTO-Werte zuweisen
        short_bewerbung.setBewerbungsschreiben("kurz");

        //Richtige Vollwertige Bewerbung
        wrapper_student = studentRepository.findById(2L);
        if(wrapper_student.isPresent()) {
            student = wrapper_student.get();
        }

        wrapper_ausschreibung = stellenausschreibungRepository.findById(1L);
        if(wrapper_ausschreibung.isPresent()) {
            auschreibung = wrapper_ausschreibung.get();
        }

        bewerbung.setStudent(student.getId());
        bewerbung.setStellenausschreibungen(auschreibung.getId());
        bewerbung.setBewerbungsschreiben("Kenntnise in Java und C++");

       // Bewerbung b = new Bewerbung(bewerbung.getId(), student, auschreibung, bewerbung.getBewerbungsschreiben());
       // bewerbungRepository.save(b);
    }

    @Test
    void applyForJob() {
        assertFalse(control.applyForJob(short_bewerbung));      // Liefert False da die Bewerbung unter 5 Zeichen enthält
        assertTrue(control.applyForJob(bewerbung));             // Muss True-liefern
    }
}