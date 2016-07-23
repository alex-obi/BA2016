package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementVerschieben;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Einfaches Werkzeug zum Auswaehlen eines Elements und dessen Anzeige im Werkzeuge Fenster
 */
public class Werkzeug_Auswahl extends Werkzeug{

    private Bauelement ausgewaehltesElement;
    private Vektor erstePosition;
    private Vektor ersterMittelpunktBauel;

    private boolean ersteVerschiebung = true;

    public Werkzeug_Auswahl(OptischeBank optischeBank) {
        super(optischeBank);
    }

    @Override
    public void auswahlAufheben() {
        if(ausgewaehltesElement != null) {
            ausgewaehltesElement.rahmenAusblenden();
        }
    }

    @Override
    public void auswaehlen() {

    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {
        if(ausgewaehltesElement != null && SwingUtilities.isLeftMouseButton(e)) {
            ausgewaehltesElement.waehleAus();
            ausgewaehltesElement.rahmenEinblenden();
            optischeBank.aktualisieren();
        }
    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            erstePosition = realePosition.kopiere();
            for(Bauelement cBauEl : optischeBank.getBauelemente()) {
                if(cBauEl.istAngeklickt(realePosition)) {
                    auswahlAufheben();
                    ausgewaehltesElement = cBauEl;
                    ersterMittelpunktBauel = cBauEl.getMittelPunkt().kopiere();
                    break;
                }
            }
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            auswahlAufheben();
        }
        optischeBank.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if(!ersteVerschiebung) {
            ausgewaehltesElement = null;
            ersteVerschiebung = true;
        }

        if(SwingUtilities.isRightMouseButton(e)) {
            auswahlAufheben();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            if(ausgewaehltesElement != null) {

                Vektor verschiebung = Vektor.subtrahiere(realePosition, erstePosition);

                if(Math.abs(ersterMittelpunktBauel.getYint() + verschiebung.getYint() - optischeBank.getOptischeAchse().getHoehe()) < Konstanten.OPTISCHEACHSE_FANGENTFERNUNG) {
                    if(ausgewaehltesElement.fangModusOptischeAchseAn()) {
                        verschiebung.setY(optischeBank.getOptischeAchse().getHoehe() - ersterMittelpunktBauel.getYint());
                    }
                }

                if (ersteVerschiebung) {
                    optischeBank.neueAktionDurchfuehren(new Aktion_BauelementVerschieben(ausgewaehltesElement, verschiebung));
                    ersteVerschiebung = false;
                } else {
                    optischeBank.letzteAktionUeberschreiben(new Aktion_BauelementVerschieben(ausgewaehltesElement, verschiebung));
                }

            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {
        optischeBank.setCursor(Cursor.getDefaultCursor());
        for(Bauelement cBauEl : optischeBank.getBauelemente()) {
            if(cBauEl.istAngeklickt(realePosition)) {
                optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                cBauEl.rahmenAusblenden();
            }
        }
    }

}
