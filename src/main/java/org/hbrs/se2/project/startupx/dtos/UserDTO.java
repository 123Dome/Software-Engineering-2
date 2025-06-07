package org.hbrs.se2.project.startupx.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    private String vorname;

    private String nachname;

    private String nutzername;

    private String passwort;

    private String email;

    private LocalDate geburtsdatum;

    private Set<Long> rollen;

    private Set<Long> kommentare;

    private Long student;

    private Long investor;
}
