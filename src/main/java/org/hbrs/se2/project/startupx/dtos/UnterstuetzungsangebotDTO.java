package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnterstuetzungsangebotDTO {
    private Long id;
    private Long investor;
    private Long startup;
    private Double betrag;
}
