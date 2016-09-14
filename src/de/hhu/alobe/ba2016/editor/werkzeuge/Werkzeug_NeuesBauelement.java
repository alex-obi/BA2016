package de.hhu.alobe.ba2016.editor.werkzeuge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementHinzufuegen;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Werkzeug zum Erstellen neuer Bauelemente.
 */
public class Werkzeug_NeuesBauelement extends Werkzeug {

    //Bauelement, das erzeugt werden soll.
    private Bauelement bauelement;

    /**
     * Initialisiert das Werkzeug Erstellen mit einem neuen Bauelement
     *
     * @param bauelement Neues Bauelement.
     */
    public Werkzeug_NeuesBauelement(Bauelement bauelement) {
        super(bauelement.getOptischeBank());
        this.bauelement = bauelement;
    }

    @Override
    public void auswahlAufheben() {
        //Loesche den Dummy wieder
        optischeBank.getZeichenBrett().zeichenObjektLoeschen(bauelement);
    }

    @Override
    public void auswaehlen() {
        //Fuege der optischen Bank einen Dummy hinzu. Der Dummy wird noch nicht zur Berechnung von Strahlen benutzt und wird bei Verlassen des Werkzeugs wieder geloescht
        optischeBank.getZeichenBrett().neuesZeichenObjekt(bauelement);
    }

    @Override
    public void mouseClicked(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mousePressed(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseReleased(MouseEvent e, Vektor realePosition) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            //Wechsele wieder zum Werkzeug Auswahl. Der Dummy wird hierdurch geloescht
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
            //Erstelle eine Aktion um das Bauelement entgueltig zur Optischen Bank hinzuzufuegen
            optischeBank.neueAktionDurchfuehren(new Aktion_BauelementHinzufuegen(optischeBank, bauelement));
        }

    }

    @Override
    public void mouseDragged(MouseEvent e, Vektor realePosition) {

    }

    @Override
    public void mouseMoved(MouseEvent e, Vektor realePosition) {
        //Positioniert das bauelement neu. Ist es in der Naehe der Optischen Achse setze Y-Koordinate auf Hoehe der Optischen Achse
        if (Math.abs(realePosition.getY() - optischeBank.getOptischeAchse().getHoehe()) < Konstanten.OPTISCHEACHSE_FANGENTFERNUNG && bauelement.fangModusOptischeAchseAn()) {
            bauelement.setzeMittelpunktNeu(new Vektor(realePosition.getX(), optischeBank.getOptischeAchse().getHoehe()));
        } else {
            bauelement.setzeMittelpunktNeu(realePosition);
        }
        optischeBank.repaint();
    }
}
