package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
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
        setComponentIds();

        userDTOBinder.setBean(userDTO);
        userDTOBinder.forField(nutzername)
                .asRequired("Benutzername darf nicht leer sein")
                .bind(UserDTO::getNutzername, UserDTO::setNutzername);
        userDTOBinder.forField(email)
                .asRequired("E-Mail darf nicht leer sein")
                .withValidator(email -> email.contains("@"), "E-Mail enthält kein '@'")
                .bind(UserDTO::getEmail, UserDTO::setEmail);
        userDTOBinder.forField(vorname)
                .asRequired("Vorname darf nicht leer sein")
                .withValidator(name -> name.matches("[a-zA-ZäöüÄÖÜß\\s-]+"), "Nur Buchstaben erlaubt")
                .bind(UserDTO::getVorname, UserDTO::setVorname);
        userDTOBinder.forField(nachname)
                .asRequired("Nachname darf nicht leer sein")
                .withValidator(name -> name.matches("[a-zA-ZäöüÄÖÜß\\s-]+"), "Nur Buchstaben erlaubt")
                .bind(UserDTO::getNachname, UserDTO::setNachname);
        userDTOBinder.forField(geburtsdatum)
                .asRequired("Geburtsdatum darf nicht leer sein")
                .bind(UserDTO::getGeburtsdatum, UserDTO::setGeburtsdatum);

        userDTOBinder.forField(passwort)
                .asRequired("Passwort ist erforderlich")
                .withValidator(pwd -> pwd.length() >= 8, "Mindestens 8 Zeichen")
                .withValidator(pwd -> pwd.matches(".*[A-Z].*"), "Mindestens ein Großbuchstabe")
                .withValidator(pwd -> pwd.matches(".*[a-z].*"), "Mindestens ein Kleinbuchstabe")
                .withValidator(pwd -> pwd.matches(".*[^a-zA-Z0-9].*"), "Mindestens ein Sonderzeichen")
                .bind(UserDTO::getPasswort, UserDTO::setPasswort);

        studentDTOBinder.setBean(studentDTO);
        studentDTOBinder.forField(matrikelnr)
                .asRequired("Matrikelnummer darf nicht leer sein")
                .withValidator(nr -> nr != null && nr > 0, "Nur positive Zahlen erlaubt")
                .bind(StudentDTO::getMatrikelnr, StudentDTO::setMatrikelnr);

        studentDTOBinder.forField(studiengang)
                .asRequired("Studiengang darf nicht leer sein")
                .withConverter(
                        dto -> dto != null ? dto.getId() : null,
                        id -> id != null ? studiengangControl.getById(id) : null
                )
                .bind(StudentDTO::getStudiengang, StudentDTO::setStudiengang);

        List<SkillDTO> allSkills = studiengangControl.findAllSkills();

        skills.setItemLabelGenerator(SkillDTO::getSkillName);
        skills.setItems(allSkills); // Alle Skills aus der DB laden

        studentDTOBinder.forField(skills)
                .asRequired("Wähle mindestens 1 Skill aus")
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

    // sets unique IDs for each component, needed for selenium test
    private void setComponentIds() {
        nutzername.setId("BenutzernameTextField");
        email.setId("EmailTextField");
        vorname.setId("VornameTextField");
        nachname.setId("NachnameTextField");
        geburtsdatum.setId("GeburtsdatumDatePicker");
        passwort.setId("PasswortTextField");
        passwort_bestätigen.setId("PasswortBestaetigenTextField");

        // Felder StudentDTO
        matrikelnr.setId("MatrikelnummerTextField");
        studiengang.setId("StudiengangComboBox");
        skills.setId("SkillsMultiComboBox");

        // Buttons
        abbrechen.setId("AbbrechenButton");
        registrieren.setId("RegistrierenButton");
    }

    private boolean checkForJorgeEasterEgg() {
        boolean isEasterEgg = "3".equals(nutzername.getValue()) &&
                "3@3".equals(email.getValue()) &&
                "3".equals(vorname.getValue()) &&
                "3".equals(nachname.getValue());

        if (isEasterEgg) {
            Dialog gifDialog = new Dialog();
            gifDialog.setCloseOnOutsideClick(true);
            gifDialog.setCloseOnEsc(true);

            com.vaadin.flow.component.html.Image gif = new com.vaadin.flow.component.html.Image(
                    "images/drei-jorge-gonzalez.gif", "Jorge Gonzalez");
            gif.setWidth("300px");

            gifDialog.add(gif);
            gifDialog.open();
            return true;
        }
        return false;
    }

    private Component createTitle() {
        return new H3("Student registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setId("registrationForm");
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
        if (checkForJorgeEasterEgg()){
            return;
        }
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
