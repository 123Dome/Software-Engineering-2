package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StellenausschreibungControlTest {

    @Autowired
    StellenausschreibungControl stellenausschreibungControl;

    @Autowired
    ManageStartupControl manageStartupControl;

    StartupDTO startupDTO;
    StellenausschreibungDTO stellenausschreibungDTO;

    @BeforeEach
    void setUp() {
        // Startup für den Test erstellen
        List<Long> kommentarListe = new LinkedList<>();
        Set<Long> studentListe = new LinkedHashSet<>();
        List<Long> stellenAusschreibungListe = new LinkedList<>();
        Set<Long> skillListe = new LinkedHashSet<>();
        LocalDate localDate = LocalDate.of(1990, 1, 1);

        startupDTO = StartupDTO.builder()
                .bewertungen(kommentarListe)
                .branche(1L)
                .studentenListe(studentListe)
                .stellenausschreibungen(stellenAusschreibungListe)
                .gruendungsdatum(localDate)
                .name("JUnitStartup")
                .anzahlMitarbeiter(1)
                .beschreibung("JUnit Beschreibung")
                .build();

        // Speichere das Startup in der Datenbank und hole die echte ID
        StartupDTO savedStartup = manageStartupControl.createStartup(startupDTO);

        //  Stellenausschreibung mit dieser Startup-ID erstellen
        stellenausschreibungDTO = StellenausschreibungDTO.builder()
                .startup(savedStartup.getId())
                .titel("JUnit-Stelle")
                .beschreibung("JUnit-Testbeschreibung")
                .skills(skillListe)
                .bewerbungen(new ArrayList<>())
                .build();
    }

    @AfterEach
    void tearDown() {
        stellenausschreibungDTO = null;
        startupDTO = null;
    }

    @Test
    void roundTripTest() {
        // 1. Alle Stellenausschreibungen zu diesem Startup merken
        List<StellenausschreibungDTO> before = stellenausschreibungControl.getAllStellenausschreibungByStartup(
                manageStartupControl.findByID(stellenausschreibungDTO.getStartup())
        );

        // 2. CREATE
        StellenausschreibungDTO created = stellenausschreibungControl.createStellenausschreibung(stellenausschreibungDTO);
        assertNotNull(created.getId());
        assertEquals("JUnit-Stelle", created.getTitel());

        // 3. Nach dem Hinzufügen prüfen
        List<StellenausschreibungDTO> afterAdd = stellenausschreibungControl.getAllStellenausschreibungByStartup(
                manageStartupControl.findByID(stellenausschreibungDTO.getStartup())
        );
        assertEquals(before.size() + 1, afterAdd.size());

        // 4. UPDATE
        created.setBeschreibung("JUnit Update Beschreibung");
        StellenausschreibungDTO updated = stellenausschreibungControl.updateStellenausschreibung(created);
        assertEquals("JUnit Update Beschreibung", updated.getBeschreibung());

        // 5. DELETE
        boolean deleted = stellenausschreibungControl.deleteStellenausschreibung(updated);
        assertTrue(deleted);

        List<StellenausschreibungDTO> afterDelete = stellenausschreibungControl.getAllStellenausschreibungByStartup(
                manageStartupControl.findByID(stellenausschreibungDTO.getStartup())
        );
        assertEquals(before.size(), afterDelete.size());
    }
}
