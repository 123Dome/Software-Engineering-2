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
    private BrancheDTO brancheDTO;
    private String beschreibung;
    private LocalDate gruendungsdatum;
    private String anzahlMitarbeiter;
    private List<KommentarDTO> kommentarDTOs = new ArrayList<>();
    private List<StellenausschreibungDTO> stellenausschreibungDTOs = new ArrayList<>();
}
