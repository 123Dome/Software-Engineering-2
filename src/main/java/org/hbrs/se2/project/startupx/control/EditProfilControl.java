package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.EditProfilException;
import org.hbrs.se2.project.startupx.control.exception.StartUpException;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.hbrs.se2.project.startupx.mapper.StartupMapper;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class EditProfilControl {

    private static final Logger logger = LoggerFactory.getLogger(EditProfilControl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudiengangRepository studiengangRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StartupRepository startupRepository;

    @Autowired
    ManageStartupControl manageStartupControl;

    @Transactional
    public boolean updateUser(UserDTO newUserDTO) {
        checkValidUser(newUserDTO);

        User existingUser = userRepository.findById(newUserDTO.getId()).orElse(null);
        if (existingUser == null) {
            throw new EditProfilException("Benutzer konnte nicht gefunden werden.");
        }

        existingUser.setNutzername(newUserDTO.getNutzername());
        existingUser.setVorname(newUserDTO.getVorname());
        existingUser.setNachname(newUserDTO.getNachname());
        existingUser.setEmail(newUserDTO.getEmail());
        existingUser.setGeburtsdatum(newUserDTO.getGeburtsdatum());
        existingUser.setPasswort(newUserDTO.getPasswort());

        try {
            userRepository.save(existingUser);
            logger.info("Benutzerprofil aktualisiert: ID={}", existingUser.getId());
            return true;
        } catch (Exception e) {
            logger.error("Fehler beim Speichern des Benutzerprofils", e);
            throw new EditProfilException("Das Profil konnte nicht gespeichert werden. Bitte versuche es erneut.", e);
        }
    }

    private void checkValidUser(UserDTO dto) {
        User emailOwner = userRepository.findByEmail(dto.getEmail());
        if (emailOwner != null && !emailOwner.getId().equals(dto.getId())) {
            throw new EditProfilException("Diese E-Mail-Adresse wird bereits verwendet.");
        }

        User usernameOwner = userRepository.findByNutzername(dto.getNutzername());
        if (usernameOwner != null && !usernameOwner.getId().equals(dto.getId())) {
            throw new EditProfilException("Dieser Benutzername ist bereits vergeben.");
        }
    }

    @Transactional
    public boolean updatePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EditProfilException("Benutzer nicht gefunden.");
        }

        User user = optionalUser.get();

        if (!user.getPasswort().equals(currentPassword)) {
            logger.warn("Passwortänderung fehlgeschlagen: falsches aktuelles Passwort für Benutzer ID={}", userId);
            return false;
        }

        user.setPasswort(newPassword);
        userRepository.save(user);
        logger.info("Passwort für Benutzer ID={} wurde geändert.", userId);
        return true;
    }

    @Transactional
    public boolean updateStudent(UserDTO newUserDTO, StudentDTO newStudentDTO) {
        if (!updateUser(newUserDTO)) {
            return false;
        }

        Student existingStudent = studentRepository.findById(newStudentDTO.getId()).orElse(null);
        if (existingStudent == null) {
            throw new EditProfilException("Student konnte nicht gefunden werden.");
        }

        Set<Skill> skills = new LinkedHashSet<>();
        for (Long skillId : newStudentDTO.getSkills()) {
            Skill skill = skillRepository.findById(skillId).orElse(null);
            if (skill != null) {
                skills.add(skill);
            } else {
                logger.warn("Skill mit ID={} nicht gefunden, wird ignoriert.", skillId);
            }
        }


        Studiengang newStudiengang = studiengangRepository.findById(newStudentDTO.getStudiengang()).orElse(null);
        if (newStudiengang == null) {
            throw new EditProfilException("Studiengang konnte nicht gefunden werden.");
        }

        existingStudent.setSkills(skills);
        existingStudent.setSteckbrief(newStudentDTO.getSteckbrief());
        existingStudent.setStudiengang(newStudiengang);

        try {
            studentRepository.save(existingStudent);
            logger.info("Studentenprofil aktualisiert: ID={}", existingStudent.getId());
            return true;
        } catch (Exception e) {
            logger.error("Fehler beim Speichern des Studentenprofils", e);
            throw new EditProfilException("Student konnte nicht gespeichert werden.", e);
        }
    }

    public StudentDTO getStudentDTO(UserDTO userDTO) {
        Student existingStudent = studentRepository.findById(userDTO.getStudent()).orElse(null);
        if (existingStudent == null) {
            throw new EditProfilException("Student nicht gefunden.");
        }
        return StudentMapper.mapToStudentDto(existingStudent);

    }

    @Transactional
    public void deleteStudent(UserDTO userDTO, StudentDTO studentDTO) {
        /*
         * Reihenfolge:
         * 1. TODO: delete Relationships of studentDTO (Startups, Bewerbungen, Kommentare etc.)
         * 2. delete Student
         * 3. delete User
         */
        Student studentToRemove = studentRepository.findById(userDTO.getStudent()).orElse(null);
        if (studentToRemove == null) {
            throw new EditProfilException("Student konnte nicht gefunden.");
        }
        Set<Long> startups = studentDTO.getStartups(); // for each of these startups, remove the student
        for (Long startupId : startups) {
            // remove student from startup
            Startup startup = startupRepository.findById(startupId).orElse(null);
            if (startup == null) {
                throw new StartUpException("Startup konnte nicht gefunden werden.");
            }
            Set<Student> studenten = startup.getStudentenListe();
            studenten.remove(studentToRemove);
            // update startup
            if (studenten.isEmpty()) {
                StartupDTO startupDTO = StartupMapper.mapToStartupDto(startup);
                manageStartupControl.deleteStartup(startupDTO); // remove startup as well
            } else {
                // update startup with new studentenliste
                startup.setStudentenListe(studenten);
                try {
                    startupRepository.save(startup);
                    logger.info("Startup: ID={} wurde Student: ID={} entfernt", startup.getId(), studentDTO.getId());
                } catch (Exception e) {
                    logger.error("Fehler beim Speichern vom Startup", e);
                    throw new EditProfilException("Startup konnte nicht gespeichert werden.", e);
                }
            }
        }
        studentRepository.deleteById(studentDTO.getId());
        logger.info("Studentenprofil gelöscht: ID={}", studentDTO.getId());
        userRepository.deleteById(userDTO.getId());
        logger.info("User gelöscht: ID={}", userDTO.getId());
    }
}
