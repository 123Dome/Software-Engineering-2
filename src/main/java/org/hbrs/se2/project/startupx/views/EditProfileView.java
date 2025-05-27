package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ScrollOptions;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.control.StudiengangControl;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "EditProfile", layout = AppView.class)
@PageTitle("Profil bearbeiten")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class EditProfileView extends Div {

    @Autowired
    private EditProfilControl editProfilControl;

    @Autowired
    private StudiengangControl studiengangControl;

    private final TextField nutzername = new TextField("Benutzername");
    private final TextField email = new TextField("Email");
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private final TextArea steckbrief = new TextArea("Steckbrief");
    private final ComboBox<StudiengangDTO> studiengang = new ComboBox<>("Studiengang");
    private final MultiSelectComboBox<SkillDTO> skills = new MultiSelectComboBox<>("Skills");

    private final Binder<UserDTO> userDTOBinder = new Binder<>(UserDTO.class);
    private final Binder<StudentDTO> studentDTOBinder = new Binder<>(StudentDTO.class);

    //private TextField matrikelnr = new TextField("Matrikelnummer");

    //TODO: Daten des Users müssen abgerufen werden und editierbar sein, danach zurückgeschrieben werden
    public EditProfileView(EditProfilControl editProfilControl, StudiengangControl studiengangControl) {
        this.editProfilControl = editProfilControl;
        this.studiengangControl = studiengangControl;
        addClassName("edit-profile-view");

        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        StudentDTO studentDTO = editProfilControl.getStudentDTO(userDTO);

        configureStudiengangComboBox(studentDTO);

        userDTOBinder.setBean(userDTO);
        userDTOBinder.forField(nutzername)
                .asRequired("Benutzername darf nicht leer sein")
                .bind(UserDTO::getNutzername, UserDTO::setNutzername);
        userDTOBinder.forField(email)
                .asRequired("Email darf nicht leer sein")
                .withValidator(email -> email.contains("@"), "E-Mail muss gültig sein")
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


        studentDTOBinder.forField(steckbrief)
                .asRequired("Steckbrief darf nicht leer sein")
                .bind(StudentDTO::getSteckbrief, StudentDTO::setSteckbrief);
        studentDTOBinder.forField(studiengang)
                .asRequired("Studiengang darf nicht leer sein")
                .withConverter(
                        sgDto -> sgDto != null ? sgDto.getId() : null,
                        id -> id != null ? studiengangControl.getById(id) : null
                )
                .bind(StudentDTO::getStudiengang, StudentDTO::setStudiengang);

        userDTOBinder.setBean(userDTO);
        studentDTOBinder.setBean(studentDTO);

        List<SkillDTO> allSkills = studiengangControl.findAllSkills();

        skills.setItemLabelGenerator(SkillDTO::getSkillName);
        skills.setItems(allSkills);

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

        VerticalLayout container = new VerticalLayout();
        container.setPadding(true);
        container.setSpacing(true);
        container.setWidthFull();
        container.setAlignItems(FlexComponent.Alignment.START);

        container.add(createTitle());
        container.add(createFormLayout());
        container.add(createButtonRow(userDTO, studentDTO));

        add(container);
    }

    private Component createTitle() {
        H3 title = new H3("Profil bearbeiten");
        title.getStyle().set("margin-bottom", "1rem");
        return title;
    }

    private void configureStudiengangComboBox(StudentDTO studentDTO) {
        List<StudiengangDTO> alleSG = studiengangControl.getAll();
        studiengang.setItemLabelGenerator(StudiengangDTO::getStudiengang);
        studiengang.setItems(alleSG);
        studiengang.setValue(
                alleSG.stream()
                        .filter(sg -> sg.getId().equals(studentDTO.getStudiengang()))
                        .findFirst()
                        .orElse(null)
        );
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum, skills, studiengang, steckbrief);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setWidthFull();
        return formLayout;
    }

    private Component createButtonRow(UserDTO userDTO, StudentDTO studentDTO) {
        Button save = new Button("Änderungen speichern", e -> {
                if (editProfilControl.updateStudent(userDTO, studentDTO)) {
                    Notification.show("Profil aktualisiert");
                }
        });

        Button changePassword = new Button("Passwort ändern");
        Dialog passwordDialog = createPasswordDialog(userDTO);
        changePassword.addClickListener(e -> passwordDialog.open());

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePassword.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(save, changePassword);
        buttons.setSpacing(true);
        return new VerticalLayout(buttons, passwordDialog);
    }

    private Dialog createPasswordDialog(UserDTO userDTO) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Passwort ändern");

        PasswordField currentPassword = new PasswordField("Aktuelles Passwort");
        PasswordField newPassword = new PasswordField("Neues Passwort");
        PasswordField confirmPassword = new PasswordField("Passwort bestätigen");

        VerticalLayout layout = new VerticalLayout(currentPassword, newPassword, confirmPassword);
        layout.setSpacing(true);

        Button save = new Button("Speichern", event -> {
            if (!isValidPassword(newPassword.getValue())) {
                Notification.show("Passwort muss mindestens 8 Zeichen, einen Groß- und Kleinbuchstaben und ein Sonderzeichen enthalten.");
                return;
            }
            if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Die Passwörter stimmen nicht überein.");
                return;
            }
            if (currentPassword.getValue().isEmpty() || newPassword.getValue().isEmpty()) {
                Notification.show("Bitte alle Felder ausfüllen.");
                return;
            }

            boolean success = editProfilControl.updatePassword(userDTO.getId(), currentPassword.getValue(), newPassword.getValue());
            if (success) {
                Notification.show("Passwort erfolgreich geändert");
                dialog.close();
            } else {
                Notification.show("Aktuelles Passwort ist falsch");
            }
        });

        Button cancel = new Button("Abbrechen", event -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        dialog.getFooter().add(buttons);
        dialog.add(layout);
        return dialog;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[^a-zA-Z0-9].*");
    }

}
