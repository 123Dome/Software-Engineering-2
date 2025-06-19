package org.hbrs.se2.project.startupx.views.student;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.*;
import org.hbrs.se2.project.startupx.dtos.*;
import org.hbrs.se2.project.startupx.util.BewerbungsStatus;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = Globals.Pages.APPLY_FOR_JOB_VIEW + "/:id", layout = AppView.class)
@PageTitle("ApplyForJobView")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class ApplyForJobView extends Div implements BeforeEnterObserver{

    private final StellenausschreibungControl stellenausschreibungControl;
    private final SkillControl skillControl;
    private final JobApplicationControl jobApplicationControl;
    private final EditProfilControl editProfilControl;
    private final ManageStartupControl manageStartupControl;

    private StellenausschreibungDTO currentStellenausschreibung;
    private BewerbungDTO existingBewerbung;
    private UserDTO currentUser;
    private StudentDTO studentDTO;
    private StartupDTO startupDTO;

    private final TextArea bewerbungsschreiben = new TextArea();
    private final Button bewerbenButton = new Button("Bewerbung abschicken");
    private final Button abbrechenButton = new Button("Bewerbung abbrechen");

    private final Button annehmenButton = new Button("Bewerbung annehmen");
    private final Button abblehnenButton = new Button("Bewerbung abblehnen");

    private Long stellenausschreibungId;

    public ApplyForJobView(StellenausschreibungControl stellenausschreibungControl,
                           SkillControl skillControl,
                           JobApplicationControl jobApplicationControl,
                           EditProfilControl editProfilControl, ManageStartupControl manageStartupControl) {
        this.stellenausschreibungControl = stellenausschreibungControl;
        this.skillControl = skillControl;
        this.jobApplicationControl = jobApplicationControl;
        this.editProfilControl = editProfilControl;
        this.manageStartupControl = manageStartupControl;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();

        String stellenausschreibungIdString = event.getRouteParameters().get("id").orElse(null);

        if (stellenausschreibungIdString != null) {
            try {
                this.stellenausschreibungId = Long.parseLong(stellenausschreibungIdString);
            } catch (NumberFormatException e) {
                add(new Paragraph("Ungültige Stellenanzeigen-ID in der URL: " + stellenausschreibungIdString));
                return;
            }
        }

        currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
        if (currentUser == null) {
            add(new Paragraph("Bitte melden Sie sich an, um diese Seite zu sehen."));
            return;
        }

        studentDTO = editProfilControl.getStudentDTO(currentUser.getStudent());

        if (stellenausschreibungId != null) {
            this.currentStellenausschreibung = stellenausschreibungControl.findById(stellenausschreibungId);

            this.existingBewerbung = jobApplicationControl.getApplicationByStudentAndStellenausschreibung(studentDTO.getId(),stellenausschreibungId);

            if (this.currentStellenausschreibung != null) {
                startupDTO = manageStartupControl.findByID(currentStellenausschreibung.getStartup());
                displayJobPostingDetails();
                if (startupDTO.getStudentenListe().contains(studentDTO.getId())) {
                    vorhandeneBewerbungen();
                } else {
                    bewerbung();
                }
            } else {
                add(new Paragraph("Stellenanzeige mit ID " + stellenausschreibungId + " nicht gefunden."));
            }
        } else {
            add(new Paragraph("Keine Stellenanzeigen-ID in der URL angegeben."));
        }
    }

    private void displayJobPostingDetails() {
        add(new H2(currentStellenausschreibung.getTitel()));
        add(new Paragraph(currentStellenausschreibung.getBeschreibung()));

        add(new Paragraph("Startup: " + startupDTO.getName()));


        if (currentStellenausschreibung.getSkills() != null && !currentStellenausschreibung.getSkills().isEmpty()) {
            H3 skillsHeader = new H3("Benötigte Skills:");
            add(skillsHeader);

            String skillNames = currentStellenausschreibung.getSkills().stream()
                    .map(skillId -> {
                        SkillDTO skillDto = skillControl.getSkillById(skillId);
                        return (skillDto != null) ? skillDto.getSkillName() : "Unbekannter Skill";
                    })
                    .collect(Collectors.joining(", "));
            add(new Paragraph(skillNames));
        } else {
            add(new Paragraph("Keine spezifischen Skills für diese Stelle angegeben."));
        }
    }

    private void bewerbung() {
        if(existingBewerbung != null) {
            BewerbungDTO app = existingBewerbung;

            Div applicationStatusDiv = new Div();
            applicationStatusDiv.add(new H3("Ihre Bewerbung mit der ID: " + existingBewerbung.getId()));
            applicationStatusDiv.add(existingBewerbung.getBewerbungsschreiben());
            applicationStatusDiv.add(new Paragraph("Status: " + app.getStatus()));

            if (app.getStatus() == BewerbungsStatus.OFFEN) {
                Button withdrawButton = new Button("Bewerbung zurückziehen");
                withdrawButton.addClickListener(e -> {
                    jobApplicationControl.deleteBewerbung(existingBewerbung);
                    this.existingBewerbung = jobApplicationControl.getApplicationByStudentAndStellenausschreibung(
                            studentDTO.getId(),
                            currentStellenausschreibung.getId()
                    );
                    removeAll();
                    displayJobPostingDetails();
                    Notification.show("Bewerbung zurückgezogen.", 3000, Notification.Position.MIDDLE);
                });
                applicationStatusDiv.add(withdrawButton);
            }

            add(applicationStatusDiv);
        } else {
            Button applyButton = new Button("Jetzt bewerben");
            applyButton.addClickListener(e -> applyForJob());
            add(applyButton);
        }
    }

    private void vorhandeneBewerbungen() {
        List<BewerbungDTO> applications = jobApplicationControl.getApplicationByStellenausschreibung(stellenausschreibungId);

                add(new H2("Bewerbungen für Stellenanzeige ID: " + stellenausschreibungId));

                if (applications.isEmpty()) {
                    add(new Paragraph("Es gibt noch keine Bewerbungen für diese Stelle."));
                } else {
                    for (BewerbungDTO app : applications) {
                        Div appCard = new Div();
                        appCard.getStyle().set("border", "1px solid #ccc").set("padding", "10px").set("margin-bottom", "10px");
                        appCard.add(new Paragraph("BewerbungsID: " + app.getId()));

                        StudentDTO bewerberSudent = editProfilControl.getStudentDTO(app.getStudent());
                        UserDTO bewerberUser = editProfilControl.getUserDTO(bewerberSudent.getUser());
                        appCard.add(new Paragraph("Bewerber Name: " + bewerberUser.getVorname()+ " " + bewerberUser.getNachname()));

                        appCard.add(new Paragraph(app.getBewerbungsschreiben()));
                        appCard.add(new Paragraph("Status: " + app.getStatus()));

                        if(app.getStatus() == BewerbungsStatus.OFFEN) {

                            annehmenButton.addClickListener(click -> {
                                    if(jobApplicationControl.accept(app.getId(), stellenausschreibungId)) {
                                            Notification.show("Bewerbung von " + bewerberUser.getVorname() + " " + bewerberUser.getNachname() + " angenommen.");
                                            removeAll();
                                            displayJobPostingDetails();
                                            vorhandeneBewerbungen();
                                        }
                            });

                            abblehnenButton.addClickListener(click -> {
                                if(jobApplicationControl.decline(app.getId())) {
                                    Notification.show("Bewerbung von " + bewerberUser.getVorname() + " " + bewerberUser.getNachname() + " abgelehnt.");
                                    removeAll();
                                    displayJobPostingDetails();
                                    vorhandeneBewerbungen();
                                }
                            });

                            HorizontalLayout buttonLayout = new HorizontalLayout(annehmenButton, abblehnenButton);
                            buttonLayout.setSpacing(true);
                            appCard.add(buttonLayout);
                        }

                        add(appCard);
                    }
                }
    }

    private void applyForJob() {
        if (studentDTO == null) {
            Notification.show("Sie müssen als Student angemeldet sein, um sich zu bewerben.", 3000, Notification.Position.MIDDLE);
            return;
        }

        if(startupDTO.getMitarbeiterList().contains(studentDTO.getId())) {
            Notification.show("Sie arbeiten bereits bei diesem Startup", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (currentStellenausschreibung == null || currentStellenausschreibung.getId() == null) {
            Notification.show("Fehler: Keine gültige Stellenanzeige zum Bewerben gefunden.", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            BewerbungDTO newApplication = new BewerbungDTO();
            newApplication.setStudent(studentDTO.getId());
            newApplication.setStellenausschreibungen(currentStellenausschreibung.getId());
            Binder<BewerbungDTO> bewerbungDTOBinder = new Binder<>(BewerbungDTO.class);
            bewerbungDTOBinder.setBean(newApplication);

            Dialog dialog = new Dialog("Neue Bewerbung schreiben");

            FormLayout formLayout = new FormLayout();

            bewerbungDTOBinder.forField(bewerbungsschreiben).bind(BewerbungDTO::getBewerbungsschreiben,BewerbungDTO::setBewerbungsschreiben);

            bewerbenButton.addClickListener(e -> {
                jobApplicationControl.applyForJob(newApplication);
                Notification.show("Bewerbung erfolgreich eingereicht!", 3000, Notification.Position.MIDDLE);
                dialog.close();
                this.existingBewerbung = jobApplicationControl.getApplicationByStudentAndStellenausschreibung(
                        studentDTO.getId(),
                        currentStellenausschreibung.getId()
                );
                removeAll();
                displayJobPostingDetails();
                bewerbung();
            });

            abbrechenButton.addClickListener(e -> {
                Notification.show("Bewerbung abgebrochen.");
                dialog.close();
            });

            formLayout.add(bewerbungsschreiben);
            dialog.add(formLayout, bewerbenButton, abbrechenButton);
            dialog.open();
        } catch (Exception e) {
            Notification.show("Fehler bei der Bewerbung: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }
}
