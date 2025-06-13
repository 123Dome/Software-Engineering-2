package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Bewertung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BewertungRepository extends JpaRepository<Bewertung, Long> {
    List<Bewertung> findAllByStartupId(Long startupId);

    List<Bewertung> findAllByUserId(Long userId);
}
