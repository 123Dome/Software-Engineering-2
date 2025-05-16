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
    private String passwort;
    private String nutzername;
    private String email;
    private LocalDate geburtsdatum;
    private Set<Long> rollen;
}
