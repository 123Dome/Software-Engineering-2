package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GruenderDTO {
    private Integer id;
    private StudentDTO studentDTO;
    private String businessMail;
    private Integer anzahlStartups;
    private String motivation;
}
