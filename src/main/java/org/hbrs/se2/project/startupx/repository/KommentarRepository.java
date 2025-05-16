package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KommentarRepository extends JpaRepository<Kommentar, Long> {
    List<Kommentar> findByStartup_id(Long startupId);
}
