package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RolleRepository extends JpaRepository<Rolle, Long> {

    Optional<Rolle> findById(Long id);

    Rolle findByBezeichnung(String bezeichnung);
}
