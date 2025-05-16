package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StellenausschreibungDTO {
    private Long id;
    private Long startup;
    private String titel;
    private String beschreibung;
    private List<Long> skills;
    private List<Long> bewerbungen;
}
