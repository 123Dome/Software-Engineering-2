package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StartupRepository extends JpaRepository<Startup, Long> {

    List<Startup> findByBranche(Branche branche);

    List<Startup> findByNameContaining(String nameContaining);

    List<Startup> findByGruendungsdatum(LocalDate gruendungsdatum);

    //TODO: bitte einen SQL-Befehl erstellen, der alle StartUps ausgibt, die mind. 1 Stellenanzeige haben
}
