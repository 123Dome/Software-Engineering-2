package org.hbrs.se2.project.startupx.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hbrs.se2.project.startupx.control.BewertungControl;

public class BewertungenUtil {
    private BewertungenUtil() {}

    public static Component createAverageStars(
            BewertungControl bewertungControl,
            Long startupId
    ) {
        double average = bewertungControl.getDurchschnittlicheBewertungZuStartup(startupId);

        if (average == 0.0) {
            return new Span("Keine Bewertungen vorhanden");
        }

        HorizontalLayout sterne = new HorizontalLayout();
        int volleSterne = (int) average;
        boolean halberStern = (average - volleSterne) >= 0.5;
        int leereSterne = 5 - volleSterne - (halberStern ? 1 : 0);

        // Volle Sterne
        for (int i = 0; i < volleSterne; i++) {
            sterne.add(new Span("★"));
        }

        if(halberStern) {
            sterne.add(new Span("⯨"));
        }

        // Leere Sterne
        for (int i = 0; i < leereSterne; i++) {
            sterne.add(new Span("☆"));
        }

        sterne.add(new Span(String.format("(%.1f Sterne)", average)));

        return sterne;
    }


}
