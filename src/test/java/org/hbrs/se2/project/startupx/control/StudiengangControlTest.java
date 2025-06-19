package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudiengangControlTest {

    @Autowired
    StudiengangControl studiengangControl;

    @Test
    void testGetAllAndGetById() {
        // Hole alle Studiengänge
        List<StudiengangDTO> all = studiengangControl.getAll();

        // Wenn Studiengänge vorhanden sind, prüfe getById
        if (!all.isEmpty()) {
            StudiengangDTO first = all.get(0);
            StudiengangDTO byId = studiengangControl.getById(first.getId());
            assertEquals(first.getId(), byId.getId());
            assertEquals(first.getStudiengang(), byId.getStudiengang());
        } else {
            //  Datenbank ist leer, kein getById-Test möglich
            System.out.println("WARNUNG: Keine Studiengänge in der Testdatenbank – getById-Test wird übersprungen.");
        }
    }

    @Test
    void testFindAllSkills() {
        // Hole alle Skills
        List<SkillDTO> skills = studiengangControl.findAllSkills();
        // Prüfe, dass kein Fehler auftritt
        assertNotNull(skills);
    }
}
