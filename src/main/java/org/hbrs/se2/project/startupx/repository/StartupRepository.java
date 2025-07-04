package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StartupRepository extends JpaRepository<Startup, Long> {

    List<Startup> findByBranche_Id(Long brancheId);

    List<Startup> findByNameContaining(String nameContaining);

    List<Startup> findByGruendungsdatum(LocalDate gruendungsdatum);

    List<Startup> findByStudentenListe_Id(Long studentenListeId);

    List<Startup> findByStellenausschreibungenIsNotEmpty();

    List<Startup> findTop5ByOrderByGruendungsdatumDesc();

    Optional<Startup> findById(Long id);
}
