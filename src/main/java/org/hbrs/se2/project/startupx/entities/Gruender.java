package org.hbrs.se2.project.startupx.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "gruender", schema = "startupx")
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

    public String getBusinessMail() {
        return businessMail;
    }

    public void setBusinessMail(String businessMail) {
        this.businessMail = businessMail;
    }

    public Integer getAnzahlStartups() {
        return anzahlStartups;
    }

    public void setAnzahlStartups(Integer anzahlStartups) {
        this.anzahlStartups = anzahlStartups;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

}