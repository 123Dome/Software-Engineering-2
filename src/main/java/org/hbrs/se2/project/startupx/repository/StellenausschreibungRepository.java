package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StellenausschreibungRepository extends JpaRepository<Stellenausschreibung, Long> {
}
