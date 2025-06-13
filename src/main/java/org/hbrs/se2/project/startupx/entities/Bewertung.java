package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "bewertung", schema = "startupx")
public class Bewertung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewertung_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @NotNull
    @Column(name = "bewertung", nullable = false)
    private Integer bewertung;

    @Column(name = "kommentar", length = Integer.MAX_VALUE)
    private String kommentar;

    @NotNull
    @Column(name = "erstellungsdatum", nullable = false)
    private LocalDate erstellungsdatum;

    @Override
    public String toString() {
        return "Bewertung mit ID: " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bewertung that = (Bewertung) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}