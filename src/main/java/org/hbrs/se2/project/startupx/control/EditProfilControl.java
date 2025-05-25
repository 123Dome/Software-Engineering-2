package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.control.exception.EditProfilException;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.StudiengangRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
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
        Student existingStudent = studentRepository.findById(userDTO.getStudents()).orElse(null);
        if (existingStudent == null) {
            throw new EditProfilException("Student nicht gefunden.");
        }
        return StudentMapper.mapToStudentDto(existingStudent);

    }
}
