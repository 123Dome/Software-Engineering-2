package org.hbrs.se2.project.startupx.views.student;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.startupx.control.*;
import org.hbrs.se2.project.startupx.dtos.*;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Route(value = Globals.Pages.STUDENT_STARTUP_VIEW + "/:id", layout = AppView.class)
@PageTitle("StartUp Details")
public class StudentStartUpView extends Div implements BeforeEnterObserver {

    private final ManageStartupControl manageStartupControl;
    private final StellenausschreibungControl stellenausschreibungControl;
    private final StudiengangControl studiengangControl;
    private final SkillControl skillControl;
    private final BewertungControl bewertungControl;
    private final AuthenticationControl authenticationControl;

    private boolean isGruender = false;

    private StartupDTO startup;
    private UserDTO currentUser;

    private final Binder<StartupDTO> binder = new Binder<>(StartupDTO.class);
    private final Binder<StellenausschreibungDTO> stellenausschreibungDTOBinder = new Binder<>(StellenausschreibungDTO.class);

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

    // Stellenausschreibungsfelder
    //Textfelder
    private final TextField titel = new TextField("Titel");
    private final TextArea beschreibung = new TextArea("Beschreibung");
    private final MultiSelectComboBox<SkillDTO> skills = new MultiSelectComboBox<>("Skills");
    private List<SkillDTO> allSkills = new ArrayList<>();

    //Button
    private final Button erstelleButton = new Button("Stellenausschreibung erstellen");
    private final Button abbrechenButton = new Button("Stellenausschreibung abbrechen");

    // Bild
    private final Image startupImage = new Image("images/startup_placeholder.png", "Startup-Logo");


