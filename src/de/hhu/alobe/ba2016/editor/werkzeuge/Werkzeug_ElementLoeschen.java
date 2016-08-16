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
import java.util.Objects;

public class Werkzeug_ElementLoeschen extends Werkzeug {

    private Strahlengang letzterStrahlengang;

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
            boolean elementGefunden = false;
            for(Bauelement cBauEl : optischeBank.getBauelemente()) {
                if(cBauEl.istAngeklickt(realePosition)) {
                    cBauEl.rahmenAusblenden();
                    optischeBank.neueAktionDurchfuehren(new Aktion_BauelementLoeschen(optischeBank, cBauEl));
                    elementGefunden = true;
                    break;
                }
            }
            Kreis pruefKreis = new Kreis(realePosition, 10);
            if(!elementGefunden) {
                for(Lichtquelle lichtquelle : optischeBank.getLichtquellen()) {
                    if(lichtquelle.isAktiv()) {
                        ArrayList<Strahlengang> strahlengaenge = lichtquelle.getStrahlengaenge();
                        for (int i = 0; i < strahlengaenge.size(); i++) {
                            if (strahlengaenge.get(i).istAngeklickt(pruefKreis)) {
                                strahlengaenge.get(i).setAktiviert(false);
                                optischeBank.neueAktionDurchfuehren(new Aktion_StrahlengangLoeschen(optischeBank, lichtquelle, strahlengaenge.get(i)));
                                elementGefunden = true;
                                break;
                            }
                        }
                        if (elementGefunden) break;
                    }
                }
            }
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            if(letzterStrahlengang != null) {
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
        boolean elementGefunden = false;
        for(Bauelement cBauEl : optischeBank.getBauelemente()) {
            if(cBauEl.istAngeklickt(realePosition)) {
                cBauEl.rahmenEinblenden();
                elementGefunden = true;
            } else {
                cBauEl.rahmenAusblenden();
            }
        }
        Kreis pruefKreis = new Kreis(realePosition, 10);
        if(letzterStrahlengang != null) {
            letzterStrahlengang.setAktiviert(false);
        }
        if(!elementGefunden) {
            for(Lichtquelle lichtquelle : optischeBank.getLichtquellen()) {
                if(lichtquelle.isAktiv()) {
                    for (Strahlengang strahlengang : lichtquelle.getStrahlengaenge()) {
                        if (strahlengang.istAngeklickt(pruefKreis)) {
                            strahlengang.setAktiviert(true);
                            letzterStrahlengang = strahlengang;
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
