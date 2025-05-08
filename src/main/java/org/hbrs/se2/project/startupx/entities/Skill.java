package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "skills", schema = "startupx")
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
    private Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> stellenausschreibungs = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "student_zu_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<org.hbrs.se2.project.startupx.entities.Student> students = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> getStellenausschreibungs() {
        return stellenausschreibungs;
    }

    public void setStellenausschreibungs(Set<org.hbrs.se2.project.startupx.entities.Stellenausschreibung> stellenausschreibungs) {
        this.stellenausschreibungs = stellenausschreibungs;
    }

    public Set<org.hbrs.se2.project.startupx.entities.Student> getStudents() {
        return students;
    }

    public void setStudents(Set<org.hbrs.se2.project.startupx.entities.Student> students) {
        this.students = students;
    }

}