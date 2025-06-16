package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import org.hbrs.se2.project.startupx.control.EditProfilControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.BrancheDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.StudentDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.entities.Branche;
import org.hbrs.se2.project.startupx.repository.BrancheRepository;
import org.hbrs.se2.project.startupx.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

@Route(value = "CreateStartUp", layout = AppView.class)
@PageTitle("StartUp erstellen")
@CssImport("./styles/views/main/main-view.css")
public class CreateStartUpView extends Div {

    private final ManageStartupControl manageStartupControl;

    //Textfelder anhand der Datenbank
    private TextField name = new TextField("Name des StartUps");
    private ComboBox<String> brancheComboBox = new ComboBox<>("Branche");
    private Map<String, Long> brancheMap = new HashMap<>();
    private TextField beschreibung = new TextField("Beschreibung");
    private IntegerField anzahlMitarbeiter = new IntegerField("AnzahlMitarbeiter");
    private Button register = new Button("Register");
    private Button cancel = new Button("Abbrechen");

    private LocalDate heute = LocalDate.now();

    private StartupDTO startupDTO = new StartupDTO();

    private Binder<StartupDTO> binder = new Binder<>(StartupDTO.class);

    public CreateStartUpView(ManageStartupControl manageStartupControl, EditProfilControl editProfilControl) {
        this.manageStartupControl = manageStartupControl;
        configureFields();
        loadBranchen();
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        UserDTO userDTO = (UserDTO) VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER);

        StudentDTO studentFromUser = editProfilControl.getStudentDTO(userDTO.getStudent());


        Set<Long> studentenids = new LinkedHashSet<>();
        List<Long> mitarbeiter = new ArrayList<>();
        studentenids.add(studentFromUser.getId());
        startupDTO.setGruendungsdatum(heute);
        //Testfall
        startupDTO.setStudentenListe(studentenids);
        startupDTO.setMitarbeiterList(mitarbeiter);

        binder.setBean(startupDTO);
        configureBinder();

        register.addClickListener(e -> {
            if (binder.validate().isOk()) {
                try {
                    manageStartupControl.createStartup(startupDTO);
                    Notification.show("Nutzer registriert!");
                    clearForm();
                    UI.getCurrent().navigate((Globals.Pages.MAIN_VIEW));
                }
                catch (Exception ex) {
                Notification.show("Fehler:" + ex.getMessage());
                }
                }
             }
        );
        cancel.addClickListener(event -> clearForm());
    }

    //lädt Branchen aus der DB und verknüpft die mit dem Dropdown-Menü
    private void loadBranchen() {
        List<BrancheDTO> brancheDTOs = manageStartupControl.getBranches();
        for (BrancheDTO dto : brancheDTOs) {
            brancheMap.put(dto.getBezeichnung(), dto.getId());
        }
        brancheComboBox.setItems(brancheMap.keySet());
    }


    private void configureFields() {
        anzahlMitarbeiter.setMin(1);
        anzahlMitarbeiter.setMax(100);
        anzahlMitarbeiter.setStep(1);
    }

    private void configureBinder() {
        binder.setBean(startupDTO);

        binder.forField(name)
                .asRequired("Name des StartUps ist erforderlich")
                .withValidator(name -> name.matches("[a-zA-ZäöüÄÖÜß\\s-]+"), "Nur Buchstaben erlaubt")
                .bind(StartupDTO::getName, StartupDTO::setName);

        binder.forField(brancheComboBox)
                .asRequired("Wähle eine Branche für das StartUp")
                .withConverter(
                        name -> brancheMap.getOrDefault(name, null),
                        id -> brancheMap.entrySet().stream()
                                .filter(e -> e.getValue().equals(id))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(""),
                        "Bitte Branche auswählen"
                )
                .bind(StartupDTO::getBranche, StartupDTO::setBranche);

        binder.forField(beschreibung)
                .asRequired("Beschreibung darf nicht leer sein")
                .bind(StartupDTO::getBeschreibung, StartupDTO::setBeschreibung);

        binder.forField(anzahlMitarbeiter)
                .asRequired("Wähle eine Anzahl von Mitarbeitern zwischen 1 und 100")
                .bind(StartupDTO::getAnzahlMitarbeiter, StartupDTO::setAnzahlMitarbeiter);
    }

    private Component createTitle() {
        return new H3("StartUp erstellen");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name,brancheComboBox, beschreibung, anzahlMitarbeiter);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(register, cancel);
        return buttonLayout;
    }

    private void clearForm() {
        startupDTO = new StartupDTO();
        binder.setBean(startupDTO);
    }

}
