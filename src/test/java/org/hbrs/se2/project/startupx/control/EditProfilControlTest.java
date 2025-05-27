package org.hbrs.se2.project.startupx.control;
import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class EditProfilControlTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EditProfilControl control;

    //Anlegen der Entity
    Long idTemp;
    User temp_user;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        temp_user = new User();

        /*
        temp_user.setEmail("mail@mail.de");
        temp_user.setNutzername("Testi-Mesti");
        temp_user.setVorname("");
        temp_user.setNachname("");
        temp_user.setPasswort("12345");
        temp_user.setGeburtsdatum(LocalDate.now());

        userRepository.save(temp_user); // Speichern in der DB
        */


        temp_user = userRepository.findUserByNutzername("TestiMesti"); // Erhalten der ID
        idTemp = temp_user.getId();

        //DTO mit welchen wir den User Updaten wollen
        userDTO.setId(idTemp);
        userDTO.setVorname("Max");
        userDTO.setNachname("Mustermann");
        userDTO.setNutzername(temp_user.getNutzername());
        userDTO.setPasswort(temp_user.getPasswort());
        userDTO.setEmail(temp_user.getEmail());
    }

    @Test
    void updateUser() {
        boolean result = control.updateUser(userDTO); //Updaten des Users, wenn das Updaten erfolgreich gibt die Methode True, zurück
        assertTrue(result);
    }
}