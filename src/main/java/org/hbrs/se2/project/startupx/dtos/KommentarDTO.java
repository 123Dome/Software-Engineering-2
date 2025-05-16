package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KommentarDTO {
    private Long id;
    private Long user;
    private Long startup;
    private String kommentar;
    private LocalDate erstellungsdatum;
    private LocalDate updated;
    private Integer bewertung;
}
