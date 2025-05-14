package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.LoginControl;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.User;
import org.hbrs.se2.project.startupx.repository.UserRepository;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "EditProfile", layout = AppView.class)
@PageTitle("Profil bearbeiten")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class EditProfileView extends Div{

    private UserRepository userRepository;



    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");

    //TODO: Daten des Users müssen abgerufen werden und editierbar sein, danach zurückgeschrieben werden
    public EditProfileView(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
        addClassName("enter-car-view");

        UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
        User user = userRepository.findUserByNutzername(userDTO.getNutzername());

        add(createTitle());
        setTextFieldWithUserData(user);
        add(createFormLayout());
        add(createSaveButton(user));
    }

    private Component createTitle() {
        return new H3("Profil bearbeiten");
    }

    private void setTextFieldWithUserData(User user){
        nutzername.setValue(user.getNutzername());
        email.setValue(user.getEmail());
        vorname.setValue(user.getVorname());
        nachname.setValue(user.getNachname());
        geburtsdatum.setValue(user.getGeburtsdatum());
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nutzername, email, vorname, nachname, geburtsdatum);
        return formLayout;
    }

    private Button createSaveButton (User user) {
        Button saveButton = new Button("Änderungen speichern", e -> {
            user.setNutzername(nutzername.getValue());
            user.setEmail(email.getValue());
            user.setVorname(vorname.getValue());
            user.setNachname(nachname.getValue());
            user.setGeburtsdatum(geburtsdatum.getValue());

            userRepository.save(user);
            Notification.show("Profil aktualisiert");

        });
        return saveButton;
    }



}
