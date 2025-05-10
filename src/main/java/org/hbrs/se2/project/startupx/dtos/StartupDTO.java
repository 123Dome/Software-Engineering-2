package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.entities.Kommentar;
import org.hbrs.se2.project.startupx.entities.Stellenausschreibung;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StartupDTO {

    private Integer id;
    private String name;
    private Branche branche;
    private String beschreibung;
    private LocalDate gruendungsdatum;
    private String anzahlMitarbeiter;
    private List<Kommentar> kommentare;
    private List<Stellenausschreibung> stellenausschreibungen;

}
