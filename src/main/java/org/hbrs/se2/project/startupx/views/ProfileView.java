package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.mapper.StudentMapper;
import org.hbrs.se2.project.startupx.repository.StudentRepository;
import org.hbrs.se2.project.startupx.util.Globals;

import java.util.List;

@Route(value = "userProfile", layout = AppView.class)
@PageTitle("Mein Profil")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class ProfileView extends Div {

    private UserDTO userDTO;

    private StudentRepository studentRepository;

    private final ManageStartupControl manageStartupControl;

    private final BewertungControl bewertungControl;

    private final VerticalLayout bewertungenLayout = new VerticalLayout();

    public ProfileView(ManageStartupControl manageStartupControl, StudentRepository studentRepository, BewertungControl bewertungControl) {
        this.studentRepository = studentRepository;
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        addClassName("profile-view");
        loadCurrentUser();
        add(createProfileLayout());
        StudentDTO studentDTO = StudentMapper.mapToStudentDto(studentRepository.findById(userDTO.getStudent()).get());
        if (!studentDTO.getStartups().isEmpty()) {
            add(setUpGrid(studentDTO));
        }
        add(setUpBewertungenKarten());
    }

    //Aktuelle Nutzerdaten laden
    private void loadCurrentUser() {
        userDTO = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
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

        infoLayout.add(
                new H3(userDTO.getVorname() + " " +  userDTO.getNachname()),
                new Paragraph("Nutzername: " + userDTO.getNutzername()),
                new Paragraph("Geburtsdatum: " + userDTO.getGeburtsdatum()),
                new Paragraph("Email: " + userDTO.getEmail())
        );

        return infoLayout;
    }

    //ImageLayout und Nutzerdaten-Layout werden zusammengefügt
    private Component createProfileLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(createProfileImage(), createUserInfoLayout());

        return layout;
    }

    private Grid setUpGrid(StudentDTO studentDTO) {
        //Soll zukünftig alle StartUps listen
        List<StartupDTO> startups = manageStartupControl.getStartups(studentDTO);
        Grid<StartupDTO> grid = new Grid<>(StartupDTO.class);
        grid.setItems(startups);

        grid.asSingleSelect().addValueChangeListener(event -> {
            StartupDTO selected = event.getValue();
            if (selected != null) {
                getUI().ifPresent(ui ->
                        ui.navigate("startup/" + selected.getId())
                );
            }
        });

        return grid;
    }

    private Component setUpBewertungenKarten(){
        bewertungenLayout.removeAll();

        bewertungenLayout.add(new H3("Meine Bewertungen"));

        List<BewertungDTO> bewertungen = bewertungControl.getAlleBewertungZuUser(userDTO.getId());

        if (bewertungen.isEmpty()) {
            bewertungenLayout.add(new Paragraph("Keine Bewertungen vorhanden"));
            return bewertungenLayout;
        }

        for(BewertungDTO bewertung : bewertungen) {
            Div card = new Div();
            card.addClassName("bewertungen-karte");
            card.setWidth("400px");
            card.getStyle().set("border", "1px solid #ccc");
            card.getStyle().set("border-radius", "8px");
            card.getStyle().set("padding", "16px");
            card.getStyle().set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)");

            // Startup zur Bewertung
            String startUpName = manageStartupControl.findByID(bewertung.getStartup()).getName();
            card.add(new H4("Startup: " + startUpName));

            // Sterne
            HorizontalLayout sterneLayout = new HorizontalLayout();
            for(int i = 0; i < 5; i++){
                Span stern = new Span(i <= bewertung.getBewertung() ? "★" : "☆");
                stern.getStyle().set("font-size", "20px");
                sterneLayout.add(stern);
            }
            card.add(sterneLayout);

            // Kommentar (optional)
            if(!bewertung.getKommentar().isEmpty()) {
                Paragraph kommentar = new Paragraph(bewertung.getKommentar());
                kommentar.getStyle().set("font-style", "italic");
                kommentar.getStyle().set("margin-top", "8px");
                card.add(kommentar);
            }

            // Datum
            Paragraph datum = new Paragraph("Datum: " + bewertung.getErstellungsdatum());
            datum.getStyle().set("font-size", "0.9em");
            datum.getStyle().set("color", "#666");
            card.add(datum);

            // Löschen-Button
            Button bewertungLöschenButton = new Button("Löschen", e -> {
                // Bestätigungsdialog
                Dialog löschenBestätigenDialog = new Dialog();
                löschenBestätigenDialog.add(new Paragraph("Wollen Sie die Bewertung wirklich löschen?"));
                Button löschenBestätigenButton = new Button("Ja", event -> {
                    try {
                        bewertungControl.deleteBewertung(bewertung.getId());
                        Notification.show("Bewertung gelöscht.");
                        setUpBewertungenKarten();
                    } catch (Exception ex) {
                        Notification.show(ex.getMessage());
                    }
                    löschenBestätigenDialog.close();
                });
                Button löschenAbbrechenButton = new Button("Abbrechen", event -> löschenBestätigenDialog.close());
                löschenBestätigenDialog.getFooter().add(löschenBestätigenButton, löschenAbbrechenButton);
                löschenBestätigenDialog.open();
            });
            card.add(bewertungLöschenButton);
            bewertungenLayout.add(card);
        }

        return bewertungenLayout;
    }
}
