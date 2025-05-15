package org.hbrs.se2.project.startupx.repository;

import org.hbrs.se2.project.startupx.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
