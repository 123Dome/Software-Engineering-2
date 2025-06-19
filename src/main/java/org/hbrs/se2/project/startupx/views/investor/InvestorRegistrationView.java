package org.hbrs.se2.project.startupx.views.investor;

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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.BrancheControl;
import org.hbrs.se2.project.startupx.control.RegistrationControl;
import org.hbrs.se2.project.startupx.dtos.*;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "registrationInvestor", layout = AppView.class)
@PageTitle("RegistrationInvestor")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class InvestorRegistrationView extends Div { // 3. Form (Spezialisierung / Vererbung)

    // Felder UserDTO
    private final TextField nutzername = new TextField("Benutzername");
    private final TextField email = new TextField("Email");
    private final TextField vorname = new TextField("Vorname");
    private final TextField nachname = new TextField("Nachname");
    private final DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private final PasswordField passwort = new PasswordField("Passwort");
    private final PasswordField passwort_bestätigen = new PasswordField("Passwort bestätigen");

    // Felder InvestorDTO
    private final ComboBox<BrancheDTO> branche = new ComboBox<>("Branche");
    private final IntegerField budget = new IntegerField("Budget");

    // Buttons
    private final Button abbrechen = new Button ("Abbrechen");
    private final Button registrieren = new Button("Registrieren");

    private final Binder<UserDTO> userDTOBinder = new Binder<>(UserDTO.class);
    private final Binder<InvestorDTO> investorDTOBinder = new Binder<>(InvestorDTO.class);

    private UserDTO userDTO = new UserDTO();
    private InvestorDTO investorDTO = new InvestorDTO();

    @Autowired
    RegistrationControl registrationControl;

    @Autowired
    BrancheControl brancheControl;

    public InvestorRegistrationView(BrancheControl brancheControl, RegistrationControl registrationControl) {
        addClassName("enter-car-view");

        this.brancheControl = brancheControl;
        this.registrationControl = registrationControl;

        add(createTitle());
        add(createFormLayout());
        configureBrancheComboBox();
        add(createButtonLayout());

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

        investorDTOBinder.setBean(investorDTO);
        investorDTOBinder.forField(branche)
                .asRequired("Branche darf nicht leer sein")
                .withConverter(
                        dto -> dto != null ? dto.getId() : null,
                        id -> id != null ? brancheControl.getById(id) : null
                )
                .bind(InvestorDTO::getBrancheId, InvestorDTO::setBrancheId);
        investorDTOBinder.forField(budget)
                .asRequired("Budget darf nicht leer sein")
                .withConverter(
                        integer -> integer != null ? integer.longValue() : null,   // Integer → Long
                        aLong -> aLong != null ? aLong.intValue() : null           // Long → Integer
                )
                .withValidator(b -> b != null && b > 0, "Nur positive Zahlen erlaubt")
                .bind(InvestorDTO::getBudget, InvestorDTO::setBudget);




        abbrechen.addClickListener(event -> {
                clearForm();
                UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
        });

        registrieren.addClickListener(event -> handleRegistration());
    }

    private void configureBrancheComboBox() {
        List<BrancheDTO> branchenListe = brancheControl.getAll();
        branche.setItemLabelGenerator(BrancheDTO::getBezeichnung);
        branche.setItems(branchenListe);
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
        return new H3("Investor registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum,
                branche, budget, passwort, passwort_bestätigen);
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

        if (userDTOBinder.validate().isOk() && investorDTOBinder.validate().isOk()) {
            userDTO.setPasswort(passwort.getValue());
            try {
                registrationControl.registerInvestor(userDTO, investorDTO);
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
        investorDTO = new InvestorDTO();
        userDTOBinder.setBean(userDTO);
        investorDTOBinder.setBean(investorDTO);
        passwort.clear();
        passwort_bestätigen.clear();
        branche.clear();
        budget.clear();
    }
}