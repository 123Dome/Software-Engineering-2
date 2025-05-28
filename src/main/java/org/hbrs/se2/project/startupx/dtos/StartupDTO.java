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

    @Override
    public String toString() {
        return "Startup: " + name + "(" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartupDTO that = (StartupDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
