package org.hbrs.se2.test.controll;
import org.hbrs.se2.project.startupx.control.AuthorizationControl;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Rolle;
import org.hbrs.se2.project.startupx.repository.RolleRepository;


import org.hbrs.se2.project.startupx.util.Globals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class AuthorizationControlTest {

    //Set-Up-Rolle+User Instanzen
    Rolle r_user = new Rolle();
    Rolle r_admin = new Rolle();
    UserDTO user;
    UserDTO user_admin;

    //Test-'Architektur'-SetUp
    @Mock
    private RolleRepository rolleRepository;

    @InjectMocks
    private AuthorizationControl control;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        //Role-SetUp
        r_user.setBezeichnung("USER");
        r_admin.setBezeichnung("ADMIN");

        //Seting-Up Users
        user = UserDTO.builder()
                .id(808L)
                .nutzername("felix")
                .rollen(Set.of(1L))
                .build();

        user_admin = UserDTO.builder()
                .id(161L)
                .nutzername("xilef")
                .rollen(Set.of(2L))
                .build();

        //"Verbinden" von einer id mit einer Rollen Entity
        when(rolleRepository.findById(1L)).thenReturn(Optional.of(r_user));
        when(rolleRepository.findById(2L)).thenReturn(Optional.of(r_admin));
    }

    @Test
    void testIsUserInRole(){
        //Positive-Test
        assertTrue(control.isUserInRole(user, "USER"));

        //Negativ-Test
        assertFalse(control.isUserInRole(user, "ADMIN"));

        //Positiv-Test
        assertTrue(control.isUserInRole(user_admin, "ADMIN"));
    }
}

