package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.RegistrationException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationControl {

    @Autowired
    UserRepository userRepository;

    public void registerUser(UserDTO userDTO) {
        // E-Mail darf nicht existieren
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RegistrationException("E-Mail existiert bereits.");
        }
        // Nutzername darf nicht existieren
        if(userRepository.findByNutzername(userDTO.getNutzername()) != null) {
            throw new RegistrationException("Nutzername existiert bereits.");
        }

        User newUser = UserMapper.INSTANCE.mapToUser(userDTO);
        userRepository.save(newUser);
    }

    // Für Echtzeitüberprüfung, ob Email existiert
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
