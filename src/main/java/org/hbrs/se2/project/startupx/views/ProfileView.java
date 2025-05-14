package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;

@Route(value = "userProfile", layout = AppView.class)
@PageTitle("Mein Profil")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class ProfileView extends Div {

    private UserDTO userDTO;

    public ProfileView() {
        addClassName("profile-view");
        loadCurrentUser();
        add(createProfileLayout());
    }

    //Aktuelle Nutzerdaten laden
    private void loadCurrentUser() {
        userDTO = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);
    }

    //Generisches Profilbild wird reingeladen, Größe angepasst
    private Image createProfileImage() {
        Image profileImage = new Image("images/genericUser.png", "Profilbild");
        profileImage.setWidth("120px");
        profileImage.setHeight("120px");
        profileImage.getStyle().set("border-radius", "50%");
        return profileImage;
    }

    //Nutzerdaten-Layout wird erstellt, Nutzerdaten werden dargestellt
    private VerticalLayout createUserInfoLayout() {
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);

        infoLayout.add(
                new H3(userDTO.getVorname() + " " +  userDTO.getNachname()),
                new Paragraph("Nutzername: " + userDTO.getNutzername()),
                new Paragraph("Geburtsdatum: " + userDTO.getGeburtsdatum()),
                new Paragraph("Email: " + userDTO.getEmail())
        );

        return infoLayout;
    }

    //ImageLayout und Nutzerdaten-Layout werden zusammengefügt
    private Component createProfileLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(createProfileImage(), createUserInfoLayout());

        return layout;
    }

}
