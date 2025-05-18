package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.RegistrationException;
import org.hbrs.se2.project.startupx.dtos.RolleDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.RolleMapper;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.RolleRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class RegistrationControl {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private RolleRepository rolleRepository;

    public void registerUser(UserDTO userDTO) {
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

        userRepository.save(newUser);
    }

    // Für Echtzeitüberprüfung, ob Email existiert
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
