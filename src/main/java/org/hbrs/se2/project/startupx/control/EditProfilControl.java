package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditProfilControl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public boolean updateUser(UserDTO newUserDTO) {
        // Fachliche Validierung: Eindeutigkeit prüfen
        checkValidUser(newUserDTO);

        // Benutzer laden
        User existingUser = userRepository.findById(newUserDTO.getId()).orElse(null);
        if (existingUser == null) {
            throw new IllegalArgumentException("Benutzer konnte nicht gefunden werden.");
        }

        // Felder ändern
        existingUser.setNutzername(newUserDTO.getNutzername());
        existingUser.setVorname(newUserDTO.getVorname());
        existingUser.setNachname(newUserDTO.getNachname());
        existingUser.setEmail(newUserDTO.getEmail());
        existingUser.setGeburtsdatum(newUserDTO.getGeburtsdatum());
        existingUser.setPasswort(newUserDTO.getPasswort());

        try {
            userRepository.save(existingUser);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Das Profil konnte nicht gespeichert werden. Bitte versuche es erneut.", e);
        }
    }

    @Transactional
    public boolean updateStudent(UserDTO newUserDTO, Student newStudentDTO) {
        if (!updateUser(newUserDTO)) {
            return false;
        }
        Student existingStudent = studentRepository.findById(newStudentDTO.getId()).orElse(null);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student konnte nicht gefunden werden.");
        }

        existingStudent.setSkills(newStudentDTO.getSkills());
        existingStudent.setSteckbrief(newStudentDTO.getSteckbrief());
        existingStudent.setStudiengang(newStudentDTO.getStudiengang());

        try {
            studentRepository.save(existingStudent);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Student konnte nicht gespeichert werden.");
        }
    }

    private void checkValidUser(UserDTO dto) {
        User emailOwner = userRepository.findByEmail(dto.getEmail());
        if (emailOwner != null && !emailOwner.getId().equals(dto.getId())) {
            throw new IllegalArgumentException("Diese E-Mail-Adresse wird bereits verwendet.");
        }

        User usernameOwner = userRepository.findByNutzername(dto.getNutzername());
        if (usernameOwner != null && !usernameOwner.getId().equals(dto.getId())) {
            throw new IllegalArgumentException("Dieser Benutzername ist bereits vergeben.");
        }
    }
}
