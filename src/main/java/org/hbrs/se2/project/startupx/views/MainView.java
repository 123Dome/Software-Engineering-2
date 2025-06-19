package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.startupx.control.AuthenticationControl;
import org.hbrs.se2.project.startupx.control.exception.LoginException;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.investor.InvestorRegistrationView;
import org.hbrs.se2.project.startupx.views.student.StudentRegistrationView;


@Route(value = Globals.Pages.MAIN_VIEW)
@RouteAlias(value = Globals.Pages.LOGIN_VIEW)
@PageTitle("Login")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationControl authenticationControl;

    public MainView(AuthenticationControl authenticationControl) {
        this.authenticationControl = authenticationControl;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        UserDTO user = authenticationControl.getCurrentUser();

        // Prüfen ob angemeldet => Prüfen ob Student/Investor
        if(user == null){ // Niemand angemeldet => LoginForm
            if (this.getChildren().findFirst().isEmpty()) {
                this.add(setUpLoginPage());
            }
        } else if(user.getStudent() != null){ // Student angemeldet => Student Dashboard
            event.rerouteTo(DashboardView.class);
        } else if(user.getInvestor() != null){ // Investor angemeldet => Investor Dashboard TODO
            event.rerouteTo(DashboardView.class);
        }
    }

    // Gibt LoginForm und die Buttons zum Registrieren zurück, falls Benutzer nicht angemeldet
    private Component setUpLoginPage(){
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);

        LoginForm loginForm = new LoginForm();
        Button registerStudentButton = new Button("Student registrieren");
        Button registerInvestorButton = new Button("Investor registrieren");

        loginForm.addLoginListener(event -> {
            boolean isAuthenticated = false;
            try {
                isAuthenticated = authenticationControl.authenticate(event.getUsername(), event.getPassword());
            } catch (LoginException e) {
                new Dialog(new Text(e.getMessage())).open();
            }
            if(isAuthenticated){
                UI.getCurrent().navigate(DashboardView.class);
            }
        });

        registerStudentButton.addClickListener(event -> {
           UI.getCurrent().navigate(StudentRegistrationView.class);
        });

        registerInvestorButton.addClickListener(event -> {
            UI.getCurrent().navigate(InvestorRegistrationView.class);
        });

        layout.add(loginForm, registerStudentButton, registerInvestorButton);

        return layout;
    }

}
