package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stellenausschreibung", schema = "startupx")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stellenausschreibung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stellenausschreibung_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Size(max = 255)
    @NotNull
    @Column(name = "titel", nullable = false)
    private String titel;

    @NotNull
    @Column(name = "beschreibung", nullable = false, length = Integer.MAX_VALUE)
    private String beschreibung;

    @ManyToMany
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "stellenausschreibung")
    private List<Bewerbung> bewerbungen = new ArrayList<>();

    @Override
    public String toString() {
        return "Stellenausschreibung: " + titel + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stellenausschreibung that = (Stellenausschreibung) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}