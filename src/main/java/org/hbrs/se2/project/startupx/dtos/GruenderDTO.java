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
public class GruenderDTO {
    private Integer id;
    private StudentDTO student;
    private String businessMail;
    private Integer anzahlStartups;
    private String motivation;
    private List<StartupDTO> startups = new ArrayList<>();
}
