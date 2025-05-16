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
@Table(name = "branche", schema = "startupx")
@Getter
@Setter
public class Branche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branche_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @OneToMany(mappedBy = "branche")
    private List<Startup> startups = new ArrayList<>();
}