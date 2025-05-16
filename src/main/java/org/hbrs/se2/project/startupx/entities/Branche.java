package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "branche", schema = "startupx")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Branche: " + bezeichnung + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branche branche = (Branche) o;
        return Objects.equals(id, branche.id);
    }

}