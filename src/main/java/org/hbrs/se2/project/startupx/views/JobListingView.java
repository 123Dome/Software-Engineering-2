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
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.repository.StartupRepository;

import java.util.List;


@Route(value = "JobListing", layout = AppView.class)
@PageTitle("Jobbörse")
@CssImport("./styles/views/main/main-view.css")
public class JobListingView extends Div{

    private Button register = new Button("Zum StartUp");

    private final StartupRepository startupRepository;


    public JobListingView(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
        add(createTitle());
        add(setUpGrid());
        add(createButtonLayout());
    }


    private Component createTitle() {
        return new H3("Jobbörse");
    }

    //Erstellen der Tabelle
    private Grid setUpGrid() {
        //Soll zukünftig alle StartUps listen, die auch eine Stelle ausgeschrieben haben
        List<Startup> startups = startupRepository.findByNameContaining("a");
        Grid<Startup> grid = new Grid<>(Startup.class);
        grid.setItems(startups);
        return grid;
    }


    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(register);
        buttonLayout.add(register);
        return buttonLayout;
    }
}
