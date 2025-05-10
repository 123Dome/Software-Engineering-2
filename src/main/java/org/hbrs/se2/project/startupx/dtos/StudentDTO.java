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
    private Integer studentId;
    private UserDTO userDTO;
    private Integer matrikelNr;
    private StudiengangDTO studiengangDTO;
    private String steckbrief;
    private List<SkillDTO> skillDTOs = new ArrayList<>();
}
