package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "kommentare", schema = "startupx")
public class Kommentare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kommentar_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.Startup startup;

    @NotNull
    @Column(name = "kommentar", nullable = false, length = Integer.MAX_VALUE)
    private String kommentar;

    @NotNull
    @Column(name = "erstellungsdatum", nullable = false)
    private LocalDate erstellungsdatum;

    @Column(name = "updated")
    private LocalDate updated;

    @Column(name = "bewertung")
    private Integer bewertung;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public org.hbrs.se2.project.startupx.entities.User getUser() {
        return user;
    }

    public void setUser(org.hbrs.se2.project.startupx.entities.User user) {
        this.user = user;
    }

    public org.hbrs.se2.project.startupx.entities.Startup getStartup() {
        return startup;
    }

    public void setStartup(org.hbrs.se2.project.startupx.entities.Startup startup) {
        this.startup = startup;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public LocalDate getErstellungsdatum() {
        return erstellungsdatum;
    }

    public void setErstellungsdatum(LocalDate erstellungsdatum) {
        this.erstellungsdatum = erstellungsdatum;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public Integer getBewertung() {
        return bewertung;
    }

    public void setBewertung(Integer bewertung) {
        this.bewertung = bewertung;
    }

}