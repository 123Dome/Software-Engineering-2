package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "stellenausschreibung", schema = "startupx")
public class Stellenausschreibung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stellenausschreibung_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Size(max = 255)
    @NotNull
    @Column(name = "titel", nullable = false)
    private String titel;

    @NotNull
    @Column(name = "beschreibung", nullable = false, length = Integer.MAX_VALUE)
    private String beschreibung;

    @ManyToMany
    private Set<Skill> skills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "stellenausschreibung")
    private Set<Bewerbung> bewerbungs = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<Bewerbung> getBewerbungs() {
        return bewerbungs;
    }

    public void setBewerbungs(Set<Bewerbung> bewerbungs) {
        this.bewerbungs = bewerbungs;
    }

}