package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rolle", schema = "startupx")
public class Rolle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rolle_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @ManyToMany
    @JoinTable(name = "user_zu_rolle",
            joinColumns = @JoinColumn(name = "rolle_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<org.hbrs.se2.project.startupx.entities.User> users = new LinkedHashSet<>();

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

    public Set<org.hbrs.se2.project.startupx.entities.User> getUsers() {
        return users;
    }

    public void setUsers(Set<org.hbrs.se2.project.startupx.entities.User> users) {
        this.users = users;
    }

}