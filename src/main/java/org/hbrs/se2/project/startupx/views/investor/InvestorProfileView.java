package org.hbrs.se2.project.startupx.views.investor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.AuthenticationControl;
import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.dtos.InvestorDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;
import org.hbrs.se2.project.startupx.views.MainView;

@Route(value = Globals.Pages.INVESTOR_PROFILE_VIEW, layout = AppView.class)
@PageTitle("Mein Profil - Investor")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class InvestorProfileView extends Div implements BeforeEnterObserver {

    private UserDTO userDTO;
    private InvestorDTO investorDTO;

    private final EditProfilControl editProfilControl;
    private final AuthenticationControl authenticationControl;

    public InvestorProfileView(EditProfilControl editProfilControl, AuthenticationControl authenticationControl) {
        this.editProfilControl = editProfilControl;
        this.authenticationControl = authenticationControl;
        addClassName("profile-view");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        userDTO = authenticationControl.getCurrentUser();
        this.investorDTO = editProfilControl.getInvestorDTObyUserId(userDTO.getId());

        if (this.investorDTO == null) {
            event.rerouteTo(MainView.class);
        } else {
            this.removeAll();
            setUpUI();
        }
    }

    private void setUpUI() {
        this.add(createProfileLayout());
    }

    //ImageLayout und Nutzerdaten-Layout werden zusammengefügt
    private Component createProfileLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(createProfileImage(), createUserInfoLayout());

        return layout;
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
}
