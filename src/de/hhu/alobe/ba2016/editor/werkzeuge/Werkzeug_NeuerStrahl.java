package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.editor.aktionen.Aktion_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

/**
 * Nach Auswahl einer Lichtquelle koennen mit diesem Werkzeug neue Strahlen der optischen Bank hinzugefuegt werden.
 * Mit der rechten Maustaste kann wieder zum Werkzeug Auswahl gewechselt werden.
 */
public class Werkzeug_NeuerStrahl extends Werkzeug {

    //Ausgewaehlte Lichtquelle, von der die Strahlen erzeugt werden sollen
    private Lichtquelle lichtquelle;

    //Speichert den Strahl, der aktuell durch den Mauscursor erzeugt werden wuerde
    private Strahlengang strahlDummy;

    /**
     * Initialisiere ein neues Werkzeug NeuerStrahl zu einer bestimmten Lichtquelle
     *
     * @param licht Erzeugende Lichtquelle
     */
    public Werkzeug_NeuerStrahl(Lichtquelle licht) {
        super(licht.getOptischeBank());
        this.lichtquelle = licht;
    }

    @Override
    public void auswahlAufheben() {
        optischeBank.getZeichenBrett().setCursor(Cursor.getDefaultCursor());
        lichtquelle.rahmenAusblenden();
        lichtquelle.loescheStrahl(strahlDummy);
    }

    @Override
    public void auswaehlen() {
        lichtquelle.rahmenEinblenden();
        //Aendere bei Auswahl des Werkzeugs den Cursor
        optischeBank.getZeichenBrett().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        //Bei Loslassen der linken Maustaste erstelle Aktion um einen neuen Strahl zu erstellen, der durch die Position des Cursors verlaeuft.
        if (SwingUtilities.isLeftMouseButton(e)) {
            //Loesche das alte Dummy Element
            lichtquelle.loescheStrahl(strahlDummy);
            //Berechne neuen Strahl und erstelle Aktion fuer die Optische Bank
            if (strahlDummy != null) {
                optischeBank.neueAktionDurchfuehren(new Aktion_NeuerStrahl(lichtquelle, strahlDummy));
                strahlDummy = null;
            }
        }
        //Bei rechter Maustaste wechsele wieder zum Werkzeug Auswahl
        if (SwingUtilities.isRightMouseButton(e)) {
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }
    }

    /**
     * Aktualisiert das Dummy Element
     */
    private void dummyElementAktualisieren(Vektor position) {
        lichtquelle.loescheStrahl(strahlDummy);
        strahlDummy = lichtquelle.berechneNeuenStrahl(position);
        if (strahlDummy != null) lichtquelle.neuerStrahl(strahlDummy);
        optischeBank.aktualisieren();
    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {
        //Erzeuge Dummy Strahl bei Bewegung der Maus bei gedrueckter linker Maustaste. Aktualisiere das Dummy Element bei jeder Bewegung
        if (SwingUtilities.isLeftMouseButton(e)) {
            dummyElementAktualisieren(realePosition);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {
        //Erzeuge Dummy Strahl bei Druck der rechten Maustaste.
        if (SwingUtilities.isLeftMouseButton(e)) {
            dummyElementAktualisieren(realePosition);
        }
    }

}
