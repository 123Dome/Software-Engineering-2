package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.RegistrationControl;
import org.hbrs.se2.project.startupx.control.StudiengangControl;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Skill;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "registrationStudent", layout = AppView.class)
@PageTitle("RegistrationStudent")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class StudentRegistrationView extends Div {

    // Felder UserDTO
    private final TextField nutzername = new TextField("Benutzername");
    private final TextField email = new TextField("Email");
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private final PasswordField passwort = new PasswordField("Passwort");
    private final PasswordField passwort_bestätigen = new PasswordField("Passwort bestätigen");

    // Felder StudentDTO
    private final IntegerField matrikelnr = new IntegerField("Matrikelnummer");
    private final ComboBox<StudiengangDTO> studiengang = new ComboBox<>("Studiengang");
    private final MultiSelectComboBox<SkillDTO> skills = new MultiSelectComboBox<>("Skills");

    // Buttons
    private final Button abbrechen = new Button("Abbrechen");
    private final Button registrieren = new Button("Registrieren");

    private final Binder<UserDTO> userDTOBinder = new Binder<>(UserDTO.class);
    private final Binder<StudentDTO> studentDTOBinder = new Binder<>(StudentDTO.class);

    private UserDTO userDTO = new UserDTO();
    private StudentDTO studentDTO = new StudentDTO();

    @Autowired
    private RegistrationControl registrationControl;

    @Autowired
    private StudiengangControl studiengangControl;

    public StudentRegistrationView(StudiengangControl studiengangControl, RegistrationControl registrationControl) {
        this.studiengangControl = studiengangControl;
        this.registrationControl = registrationControl;
        addClassName("enter-car-view");

        configureStudiengangComboBox();

        userDTOBinder.setBean(userDTO);
        userDTOBinder.bindInstanceFields(this);

        studentDTOBinder.setBean(studentDTO);
        studentDTOBinder.forField(matrikelnr)
                .bind(StudentDTO::getMatrikelnr, StudentDTO::setMatrikelnr);

        studentDTOBinder.forField(studiengang)
                .withConverter(
                        dto -> dto != null ? dto.getId() : null,
                        id -> id != null ? studiengangControl.getById(id) : null
                )
                .bind(StudentDTO::getStudiengang, StudentDTO::setStudiengang);

        List<SkillDTO> allSkills = studiengangControl.findAllSkills();

        skills.setItemLabelGenerator(SkillDTO::getSkillName);
        skills.setItems(allSkills); // Alle Skills aus der DB laden

        studentDTOBinder.forField(skills)
                .withConverter(
                        (Set<SkillDTO> selectedSkills) -> {
                            if (selectedSkills == null || selectedSkills.isEmpty()) {
                                return Set.of();
                            }
                            return selectedSkills.stream()
                                    .map(SkillDTO::getId)
                                    .collect(Collectors.toSet());
                        },
                        (Set<Long> skillIds) -> {
                            if (skillIds == null || skillIds.isEmpty()) {
                                return Set.of();
                            }
                            return allSkills.stream()
                                    .filter(skill -> skillIds.contains(skill.getId()))
                                    .collect(Collectors.toSet());
                        }
                )
                .bind(StudentDTO::getSkills, StudentDTO::setSkills);

        skills.setHeight("auto");
        skills.setWidth("auto");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        // Listener
        abbrechen.addClickListener(event -> {
            clearForm();
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
        });

        registrieren.addClickListener(e -> handleRegistration());
    }

    private void configureStudiengangComboBox() {
        List<StudiengangDTO> studiengangListe = studiengangControl.getAll();
        studiengang.setItemLabelGenerator(StudiengangDTO::getStudiengang);
        studiengang.setItems(studiengangListe);
    }

    private Component createTitle() {
        return new H3("Student registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum,
                matrikelnr, studiengang, skills, passwort, passwort_bestätigen);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        registrieren.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(registrieren, abbrechen);
        return buttonLayout;
    }

    private void handleRegistration() {
        if (!passwort.getValue().equals(passwort_bestätigen.getValue())) {
            Notification.show("Passwörter stimmen nicht überein");
            return;
        }

        if (userDTOBinder.validate().isOk() && studentDTOBinder.validate().isOk()) {
            userDTO.setPasswort(passwort.getValue());
            try {
                registrationControl.registerStudent(userDTO, studentDTO);
                Notification.show("Nutzer registriert!");
                clearForm();
                UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            } catch (Exception ex) {
                Notification.show("Fehler: " + ex.getMessage());
            }
        } else {
            Notification.show("Überprüfe deine Eingaben");
        }
    }

    private void clearForm() {
        userDTO = new UserDTO();
        studentDTO = new StudentDTO();
        userDTOBinder.setBean(userDTO);
        studentDTOBinder.setBean(studentDTO);
        passwort.clear();
        passwort_bestätigen.clear();
        studiengang.clear();
    }
}
