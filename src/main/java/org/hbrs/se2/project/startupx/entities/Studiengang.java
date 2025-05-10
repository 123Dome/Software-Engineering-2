package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "studiengang", schema = "startupx")
@Setter
@Getter
public class Studiengang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studiengang_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "studiengang", nullable = false)
    private String studiengang;

    @OneToMany(mappedBy = "studiengang")
    private Set<Student> students = new LinkedHashSet<>();

}