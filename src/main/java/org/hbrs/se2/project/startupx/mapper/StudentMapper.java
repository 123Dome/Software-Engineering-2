package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.Student;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public static StudentDTO mapToStudentDto(Student student) {
        if (student == null) {
            return null;
        }

        List<Long> skillIds = new ArrayList<>();
        Set<Long> startupIds = new LinkedHashSet<>();

        if (student.getSkills() != null) {
            skillIds = student.getSkills().stream()
                    .map(Skill::getId)
                    .toList();
        }

        if (student.getStartups() != null) {
            startupIds = student.getStartups().stream()
                    .map(Startup::getId)
                    .collect(Collectors.toSet());
        }

        StudentDTO studentDTO = StudentDTO.builder()
                .id(student.getId())
                .user(student.getUser().getId())
                .matrikelnr(student.getMatrikelnr())
                .studiengang(student.getStudiengang().getId())
                .steckbrief(student.getSteckbrief())
                .skills(skillIds)
                .startups(startupIds)
                .build();

        return studentDTO;
    }
}
