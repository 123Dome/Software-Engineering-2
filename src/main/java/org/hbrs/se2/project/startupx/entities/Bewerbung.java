package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "bewerbung", schema = "startupx")
@Setter
@Getter
@Builder
public class Bewerbung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewerbungs_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "stellenausschreibung_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.Stellenausschreibung stellenausschreibung;

    @NotNull
    @Column(name = "bewerbungsschreiben", nullable = false, length = Integer.MAX_VALUE)
    private String bewerbungsschreiben;

}