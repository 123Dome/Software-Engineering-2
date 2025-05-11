package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rolle", schema = "startupx")
@Setter
@Getter
public class Rolle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rolle_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_zu_rolle", schema = "startupx",
            joinColumns = @JoinColumn(name = "rolle_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new LinkedHashSet<>();

}