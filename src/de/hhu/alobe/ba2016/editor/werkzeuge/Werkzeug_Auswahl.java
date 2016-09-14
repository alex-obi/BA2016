package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementVerschieben;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Werkzeug zum Auswaehlen und Verschieben eines Elements und dessen Anzeige.
 */
public class Werkzeug_Auswahl extends Werkzeug {

    //Element, welches durch dieses Werkzeug ausgewaehlt wurde:
    private Bauelement ausgewaehltesElement;

    //Position des Mauszeigers, an dem das Element zuerst ausgewaehlt wurde
    private Vektor erstePosition;

    //Position des Mittelpunkts des Bauelements, an dem das Element zuerst ausgewaehlt wurde
    private Vektor ersterMittelpunktBauel;

    //Gibt an, ob dies die erste Verschiebung des Bauelements ist, nachdem dieses ausgewaehlt wurde
    private boolean ersteVerschiebung = true;

    /**
     * Initialisiert ein neues Werkzeug zur Auswahl von Bauelementen.
     *
     * @param optischeBank Referenz auf OPtische Bank
     */
    public Werkzeug_Auswahl(OptischeBank optischeBank) {
        super(optischeBank);
    }

    @Override
    public void auswahlAufheben() {
        if (ausgewaehltesElement != null) {
            ausgewaehltesElement.rahmenAusblenden();
            ausgewaehltesElement = null;
        }
    }

    @Override
    public void auswaehlen() {

    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {
        if (ausgewaehltesElement != null && SwingUtilities.isLeftMouseButton(e) && ausgewaehltesElement.istAngeklickt(realePosition)) {
            ausgewaehltesElement.rahmenEinblenden();
            ausgewaehltesElement.waehleAus();
            optischeBank.aktualisieren();
        }
    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {
        //Ueberpruefe ob die Mausaktion mit linker Maustaste ausgefuehrt wurde und welches Element ausgewaehlt wurde
        if (SwingUtilities.isLeftMouseButton(e)) {
            erstePosition = realePosition.kopiere();
            for (Bauelement cBauEl : optischeBank.getBauelemente()) {
                if (cBauEl.istAngeklickt(realePosition)) {
                    //Hebe alte Auswahl auf
                    auswahlAufheben();
                    //Waehle neues Element durch Aenderung der Werte dieser Klasse aus
                    ausgewaehltesElement = cBauEl;
                    ersterMittelpunktBauel = cBauEl.getMittelPunkt().kopiere();
                    break;
                }
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            auswahlAufheben();
        }
        optischeBank.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if (!ersteVerschiebung) {
            ausgewaehltesElement = null;
            ersteVerschiebung = true;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            auswahlAufheben();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (ausgewaehltesElement != null) {
                //Verschiebe das ausgewaehlte Element relativ zu der Position, an dem es zuerst ausgewaehlt wurde
                Vektor verschiebung = Vektor.subtrahiere(realePosition, erstePosition);

                if (Math.abs(ersterMittelpunktBauel.getY() + verschiebung.getY() - optischeBank.getOptischeAchse().getHoehe()) < Konstanten.OPTISCHEACHSE_FANGENTFERNUNG) {
                    if (ausgewaehltesElement.fangModusOptischeAchseAn()) {
                        verschiebung.setY(optischeBank.getOptischeAchse().getHoehe() - ersterMittelpunktBauel.getY());
                    }
                }
                if (ersteVerschiebung) {
                    //Wenn es die erste Verschiebung war erstelle neue Aktion fuer die Optische Bank
                    optischeBank.neueAktionDurchfuehren(new Aktion_BauelementVerschieben(ausgewaehltesElement, verschiebung));
                    ersteVerschiebung = false;
                } else {
                    //Ansonsten ueberschreibe die letzte Aktion mit der neuen Verschiebung
                    optischeBank.letzteAktionUeberschreiben(new Aktion_BauelementVerschieben(ausgewaehltesElement, verschiebung));
                }

            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {
        //Aendere Mauscursor, wenn Maus ueber einem Bauelement ist
        optischeBank.getZeichenBrett().setCursor(Cursor.getDefaultCursor());
        for (Bauelement cBauEl : optischeBank.getBauelemente()) {
            if (cBauEl.istAngeklickt(realePosition)) {
                optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }

}
