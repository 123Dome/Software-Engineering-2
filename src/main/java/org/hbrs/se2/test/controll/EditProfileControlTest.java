package org.hbrs.se2.test.controll;

import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.*;

public class EditProfileControlTest {

    //Set-Up der TestKlasse
    UserDTO n_user;

    User realUser;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EditProfilControl control;

    @BeforeMethod
    void setUp(){
        MockitoAnnotations.openMocks(this);

        //Seting-Up Users
        n_user = UserDTO.builder()
                .id(404L)
                .nutzername("felixxx")
                .rollen(Set.of(1L))
                .build();

        realUser = new User();
        realUser.setId(n_user.getId());
        when(userRepository.findById(404L)).thenReturn(Optional.of(realUser));
        // Mock save einfach so, dass es nichts tut bzw. das Objekt zurückgibt
        when(userRepository.save(any(User.class))).thenReturn(realUser);
    }

    @Test
    void testUpdateUser(){
        assertNotEquals(realUser.getNutzername(), n_user.getNutzername());

        boolean result = control.updateUser(n_user);
        assertTrue(result);

        assertEquals(realUser.getNutzername(), n_user.getNutzername());
    }
}
