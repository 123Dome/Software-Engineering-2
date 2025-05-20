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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Route(value = "ShowAllStartUps", layout = AppView.class)
@PageTitle("Alle StartUps")
@CssImport("./styles/views/main/main-view.css")
public class ShowAllStartUpsView extends Div{

    private Button zumStartUp = new Button("Zum StartUp");

    private final ManageStartupControl manageStartupControl;

    public ShowAllStartUpsView(ManageStartupControl manageStartupControl) {
        this.manageStartupControl = manageStartupControl;
        add(createTitle());
        add(setUpGrid());
        add(createButtonLayout());
    }


    private Component createTitle() {
        return new H3("Liste von StartUps");
    }

    //Erstellen der Tabelle
    private Grid setUpGrid() {
        //Soll zukünftig alle StartUps listen
        List<StartupDTO> startups = manageStartupControl.findAll();
        Grid<StartupDTO> grid = new Grid<>(StartupDTO.class);
        grid.setItems(startups);
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
