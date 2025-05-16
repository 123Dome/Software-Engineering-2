package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KommentarDTO {
    private Long id;
    private Long user;
    private Long startup;
    private String kommentar;
    private LocalDate erstellungsdatum;
    private LocalDate updated;
    private Integer bewertung;
}
