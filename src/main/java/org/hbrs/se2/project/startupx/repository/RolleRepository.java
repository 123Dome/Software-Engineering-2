package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolleRepository extends JpaRepository<Rolle, Long> {
    Rolle findByBezeichnung(String bezeichnung);
}
