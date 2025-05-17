package org.hbrs.se2.project.startupx.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    @NotBlank(message = "Vorname darf nicht leer sein")
    private String vorname;

    @NotBlank(message = "Nachname darf nicht leer sein")
    private String nachname;

    @NotBlank(message = "Nutzername darf nicht leer sein")
    // TODO: Nutzername länge? @Size(min = 4, max = 20, message = "Nutzername muss zwischen 4 und 20 Zeichen lang sein")
    private String nutzername;

    @NotBlank(message = "Passwort darf nicht leer sein")
    // TODO: Mindestlänge Passwort? @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein")
    private String passwort;

    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "Bitte gib eine gültige E-Mail-Adresse ein")
    private String email;

    @Past(message = "Geburtsdatum muss in der Vergangenheit liegen")
    private LocalDate geburtsdatum;

    private Set<RolleDTO> rollen;
}
