package de.hhu.alobe.ba2016.editor.werkzeuge;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementLoeschen;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Werkzeug_ElementLoeschen extends Werkzeug {

    public Werkzeug_ElementLoeschen(OptischeBank optischeBank) {
        super(optischeBank);
    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void auswahlAufheben() {
        optischeBank.getZeichenBrett().setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void auswaehlen() {
        optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            for(Bauelement cBauEl : optischeBank.getBauelemente()) {
                if(cBauEl.istAngeklickt(realePosition)) {
                    cBauEl.rahmenAusblenden();
                    optischeBank.neueAktionDurchfuehren(new Aktion_BauelementLoeschen(optischeBank, cBauEl));
                    break;
                }
            }
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }
        optischeBank.aktualisieren();
    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {
        for(Bauelement cBauEl : optischeBank.getBauelemente()) {
            if(cBauEl.istAngeklickt(realePosition)) {
                cBauEl.rahmenEinblenden();
            } else {
                cBauEl.rahmenAusblenden();
            }
        }
        optischeBank.repaint();
    }
}
