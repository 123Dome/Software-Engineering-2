package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.dtos.BewerbungDTO;
import org.hbrs.se2.project.startupx.entities.Bewerbung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BewerbungRepository extends JpaRepository<Bewerbung, Long> {
    Bewerbung findBewerbungByStudent_IdAndStellenausschreibung_Id(Long studentId, Long stellenausschreibungId);

    List<Bewerbung> findByStellenausschreibung_Id(Long stellenausschreibungId);
}
