package org.hbrs.se2.test.controll;
import org.hbrs.se2.project.startupx.control.JobApplicationControl;
import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.repository.BewerbungRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.*;

public class JobApplicationControlTest {
    //Set-UP
    BewerbungDTO b_dto; //Normales Bewerbungs-DTO
    BewerbungDTO b_ni_dto; //Bewerbungs-DTO ohne Inhalt
    BewerbungDTO b_ns_dto; //Bewerbungs-DTO mit Inhalt aber ohne Student
    Stellenausschreibung stellenausschreibung;
    Bewerbung b;
    List<BewerbungDTO> expectedList;
    Student student;
    Startup unicorn;

    @Mock
    private BewerbungRepository bewerbungRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StellenausschreibungRepository stellenausschreibungRepository;

    @InjectMocks
    private JobApplicationControl control;

    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        student = Student.builder()
                .id(500L)
                .build();

        unicorn = Startup.builder()
                .name("KaffeSenso")
                .id(24L)
                .build();

        stellenausschreibung = Stellenausschreibung.builder()
                .id(122L)
                .titel("Kaffe-StartUp")
                .beschreibung("Innovatie Kaffebohnen mit neuer Mahltechnologie!!!")
                .startup(unicorn)
                .build();

        b_ni_dto = BewerbungDTO.builder()
                .bewerbungsschreiben(null)
                .build();

        b_ns_dto = BewerbungDTO.builder()
                .id(55L)
                .bewerbungsschreiben("Ich bin unfassbar gut in WebEntwicklung!")
                .student(null)
                .build();

        b_dto = BewerbungDTO.builder()
                .id(24L)
                .student(500L)
                .stellenausschreibungen(122L)
                .bewerbungsschreiben("Extremly good")
                .build();

        b = new Bewerbung(b_dto.getId(), student, stellenausschreibung, b_dto.getBewerbungsschreiben());

        expectedList = List.of(
                BewerbungDTO.builder()
                        .id(b.getId())
                        .student(b.getStudent().getId())
                        .stellenausschreibungen(b.getStellenausschreibung().getId())
                        .bewerbungsschreiben(b.getBewerbungsschreiben())
                        .build()
        );

        when(studentRepository.findById(500L)).thenReturn(Optional.of(student));
        when(bewerbungRepository.findAll()).thenReturn(List.of(b));
    }

    @Test
    void testApplyForJob(){
        assertFalse(control.applyForJob(b_ni_dto));
        assertFalse(control.applyForJob(b_ns_dto));
        assertFalse(control.applyForJob(b_dto));
    }

    @Test
    void testGetApplicationsByStudent(){
        List<BewerbungDTO> actual = control.getApplicationsByStudent(500L);
        assertNotSame(expectedList, actual);
    }

    @Test
    void testGetApplicationByStartup(){
        List<BewerbungDTO> actual = control.getApplicationsByStartup(24L);
        assertNotSame(expectedList, actual);
    }
}
