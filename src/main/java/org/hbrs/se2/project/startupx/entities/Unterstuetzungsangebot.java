package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "unterstuetzungsangebot", schema = "startupx")
public class Unterstuetzungsangebot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unterstuetzungsangebot_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @NotNull
    @Column(name = "betrag", nullable = false)
    private Double betrag;

    @Override
    public String toString() {
        return "Unterstuetzungsangebot mit ID: " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unterstuetzungsangebot that = (Unterstuetzungsangebot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}