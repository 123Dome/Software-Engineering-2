package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "kommentar", schema = "startupx")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Kommentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kommentar_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @NotNull
    @Column(name = "kommentar", nullable = false, length = Integer.MAX_VALUE)
    private String kommentar;

    @NotNull
    @Column(name = "erstellungsdatum", nullable = false)
    private LocalDate erstellungsdatum;

    @Column(name = "updated")
    private LocalDate updated;

    @Column(name = "bewertung")
    private Integer bewertung;

}