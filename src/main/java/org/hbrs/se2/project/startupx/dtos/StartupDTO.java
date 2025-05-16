package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartupDTO {
    private Long id;
    private String name;
    private Long branche;
    private String beschreibung;
    private LocalDate gruendungsdatum;
    private Integer anzahlMitarbeiter;
    private List<Long> kommentare;
    private List<Long> stellenausschreibungen;
    private Set<Long> studentenListe;
}
