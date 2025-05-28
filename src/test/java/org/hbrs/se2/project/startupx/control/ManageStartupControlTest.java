package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.control.exception.StartUpException;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class ManageStartupControlTest {

    @Autowired
    ManageStartupControl manageStartupControl;

    StartupDTO startupDTO;

    LocalDate localDate;

    // Neues StartupDTO wird über Builder erstellt
    @BeforeEach
    void setUp() {
        List<Long> kommentarListe = new LinkedList<>();
        Set<Long> studentListe = new LinkedHashSet<>();
        List<Long> stellenAusschreibungListe = new LinkedList<>();
        localDate = LocalDate.of(1990, 1, 1);
        startupDTO = StartupDTO.builder()
                .kommentare(kommentarListe)
                .branche(1L)
                .studentenListe(studentListe)
                .stellenausschreibungen(stellenAusschreibungListe)
                .gruendungsdatum(localDate)
                .name("JUnitStartup")
                .anzahlMitarbeiter(1)
                .beschreibung("JUnit Beschreibung")
                .build();
    }

    @AfterEach
    void tearDown() {
        startupDTO = null;
    }

    @Test
    void RoundTripTest() {
        // Liste aller vorhandener Startups in der DB
        List<StartupDTO> startupListBeforeAddedNewStartup = manageStartupControl.findAll();

        StartupDTO savedStartup = manageStartupControl.createStartup(startupDTO);

        // Liste aller Startups nachdem ein neues Startup gegründet wurde
        List<StartupDTO> startupListAfterAddedNewStartup = manageStartupControl.findAll();

        // Größe der Liste sollte +1 sein
        assertEquals(startupListBeforeAddedNewStartup.size()+1, startupListAfterAddedNewStartup.size());

        // Auslesen der ID vom letzten Startup, wird es anhand der ID gefunden?
        Long id = savedStartup.getId();
        StartupDTO findById = manageStartupControl.findByID(id);
        assertEquals(id, findById.getId());

        // Update der Beschreibung in der StartupDTO
        findById.setBeschreibung("JUnit Update Beschreibung");
        manageStartupControl.updateStartup(findById);

        // Überprüfung ob die Updates in die Datenbank geschrieben wurden
        StartupDTO updatedStartupDTO = manageStartupControl.findByID(id);
        assertEquals(findById.getName(), updatedStartupDTO.getName());
        assertEquals(findById.getBeschreibung(), updatedStartupDTO.getBeschreibung());

        // Wird das richtige Startup zum Gründungsdatum gefunden
        List<StartupDTO> findByGruendungsdatum = manageStartupControl.findByGruendungsdatum(localDate);
        assertEquals(findById, findByGruendungsdatum.get(0));

        // Neu angelegtes Startup im JUnit Test wird gelöscht
        manageStartupControl.deleteStartup(findById);
        List<StartupDTO> startupListAfterDeleteOne = manageStartupControl.findAll();

        // Ist es wieder die ursprüngliche Anzahl an Startups nachdem das Startup gelöscht wurde
        assertEquals(startupListBeforeAddedNewStartup.size(), startupListAfterDeleteOne.size());

        // Startup kann nicht mehr anhand der ID gefunden werden -> StartupException wird geschmissen
        assertThrows(StartUpException.class, () -> {
            manageStartupControl.findByID(id);
        });

    }

    // Test um wirklich 5 Startups aufgelistet werden
    @Test
    void findTop5ByGruendungsdatum() {
        List<StartupDTO> last5StartupDTOList = manageStartupControl.findTop5ByOrderByGruendungsdatumDesc();
        assertEquals(5, last5StartupDTOList.size());
        assertNotEquals(4, last5StartupDTOList.size());

    }
}