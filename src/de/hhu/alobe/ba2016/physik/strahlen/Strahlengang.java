package de.hhu.alobe.ba2016.physik.strahlen;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;
import java.util.ArrayList;

/**
 * Strahlengang bestehend aus Verbindung mehrerer Geraden und einem Strahl am Ende dieser Verbindungsgeraden.
 */
public class Strahlengang implements Zeichenbar {

    //Speichert den Startwert um Strahlengang zuruecksetzen zu koennen
    private Strahl anfangsStrahl;

    //Speichert, ob dieser Strahl gezeichnet werden soll.
    private boolean aktiviert;

    //Speichert aktuelle Strahlenabschnitte und die Richtung des letzten, ungehinderten Abschnittes, der das Bild verlaesst.
    private ArrayList<Gerade> strahlenAbschnitte;

    //Strahl am Ende der verbundenen Geraden, mit dem Kollisionen ueberprueft werden.
    private Strahl aktuellerStrahl;

    /**
     * Initialisiert den Strahlengang mit einem Anfangsstrahl.
     *
     * @param anfangsStrahl Anfangsstrahl.
     */
    public Strahlengang(Strahl anfangsStrahl) {
        this.anfangsStrahl = anfangsStrahl;
        this.aktuellerStrahl = anfangsStrahl;
        strahlenAbschnitte = new ArrayList<>();
    }

    /**
     * Haengt einen neuen Strahl an diesen Strahlengang an. Der aktuelle Strahl wird dann zu einer Geraden mit Laenge bis zum Basisvektors des neuen Strahls.
     *
     * @param neuerStrahl Neuer Strahl, der angehaengt werden soll.
     */
    public void neuenStrahlAnhaengen(Strahl neuerStrahl) {
        if (strahlenAbschnitte.size() >= Konstanten.MAX_STRAHLLAENGE) {
            strahlengangBeenden(neuerStrahl.getBasisVektor());
        } else {
            strahlenAbschnitte.add(new Gerade(aktuellerStrahl, Vektor.gibAbstand(neuerStrahl.getBasisVektor(), aktuellerStrahl.getBasisVektor())));
            this.aktuellerStrahl = neuerStrahl;
        }
    }

    /**
     * Beendet diesen Strahlengang. Dies kann zum Beispiel durch Absorption oder dem Erreichen der maximalen Strahlenlaenge erfolgen.
     * Der aktuelle Strahl wird als Gerade in die Liste der Strahlenabschnitte eingefuegt und auf null gesetzt.
     *
     * @param letztePosition Position, die den aktuellen Strahl zu einer Geraden begrenzt, und dem letzten Punkt des Strahlengangs entspricht.
     */
    public void strahlengangBeenden(Vektor letztePosition) {
        strahlenAbschnitte.add(new Gerade(aktuellerStrahl, Vektor.gibAbstand(letztePosition, aktuellerStrahl.getBasisVektor())));
        aktuellerStrahl = null;
    }

    /**
     * Setzt den Strahlengang zurueck. Er besteht danach nur noch aus einem Anfangsstrahl.
     */
    public void resetteStrahlengang() {
        this.aktuellerStrahl = anfangsStrahl;
        //Loesche die alte Liste der Strahlenabschnitte:
        strahlenAbschnitte = new ArrayList<>();
    }

    /**
     * Gibt die Quellpunkte der Geraden und des aktuellen Strahls in einer Liste zurueck.
     *
     * @param auchVirtuell Gibt an, ob auch virtuelle Bilder ausgegebenen werden sollen.
     * @return Liste aller Bilder, die durch diesen Strahlengang entstehen.
     */
    public ArrayList<Vektor> gibBildpunkte(boolean auchVirtuell) {
        ArrayList<Vektor> retList = new ArrayList<>();
        for (Gerade g : strahlenAbschnitte) {
            if (g.getQuellEntfernung() != 0 && g.getQuellEntfernung() < g.getLaenge() + Konstanten.TOLERANZ_BILD && (g.getQuellEntfernung() > 0 || auchVirtuell)) {
                retList.add(g.gibQuellPunkt());
            }
        }
        if (aktuellerStrahl != null) {
            if (aktuellerStrahl.getQuellEntfernung() != 0 && (aktuellerStrahl.getQuellEntfernung() > 0 || auchVirtuell)) {
                retList.add(aktuellerStrahl.gibQuellPunkt());
            }
        }
        return retList;
    }

    /**
     * Gibt an, ob dieser Strahl innerhalb eines Toleranzradius angeklickt wurde.
     *
     * @param pruefKreis Kreis um den Mauszeiger mit Toleranzradius.
     * @return Wahrheitswert, ob dieser Strahlengang innerhalb des Pruefkreises verlaeuft.
     */
    public boolean istAngeklickt(Kreis pruefKreis) {
        if (aktuellerStrahl != null) {
            if (pruefKreis.schneidetStrahl(aktuellerStrahl)) return true;
        }
        for (Gerade gerade : strahlenAbschnitte) {
            if (pruefKreis.schneidetGerade(gerade)) return true;
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Color alteFarbe = g.getColor();
        if (aktiviert) {
            g.setColor(Color.RED);
        }
        for (int i = 0; i < strahlenAbschnitte.size(); i++) {
            strahlenAbschnitte.get(i).paintComponent(g);
        }
        if (aktuellerStrahl != null) {
            aktuellerStrahl.paintComponent(g);
        }
        g.setColor(alteFarbe);
    }

    /**
     * @param aktiviert Wert, ob Strahlengang aktiviert werden soll.
     */
    public void setAktiviert(boolean aktiviert) {
        this.aktiviert = aktiviert;
    }

    /**
     * @return Anfangsstrahl des Strahlengangs.
     */
    public Strahl getAnfangsStrahl() {
        return anfangsStrahl;
    }

    /**
     * @return Aktueller Strahl des Strahlengangs.
     */
    public Strahl getAktuellerStrahl() {
        return aktuellerStrahl;
    }

}
