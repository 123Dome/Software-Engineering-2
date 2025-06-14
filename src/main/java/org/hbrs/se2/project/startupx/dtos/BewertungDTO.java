package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BewertungDTO {
    private Long id;
    private Long user;
    private Long startup;
    private Integer bewertung;
    private String kommentar;
    private LocalDate erstellungsdatum;
}
