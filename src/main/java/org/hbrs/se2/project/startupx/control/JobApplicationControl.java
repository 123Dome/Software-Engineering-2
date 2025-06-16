package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.mapper.BewerbungMapper;
import org.hbrs.se2.project.startupx.repository.BewerbungRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Control-Klasse zur Verwaltung von Bewerbungen.
 * Bietet Methoden zum Erstellen und Abfragen von Bewerbungen auf Stellenausschreibungen.
 * zum aktuellen Zeitpunkt waren noch nicht alle Views fertig daher muss hier noch später etwas ergänzt werden
 */
@Service
@Transactional
public class JobApplicationControl {

    @Autowired
    private BewerbungRepository bewerbungRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StellenausschreibungRepository stellenausschreibungRepository;
    @Autowired
    private ManageStartupControl manageStartupControl;
    @Autowired
    private StartupRepository startupRepository;
    @Autowired
    private StellenausschreibungControl stellenausschreibungControl;


    @Transactional
    public boolean applyForJob(BewerbungDTO bewerbungDTO) {
        bewerbungDTO.setStatus(BewerbungsStatus.OFFEN);

        Optional<Student> studentOpt = studentRepository.findById(bewerbungDTO.getStudent());
        if (studentOpt.isEmpty()) {
            System.err.println("Fehler: Student mit ID " + bewerbungDTO.getStudent() + " nicht gefunden.");
            return false;
        }
        Student student = studentOpt.get();

        Optional<Stellenausschreibung> jobOpt = stellenausschreibungRepository.findById(bewerbungDTO.getStellenausschreibungen());
        if (jobOpt.isEmpty()) {
            System.err.println("Fehler: Stellenausschreibung mit ID " + bewerbungDTO.getStellenausschreibungen() + " nicht gefunden.");
            return false;
        }
        Stellenausschreibung stellenausschreibung = jobOpt.get();

        Bewerbung bewerbung = BewerbungMapper.toEntity(bewerbungDTO,student,stellenausschreibung);

        try {
            bewerbungRepository.save(bewerbung);
            return true;
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern der Bewerbung: " + e.getMessage());
            return false;
        }
    }

    public void deleteBewerbung(BewerbungDTO bewerbungDTO) {
        bewerbungRepository.deleteById(bewerbungDTO.getId());
    }

    public List<BewerbungDTO> getApplicationByStellenausschreibung(Long stellenausschreibungId) {
        List<Bewerbung> bewerbungList = bewerbungRepository.findByStellenausschreibung_Id(stellenausschreibungId);

        return bewerbungList.stream().map(BewerbungMapper::toDTO).collect(Collectors.toList());
    }

    public List<BewerbungDTO> getApplicationsByStudent(Long studentId) {
        return bewerbungRepository.findAll()
                .stream()
                .filter(b -> b.getStudent().getId().equals(studentId))
                .map(BewerbungMapper::toDTO)
                .toList();
    }

    public BewerbungDTO getApplicationByStudentAndStellenausschreibung(Long studentId, Long stellenausschreibungId) {
        return BewerbungMapper.toDTO(bewerbungRepository.findBewerbungByStudent_IdAndStellenausschreibung_Id(studentId, stellenausschreibungId));
    }

    public List<BewerbungDTO> getApplicationsByStartup(Long startupId) {
        return bewerbungRepository.findAll()
                .stream()
                .filter(b -> b.getStellenausschreibung().getStartup().getId().equals(startupId))
                .map(BewerbungMapper::toDTO)
                .toList();
    }

    @Transactional
    public boolean accept(Long bewerbungsId, Long stellenausschreibungId) {
        try {
            Bewerbung bewerbung = bewerbungRepository.findById(bewerbungsId).get();
            bewerbung.setStatus(BewerbungsStatus.ANGENOMMEN);
            bewerbungRepository.save(bewerbung);
            Student student = studentRepository.findById(bewerbung.getStudent().getId()).get();
            Startup startup = startupRepository.findById(bewerbung.getStellenausschreibung().getStartup().getId()).get();
            manageStartupControl.neuerMitarbeiter(student.getId(), startup.getId());
            stellenausschreibungControl.closeStellenausschreibung(stellenausschreibungId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean decline(Long bewerbungsId) {
        try {
            Bewerbung bewerbung = bewerbungRepository.findById(bewerbungsId).get();
            bewerbung.setStatus(BewerbungsStatus.ABGELEHNT);
            bewerbungRepository.save(bewerbung);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
