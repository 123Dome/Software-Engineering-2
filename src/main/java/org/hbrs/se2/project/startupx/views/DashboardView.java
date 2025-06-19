package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.control.ManageStartupControl;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.util.Globals;

import java.util.List;

@CssImport("./styles/views/main/main-view.css")
@Route(value = Globals.Pages.DASHBOARD_VIEW, layout = AppView.class)
@JsModule("./styles/shared-styles.js")
public class DashboardView extends VerticalLayout {

    //Neue Startseite
    public DashboardView(ManageStartupControl manageStartupControl, BewertungControl bewertungControl) {
        setPadding(true);
        setSpacing(true);

        add(new H3("Neueste StartUps"));

        //Soll die 5 neusten StartUps anzeigen
        List<StartupDTO> startups = manageStartupControl.findTop5ByOrderByGruendungsdatumDesc();

        FlexLayout gallery = new FlexLayout();
        gallery.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        //Zu jedem StartUp soll ein generisches Bild in einer Gallery angezeigt werden
        for (StartupDTO s : startups) {
            VerticalLayout card = new VerticalLayout();
            card.setPadding(true);
            card.setSpacing(true);
            card.getStyle().set("border", "1px solid #ccc");
            card.getStyle().set("padding", "10px");
            card.getStyle().set("width", "220px");
            card.getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)");

            // Hover-Effekt
            card.getElement().addEventListener("mouseenter", e ->
                    card.getStyle().set("box-shadow", "4px 4px 12px rgba(0,0,0,0.2)")
            );
            card.getElement().addEventListener("mouseleave", e ->
                    card.getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)")
            );

            Image logo = new Image("images/startup_placeholder.png", "StartUp-Logo");
            logo.setWidth("100%");
            logo.getStyle().set("border-radius", "4px");
            logo.getStyle().set("margin-bottom", "10px");

            // Inhalt mit Zeilenumbrüchen
            H3 name = new H3(s.getName());
            name.getStyle().set("margin", "0");

            Span description = new Span(s.getBeschreibung() != null ? s.getBeschreibung() : "-");
            description.getStyle().set("white-space", "pre-wrap"); // <- ermöglicht Zeilenumbruch

            Span date = new Span("Gegründet: " + (s.getGruendungsdatum() != null ? s.getGruendungsdatum().toString() : "Unbekannt"));

            card.add(logo, name, description, date);

            double average = bewertungControl.getDurchschnittlicheBewertungZuStartup(s.getId());
            if (average != 0.0) {
                Span rating = new Span("Ø Bewertung: " + String.format("%.1f", average) + " ⭐");
                card.add(rating);
            }

            // Navigation zum StartUp
            card.addClickListener(click ->
                    getUI().ifPresent(ui -> ui.navigate("startup/" + s.getId()))
            );

            gallery.add(card);
        }


        add(gallery);
    }
}
