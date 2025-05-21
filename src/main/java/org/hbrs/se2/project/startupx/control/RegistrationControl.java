package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.RegistrationException;
import org.hbrs.se2.project.startupx.dtos.RolleDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.RolleRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.repository.StudiengangRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class RegistrationControl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private StudiengangRepository studiengangRepository;

    @Autowired
    private StudentRepository studentRepository;

    public User registerUser(UserDTO userDTO) {
        // E-Mail darf nicht existieren
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RegistrationException("E-Mail existiert bereits.");
        }
        // Nutzername darf nicht existieren
        if(userRepository.findByNutzername(userDTO.getNutzername()) != null) {
            throw new RegistrationException("Nutzername existiert bereits.");
        }
        // Jeder User hat erstmal die Standardrolle User
        Rolle defaultRolle = rolleRepository.findByBezeichnung("user");

        if (defaultRolle == null) {
            throw new RegistrationException("Rolle 'user' nicht gefunden.");
        }

        Set<Rolle> defaultRollen = new LinkedHashSet<>();
        defaultRollen.add(defaultRolle);



        Set<Kommentar> kommentarList = new LinkedHashSet<>();
        Student student = null;

        User newUser = UserMapper.mapToUser(userDTO, defaultRollen, kommentarList, student);

        newUser.getRollen().add(defaultRolle);

        return userRepository.save(newUser);
    }

    public void registerStudent(UserDTO userDTO, StudentDTO studentDTO) {
        // E-Mail darf nicht existieren
        User newUser = registerUser(userDTO);

        if (studentRepository.findByMatrikelnr(studentDTO.getMatrikelnr()) != null) {
            throw new RegistrationException("Matrikelnr existiert bereits.");
        }

        Set<Bewerbung> bewerbungSet = new LinkedHashSet<>();
        Set<Skill> skillSet = new LinkedHashSet<>();
        Studiengang studiengang = studiengangRepository.getReferenceById(studentDTO.getStudiengang());
        Set<Startup> startupSet = new LinkedHashSet<>();

        Student newStudent = StudentMapper.mapToStudent(studentDTO, bewerbungSet, skillSet, startupSet, newUser, studiengang);

        studentRepository.save(newStudent);

    }

    // Für Echtzeitüberprüfung, ob Email existiert
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
