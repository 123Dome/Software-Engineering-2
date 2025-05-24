package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ScrollOptions;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.control.StudiengangControl;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    private final TextField steckbrief = new TextField("Steckbrief");
    private final ComboBox<StudiengangDTO> studiengang = new ComboBox<>("Studiengang");

    private final Binder<UserDTO> userDTOBinder = new Binder<>(UserDTO.class);
    private final Binder<StudentDTO> studentDTOBinder = new Binder<>(StudentDTO.class);

    public EditProfileView(EditProfilControl editProfilControl, StudiengangControl studiengangControl) {
        this.editProfilControl = editProfilControl;
        this.studiengangControl = studiengangControl;
        addClassName("edit-profile-view");

        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        StudentDTO studentDTO = editProfilControl.getStudentDTO(userDTO);

        configureStudiengangComboBox(studentDTO);

        userDTOBinder.bindInstanceFields(this);
        studentDTOBinder.forField(steckbrief).bind(StudentDTO::getSteckbrief, StudentDTO::setSteckbrief);
        studentDTOBinder.forField(studiengang)
                .withConverter(
                        sgDto -> sgDto != null ? sgDto.getId() : null,
                        id -> id != null ? studiengangControl.getById(id) : null
                )
                .bind(StudentDTO::getStudiengang, StudentDTO::setStudiengang);

        userDTOBinder.setBean(userDTO);
        studentDTOBinder.setBean(studentDTO);

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
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum, steckbrief, studiengang);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.setWidthFull();
        return formLayout;
    }

    private Component createButtonRow(UserDTO userDTO, StudentDTO studentDTO) {
        Button save = new Button("Änderungen speichern", e -> {
            if (checkTextfields()) {
                if (editProfilControl.updateStudent(userDTO, studentDTO)) {
                    Notification.show("Profil aktualisiert");
                }
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

    private boolean checkTextfields() {
        if (nutzername.isEmpty()) throw new IllegalArgumentException("Nutzername darf nicht leer sein");
        if (email.isEmpty()) throw new IllegalArgumentException("Email darf nicht leer sein");
        if (vorname.isEmpty()) throw new IllegalArgumentException("Vorname darf nicht leer sein");
        if (nachname.isEmpty()) throw new IllegalArgumentException("Nachname darf nicht leer sein");
        if (geburtsdatum.isEmpty()) throw new IllegalArgumentException("Geburtsdatum darf nicht leer sein");
        if (studiengang.isEmpty()) throw new IllegalArgumentException("Es muss ein Studiengang ausgewählt sein");
        return true;
    }
}
