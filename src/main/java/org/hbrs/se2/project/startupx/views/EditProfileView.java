package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
@Route(value = "EditProfile", layout = AppView.class)
@PageTitle("Profil bearbeiten")
@CssImport("./styles/views/entercar/enter-car-view.css")
public class EditProfileView extends Div{

    private TextField nutzername = new TextField("Benutzername");
    private TextField email = new TextField("Email");
    private TextField vorname = new TextField("Vorname");
    private TextField nachname = new TextField("Nachname");

    private DatePicker geburtsdatum = new DatePicker("Geburtsdatum");

    //TODO: Daten des Users müssen abgerufen werden und editierbar sein, danach zurückgeschrieben werden
    public EditProfileView() {
        addClassName("enter-car-view");
        add(createTitle());
    }

    private Component createTitle() {
        return new H3("Profil bearbeiten");
    }

}
