package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

/**
 * Nach Auswahl einer Lichtquelle können mit diesem Werkzeug neue Strahlen der optischen Bank hinzugefuegt werden
 */
public class Werkzeug_NeuerStrahl extends  Werkzeug{

    private Lichtquelle lichtquelle;

    public Werkzeug_NeuerStrahl(OptischeBank optischeBank, Lichtquelle licht) {
        super(optischeBank);
        this.lichtquelle = licht;
    }

    @Override
    public void auswahlAufheben() {
        optischeBank.getZeichenBrett().setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void auswaehlen() {
        optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if(SwingUtilities.isLeftMouseButton(e)) {
            Strahlengang erzeugterStrahlengang = lichtquelle.berechneNeuenStrahl(realePosition);
            if(erzeugterStrahlengang != null) {
                optischeBank.neueAktionDurchfuehren(new Aktion_NeuerStrahl(lichtquelle, erzeugterStrahlengang));
            }
        }
        if(SwingUtilities.isRightMouseButton(e)) {
            lichtquelle.rahmenAusblenden();
            optischeBank.repaint();
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {

    }

}
