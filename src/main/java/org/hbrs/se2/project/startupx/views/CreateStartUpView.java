package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "CreateStartUp", layout = AppView.class)
@PageTitle("StartUp erstellen")
@CssImport("./styles/views/main/main-view.css")
public class CreateStartUpView extends Div {

    //Textfelder anhand der Datenbank, muss evtl. noch genauer angepasst werden
    private TextField name = new TextField("Name des StartUps");
    private TextField branche = new TextField("Branche");
    private TextField beschreibung = new TextField("Beschreibung");
    private TextField anzahlMitarbeiter = new TextField("AnzahlMitarbeiter");

    private Button register = new Button("Register");


    public CreateStartUpView() {
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
    }



    private Component createTitle() {
        return new H3("StartUp erstellen");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name,branche, beschreibung, anzahlMitarbeiter);
        return formLayout;
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
