package org.hbrs.se2.test.controll;
import org.hbrs.se2.project.startupx.control.KommentarControl;
import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.*;

public class KommentarControlTest {
    //Set-Up
    User user;
    Startup unicorn;
    KommentarDTO k_dto;
    Kommentar kommentar;
    Kommentar outdatet_kommentar;

    @Mock
    private KommentarRepository kommentarRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StartupRepository startupRepository;

    @InjectMocks
    private KommentarControl control;

    @BeforeMethod
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(500L)
                .build();

        unicorn = Startup.builder()
                .name("KaffeSenso")
                .id(24L)
                .build();

        k_dto = KommentarDTO.builder()
                .id(2L)
                .user(500L)
                .kommentar("Coole Idee")
                .bewertung(3)
                .startup(24L)
                .build();

        outdatet_kommentar = Kommentar.builder()
                .user(user)
                .startup(unicorn)
                .id(2L)
                .build();

        when(userRepository.findById(500L)).thenReturn(Optional.of(user));
        when(startupRepository.findById(24L)).thenReturn(Optional.of(unicorn));
        when(kommentarRepository.findById(2L)).thenReturn(Optional.of(outdatet_kommentar));
    }

    @Test
    void testCreateKommentar(){
        KommentarDTO result = control.createKommentar(k_dto);

        assertSame(result.getId(), k_dto.getId());
    }

    @Test
    void testGetAllKommentarZuStartUp(){
        KommentarDTO new_k;
        new_k = KommentarDTO.builder()
                .id(2L)
                .user(500L)
                .kommentar("Coole Idee, sogar sehr gut!")
                .bewertung(5)
                .startup(24L)
                .build();

        assertNotSame(outdatet_kommentar.getKommentar(), k_dto.getKommentar());
        control.updateKommentar(new_k);
    }

    @Test
    void testDeleteKommentar(){
        assertNotNull(k_dto);
        boolean result = control.deleteKommentar(k_dto);
        assertTrue(result);
    }
}
