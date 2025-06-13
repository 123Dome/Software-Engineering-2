package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;

import java.util.List;

@Route(value = "startup/:id", layout = AppView.class)
@PageTitle("StartUp Details")
public class StartUpView extends Div implements BeforeEnterObserver {

    private final ManageStartupControl manageStartupControl;
    private final BewertungControl bewertungControl;

    private StartupDTO startup;
    private UserDTO currentUser;

    private final Binder<StartupDTO> binder = new Binder<>(StartupDTO.class);

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

    // Bild
    private final Image startupImage = new Image("images/startup_placeholder.png", "Startup-Logo");

    public StartUpView(ManageStartupControl manageStartupControl, BewertungControl bewertungControl) {
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        addClassName("startup-details-view");

        bearbeitenButton.addClickListener(e -> switchToEditMode());
        speichernButton.addClickListener(e -> saveChanges());

        binder.forField(nameField)
                .asRequired("Name darf nicht leer sein")
                .withValidator(name -> name.matches("[a-zA-ZäöüÄÖÜß\\s-]+"), "Nur Buchstaben erlaubt")
                .bind(StartupDTO::getName, StartupDTO::setName);

        binder.forField(beschreibungField)
                .asRequired("Beschreibung darf nicht leer sein")
                .bind(StartupDTO::getBeschreibung, StartupDTO::setBeschreibung);

        binder.forField(mitarbeiterField)
                .asRequired("Mitarbeiterzahl erforderlich")
                .bind(
                        dto -> dto.getAnzahlMitarbeiter() != null ? dto.getAnzahlMitarbeiter().doubleValue() : null,
                        (dto, value) -> dto.setAnzahlMitarbeiter(value != null ? value.intValue() : null)
                );

        binder.forField(gruendungField)
                .asRequired("Gründungsdatum erforderlich")
                .bind(StartupDTO::getGruendungsdatum, StartupDTO::setGruendungsdatum);

        binder.forField(brancheField)
                .asRequired("Branche-ID erforderlich")
                .bind(
                        dto -> dto.getBranche() != null ? dto.getBranche().doubleValue() : null,
                        (dto, value) -> dto.setBranche(value != null ? value.longValue() : null)
                );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        startupImage.setWidth("120px");
        startupImage.setHeight("auto");

        // Hole die ID des Startups aus der URL (z.B. ./startup/3 → id = 3)
        String idString = event.getRouteParameters().get("id").orElse(null);
        if (idString != null) {
            try {
                Long id = Long.parseLong(idString);
                this.startup = manageStartupControl.findByID(id);
                this.currentUser = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);

                // Prüfe, ob der eingeloggte User zur Gründerliste des Startups gehört
                boolean isGruender = startup.getStudentenListe() != null &&
                        currentUser != null &&
                        startup.getStudentenListe().contains(currentUser.getStudent());

                //Read-Only Ansicht
                readOnlyLayout.removeAll();
                readOnlyLayout.add(createReadOnlyLayout());
                add(readOnlyLayout);

                // Falls der eingeloggte User ein Gründer ist, darf er bearbeiten
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
        layout.add(startupImage);
        layout.add(new H3(startup.getName()));
        layout.add(new Paragraph("Beschreibung: " + startup.getBeschreibung()));
        layout.add(new Paragraph("Branche: " + manageStartupControl.getBrancheNameById(startup.getBranche())));
        layout.add(new Paragraph("Gründungsdatum: " + startup.getGruendungsdatum()));
        layout.add(new Paragraph("Mitarbeiteranzahl: " + startup.getAnzahlMitarbeiter()));
        layout.add(setUpBewerten());
        layout.add(setUpBewertungenGrid());

        List<Long> stellenanzeigen = startup.getStellenausschreibungen();
        if (stellenanzeigen != null && !stellenanzeigen.isEmpty()) {
            layout.add(new H3("Stellenanzeigen:"));
            for (Long id : stellenanzeigen) {
                layout.add(new Span("Anzeige ID: " + id));
            }
        }

        return layout;
    }

    private VerticalLayout createEditLayout() {
        //generisches Bild
        Image editImage = new Image("images/startup_placeholder.png", "StartUp-Logo");
        editImage.setWidth("120px");

        //Branche als String
        String branchenName = manageStartupControl.getBrancheNameById(startup.getBranche());
        Paragraph branchenInfo = new Paragraph("Aktuelle Branche: " + branchenName);

        // Stellenanzeigen
        VerticalLayout stellenanzeigenLayout = new VerticalLayout();
        stellenanzeigenLayout.add(new Paragraph("Stellenanzeigen:"));
        if (startup.getStellenausschreibungen() != null && !startup.getStellenausschreibungen().isEmpty()) {
            for (Long id : startup.getStellenausschreibungen()) {
                stellenanzeigenLayout.add(new Span("- #" + id));
            }
        } else {
            stellenanzeigenLayout.add(new Span("Keine Stellenanzeigen vorhanden."));
        }

        // Layout
        VerticalLayout layout = new VerticalLayout(
                new H3("StartUp bearbeiten"),
                editImage,
                branchenInfo,
                nameField, beschreibungField, mitarbeiterField, gruendungField, brancheField,
                stellenanzeigenLayout,
                speichernButton,
                setUpBewertungenGrid()
        );

        //Binder setzen
        binder.setBean(startup);

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
        if (binder.validate().isOk()) {
            try {
                manageStartupControl.updateStartup(startup);
                Notification.show("Änderungen gespeichert.");

                //Wechsel zurück zu Read only
                remove(editLayout);
                readOnlyLayout.removeAll();
                readOnlyLayout.add(createReadOnlyLayout());
                add(readOnlyLayout, bearbeitenButton);

            } catch (Exception e) {
                Notification.show("Fehler beim Speichern: " + e.getMessage());
            }
        } else {
            Notification.show("Bitte überprüfe deine Eingaben.");
        }
    }

    private Component setUpBewerten() {
        Button bewertenButton = new Button("Bewerten");

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Bewertung abgeben");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(new Paragraph("Hier kannst du deine Bewertung abgeben.")); // Placeholder

        Button dialogSchließenButton = new Button("Schließen", e -> dialog.close());
        dialog.getFooter().add(dialogSchließenButton);

        dialog.add(dialogLayout);

        bewertenButton.addClickListener(e -> {
            dialog.open();
        });

        return bewertenButton;
    }

    private Component setUpBewertungenGrid(){
        List<BewertungDTO> bewertungen = bewertungControl.getAlleBewertungZuStartup(startup.getId());

        if(bewertungen.isEmpty()){
            return new Paragraph("Keine Bewertungen vorhanden");
        }

        Grid<BewertungDTO> grid = new Grid<>(BewertungDTO.class);
        grid.setItems(bewertungen);

        grid.setColumns("user", "bewertung", "kommentar", "erstellungsdatum");
        grid.getColumnByKey("user").setHeader("User-ID");
        grid.getColumnByKey("bewertung").setHeader("Sterne");
        grid.getColumnByKey("kommentar").setHeader("Kommentar");
        grid.getColumnByKey("erstellungsdatum").setHeader("Datum");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new H3("Bewertungen"), grid);

        return layout;
    }
}
