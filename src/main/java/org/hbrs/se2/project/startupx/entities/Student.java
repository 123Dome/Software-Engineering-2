package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "student", schema = "startupx")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.User user;

    @NotNull
    @Column(name = "matrikelnr", nullable = false)
    private Integer matrikelnr;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "studiengang_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.Studiengang studiengang;

    @Column(name = "steckbrief", length = Integer.MAX_VALUE)
    private String steckbrief;

    @OneToMany(mappedBy = "student")
    private Set<Bewerbung> bewerbungs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Gruender> gruenders = new LinkedHashSet<>();

    @ManyToMany
    private Set<Skill> skills = new LinkedHashSet<>();

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

    public Integer getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(Integer matrikelnr) {
        this.matrikelnr = matrikelnr;
    }

    public org.hbrs.se2.project.startupx.entities.Studiengang getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(org.hbrs.se2.project.startupx.entities.Studiengang studiengang) {
        this.studiengang = studiengang;
    }

    public String getSteckbrief() {
        return steckbrief;
    }

    public void setSteckbrief(String steckbrief) {
        this.steckbrief = steckbrief;
    }

    public Set<Bewerbung> getBewerbungs() {
        return bewerbungs;
    }

    public void setBewerbungs(Set<Bewerbung> bewerbungs) {
        this.bewerbungs = bewerbungs;
    }

    public Set<Gruender> getGruenders() {
        return gruenders;
    }

    public void setGruenders(Set<Gruender> gruenders) {
        this.gruenders = gruenders;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

}