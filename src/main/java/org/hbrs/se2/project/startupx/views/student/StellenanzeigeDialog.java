package org.hbrs.se2.project.startupx.views.student;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hbrs.se2.project.startupx.control.StellenausschreibungControl;
import org.hbrs.se2.project.startupx.dtos.SkillDTO;
import org.hbrs.se2.project.startupx.dtos.StellenausschreibungDTO;

@UIScope
public class StellenanzeigeDialog extends Dialog {

    private StellenausschreibungControl stellenausschreibungControl;

    //Textfelder
    private final TextField titel = new TextField("Titel");
    private final TextArea beschreiung = new TextArea("Beschreibung");
    private final MultiSelectComboBox<SkillDTO> skills = new MultiSelectComboBox<>("Skills");

    //Button
    private final Button erstelleButton = new Button("Stellenausschreibung erstellen");
    private final Button abbrechenButton = new Button("Stellenausschreibung abbrechen");

    private final Binder<StellenausschreibungDTO> binder = new Binder<>(StellenausschreibungDTO.class);

    private Long startupId;

    public StellenanzeigeDialog(Long startupId, StellenausschreibungControl stellenausschreibungControl) {
        this.stellenausschreibungControl = stellenausschreibungControl;
        this.startupId = startupId;
    }



}
