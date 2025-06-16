package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "startup", schema = "startupx")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "startup_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.SET_DEFAULT)
    @ColumnDefault("0")
    @JoinColumn(name = "branche_id", nullable = false)
    private Branche branche;

    @Column(name = "beschreibung", length = Integer.MAX_VALUE)
    private String beschreibung;

    @Column(name = "gruendungsdatum")
    private LocalDate gruendungsdatum;

    @Column(name = "anzahl_mitarbeiter")
    private Integer anzahlMitarbeiter;

    @OneToMany(mappedBy = "startup")
    @OrderBy("erstellungsdatum DESC")
    private List<Bewertung> bewertungen;

    @OneToMany(mappedBy = "startup")
    private List<Stellenausschreibung> stellenausschreibungen;

    @OneToMany(mappedBy = "startup")
    private List<Student> mitarbeiterList;


    @ManyToMany
    @JoinTable(
            name = "startup_zu_student",
            schema = "startupx",
            joinColumns = @JoinColumn(name = "startup_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> studentenListe;

    @Override
    public String toString() {
        return "Startup: " + name + "(" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Startup that = (Startup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}