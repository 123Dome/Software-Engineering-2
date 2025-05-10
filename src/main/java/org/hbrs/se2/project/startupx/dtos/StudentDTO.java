package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.entities.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Integer studentId;
    private User user;
    private Integer matrikelNr;
    private Studiengang studiengang;
    private String steckbrief;
    private List<SkillDTO> skills = new ArrayList<>();
}
