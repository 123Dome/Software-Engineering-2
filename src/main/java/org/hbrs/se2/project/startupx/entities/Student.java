package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "student", schema = "startupx")
@Setter
@Getter
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
    private Set<Bewerbung> bewerbungen = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "studentenListe")
    private Set<Startup> startups = new LinkedHashSet<>();

    @ManyToMany
    private Set<Skill> skills = new LinkedHashSet<>();

}