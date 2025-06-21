package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Transactional
class BewertungControlTest {

    @Autowired
    BewertungControl bewertungControl;

    BewertungDTO bewertungDTO;
    List<BewertungDTO> bewertungDTOList;


    @BeforeEach
    void setUp() {
        bewertungDTO = BewertungDTO.builder().user(31L).startup(28L).bewertung(4).kommentar("Coole Idee").erstellungsdatum(LocalDate.now()).build();

    }

    @Test
    void createKommentar() {
        bewertungControl.createBewertung(bewertungDTO);

        bewertungDTOList = bewertungControl.getAlleBewertungZuStartup(28L);

        assertTrue(!bewertungDTOList.isEmpty());
    }

    @Test
    void getAllKommentareZuStartup(){
//        List<KommentarDTO> kommentarDTOList = new ArrayList<KommentarDTO>();
//        kommentarDTOList.add(bewertungDTO);
//
//        assertEquals(kommentarDTOList.size(), control.getAllKommentarZuStartup(startupDTO).size());
    }

}