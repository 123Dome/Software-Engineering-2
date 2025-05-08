package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "bewerbung", schema = "startupx")
public class Bewerbung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewerbungs_id", nullable = false)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public org.hbrs.se2.project.startupx.entities.Student getStudent() {
        return student;
    }

    public void setStudent(org.hbrs.se2.project.startupx.entities.Student student) {
        this.student = student;
    }

    public org.hbrs.se2.project.startupx.entities.Stellenausschreibung getStellenausschreibung() {
        return stellenausschreibung;
    }

    public void setStellenausschreibung(org.hbrs.se2.project.startupx.entities.Stellenausschreibung stellenausschreibung) {
        this.stellenausschreibung = stellenausschreibung;
    }

    public String getBewerbungsschreiben() {
        return bewerbungsschreiben;
    }

    public void setBewerbungsschreiben(String bewerbungsschreiben) {
        this.bewerbungsschreiben = bewerbungsschreiben;
    }

}