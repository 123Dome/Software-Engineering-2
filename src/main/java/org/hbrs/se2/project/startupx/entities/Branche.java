package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "branche", schema = "startupx")
public class Branche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branche_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @OneToMany(mappedBy = "branche")
    private Set<org.hbrs.se2.project.startupx.entities.Startup> startups = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Set<org.hbrs.se2.project.startupx.entities.Startup> getStartups() {
        return startups;
    }

    public void setStartups(Set<org.hbrs.se2.project.startupx.entities.Startup> startups) {
        this.startups = startups;
    }

}