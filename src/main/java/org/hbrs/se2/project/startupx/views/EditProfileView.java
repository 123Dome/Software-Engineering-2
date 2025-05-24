package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.control.StudiengangControl;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.StudiengangDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "EditProfile", layout = AppView.class)
@PageTitle("Profil bearbeiten")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class EditProfileView extends Div{

    @Autowired
    private EditProfilControl editProfilControl;

    @Autowired
    private StudiengangControl studiengangControl;

    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");

    //private TextField matrikelnr = new TextField("Matrikelnummer");
    private TextField steckbrief = new TextField("Steckbrief");
    private ComboBox<StudiengangDTO> studiengang = new ComboBox<>("Studiengang");

    //TODO: Daten des Users müssen abgerufen werden und editierbar sein, danach zurückgeschrieben werden
    public EditProfileView(EditProfilControl editProfilControl, StudiengangControl studiengangControl) {
        this.studiengangControl = studiengangControl;
        this.editProfilControl = editProfilControl;
        addClassName("EditProfileView");

        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        StudentDTO studentDTO = editProfilControl.getStudentDTO(userDTO);

        add(createTitle());
        setTextFieldWithUserData(userDTO, studentDTO);
        add(createFormLayout());
        add(createSaveButton2(userDTO, studentDTO));
        Button changePasswordButton = new Button("Passwort ändern");
        Dialog passwordDialog = createPasswordDialog(userDTO);
        changePasswordButton.addClickListener(e -> passwordDialog.open());

        add(changePasswordButton, passwordDialog);
    }

    private Component createTitle() {
        return new H3("Profil bearbeiten");
    }

    private void setTextFieldWithUserData(UserDTO userDTO, StudentDTO studentDTO){
        nutzername.setValue(userDTO.getNutzername());
        email.setValue(userDTO.getEmail());
        vorname.setValue(userDTO.getVorname());
        nachname.setValue(userDTO.getNachname());
        geburtsdatum.setValue(userDTO.getGeburtsdatum());
        steckbrief.setValue(studentDTO.getSteckbrief());
        studiengang.setItems(studiengangControl.getAll());
        studiengang.setItemLabelGenerator(StudiengangDTO::getStudiengang);
    }

    private Button createSaveButton2 (UserDTO userDTO, StudentDTO studentDTO) {
        Button saveButton = new Button("Änderungen speichern", e -> {
            if(checkTextfields()){
                StudentDTO newStudentDTO = new StudentDTO();
                UserDTO newUserDTO = new UserDTO();
                newUserDTO.setId(userDTO.getId());
                newUserDTO.setRollen(userDTO.getRollen());
                newUserDTO.setNutzername(nutzername.getValue());
                newUserDTO.setEmail(email.getValue());
                newUserDTO.setVorname(vorname.getValue());
                newUserDTO.setNachname(nachname.getValue());
                newUserDTO.setGeburtsdatum(geburtsdatum.getValue());
                newUserDTO.setPasswort(userDTO.getPasswort());

                newStudentDTO.setId(userDTO.getId());
                newStudentDTO.setSteckbrief(steckbrief.getValue());
                newStudentDTO.setStudiengang(studiengang.getValue().getId());
                if(editProfilControl.updateStudent(newUserDTO, studentDTO)){
                    Notification.show("Profil aktualisiert");
                }
            }
            // TODO: Fehlerausgabe
        });
        return saveButton;
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum, steckbrief, studiengang);
        return formLayout;
    }

    private Button createSaveButton (UserDTO userDTO) {
        Button saveButton = new Button("Änderungen speichern", e -> {
            if(checkTextfields()){
                UserDTO newUserDTO = new UserDTO();
                newUserDTO.setId(userDTO.getId());
                newUserDTO.setRollen(userDTO.getRollen());
                newUserDTO.setNutzername(nutzername.getValue());
                newUserDTO.setEmail(email.getValue());
                newUserDTO.setVorname(vorname.getValue());
                newUserDTO.setNachname(nachname.getValue());
                newUserDTO.setGeburtsdatum(geburtsdatum.getValue());
                newUserDTO.setPasswort(userDTO.getPasswort());
                if(editProfilControl.updateUser(newUserDTO)){
                    Notification.show("Profil aktualisiert");
                }
            }
            // TODO: Fehlerausgabe
        });
        return saveButton;
    }

    private Dialog createPasswordDialog(UserDTO userDTO) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Passwort ändern");

        PasswordField currentPassword = new PasswordField("Aktuelles Passwort");
        PasswordField newPassword = new PasswordField("Neues Passwort");
        PasswordField confirmPassword = new PasswordField("Passwort bestätigen");

        VerticalLayout layout = new VerticalLayout(currentPassword, newPassword, confirmPassword);
        layout.setSpacing(true);
        layout.setPadding(false);

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


    private boolean checkTextfields(){
        // TODO: Eigene Exceptionklasse erstellen
        if(nutzername.getValue() == null || nutzername.getValue().isEmpty()){
            throw new IllegalArgumentException("Nutzername darf nicht leer sein");
        }
        if(email.getValue() == null || email.getValue().isEmpty()){
            // TODO: Prüfung auf richtiges E-Mail Format
            throw new IllegalArgumentException("Email darf nicht leer sein");
        }
        if(vorname.getValue() == null || vorname.getValue().isEmpty()){
            throw new IllegalArgumentException("Vorname darf nicht leer sein");
        }
        if(nachname.getValue() == null || nachname.getValue().isEmpty()){
            throw new IllegalArgumentException("Nachname darf nicht leer sein");
        }
        if(geburtsdatum.getValue() == null){
            throw new IllegalArgumentException("Geburtsdatum darf nicht leer sein");
        }
        if (studiengang.getValue()== null) {
            throw new IllegalArgumentException("Es muss ein Studiengang ausgewählt sein");
        }
        return true;
    }

}
