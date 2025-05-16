package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
