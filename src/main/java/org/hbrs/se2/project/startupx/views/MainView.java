package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.hbrs.se2.project.startupx.control.LoginControl;
import org.hbrs.se2.project.startupx.control.exception.DatabaseUserException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 * ToDo: Integration einer Seite zur Registrierung von Benutzern
 */
@Route(value = "" )
@RouteAlias(value = "login")
public class MainView extends VerticalLayout {

    // Default: Einfügung des LoginControl-Objekts als Singleton (Spring-Annotation)
    // Frage: Gibt es hier vielleicht Probleme ... ?
    @Autowired
    private LoginControl loginControl;

    Notification notification =new Notification("Registrierung von Studenten noch nicht fertig. Kommt noch :)");

    public MainView() {
        setSizeFull();
        LoginForm component = new LoginForm();

        component.addLoginListener(e -> {

            boolean isAuthenticated = false;
            try {
                isAuthenticated = loginControl.authentificate( e.getUsername() , e.getPassword() );

            } catch (DatabaseUserException databaseException) {
                Dialog dialog = new Dialog();
                dialog.add( new Text( databaseException.getReason()) );
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            }
            if (isAuthenticated) {
                grabAndSetUserIntoSession();
                navigateToMainPage();

            } else {
                // Kann noch optimiert werden
                component.setError(true);
            }
        });

        // Registrierungs-Button hinzugefügt, verweist auf die Registrierungsseite
        Button registerButton = new Button("Zur Registrierung", event ->
                UI.getCurrent().navigate(RegistrationView.class)
        );

        //TODO: Wenn StudentRegistration fertig ist, soll er zur StudentRegistrationView navigieren
        Button registerStudentButton = new Button("Zur Registrierung von Studenten", event -> {
            UI.getCurrent().navigate(RegistrationView.class);
                    notification.open();
            }
        );

        add(component, registerButton, registerStudentButton);

        this.setAlignItems( Alignment.CENTER );
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute( Globals.CURRENT_USER, userDTO );
    }


    private void navigateToMainPage() {
        // Navigation zur Startseite, hier auf die Teil-Komponente Show-Cars.
        // Die anzuzeigende Teil-Komponente kann man noch individualisieren, je nach Rolle,
        // die ein Benutzer besitzt
        UI.getCurrent().navigate(Globals.Pages.MAIN_VIEW);

    }
}
