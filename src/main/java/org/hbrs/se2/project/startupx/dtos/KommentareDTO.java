package org.hbrs.se2.project.startupx.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.entities.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KommentareDTO {

    private Integer id;
    private User user;
    private Startup startup;
    private String kommentar;
    private LocalDate erstellungsdatum;
    private LocalDate updated;
    private Integer bewertung;
}
