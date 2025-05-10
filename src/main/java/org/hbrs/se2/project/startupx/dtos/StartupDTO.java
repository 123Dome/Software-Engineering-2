package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StartupDTO {
    private Integer id;
    private String name;
    private BrancheDTO branche;
    private String beschreibung;
    private LocalDate gruendungsdatum;
    private Integer anzahlMitarbeiter;
    private List<KommentarDTO> kommentare = new ArrayList<>();
    private List<StellenausschreibungDTO> stellenausschreibungen = new ArrayList<>();
}
