package org.hbrs.se2.project.startupx.views.investor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.components.BewertungComponent;
import org.hbrs.se2.project.startupx.control.AuthenticationControl;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.UnterstuetzungsangebotDTO;
import org.hbrs.se2.project.startupx.util.BewertungenUtil;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;

@Route(value = Globals.Pages.INVESTOR_STARTUP_VIEW + "/:id", layout = AppView.class)
@PageTitle("StartUp Details")
public class InvestorStartUpView extends Div implements BeforeEnterObserver {

    private StartupDTO startupDTO;

    private final AuthenticationControl authenticationControl;
    private final BewertungControl bewertungControl;
    private final ManageStartupControl manageStartupControl;

    private final BewertungComponent bewertungComponent;

    public InvestorStartUpView(
            ManageStartupControl manageStartupControl,
            AuthenticationControl authenticationControl,
            BewertungControl bewertungControl
    ) {
        this.manageStartupControl = manageStartupControl;
        this.authenticationControl = authenticationControl;
        this.bewertungControl = bewertungControl;
        this.bewertungComponent = new BewertungComponent(bewertungControl);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String idString = beforeEnterEvent.getRouteParameters().get("id").orElse(null);
        if (idString != null) {
            try {
                Long id = Long.parseLong(idString);
                this.startupDTO = manageStartupControl.findByID(id);

                add(createUI());
            } catch (Exception e) {
                removeAll();
                add(new Paragraph("Fehler beim Laden des Startups."));
            }
        }
    }

    private VerticalLayout createUI() {
        VerticalLayout layout = new VerticalLayout();

        layout.add(createStartupDetails());
        layout.add(createSendUnterstuetzungsangebotButton());
        layout.add(bewertungComponent.createBewertungenCards(
                bewertungControl.getAlleBewertungZuStartup(startupDTO.getId()),
                authenticationControl.getCurrentUser(),
                () -> {}
        ));

        return layout;
    }

    private VerticalLayout createStartupDetails() {
        VerticalLayout layout = new VerticalLayout();

        Image image = new Image("images/startup_placeholder.png", "Startup-Logo");
        image.setWidth("120px");

        layout.add(image);
        layout.add(new H3(startupDTO.getName()));
        layout.add(new Paragraph("Beschreibung: " + startupDTO.getBeschreibung()));
        layout.add(new Paragraph("Branche: " + manageStartupControl.getBrancheNameById(startupDTO.getBranche())));
        layout.add(new Paragraph("Gründungsdatum: " + startupDTO.getGruendungsdatum()));
        layout.add(new Paragraph("Mitarbeiteranzahl: " + startupDTO.getAnzahlMitarbeiter()));
        double average = bewertungControl.getDurchschnittlicheBewertungZuStartup(startupDTO.getId());
        layout.add(new Paragraph("Ø Bewertung: " + BewertungenUtil.createAverageStars(average)));

        return layout;
    }

    private Button createSendUnterstuetzungsangebotButton() {
        Button button = new Button("Unterstützungsangebot senden");

        button.addClickListener(e -> {
            createUnterstuetzungsangebotDialog().open();
        });

        return button;
    }

    private Dialog createUnterstuetzungsangebotDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Unterstuetzungsangebot erstellen");

        NumberField amountField = new NumberField("Betrag (€)");
        amountField.setValue(null);
        dialog.add(amountField);

        Button save = new Button("Senden", click -> {
            Double amount = amountField.getValue();

            if(amount == null || amount <= 0) {
                amountField.setInvalid(true);
                amountField.setErrorMessage("Ungültiger Betrag");
                return;
            }

            Long investorId = authenticationControl.getCurrentUser().getInvestor();
            Long startupId = this.startupDTO.getId();
            UnterstuetzungsangebotDTO offerDTO = new UnterstuetzungsangebotDTO();
            offerDTO.setInvestor(investorId);
            offerDTO.setStartup(startupId);
            offerDTO.setBetrag(amount);
            try {
                manageStartupControl.createUnterstuetzungsangebot(offerDTO);
                dialog.close();
                Notification.show("Unterstützungsangebot gesendet: " + amount + " €");
            } catch (Exception e) {
                Notification.show("Fehler beim Speichern: " + e.getMessage());
            }
        });
        Button cancel = new Button("Abbrechen", click -> {
            dialog.close();
        });
        dialog.getFooter().add(save, cancel);

        return dialog;
    }
}
