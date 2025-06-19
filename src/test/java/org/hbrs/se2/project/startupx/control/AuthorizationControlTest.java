package org.hbrs.se2.project.startupx.control;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.mapper.UserMapper;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.hbrs.se2.project.startupx.util.Globals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class AuthorizationControlTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationControl control;


    User testUser;
    UserDTO u_dto;


    //SetUp für die Tests
    @BeforeEach
    void setUp() {

        //User "Testi Mesti" aus der DB holen
        testUser = userRepository.findById(31L).get();

        //UserDTO Anlegen
        u_dto = UserMapper.mapToUserDto(testUser);
    }

    //Tests
    @Test
    void isUserInRole() {
        //Testen ob die ID auch zu User assoziert wird
        assertTrue(control.isUserInRole(u_dto, "user"));

        //Testen ob er auch wirklich nur User ist
        assertFalse(control.isUserInRole(u_dto, "admin"));

        //Testen ob er nach hinzufügen der Rolle diese auch wirklich hat
        u_dto.setRollen(Set.of(1L));
        assertTrue(control.isUserInRole(u_dto, "admin"));

        //Testen das die DTO nur eine Rolle gleichzeitig haben kann
        boolean result = (control.isUserInRole(u_dto, "user")) && (control.isUserInRole(u_dto, "admin"));
        assertFalse(result);
    }

    @Test
    void isUserisAllowedToAccessThisFeature() {
        String feature_main = Globals.Pages.MAIN_VIEW;
        boolean result= control.isUserisAllowedToAccessThisFeature(u_dto, "user", feature_main, null);

        assertFalse(result);
    }
}