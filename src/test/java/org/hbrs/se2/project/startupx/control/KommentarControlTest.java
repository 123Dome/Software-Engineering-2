package org.hbrs.se2.project.startupx.control;

import jakarta.transaction.Transactional;
import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.repository.BewertungRepository;
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

import static org.junit.Assert.assertEquals;

//@SpringBootTest
//@Transactional
//class KommentarControlTest {
//
//    @Autowired
//    BewertungControl control;
//
//    @Autowired
//    BewertungRepository bewertungRepository;
//
//    @Autowired
//    StartupRepository startupRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    //Entity`s
//    BewertungDTO kommentarDTO;
//    BewertungDTO vergleich;
//    StartupDTO startupDTO;
//    Startup unicorn;
//
//    //Wrapper
//    Optional<Startup> wrapper_startup;
//
//    @BeforeEach
//    void setUp() {
//        wrapper_startup = startupRepository.findById(28L);
//
//        if(wrapper_startup.isPresent()){
//            unicorn = wrapper_startup.get();
//        }
//
//        startupDTO = new StartupDTO(unicorn.getId(), unicorn.getName(), null, unicorn.getBeschreibung(), unicorn.getGruendungsdatum(), unicorn.getAnzahlMitarbeiter(), null, null, null);
//        //Setupen des KommentarDTO + Vergleich Kommentar
//
//        kommentarDTO = new BewertungDTO(100L, 31L, 5L, 4, "Coole Idee", LocalDate.now());
//        vergleich = new BewertungDTO(101L, 31L, 5L, 4,"Nicht so mein fall", LocalDate.now());
//    }
//
//    @Test
//    void createKommentar() {
//        BewertungDTO temp = control.createBewertung(kommentarDTO);
//
//        assertEquals(kommentarDTO.getKommentar(), temp.getKommentar());
//        assertNotEquals(temp.getKommentar(), vergleich.getKommentar());
//    }
//
//    @Test
//    void getAllKommentareZuStartup(){
//        List<KommentarDTO> kommentarDTOList = new ArrayList<KommentarDTO>();
//        kommentarDTOList.add(kommentarDTO);
//
//        assertEquals(kommentarDTOList.size(), control.getAllKommentarZuStartup(startupDTO).size());
//    }

}