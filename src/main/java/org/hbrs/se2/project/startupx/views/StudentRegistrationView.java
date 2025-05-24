package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.RegistrationControl;
import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "registrationStudent", layout = AppView.class)
@PageTitle("RegistrationStudent")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class StudentRegistrationView extends Div { // 3. Form (Spezialisierung / Vererbung)


    //Registrierungsformular UserDTO
    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private PasswordField passwort = new PasswordField("Passwort");


    //Registrierungsformular StudentDTO
    //TODO: Steckbrief Textfeld, Skills angeben lassen(noch nicht, bis Control da ist),Studiengänge über StudiengangControl ziehen
    private IntegerField matrikelnr = new IntegerField("Matrikelnummer");
    private TextField studiengang = new TextField("Studiengang");

    private PasswordField passwort_bestätigen = new PasswordField("Passwort bestätigen");

    private Button abbrechen = new Button ("Abbrechen");
    private Button registrieren = new Button("Registrieren");

    private Binder<UserDTO> userDTOBinder = new Binder<>(UserDTO.class);
    private Binder<StudentDTO> studentDTOBinder = new Binder<>(StudentDTO.class);

    private UserDTO userDTO = new UserDTO();
    private StudentDTO studentDTO = new StudentDTO();

    @Autowired
    RegistrationControl registrationControl;

    public StudentRegistrationView() {
        addClassName("enter-car-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        userDTOBinder.setBean(userDTO);
        userDTOBinder.bindInstanceFields(this);

        studentDTOBinder.setBean(studentDTO);
        studentDTOBinder.bindInstanceFields(this);

        abbrechen.addClickListener(event -> clearForm());

        registrieren.addClickListener(
                e -> {
                    String passwort = this.passwort.getValue();
                    String passwort_confirm = passwort_bestätigen.getValue();

                    if(!passwort_confirm.equals(passwort)) {
                        Notification.show("Passwörter stimmen nicht überein");
                        return;
                    }

                    if(userDTOBinder.validate().isOk()) {
                        userDTO.setPasswort(passwort);
                        try {
                            registrationControl.registerStudent(userDTO, studentDTO);
                            Notification.show("Nutzer registriert!");
                            clearForm();
                            UI.getCurrent().navigate((Globals.Pages.LOGIN_VIEW));
                        } catch (Exception ex) {
                            Notification.show("Fehler:" + ex.getMessage());
                        }
                    } else {
                        Notification.show("Überprüfe deine Eingaben");
                    }
                });
    }

    //TODO: umwandeln in loadStudiengang()
    /*
    private void loadBranchen() {
        List<BrancheDTO> brancheDTOs = manageStartupControl.getBranches();
        for (BrancheDTO dto : brancheDTOs) {
            brancheMap.put(dto.getBezeichnung(), dto.getId());
        }
        brancheComboBox.setItems(brancheMap.keySet());
    }
     */
    // TODO: Echtzeitüberwachung E-Mail und Benutzername?

    private void clearForm() {
        userDTO = new UserDTO();
        userDTOBinder.setBean(userDTO);
        passwort.clear();
        passwort_bestätigen.clear();
    }


    private Component createTitle() {
        return new H3("Student registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername,email, vorname, nachname, geburtsdatum, matrikelnr, studiengang, passwort, passwort_bestätigen);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        registrieren.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(registrieren);
        buttonLayout.add(abbrechen);
        return buttonLayout;
    }
}