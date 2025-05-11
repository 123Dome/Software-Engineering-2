package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.util.Globals;

@Route(value = "registration")
@PageTitle("Registration")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class RegistrationView extends Div { // 3. Form (Spezialisierung / Vererbung)

    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");
    private TextField password = new TextField("Passwort");
    private TextField password_confirm = new TextField("Passwort bestätigen");

    private Button cancel = new Button ("Cancel");
    private Button register = new Button("Register");

    private Binder<User> binder = new Binder(User.class);

    public RegistrationView() {
        addClassName("enter-car-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        email.addValueChangeListener(
                event -> {
                    System.out.println("New Value: " + event.getValue());

                    // Weitere Bearbeitung des aktuell eingegebenen Werts (z.B. Abfrage in Richtung DB)
                });

        // Default Mapping of Cars attributes and the names of this View based on names
        // Source: https://vaadin.com/docs/flow/binding-data/tutorial-flow-components-binder-beans.html
        binder.bindInstanceFields(this); // Nr. 1 HOOK / API-Methode
        clearForm();

        // Registrierung eines Listeners Nr. 1 (moderne Variante mit Lambda-Expression)
        cancel.addClickListener(event -> clearForm());

        // Registrierung eines Listeners Nr. 2 (traditionelle Variante mit anonymen Objekt)
        cancel.addClickListener(
                new ComponentEventListener() {
                    @Override
                    public void onComponentEvent(ComponentEvent event) { // Nr. 2 Callback!!
                        clearForm();
                    }
                });

        register.addClickListener(
                e -> {
                    // Speicherung der Daten über das zuhörige Control-Object.
                    // Daten des Autos werden aus Formular erfasst und als DTO übergeben.
                    // Zusätzlich wird das aktuelle UserDTO übergeben.
                    //User user = (User) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
                    //carService.createCar(binder.getBean(), user);

                    //ToDO: Implementierung einer RegistrationControl zur Registrierung; Binder;

                    Notification.show("Nutzer angelegt.");
                    clearForm();
                    UI.getCurrent().navigate((Globals.Pages.LOGIN_VIEW));
                });
    }

    private void clearForm() {
        binder.setBean(new User());
    }

    private Component createTitle() {
        return new H3("Student registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername,email, vorname, nachname, geburtsdatum,password,password_confirm);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(register);
        buttonLayout.add(register);
        return buttonLayout;
    }
}