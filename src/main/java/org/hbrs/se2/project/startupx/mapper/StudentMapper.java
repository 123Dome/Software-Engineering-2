package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Student;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.repository.KommentarRepository;
import org.hbrs.se2.project.startupx.repository.StellenausschreibungRepository;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public static StudentDTO toDto(Student student) {
        if (student == null) return null;

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId().intValue());
        studentDTO.setUser(student.getUser().getId());
        studentDTO.setMatrikelnr(student.getMatrikelnr());
        studentDTO.setStudiengang(student.getStudiengang().getId());
        studentDTO.setSteckbrief(student.getSteckbrief());

        studentDTO.setSkills(
                student.getSkills() != null ?
                        student.getSkills().stream()
                                .map(Skill::getId)
                                .collect(Collectors.toList()) : new ArrayList<>());

        studentDTO.setStartups(
                student.getStartups() != null ?
                        student.getStartups().stream()
                                .map(Startup::getId)
                                .collect(Collectors.toSet()) : Set.of());

        return studentDTO;
    }
}
