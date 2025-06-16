package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;
import org.hbrs.se2.project.startupx.util.BewerbungsStatusConverter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;


@Entity
@Table(name = "bewerbung", schema = "startupx")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bewerbung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewerbungs_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Convert(converter = BewerbungsStatusConverter.class)
    @Column(name = "status", nullable = false)
    private BewerbungsStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "stellenausschreibung_id", nullable = false)
    private Stellenausschreibung stellenausschreibung;

    @NotNull
    @Column(name = "bewerbungsschreiben", nullable = false, length = Integer.MAX_VALUE)
    private String bewerbungsschreiben;
}