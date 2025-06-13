package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
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
        add(setUpBewertungenGrid());
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

    private Component setUpBewertungenGrid(){
        List<BewertungDTO> bewertungen = bewertungControl.getAlleBewertungZuUser(userDTO.getId());

        if(bewertungen.isEmpty()){
            return new Paragraph("Keine Bewertungen vorhanden");
        }

        Grid<BewertungDTO> grid = new Grid<>(BewertungDTO.class);
        grid.setItems(bewertungen);

        grid.setColumns("startup", "bewertung", "kommentar", "erstellungsdatum");
        grid.getColumnByKey("startup").setHeader("Startup-ID");
        grid.getColumnByKey("bewertung").setHeader("Sterne");
        grid.getColumnByKey("kommentar").setHeader("Kommentar");
        grid.getColumnByKey("erstellungsdatum").setHeader("Datum");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new H3("Meine Bewertungen"), grid);

        return layout;
    }
}
