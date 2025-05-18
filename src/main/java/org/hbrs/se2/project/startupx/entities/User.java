package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "startupx")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "vorname", nullable = false)
    private String vorname;

    @Size(max = 255)
    @NotNull
    @Column(name = "nachname", nullable = false)
    private String nachname;

    @Size(max = 255)
    @NotNull
    @Column(name = "passwort", nullable = false)
    private String passwort;

    @Size(max = 255)
    @NotNull
    @Column(name = "nutzername", nullable = false)
    private String nutzername;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "geburtsdatum", nullable = false)
    private LocalDate geburtsdatum;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Kommentar> kommentare = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    private Student students;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    private Set<Rolle> rollen = new LinkedHashSet<>();

}