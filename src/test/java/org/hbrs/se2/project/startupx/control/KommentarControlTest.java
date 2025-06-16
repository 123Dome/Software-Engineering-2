package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.hbrs.se2.project.startupx.dtos.KommentarDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StartupRepository;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class KommentarControlTest {

    @Autowired
    KommentarControl control;

    @Autowired
    KommentarRepository kommentarRepository;

    @Autowired
    StartupRepository startupRepository;

    @Autowired
    UserRepository userRepository;

    //Entity`s
    KommentarDTO kommentarDTO;
    KommentarDTO vergleich;
    StartupDTO startupDTO;
    Startup unicorn;

    //Wrapper
    Optional<Startup> wrapper_startup;

    @BeforeEach
    void setUp() {
        wrapper_startup = startupRepository.findById(28L);

        if(wrapper_startup.isPresent()){
            unicorn = wrapper_startup.get();
        }

        startupDTO = new StartupDTO(unicorn.getId(), unicorn.getName(), null, unicorn.getBeschreibung(), unicorn.getGruendungsdatum(), unicorn.getAnzahlMitarbeiter(), null, null, null);
        //Setupen des KommentarDTO + Vergleich Kommentar

        kommentarDTO = new KommentarDTO(100L, 31L, 5L, "Coole Idee", LocalDate.now(), null, 4);
        vergleich = new KommentarDTO(101L, 31L, 5L, "Nicht so mein fall",LocalDate.now(), null, 2);
    }

    @Test
    void createKommentar() {
        KommentarDTO temp = control.createKommentar(kommentarDTO);

        assertEquals(kommentarDTO.getKommentar(), temp.getKommentar());
        assertNotEquals(temp.getKommentar(), vergleich.getKommentar());
    }

    @Test
    void getAllKommentareZuStartup(){
        List<KommentarDTO> kommentarDTOList = new ArrayList<KommentarDTO>();
        kommentarDTOList.add(kommentarDTO);

        assertEquals(kommentarDTOList.size(), control.getAllKommentarZuStartup(startupDTO).size());
    }

}