package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.SkillRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.StudiengangRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class EditProfilControl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudiengangRepository studiengangRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    StudentRepository studentRepository;

    public User loadUser(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return userRepository.findUserByNutzername(dto.getNutzername());
    }

    @Transactional
    public boolean updateUser(UserDTO newUserDTO) {
        if (checkValidUser(newUserDTO)) {
            User existingUser = userRepository.findById(newUserDTO.getId()).orElse(null);
            if (existingUser != null) {
                try {
                    existingUser.setNutzername(newUserDTO.getNutzername());
                    existingUser.setVorname(newUserDTO.getVorname());
                    existingUser.setGeburtsdatum(newUserDTO.getGeburtsdatum());
                    existingUser.setEmail(newUserDTO.getEmail());
                    existingUser.setNachname(newUserDTO.getNachname());
                    existingUser.setPasswort(newUserDTO.getPasswort());

                    System.out.println("Saving user: " + existingUser);
                    userRepository.save(existingUser);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    private boolean checkValidUser(UserDTO newUserDTO) {
        // TODO: Aktueller Benutzer muss bei der Suche ignoriert werden
//        if(userRepository.existsByEmail(newUserDTO.getEmail())) {
//            throw new IllegalArgumentException("E-Mail existiert bereits.");
//        }
//        if(userRepository.existsByNutzername(newUserDTO.getNutzername())) {
//            throw new IllegalArgumentException("Nutzername existiert bereits.");
//        }
        if(newUserDTO.getGeburtsdatum().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Geburtsdatum ist ungültig.");
        }

        return true;
    }

    @Transactional
    public boolean updatePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Passwortprüfung (hier ohne Hashing – falls du Hashing nutzt, bitte anpassen!)
            if (!user.getPasswort().equals(currentPassword)) {
                return false; // altes Passwort stimmt nicht
            }

            // neues Passwort setzen und speichern
            user.setPasswort(newPassword);
            userRepository.save(user);
            return true;
        }
        return false; // kein User gefunden
    }

    @Transactional
    public boolean updateStudent(UserDTO newUserDTO, StudentDTO newStudentDTO) {
        if (!updateUser(newUserDTO)) {
            return false;
        }
        Student existingStudent = studentRepository.findById(newStudentDTO.getId()).orElse(null);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student konnte nicht gefunden werden.");
        }

        /*
        Set<Skill> existingSkills = new LinkedHashSet<>();
        for (Long skill : newStudentDTO.getSkills()) {
            existingSkills.add(skillRepository.findById(skill).orElse(null));
        }
         */

        Studiengang newStudiengang = studiengangRepository.findById(newStudentDTO.getStudiengang()).orElse(null);

        //existingStudent.setSkills(existingSkills);
        existingStudent.setSteckbrief(newStudentDTO.getSteckbrief());
        existingStudent.setStudiengang(newStudiengang);

        try {
            studentRepository.save(existingStudent);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Student konnte nicht gespeichert werden.");
        }
    }

    public StudentDTO getStudentDTO(UserDTO userDTO) {
        Student existingStudent = studentRepository.findById(userDTO.getStudents()).orElse(null);
        return StudentMapper.mapToStudentDto(existingStudent);
    }
}