    public StudentStartUpView(ManageStartupControl manageStartupControl, StellenausschreibungControl stellenausschreibungControl, StudiengangControl studiengangControl, SkillControl skillControl, BewertungControl bewertungControl, AuthenticationControl authenticationControl) {
        this.studiengangControl = studiengangControl;
        this.stellenausschreibungControl = stellenausschreibungControl;
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        this.skillControl = skillControl;
        this.authenticationControl = authenticationControl;
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
                this.currentUser = authenticationControl.getCurrentUser();

                // Prüfe, ob der eingeloggte User zur Gründerliste des Startups gehört
                this.isGruender = startup.getStudentenListe() != null &&
                        currentUser != null &&
                        startup.getStudentenListe().contains(currentUser.getStudent());

                //Read-Only Ansicht
                readOnlyLayout.removeAll();
                readOnlyLayout.add(createReadOnlyLayout());
                add(readOnlyLayout);

                // Falls der eingeloggte User ein Gründer ist, darf er bearbeiten
                if (isGruender) {
                    Button openStellenausschreibungButton = new Button("Neue Stellenausschreibung", e -> createStellenausschreibung());
                    bearbeitenButton.setVisible(true);
                    add(bearbeitenButton, openStellenausschreibungButton);
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
        layout.add(setUpDurchschnittBewertungenInSternen());
        // Nur Studenten die Möglichkeit zum bewerben geben
        if(currentUser.getStudent() != null) {
            layout.add(setUpBewertenButton());
        }
        layout.add(setUpBewertungenCards());

        List<StellenausschreibungDTO> stellenausschreibungDTOS = stellenausschreibungControl.getAllOpenStellenausschreibungByStartup(startup);
        layout.add(new H3("Stellenanzeigen"));

        for(StellenausschreibungDTO dto : stellenausschreibungDTOS) {
            H4 title = new H4(dto.getTitel());


            RouterLink link = new RouterLink();
            link.setRoute(ApplyForJobView.class, new RouteParameters("id", dto.getId().toString()));
            link.add(title);

            Span skillsSpan = new Span();

            String skillNames = "";
            if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
                skillNames = dto.getSkills().stream()
                        .map(skillId -> {
                            SkillDTO skillDto = skillControl.getSkillById(skillId);
                            return skillDto.getSkillName();
                        })
                        .collect(Collectors.joining(", "));
            }

            if (!skillNames.isEmpty()) {
                skillsSpan = new Span("Benötigte Skills: " + skillNames);
            }

            Span anzahlBewerbungen = new Span();
            if (isGruender) {
                anzahlBewerbungen = new Span("Anzahl Bewerbungen: " + String.valueOf(stellenausschreibungControl.findById(dto.getId()).getBewerbungen().size()));
            }


            VerticalLayout lineLayout = new VerticalLayout(link, skillsSpan, anzahlBewerbungen);
            lineLayout.setSpacing(true);

            layout.add(lineLayout);

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
                setUpDurchschnittBewertungenInSternen(),
                setUpBewertungenCards()
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

    private void createStellenausschreibung() {
        StellenausschreibungDTO newStellenausschreibung = new StellenausschreibungDTO();
        newStellenausschreibung.setStartup(startup.getId());
        stellenausschreibungDTOBinder.setBean(newStellenausschreibung);

        Dialog dialog = new Dialog("Neue Stellenausschreibung erstellen");

        FormLayout formlayout = new FormLayout();

        loadSkills();

        stellenausschreibungDTOBinder.forField(skills)
                .asRequired("Wähle mindestens 1 Skill aus")
                .withConverter(
                        (Set<SkillDTO> selectedSkills) -> {
                            if (selectedSkills == null || selectedSkills.isEmpty()) {
                                return Set.of();
                            }
                            return selectedSkills.stream()
                                    .map(SkillDTO::getId)
                                    .collect(Collectors.toSet());
                        },
                        (Set<Long> skillIds) -> {
                            if (skillIds == null || skillIds.isEmpty()) {
                                return Set.of();
                            }
                            return allSkills.stream()
                                    .filter(skill -> skillIds.contains(skill.getId()))
                                    .collect(Collectors.toSet());
                        }
                )
                .bind(StellenausschreibungDTO::getSkills, StellenausschreibungDTO::setSkills);

        stellenausschreibungDTOBinder.forField(titel).bind(StellenausschreibungDTO::getTitel, StellenausschreibungDTO::setTitel);
        stellenausschreibungDTOBinder.forField(beschreibung).bind(StellenausschreibungDTO::getBeschreibung, StellenausschreibungDTO::setBeschreibung);

        erstelleButton.addClickListener(e -> {
            stellenausschreibungControl.createStellenausschreibung(stellenausschreibungDTOBinder.getBean());
            Notification.show("Stellenausschreibung erstellt.");
            dialog.close();
        });

        abbrechenButton.addClickListener(e -> {
            Notification.show("Stellenausschreibung nicht erstellt.");
            dialog.close();
        });

        formlayout.add(titel, beschreibung, skills);
        dialog.add(formlayout, erstelleButton, abbrechenButton);
        dialog.open();
    }

    private void loadSkills() {
        allSkills = studiengangControl.findAllSkills();

        skills.setItemLabelGenerator(SkillDTO::getSkillName);
        skills.setItems(allSkills);
    }

    private Component setUpBewertenButton() {
        Button bewertenButton = new Button("Bewerten");
        Dialog dialog = new Dialog();
        TextField kommentarField = new TextField("Kommentar (optional)");
        HorizontalLayout sterneLayout = new HorizontalLayout();
        VerticalLayout dialogLayout = new VerticalLayout();

        dialog.setHeaderTitle("Bewertung abgeben");

        Span[] sterne = new Span[5];
        AtomicInteger selectedRating = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            Span stern = new Span("☆");
            stern.getStyle().set("font-size", "24px");
            stern.getStyle().set("cursor", "pointer");
            final int index = i;

            stern.addClickListener(e -> {
                selectedRating.set(index + 1);
               for(int j = 0; j < 5; j++) {
                   sterne[j].setText(j <= index ? "★" : "☆");
               }
            });

            sterne[i] = stern;
            sterneLayout.add(stern);
        }


        Button dialogSchließenButton = new Button("Schließen", e -> dialog.close());
        Button bewertungSpeichernButton = new Button("Speichern", e -> {
            int bewertung = selectedRating.get();
            String kommentar = kommentarField.getValue();

            if (bewertung == 0) {
                Notification.show("Bitte Sterne auswählen!");
                return;
            }

            BewertungDTO neueBewertungDTO = new BewertungDTO();
            neueBewertungDTO.setBewertung(bewertung);
            neueBewertungDTO.setKommentar(kommentar);
            neueBewertungDTO.setErstellungsdatum(LocalDate.now());
            neueBewertungDTO.setStartup(startup.getId());
            neueBewertungDTO.setUser(currentUser.getId());

            bewertungControl.createBewertung(neueBewertungDTO);
            Notification.show("Bewertung gespeichert.");

            dialog.close();
            // Ansicht aktualisieren:
            readOnlyLayout.removeAll();
            readOnlyLayout.add(createReadOnlyLayout());
        });

        dialogLayout.add(sterneLayout, kommentarField);

        dialog.add(dialogLayout);
        dialog.getFooter().add(bewertungSpeichernButton, dialogSchließenButton);


        bewertenButton.addClickListener(e -> {
            dialog.open();
        });

        return bewertenButton;
    }

    private Component setUpBewertungenCards(){
        List<BewertungDTO> bewertungen = bewertungControl.getAlleBewertungZuStartup(startup.getId());
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("bewertungen-liste");

        layout.add(new H3("Bewertungen"));

        if(bewertungen.isEmpty()){
            layout.add(new Paragraph("Keine Bewertungen vorhanden"));
            return layout;
        }

        for(BewertungDTO bewertung : bewertungen){
            Div card = new Div();
            card.addClassName("bewertungen-karte");
            card.setWidth("400px");
            card.getStyle().set("border", "1px solid #ccc");
            card.getStyle().set("border-radius", "8px");
            card.getStyle().set("padding", "16px");
            card.getStyle().set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)");

            // Überschrift (Nutzer)
            String nutzername = bewertungControl.getUserNameById(bewertung.getUser());
            H4 userHeader = new H4(nutzername);
            card.add(userHeader);

            // Sterne
            HorizontalLayout sterneLayout = new HorizontalLayout();
            for(int i = 0; i < 5; i++){
                Span stern = new Span(i <= bewertung.getBewertung() ? "★" : "☆");
                stern.getStyle().set("font-size", "20px");
                sterneLayout.add(stern);
            }
            card.add(sterneLayout);

            // Kommentar (optional)
            if(!bewertung.getKommentar().isEmpty()) {
                Paragraph kommentar = new Paragraph(bewertung.getKommentar());
                kommentar.getStyle().set("font-style", "italic");
                kommentar.getStyle().set("margin-top", "8px");
                card.add(kommentar);
            }

            // Datum
            Paragraph datum = new Paragraph("Datum: " + bewertung.getErstellungsdatum());
            datum.getStyle().set("font-size", "0.9em");
            datum.getStyle().set("color", "#666");
            card.add(datum);

            // Löschen-Button, wenn eigene Bewertung
            if (currentUser != null && currentUser.getId().equals(bewertung.getUser())){
                Button bewertungLöschenButton = new Button("Löschen");
                bewertungLöschenButton.getStyle().set("margin-top", "8px");

                bewertungLöschenButton.addClickListener(e -> {
                    // Bestätigungsdialog
                    Dialog löschenBestätigenDialog = new Dialog();
                    löschenBestätigenDialog.add(new Paragraph("Wollen Sie die Bewertung wirklich löschen?"));
                    Button löschenBestätigenButton = new Button("Ja", event -> {
                        try {
                            bewertungControl.deleteBewertung(bewertung.getId());
                            Notification.show("Bewertung gelöscht.");
                            readOnlyLayout.removeAll();
                            readOnlyLayout.add(createReadOnlyLayout());
                        } catch (Exception ex) {
                            Notification.show(ex.getMessage());
                        }
                        löschenBestätigenDialog.close();
                    });
                    Button löschenAbbrechenButton = new Button("Abbrechen", event -> löschenBestätigenDialog.close());
                    löschenBestätigenDialog.getFooter().add(löschenBestätigenButton, löschenAbbrechenButton);
                    löschenBestätigenDialog.open();
                });
                card.add(bewertungLöschenButton);
            }

            layout.add(card);
        }

        return layout;
    }

    private Component setUpDurchschnittBewertungenInSternen() {
        List<BewertungDTO> bewertungen = bewertungControl.getAlleBewertungZuStartup(startup.getId());
        if(bewertungen.isEmpty()){
            return new Paragraph("Keine Bewertungen vorhanden");
        }

        double average = bewertungen.stream()
                .mapToInt(BewertungDTO::getBewertung)
                .average()
                .orElse(0);

        HorizontalLayout sterne = new HorizontalLayout();
        int volleSterne = (int) average;
        boolean halberStern = (average - volleSterne) >= 0.5;
        int leereSterne = 5 - volleSterne - (halberStern ? 1 : 0);

        // Volle Sterne
        for (int i = 0; i < volleSterne; i++) {
            sterne.add(new Span("★"));
        }

        if(halberStern) {
            sterne.add(new Span("⯨"));
        }

        // Leere Sterne
        for (int i = 0; i < leereSterne; i++) {
            sterne.add(new Span("☆"));
        }

        sterne.add(new Span(String.format("(%.1f Sterne)", average)));

        return sterne;
    }
}
