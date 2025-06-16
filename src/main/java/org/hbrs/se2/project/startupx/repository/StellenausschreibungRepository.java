package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.hbrs.se2.project.startupx.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StellenausschreibungRepository extends JpaRepository<Stellenausschreibung, Long> {

    List<Stellenausschreibung> findByStartup_Id(Long startupId);
    List<Stellenausschreibung> findByStartup_IdAndStatus(Long startupId, Status status);
}
