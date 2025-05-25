package org.hbrs.se2.project.startupx.mapper;

import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.entities.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public static StudentDTO mapToStudentDto(Student student) {
        if (student == null) {
            return null;
        }

        Set<Long> skillIds = new LinkedHashSet<>();
        Set<Long> startupIds = new LinkedHashSet<>();

        if (student.getSkills() != null) {
            skillIds = student.getSkills().stream()
                    .map(Skill::getId)
                    .collect(Collectors.toSet());
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

    public static Student mapToStudent(StudentDTO studentDTO, Set<Bewerbung> bewerbungSet, Set<Skill> skillSet, Set<Startup> startupSet, User user, Studiengang studiengang) {
        if (studentDTO == null) {
            return null;
        }

        return Student.builder()
                .user(user)
                .bewerbungen(bewerbungSet)
                .skills(skillSet)
                .studiengang(studiengang)
                .steckbrief(studentDTO.getSteckbrief())
                .matrikelnr(studentDTO.getMatrikelnr())
                .startups(startupSet)
                .build();
    }
}
