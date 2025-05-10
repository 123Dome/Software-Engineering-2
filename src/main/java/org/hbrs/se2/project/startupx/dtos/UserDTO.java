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
public class UserDTO {
    private Integer id;
    private String vorname;
    private String nachname;
    private String passwort;
    private String nutzername;
    private String email;
    private LocalDate geburtsdatum;
    private List<RolleDTO> rollen = new ArrayList<>();
}
