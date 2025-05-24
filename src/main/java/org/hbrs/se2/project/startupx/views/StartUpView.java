package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;

import java.time.LocalDate;

@Route(value = "startup/:id", layout = AppView.class)
@PageTitle("StartUp Details")
public class StartUpView extends Div implements BeforeEnterObserver {

    private final ManageStartupControl manageStartupControl;
    private StartupDTO startup;
    private UserDTO currentUser;

    private final VerticalLayout readOnlyLayout = new VerticalLayout();
    private final VerticalLayout editLayout = new VerticalLayout();
    private final Button bearbeitenButton = new Button("Bearbeiten");
    private final Button speichernButton = new Button("Speichern");

    // Formularfelder
    private final TextField nameField = new TextField("Name");
    private final TextArea beschreibungField = new TextArea("Beschreibung");
    private final NumberField mitarbeiterField = new NumberField("Mitarbeiterzahl");
    private final DatePicker gruendungField = new DatePicker("Gründungsdatum");
    private final NumberField brancheField = new NumberField("Branche ID");

    public StartUpView(ManageStartupControl manageStartupControl) {
        this.manageStartupControl = manageStartupControl;
        addClassName("startup-details-view");

        bearbeitenButton.addClickListener(e -> switchToEditMode());
        speichernButton.addClickListener(e -> saveChanges());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idString = event.getRouteParameters().get("id").orElse(null);
        if (idString != null) {
            try {
                Long id = Long.parseLong(idString);
                this.startup = manageStartupControl.findByID(id);
                this.currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);

                // Nur Gründer sehen den Bearbeiten-Button
                boolean isGruender = startup.getStudentenListe() != null &&
                        currentUser != null &&
                        startup.getStudentenListe().contains(currentUser.getStudents());

                add(readOnlyLayout);
                readOnlyLayout.removeAll();
                readOnlyLayout.add(createReadOnlyLayout());

                if (isGruender) {
                    bearbeitenButton.setVisible(true);
                    add(bearbeitenButton);
                }

            } catch (Exception e) {
                add(new Paragraph("Fehler beim Laden des StartUps."));
            }
        }
    }

    private VerticalLayout createReadOnlyLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3(startup.getName()));
        layout.add(new Paragraph("Beschreibung: " + startup.getBeschreibung()));
        layout.add(new Paragraph("Branche-ID: " + startup.getBranche()));
        layout.add(new Paragraph("Gründungsdatum: " + startup.getGruendungsdatum()));
        layout.add(new Paragraph("Mitarbeiteranzahl: " + startup.getAnzahlMitarbeiter()));
        return layout;
    }

    private VerticalLayout createEditLayout() {
        nameField.setValue(startup.getName() != null ? startup.getName() : "");
        beschreibungField.setValue(startup.getBeschreibung() != null ? startup.getBeschreibung() : "");
        mitarbeiterField.setValue(startup.getAnzahlMitarbeiter() != null ? startup.getAnzahlMitarbeiter().doubleValue() : 0);
        gruendungField.setValue(startup.getGruendungsdatum());
        brancheField.setValue(startup.getBranche() != null ? startup.getBranche().doubleValue() : 0);

        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("StartUp bearbeiten"));
        layout.add(nameField, beschreibungField, mitarbeiterField, gruendungField, brancheField, speichernButton);
        return layout;
    }

    private void switchToEditMode() {
        remove(readOnlyLayout);
        remove(bearbeitenButton);
        editLayout.removeAll();
        editLayout.add(createEditLayout());
        add(editLayout);
    }

    private void saveChanges() {
        try {
            startup.setName(nameField.getValue());
            startup.setBeschreibung(beschreibungField.getValue());
            startup.setAnzahlMitarbeiter(mitarbeiterField.getValue().intValue());
            startup.setGruendungsdatum(gruendungField.getValue());
            startup.setBranche(brancheField.getValue().longValue());

            manageStartupControl.updateStartup(startup);
            Notification.show("Änderungen gespeichert.");

            // Zurück zur Leseansicht
            remove(editLayout);
            add(readOnlyLayout);
            readOnlyLayout.removeAll();
            readOnlyLayout.add(createReadOnlyLayout());
            add(bearbeitenButton);
        } catch (Exception e) {
            Notification.show("Fehler beim Speichern: " + e.getMessage());
        }
    }
}
