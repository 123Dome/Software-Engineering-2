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


@Route(value = "ShowAllStartUps", layout = AppView.class)
@PageTitle("Alle StartUps")
@CssImport("./styles/views/main/main-view.css")
public class ShowAllStartUpsView extends Div{

    private Button register = new Button("Zum StartUp");

    private final StartupRepository startupRepository;

    //Zeigt alle bisher erstellten StartUps
    public ShowAllStartUpsView(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
        add(createTitle());
        add(setUpGrid());
        add(createButtonLayout());
    }


    private Component createTitle() {
        return new H3("StartUpListe");
    }

    //Erstellt Tabelle
    private Grid setUpGrid() {
        List<Startup> startups = startupRepository.findAll();
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
