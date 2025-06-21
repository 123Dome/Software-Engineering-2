package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "investor", schema = "startupx")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investor_id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "branche_id")
    private Branche branche;

    @Column(name = "steckbrief", length = Integer.MAX_VALUE)
    private String steckbrief;

    @NotNull
    @Column(name = "budget", nullable = false)
    private Long budget;

    @OneToMany(mappedBy = "investor")
    private Set<Unterstuetzungsangebot> unterstuetzungsangebote = new LinkedHashSet<>();

    @Override
    public String toString() {
        return "Investor mit ID: " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Investor that = (Investor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}