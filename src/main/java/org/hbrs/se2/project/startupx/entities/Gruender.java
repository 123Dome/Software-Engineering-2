package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "gruender", schema = "startupx")
@Setter
@Getter
public class Gruender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gruender_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "student_id", nullable = false)
    private org.hbrs.se2.project.startupx.entities.Student student;

    @Size(max = 255)
    @NotNull
    @Column(name = "business_mail", nullable = false)
    private String businessMail;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "anzahl_startups", nullable = false)
    private Integer anzahlStartups;

    @Column(name = "motivation", length = Integer.MAX_VALUE)
    private String motivation;

    @ManyToMany(mappedBy = "gruenderListe")
    private List<Startup> startupList;
}