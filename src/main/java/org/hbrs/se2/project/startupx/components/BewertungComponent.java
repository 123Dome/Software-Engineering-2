package org.hbrs.se2.project.startupx.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.startupx.control.BewertungControl;
import org.hbrs.se2.project.startupx.dtos.BewertungDTO;
import org.hbrs.se2.project.startupx.dtos.StartupDTO;
import org.hbrs.se2.project.startupx.dtos.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BewertungComponent {

    private final BewertungControl bewertungControl;

    public BewertungComponent(BewertungControl bewertungControl) {
        this.bewertungControl = bewertungControl;
    }

    /**
     * Baut eine vertikale Liste mit allen Bewertungskarten.
     */
    public Component createBewertungenCards(
            List<BewertungDTO> bewertungDTOs,
            UserDTO currentUser,
            Runnable onChange
    ){
        VerticalLayout layout = new VerticalLayout();

        if (bewertungDTOs.isEmpty()) {
            return layout;
        }

        layout.add(new H3("Bewertungen"));

        for(BewertungDTO bewertungDTO : bewertungDTOs) {
            layout.add(createBewertungCard(bewertungDTO, currentUser, onChange));
        }

        return layout;
    }

    /**
     * Baut eine einzelne Bewertungskarte.
     */
    private Component createBewertungCard(
            BewertungDTO bewertungDTO,
            UserDTO currentUser,
            Runnable onChange
    ) {
        Div card = new Div();
        card.setWidth("400px");
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("padding", "16px");
        card.getStyle().set("box-shadow", "2px 2px 6px rgba(0,0,0,0.1)");

        String username = bewertungControl.getUserNameById(bewertungDTO.getUser());
        card.add(new H4(username));

        HorizontalLayout stars = new HorizontalLayout();
        for(int i = 0; i < bewertungDTO.getBewertung(); i++) {
            Span star = new Span("⭐");
            star.getStyle().set("font-size", "20px");
            stars.add(star);
        }
        card.add(stars);

        if(bewertungDTO.getKommentar() != null && !bewertungDTO.getKommentar().isEmpty()) {
            Paragraph kommentar = new Paragraph(bewertungDTO.getKommentar());
            kommentar.getStyle().set("font-style", "italic");
            kommentar.getStyle().set("margin-top", "8px");
            card.add(kommentar);
        }

        Paragraph datum = new Paragraph("Datum: " + bewertungDTO.getErstellungsdatum());
        datum.getStyle().set("font-size", "0.9em");
        datum.getStyle().set("color", "#666");
        card.add(datum);

        if (currentUser != null && currentUser.getId().equals(bewertungDTO.getUser())){
            Button bewertungLöschenButton = new Button("Löschen");
            bewertungLöschenButton.getStyle().set("margin-top", "8px");

            bewertungLöschenButton.addClickListener(e -> {
                createBewertungLöschenDialog(bewertungDTO, onChange).open();
            });
            card.add(bewertungLöschenButton);
        }

        return card;
    }

    /**
     * Baut den Lösch-Dialog für eine Bewertung.
     */
    private Dialog createBewertungLöschenDialog(
            BewertungDTO bewertungDTO,
            Runnable onChange
    ) {
        Dialog löschenBestätigenDialog = new Dialog();
        löschenBestätigenDialog.add(new Paragraph("Wollen Sie die Bewertung wirklich löschen?"));
        Button löschenBestätigenButton = new Button("Ja", event -> {
            try {
                bewertungControl.deleteBewertung(bewertungDTO.getId());
                Notification.show("Bewertung gelöscht.");
                onChange.run();
            } catch (Exception ex) {
                Notification.show(ex.getMessage());
            }
            löschenBestätigenDialog.close();
        });
        Button löschenAbbrechenButton = new Button("Abbrechen", event -> löschenBestätigenDialog.close());
        löschenBestätigenDialog.getFooter().add(löschenBestätigenButton, löschenAbbrechenButton);
        return löschenBestätigenDialog;
    }

    /**
     * Erstellt den Button zum Bewerten
     */
    public Button createBewertenButton(
            StartupDTO startupDTO,
            UserDTO currentUser,
            Runnable onChange
    ) {
        Button bewertenButton = new Button("Bewerten");

        bewertenButton.addClickListener(
                buttonClickEvent -> createBewertenDialog(startupDTO, currentUser, onChange).open()
        );

        return bewertenButton;
    }

    /**
     * Baut den Dialog zum Abgeben einer Bewertung.
     */
    private Dialog createBewertenDialog(
            StartupDTO startupDTO,
            UserDTO currentUser,
            Runnable onChange
    ) {
        Dialog dialog = new Dialog();
        TextField kommentarField = new TextField("Kommentar (optional)");
        HorizontalLayout sterneLayout = new HorizontalLayout();
        VerticalLayout dialogLayout = new VerticalLayout();

        dialog.setHeaderTitle("Bewertung abgeben");

        Span[] sterne = new Span[5];
        AtomicInteger selectedRating = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            Span stern = new Span("☆");
            stern.getStyle().set("font-size", "24px");
            stern.getStyle().set("cursor", "pointer");
            final int index = i;

            stern.addClickListener(e -> {
                selectedRating.set(index + 1);
                for(int j = 0; j < 5; j++) {
                    sterne[j].setText(j <= index ? "★" : "☆");
                }
            });

            sterne[i] = stern;
            sterneLayout.add(stern);
        }


        Button dialogSchließenButton = new Button("Schließen", e -> dialog.close());
        Button bewertungSpeichernButton = new Button("Speichern", e -> {
            int bewertung = selectedRating.get();
            String kommentar = kommentarField.getValue();

            if (bewertung == 0) {
                Notification.show("Bitte Sterne auswählen!");
                return;
            }

            BewertungDTO neueBewertungDTO = new BewertungDTO();
            neueBewertungDTO.setBewertung(bewertung);
            neueBewertungDTO.setKommentar(kommentar);
            neueBewertungDTO.setErstellungsdatum(LocalDate.now());
            neueBewertungDTO.setStartup(startupDTO.getId());
            neueBewertungDTO.setUser(currentUser.getId());

            bewertungControl.createBewertung(neueBewertungDTO);
            Notification.show("Bewertung gespeichert.");

            dialog.close();
            onChange.run();
        });

        dialogLayout.add(sterneLayout, kommentarField);

        dialog.add(dialogLayout);
        dialog.getFooter().add(bewertungSpeichernButton, dialogSchließenButton);

        return dialog;
    }
}
