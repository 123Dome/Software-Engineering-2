package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StellenausschreibungRepository extends JpaRepository<Stellenausschreibung, Long> {
    List<Stellenausschreibung> findByStartup_Id(Long startupId);
}
