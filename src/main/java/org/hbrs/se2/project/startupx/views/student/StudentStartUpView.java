package org.hbrs.se2.project.startupx.views.student;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.startupx.components.BewertungComponent;
import org.hbrs.se2.project.startupx.control.*;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.BewertungenUtil;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.AppView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    // Ausgelagerte Components für die Bewertungen
    private final BewertungComponent bewertungComponent;


    public StudentStartUpView(ManageStartupControl manageStartupControl, StellenausschreibungControl stellenausschreibungControl, StudiengangControl studiengangControl, SkillControl skillControl, BewertungControl bewertungControl, AuthenticationControl authenticationControl) {
        this.studiengangControl = studiengangControl;
        this.stellenausschreibungControl = stellenausschreibungControl;
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        this.skillControl = skillControl;
        this.authenticationControl = authenticationControl;
        this.bewertungComponent = new BewertungComponent(bewertungControl);
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
        double average = bewertungControl.getDurchschnittlicheBewertungZuStartup(startup.getId());
        layout.add(new Paragraph("Ø Bewertung: " + BewertungenUtil.createAverageStars(average)));        // Nur Studenten die Möglichkeit zum bewerben geben
        if(currentUser.getStudent() != null) {
            layout.add(bewertungComponent.createBewertenButton(
                    startup,
                    currentUser,
                    () -> {
                        readOnlyLayout.removeAll();
                        readOnlyLayout.add(createReadOnlyLayout());
                    })
            );
        }
        layout.add(bewertungComponent.createBewertungenCards(
                bewertungControl.getAlleBewertungZuStartup(startup.getId()),
                currentUser,
                () -> {
                    readOnlyLayout.removeAll();
                    readOnlyLayout.add(createReadOnlyLayout());
                })
        );

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
                speichernButton
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

}
