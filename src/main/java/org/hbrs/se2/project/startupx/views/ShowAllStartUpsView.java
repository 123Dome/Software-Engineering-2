package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.AuthenticationControl;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.util.Globals;

import java.util.List;


@Route(value = "ShowAllStartUps", layout = AppView.class)
@PageTitle("Alle StartUps")
@CssImport("./styles/views/main/main-view.css")
public class ShowAllStartUpsView extends Div{

    private Button zumStartUp = new Button("Zum StartUp");
    private TextField searchField = new TextField();

    private final ManageStartupControl manageStartupControl;
    private final BewertungControl bewertungControl;
    private final AuthenticationControl authenticationControl;


    public ShowAllStartUpsView(ManageStartupControl manageStartupControl, BewertungControl bewertungControl, AuthenticationControl authenticationControl) {
        this.manageStartupControl = manageStartupControl;
        this.bewertungControl = bewertungControl;
        this.authenticationControl = authenticationControl;
        add(createTitle());
        add(setUpSearchField());
        add(setUpGrid());
        //add(createButtonLayout());
    }

    private Component createTitle() {
        return new H3("Liste von StartUps");
    }

    private TextField setUpSearchField() {
        searchField.setWidth("100%");
        searchField.setPlaceholder("Suche");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        return searchField;
    }

    //Erstellen der Tabelle
    private Grid setUpGrid() {
        //Soll zukünftig alle StartUps listen
        Grid<StartupDTO> grid = new Grid<>(StartupDTO.class, false); //keine automatsiche Erstellung von Spalten

        grid.addColumn(StartupDTO::getName).setHeader("Name");
        grid.addColumn(StartupDTO::getBeschreibung).setHeader("Beschreibung");
        grid.addColumn(StartupDTO::getGruendungsdatum).setHeader("Gründungsdatum");
        grid.addColumn(StartupDTO::getAnzahlMitarbeiter).setHeader("Mitarbeiterzahl");
        grid.addColumn(dto -> manageStartupControl.getBrancheNameById(dto.getBranche()))
                .setHeader("Branche");
        grid.addColumn(dto -> {
            double average = bewertungControl.getDurchschnittlicheBewertungZuStartup(dto.getId());
            if (average > 0.0) {
                return String.format("%.1f ⭐", average);
            } else {
                return ""; // Kein Text, wenn keine Bewertung
            }
        }).setHeader("Ø Bewertung");

        grid.asSingleSelect().addValueChangeListener(event -> {
            StartupDTO selected = event.getValue();
            if (selected != null) {
                String route;
                var user = authenticationControl.getCurrentUser();
                if (user.getStudent() != null) {
                    route = Globals.Pages.STUDENT_STARTUP_VIEW + "/" + selected.getId();
                } else if (user.getInvestor() != null) {
                    route = Globals.Pages.INVESTOR_STARTUP_VIEW + "/" + selected.getId();
                } else {
                    route = Globals.Pages.DASHBOARD_VIEW;
                }

                getUI().ifPresent(ui -> ui.navigate(route));
            }
        });


        List<StartupDTO> startups = manageStartupControl.findAll();
        GridListDataView<StartupDTO> dataView = grid.setItems(startups);

        searchField.addValueChangeListener(e -> grid.getDataProvider().refreshAll());

        dataView.addFilter(startupDTO -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesStartupName = startupDTO.getName().contains(searchTerm);
            boolean matchesBeschreibung = startupDTO.getBeschreibung().contains(searchTerm);
            boolean matchesGruendungsdatum = startupDTO.getGruendungsdatum().toString().contains(searchTerm);
            boolean matchesAnzahlMitarbeiter = startupDTO.getAnzahlMitarbeiter().toString().equals(searchTerm);
            boolean matchesBranche = manageStartupControl.getBrancheNameById(startupDTO.getBranche()).contains(searchTerm);
            boolean matchesBewertung = Double.toString(bewertungControl.getDurchschnittlicheBewertungZuStartup(startupDTO.getId())).contains(searchTerm);

            return matchesStartupName
                    || matchesBeschreibung
                    || matchesGruendungsdatum
                    || matchesAnzahlMitarbeiter
                    || matchesBranche
                    || matchesBewertung;
        });

        return grid;
    }


    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        zumStartUp.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(zumStartUp);
        return buttonLayout;
    }
}
