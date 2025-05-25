package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.repository.StartupRepository;

import java.util.List;


@Route(value = "JobListing", layout = AppView.class)
@PageTitle("Jobbörse")
@CssImport("./styles/views/main/main-view.css")
public class JobListingView extends Div{

    private Button register = new Button("Zum StartUp");

    private final ManageStartupControl manageStartupControl;

    public JobListingView(ManageStartupControl manageStartupControl) {
        this.manageStartupControl = manageStartupControl;
        add(createTitle());
        add(setUpGrid());
        add(createButtonLayout());
    }

    private Component createTitle() {
        return new H3("Jobbörse");
    }

    private Grid setUpGrid() {
        List<StartupDTO> startups = manageStartupControl.findByHavingAnyStellenausschreibungJoin();
        Grid<StartupDTO> grid = new Grid<>(StartupDTO.class, false); //keine automatische Spalten generieren
        grid.setItems(startups);

        grid.addColumn(StartupDTO::getName).setHeader("Name");
        grid.addColumn(StartupDTO::getBeschreibung).setHeader("Beschreibung");
        grid.addColumn(StartupDTO::getGruendungsdatum).setHeader("Gründungsdatum");
        grid.addColumn(StartupDTO::getAnzahlMitarbeiter).setHeader("Mitarbeiterzahl");

        //Name der Branche
        grid.addColumn(startup -> manageStartupControl.getBrancheNameById(startup.getBranche()))
                .setHeader("Branche");

        //Anzahl der Stellenausschreibungen
        grid.addColumn(startup -> startup.getStellenausschreibungen() != null
                        ? startup.getStellenausschreibungen().size()
                        : 0)
                .setHeader("Stellenanzeigen");

        //Weiterleitung bei Click
        grid.asSingleSelect().addValueChangeListener(event -> {
            StartupDTO selected = event.getValue();
            if (selected != null) {
                getUI().ifPresent(ui ->
                        ui.navigate("startup/" + selected.getId())
                );
            }
        });

        return grid;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(register);
        return buttonLayout;
    }
}
