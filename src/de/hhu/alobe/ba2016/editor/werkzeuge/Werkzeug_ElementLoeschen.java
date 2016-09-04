package de.hhu.alobe.ba2016.editor.werkzeuge;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementLoeschen;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_StrahlengangLoeschen;
import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Werkzeug zum Löschen eines Elements (Bauelement oder Strahl) aus der Optischen Bank.
 * Über die rechte Maustaste lässt sich wieder zum Auswahlwerkzeug wechseln.
 */
public class Werkzeug_ElementLoeschen extends Werkzeug {

    //Befindet sich der Cursor über einem Strahlengang, wird dieses Datenfeld initialisiert
    private Strahlengang letzterStrahlengang;

    //Die, zum letzten Strahlengang zugehörige Lichtquelle
    private Lichtquelle lezteLichtquelle;

    /**
     * Radius um den Cursor, in dem Strahlen eingefangen und gelöscht werden können.
     */
    public static final double FANGRADIUS_STRAHL = 10;

    /**
     * Initialisiere ein neues Werkzeug Löschen zu der übergebenen Optischen Bank.
     *
     * @param optischeBank Referenz auf Optische Bank
     */
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
        //Ändert den Cursor, wenn dieses Werkzeug ausgewählt wurde
        //todo: passenderen Cursor zeichnen und einfügen
        optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        //Löscht beim Loslassen der linken Maustaste ein Element, falls sich der Cursor darüber befindet
        if (SwingUtilities.isLeftMouseButton(e)) {
            //Prüfe zuerst, ob ein Bauelement angeklickt wurde und lösche dieses
            boolean elementGefunden = false;
            for (Bauelement cBauEl : optischeBank.getBauelemente()) {
                if (cBauEl.istAngeklickt(realePosition)) {
                    cBauEl.rahmenAusblenden();
                    optischeBank.neueAktionDurchfuehren(new Aktion_BauelementLoeschen(optischeBank, cBauEl));
                    elementGefunden = true;
                    break;
                }
            }
            //Falls kein Bauelement gefunden wurde lösche -falls vorhanden- den zuletzt ausgewählten Strahlengang
            if (!elementGefunden && lezteLichtquelle != null && lezteLichtquelle != null) {
                letzterStrahlengang.setAktiviert(false);
                optischeBank.neueAktionDurchfuehren(new Aktion_StrahlengangLoeschen(lezteLichtquelle, letzterStrahlengang));

            }
        }
        //Bei rechter Maustaste wechsel wieder zum Auswahlwerkzeug
        if (SwingUtilities.isRightMouseButton(e)) {
            if (letzterStrahlengang != null) {
                letzterStrahlengang.setAktiviert(false);
            }
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }
        optischeBank.aktualisieren();
    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {
        //Markiere bei bewegter Maus das -falls vorhanden- aktuell an diesem Ort befindliche Bauelement
        boolean elementGefunden = false;
        for (Bauelement cBauEl : optischeBank.getBauelemente()) {
            if (cBauEl.istAngeklickt(realePosition)) {
                cBauEl.rahmenEinblenden();
                elementGefunden = true;
            } else {
                cBauEl.rahmenAusblenden();
            }
        }
        //Falls kein Bauelement unterhalb des Cursors gefunden wurde suche innerhalb von FANGRADIUS_STRAHL nach einem Strahlengang und lösche diesen
        Kreis pruefKreis = new Kreis(realePosition, FANGRADIUS_STRAHL);
        if (letzterStrahlengang != null) {
            letzterStrahlengang.setAktiviert(false);
        }
        if (!elementGefunden) {
            for (Lichtquelle lichtquelle : optischeBank.getLichtquellen()) {
                if (lichtquelle.isAktiv()) {
                    for (Strahlengang strahlengang : lichtquelle.getStrahlengaenge()) {
                        if (strahlengang.istAngeklickt(pruefKreis)) {
                            strahlengang.setAktiviert(true);
                            letzterStrahlengang = strahlengang;
                            lezteLichtquelle = lichtquelle;
                            elementGefunden = true;
                            break;
                        }
                    }
                    if (elementGefunden) break;
                }
            }

        }
        optischeBank.repaint();
    }
}
