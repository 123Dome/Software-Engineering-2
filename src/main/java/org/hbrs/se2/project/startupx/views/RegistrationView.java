package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.RegistrationControl;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "registration", layout = AppView.class)
@PageTitle("Registration")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class RegistrationView extends Div { // 3. Form (Spezialisierung / Vererbung)


    //Registrierungsformular
    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private PasswordField passwort = new PasswordField("Passwort");
    private PasswordField passwort_bestätigen = new PasswordField("Passwort bestätigen");

    private Button abbrechen = new Button ("Abbrechen");
    private Button registrieren = new Button("Registrieren");

    private Binder<UserDTO> binder = new Binder<>(UserDTO.class);
    private UserDTO userDTO = new UserDTO();

    @Autowired
    RegistrationControl registrationControl;

    public RegistrationView() {
        addClassName("enter-car-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.setBean(userDTO);
        binder.bindInstanceFields(this);

        abbrechen.addClickListener(event -> clearForm());

        registrieren.addClickListener(
                e -> {
                    String passwort = this.passwort.getValue();
                    String passwort_confirm = passwort_bestätigen.getValue();

                    if(!passwort_confirm.equals(passwort)) {
                        Notification.show("Passwörter stimmen nicht überein");
                        return;
                    }
                    
                    if (checkForJorgeEasterEgg()) {
                        Notification.show("Nutzer nicht registriert");
                        return;
                    }

                    if(binder.validate().isOk()) {
                        userDTO.setPasswort(passwort);
                        try {
                            registrationControl.registerUser(userDTO);
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

    private boolean checkForJorgeEasterEgg() {
        boolean isEasterEgg = "3".equals(nutzername.getValue()) &&
                "3".equals(email.getValue()) &&
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

    // TODO: Echtzeitüberwachung E-Mail und Benutzername?

    private void clearForm() {
        userDTO = new UserDTO();
        binder.setBean(userDTO);
        passwort.clear();
        passwort_bestätigen.clear();
    }


    private Component createTitle() {
        return new H3("Student registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername,email, vorname, nachname, geburtsdatum, passwort, passwort_bestätigen);
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