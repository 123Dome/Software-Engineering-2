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
@Table(name = "skills", schema = "startupx")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @ManyToMany
    @JoinTable(name = "ausschreibung_zu_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "stellenausschreibung_id"))
    private List<Stellenausschreibung> stellenausschreibungen = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "student_zu_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students = new ArrayList<>();

}