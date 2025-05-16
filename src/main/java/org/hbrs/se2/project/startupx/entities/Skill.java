package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "skills", schema = "startupx")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Long id;

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
            schema = "startupx",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students = new LinkedHashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill that = (Skill) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Skill: " + skillName + " (" + id + ")";
    }
}