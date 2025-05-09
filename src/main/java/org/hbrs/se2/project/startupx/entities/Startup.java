package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "startup", schema = "startupx")
@Getter
@Setter
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "startup_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_DEFAULT)
    @ColumnDefault("0")
    @JoinColumn(name = "branche_id", nullable = false)
    private Branche branche;

    @Column(name = "beschreibung", length = Integer.MAX_VALUE)
    private String beschreibung;

    @Column(name = "gruendungsdatum")
    private LocalDate gruendungsdatum;

    @Column(name = "anzahl_mitarbeiter")
    private Integer anzahlMitarbeiter;

    @OneToMany(mappedBy = "startup")
    @OrderBy("erstellungsdatum DESC")
    private List<Kommentare> kommentare = new ArrayList<>();

    @OneToMany(mappedBy = "startup")
    private List<Stellenausschreibung> stellenausschreibungen = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "startup_zu_gruender",
            joinColumns = @JoinColumn(name = "startup_id"),
            inverseJoinColumns = @JoinColumn(name = "gruender_id")
    )
    private List<Gruender> gruenderListe = new ArrayList<>();

}