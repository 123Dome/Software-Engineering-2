package org.hbrs.se2.project.startupx.views.student;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.components.BewertungComponent;
import org.hbrs.se2.project.startupx.control.*;
import org.hbrs.se2.project.startupx.dtos.*;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;
import org.hbrs.se2.project.startupx.views.MainView;

import java.util.List;

@Route(value = Globals.Pages.STUDENT_PROFILE_VIEW, layout = AppView.class)
@PageTitle("Mein Profil - Student")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class StudentProfileView extends Div implements BeforeEnterObserver {

    private UserDTO userDTO;
    private StudentDTO studentDTO;

    private final EditProfilControl editProfilControl;
    private final AuthenticationControl authenticationControl;
    private final ManageStartupControl manageStartupControl;
    private final BewertungControl bewertungControl;
    private final JobApplicationControl jobApplicationControl;
    private final StellenausschreibungControl stellenausschreibungControl;

    private final VerticalLayout bewerbungsLayout = new VerticalLayout();

    private final BewertungComponent bewertungComponent;

    public StudentProfileView(ManageStartupControl manageStartupControl, BewertungControl bewertungControl,
                              JobApplicationControl jobApplicationControl, EditProfilControl editProfilControl,
                              StellenausschreibungControl stellenausschreibungControl, AuthenticationControl authenticationControl) {
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        this.editProfilControl = editProfilControl;
        this.jobApplicationControl = jobApplicationControl;
        this.stellenausschreibungControl = stellenausschreibungControl;
        this.authenticationControl = authenticationControl;
        this.bewertungComponent = new BewertungComponent(bewertungControl);
        addClassName("profile-view");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        userDTO = authenticationControl.getCurrentUser();
        this.studentDTO = editProfilControl.getStudentDTObyUserId(userDTO.getId());
        // Wenn KEIN Student => Zurück zur MainView
        if (studentDTO == null) {
            event.rerouteTo(MainView.class);
        } else {
            this.removeAll();
            setUpUI();
        }
    }

    private void setUpUI() {
        HorizontalLayout reviewsAndApplicationsLayout = new HorizontalLayout();
        reviewsAndApplicationsLayout.add(bewertungComponent.createBewertungenCards(
                bewertungControl.getAlleBewertungZuUser(userDTO.getId()),
                userDTO,
                this::setUpUI
        ));
        reviewsAndApplicationsLayout.add(setUpBewerbungenCards());
        reviewsAndApplicationsLayout.setSpacing(true);

        if (!this.studentDTO.getStartups().isEmpty()) {
            add(
                    createProfileLayout(),
                    setUpStartupGrid(),
                    reviewsAndApplicationsLayout
            );
        } else {
            add(
                    createProfileLayout(),
                    reviewsAndApplicationsLayout
            );
        }
    }

    //ImageLayout und Nutzerdaten-Layout werden zusammengefügt
    private Component createProfileLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(createProfileImage(), createUserInfoLayout());

        return layout;
    }

    //Generisches Profilbild wird reingeladen, Größe angepasst
    private Image createProfileImage() {
        Image profileImage = new Image("images/genericUser.png", "Profilbild");
        profileImage.setWidth("120px");
        profileImage.setHeight("120px");
        profileImage.getStyle().set("border-radius", "50%");
        return profileImage;
    }

    //Nutzerdaten-Layout wird erstellt, Nutzerdaten werden dargestellt
    private VerticalLayout createUserInfoLayout() {
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);

        String arbeitgeber = null;
        StartupDTO startupDTO;
        if (studentDTO != null && studentDTO.getStartupId() != null) {
            startupDTO = manageStartupControl.findByID(studentDTO.getStartupId());
            arbeitgeber = startupDTO.getName();
        } else {
            startupDTO = null;
        }

        infoLayout.add(
                new H3(userDTO.getVorname() + " " +  userDTO.getNachname()),
                new Paragraph("Nutzername: " + userDTO.getNutzername()),
                new Paragraph("Geburtsdatum: " + userDTO.getGeburtsdatum()),
                new Paragraph("Email: " + userDTO.getEmail())
        );

        if (arbeitgeber != null) {
            Paragraph arbeitgerParagraph = new Paragraph("Arbeitgeber: " + arbeitgeber);
            arbeitgerParagraph.addClickListener(click -> {
                String startupId = startupDTO.getId().toString();
                UI.getCurrent().navigate("startup/"+startupId);
            });

            infoLayout.add(arbeitgerParagraph);
        }

        return infoLayout;
    }

    private Component setUpStartupGrid() {
        //Soll zukünftig alle StartUps listen
        List<StartupDTO> startups = manageStartupControl.getStartups(this.studentDTO);
        Grid<StartupDTO> grid = new Grid<>(StartupDTO.class);
        grid.setItems(startups);

        grid.asSingleSelect().addValueChangeListener(event -> {
            StartupDTO selected = event.getValue();
            if (selected != null) {
                getUI().ifPresent(ui ->
                        ui.navigate( Globals.Pages.STUDENT_STARTUP_VIEW +"/" + selected.getId())
                );
            }
        });

        return grid;
    }

    private Component setUpBewerbungenCards() {
        bewerbungsLayout.removeAll();

        bewerbungsLayout.add(new H3("Meine Bewerbungen"));

        List<BewerbungDTO> bewerbungen = jobApplicationControl.getApplicationsByStudent(studentDTO.getId());

        if (bewerbungen.isEmpty()) {
            bewerbungsLayout.add(new Paragraph("Keine Bewerbungen vorhanden"));
            return bewerbungsLayout;
        }

        for(BewerbungDTO bewerbung : bewerbungen) {
            Div card = new Div();
            card.addClassName("bewerbungen-karte");
            card.setWidth("400px");
            card.getStyle().set("border", "1px solid #ccc");
            card.getStyle().set("border-radius", "8px");
            card.getStyle().set("padding", "16px");
            card.getStyle().set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)");

            StellenausschreibungDTO stellenausschreibungDTO = stellenausschreibungControl.findById(bewerbung.getStellenausschreibungen());

            // Startup zur Bewertung
            String startUpName = manageStartupControl.findByID(stellenausschreibungDTO.getStartup()).getName();
            card.add(new H4("Startup: " + startUpName));
            card.add(new Paragraph("Titel der Stellenausschreibung: " + stellenausschreibungDTO.getTitel()));
            card.add(new Paragraph("Status: " + bewerbung.getStatus()));

            Button zurAusschreibung = new Button("Zur Stellenausschreibung");
            zurAusschreibung.addClickListener(click -> {
                        String stellenausschreibungsId = stellenausschreibungDTO.getId().toString();
                        UI.getCurrent().navigate("application/"+stellenausschreibungsId);
                    }
            );

            card.add(zurAusschreibung);
            bewerbungsLayout.add(card);
        }

        return bewerbungsLayout;
    }
}
