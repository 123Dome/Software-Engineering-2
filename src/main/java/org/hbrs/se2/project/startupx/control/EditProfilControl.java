package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class EditProfilControl {

    @Autowired
    UserRepository userRepository;

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
}
