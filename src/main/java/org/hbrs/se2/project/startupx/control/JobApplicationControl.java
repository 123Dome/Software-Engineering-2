package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.mapper.BewerbungMapper;
import org.hbrs.se2.project.startupx.repository.BewerbungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Control-Klasse zur Verwaltung von Bewerbungen.
 * Bietet Methoden zum Erstellen und Abfragen von Bewerbungen auf Stellenausschreibungen.
 * zum aktuellen Zeitpunkt waren noch nicht alle Views fertig daher muss hier noch später etwas ergänzt werden
 */
@Service
public class JobApplicationControl {

    private final BewerbungRepository bewerbungRepository;
    private final StudentRepository studentRepository;
    private final StellenausschreibungRepository stellenausschreibungRepository;

    @Autowired
    public JobApplicationControl(BewerbungRepository bewerbungRepository,
                                 StudentRepository studentRepository,
                                 StellenausschreibungRepository stellenausschreibungRepository) {
        this.bewerbungRepository = bewerbungRepository;
        this.studentRepository = studentRepository;
        this.stellenausschreibungRepository = stellenausschreibungRepository;
    }

    @Transactional
    public boolean applyForJob(BewerbungDTO bewerbungDTO) {
        if (bewerbungDTO.getBewerbungsschreiben() == null
                || bewerbungDTO.getBewerbungsschreiben().replaceAll("\\s", "").length() < 5) {
            return false;
        }

        Optional<Student> studentOpt = studentRepository.findById(bewerbungDTO.getStudent());
        Optional<Stellenausschreibung> jobOpt = stellenausschreibungRepository.findById(bewerbungDTO.getStellenausschreibungen());

        if (studentOpt.isEmpty() || jobOpt.isEmpty()) {
            return false;
        }

        boolean alreadyExists = bewerbungRepository
                .findAll()
                .stream()
                .anyMatch(b -> b.getStudent().getId().equals(bewerbungDTO.getStudent())
                        && b.getStellenausschreibung().getId().equals(bewerbungDTO.getStellenausschreibungen()));

        if (alreadyExists) {
            return false;
        }

        Bewerbung bewerbung = Bewerbung.builder()
                .student(studentOpt.get())
                .stellenausschreibung(jobOpt.get())
                .bewerbungsschreiben(bewerbungDTO.getBewerbungsschreiben())
                .build();

        bewerbungRepository.save(bewerbung);
        return true;
    }


    public List<BewerbungDTO> getApplicationsByStudent(Long studentId) {
        return bewerbungRepository.findAll()
                .stream()
                .filter(b -> b.getStudent().getId().equals(studentId))
                .map(BewerbungMapper::toDTO)
                .toList();
    }

    public List<BewerbungDTO> getApplicationsByStartup(Long startupId) {
        return bewerbungRepository.findAll()
                .stream()
                .filter(b -> b.getStellenausschreibung().getStartup().getId().equals(startupId))
                .map(BewerbungMapper::toDTO)
                .toList();
    }
}
