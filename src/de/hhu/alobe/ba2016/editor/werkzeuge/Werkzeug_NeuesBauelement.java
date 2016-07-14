package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementHinzufuegen;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Linse;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Werkzeug_NeuesBauelement extends Werkzeug {

    private Bauelement bauelement;

    public Werkzeug_NeuesBauelement(OptischeBank optischeBank, Bauelement bauelement) {
        super(optischeBank);
        this.bauelement = bauelement;
    }

    @Override
    public void auswahlAufheben() {

    }

    @Override
    public void auswaehlen() {

    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            bauelement.verschiebeUm(realePosition);
            optischeBank.neueAktionDurchfuehren(new Aktion_BauelementHinzufuegen(optischeBank, bauelement));
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }

    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {

    }
}
