package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.startupx.entities.Startup;
import org.hbrs.se2.project.startupx.repository.StartupRepository;

import java.util.List;

@CssImport("./styles/views/main/main-view.css")
@Route(value = "main", layout = AppView.class)
@JsModule("./styles/shared-styles.js")
public class MainViewDashboard extends VerticalLayout {

    //Neue Startseite
    public MainViewDashboard(StartupRepository startupRepository) {
        setPadding(true);
        setSpacing(true);

        add(new H2("Neueste StartUps"));

        //Soll die 5 neusten StartUps anzeigen, aktuell noch alle
        List<Startup> startups = startupRepository.findAll();

        FlexLayout gallery = new FlexLayout();
        gallery.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        //Zu jedem StartUp soll ein generisches Bild in einer Gallery angezeigt werden
        for (Startup s : startups) {
            VerticalLayout card = new VerticalLayout();
            card.getStyle().set("border", "1px solid #ccc");
            card.getStyle().set("padding", "10px");
            card.getStyle().set("width", "200px");
            card.getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)");

            Image logo = new Image("images/logo.png", "StartUp-Logo");
            logo.setWidth("100px"); // Größe anpassen
            logo.setHeight("auto");

            card.add(
                    new H3(s.getName()),
                    new Text(s.getBeschreibung() != null ? s.getBeschreibung() : ""),
                    new Text("Gegründet: " + s.getGruendungsdatum())
            );

            gallery.add(card);
        }

        add(gallery);
    }
}
