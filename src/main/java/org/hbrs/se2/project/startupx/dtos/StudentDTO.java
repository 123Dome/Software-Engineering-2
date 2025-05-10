package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Integer id;
    private UserDTO user;
    private Integer matrikelnr;
    private StudiengangDTO studiengang;
    private String steckbrief;
    private List<SkillDTO> skills = new ArrayList<>();
}
