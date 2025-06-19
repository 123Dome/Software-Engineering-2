package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {
    Investor findByUser_Id(Long userId);
}
