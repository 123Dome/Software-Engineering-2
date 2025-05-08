package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "startup", schema = "startupx")
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
    private Set<Kommentare> kommentares = new LinkedHashSet<>();

    @OneToMany(mappedBy = "startup")
    private Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> stellenausschreibungs = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branche getBranche() {
        return branche;
    }

    public void setBranche(Branche branche) {
        this.branche = branche;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDate getGruendungsdatum() {
        return gruendungsdatum;
    }

    public void setGruendungsdatum(LocalDate gruendungsdatum) {
        this.gruendungsdatum = gruendungsdatum;
    }

    public Integer getAnzahlMitarbeiter() {
        return anzahlMitarbeiter;
    }

    public void setAnzahlMitarbeiter(Integer anzahlMitarbeiter) {
        this.anzahlMitarbeiter = anzahlMitarbeiter;
    }

    public Set<Kommentare> getKommentares() {
        return kommentares;
    }

    public void setKommentares(Set<Kommentare> kommentares) {
        this.kommentares = kommentares;
    }

    public Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> getStellenausschreibungs() {
        return stellenausschreibungs;
    }

    public void setStellenausschreibungs(Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> stellenausschreibungs) {
        this.stellenausschreibungs = stellenausschreibungs;
    }

}