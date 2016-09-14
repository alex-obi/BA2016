package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

/**
 * Vereinfacht eine Linse oder einen Hohlspiegel als Hauptebene, also einer brechenden oder reflektierenden Ebene mit ausgezeichneten Brennpunkten.
 */
public class Hauptebene extends Flaeche {

    //Mittelpunkt der Hauptebene, auf den die Gerade zentriert wird
    private Vektor mittelpunkt;

    //Kollisionsflaeche der Hauptebne als Gerade
    private Gerade hauptebene;

    /**
     * Mindesthoehe der Hauptebene um zu kleine grafische Anzeige zu verhindern.
     */
    public static final int HAUPTEBENE_MINDESTHOEHE = 80;

    //Brennweite vor der Hauptebene (in negative X-Richtung)
    private double brennweiteVor;

    //Brennweite hinter der Hauptebene (in positive X-Richtung)
    private double brennweiteHinter;

    /**
     * Initialisiert eine Hauptebene mit einer einzigen Brennweite, die vor und hinter der Hauptebene identisch ist.
     *
     * @param modus        Berechnungsmodus der Hauptebene (Reflektion, Brechung, Absorbtion, ...).
     * @param nMittelpunkt Mittelpunkt der Hauptebene.
     * @param nBrennweite  Beidseitige Brennweite.
     * @param hoehe        Hoehe der Hauptebene.
     */
    public Hauptebene(int modus, Vektor nMittelpunkt, double nBrennweite, double hoehe) {
        this.modus = modus;
        mittelpunkt = nMittelpunkt.kopiere();
        brennweiteVor = this.brennweiteHinter = nBrennweite;
        hauptebene = new Gerade(new Vektor(mittelpunkt.getX(), mittelpunkt.getY() + hoehe / 2), new Vektor(mittelpunkt.getX(), mittelpunkt.getY() - hoehe / 2));
    }

    /**
     * Initialisiert eine Hauptebene mit unterschiedlichen Brennweiten vor und hinter der Hauptebene.
     *
     * @param modus             Berechnungsmodus der Hauptebene (Reflektion, Brechung, Absorbtion, ...).
     * @param nMittelpunkt      Mittelpunkt der Hauptebene.
     * @param nBrennweiteVor    Brennweite vor der Hauptebene (negative X-Richtung).
     * @param nBrennweiteHinter Brennweite hinter der Hauptebene (positive X-Richtung).
     * @param hoehe             Hoehe der Hauptebene.
     */
    public Hauptebene(int modus, Vektor nMittelpunkt, double nBrennweiteVor, double nBrennweiteHinter, double hoehe) {
        this.modus = modus;
        mittelpunkt = nMittelpunkt.kopiere();
        brennweiteVor = nBrennweiteVor;
        brennweiteHinter = nBrennweiteHinter;
        hauptebene = new Gerade(new Vektor(mittelpunkt.getX(), mittelpunkt.getY() + hoehe / 2), new Vektor(mittelpunkt.getX(), mittelpunkt.getY() - hoehe / 2));
    }

    /**
     * Setzt die Hoehe der Hauptebene neu
     *
     * @param nHoehe Neue Hoehe.
     */
    public void setHoehe(double nHoehe) {
        hauptebene.verschiebeUm(new Vektor(0, (nHoehe - hauptebene.getLaenge()) / 2));
        hauptebene.setLaenge(nHoehe);
    }

    /**
     * Setzt Brennweite vor und hinter der Hauptebene auf den uebergebenen Wert.
     *
     * @param nBrennweite Neue Brennweite.
     */
    public void setBrennweite(double nBrennweite) {
        brennweiteVor = brennweiteHinter = nBrennweite;
    }

    @Override
    public double kollisionUeberpruefen(Strahl strahl) {
        return hauptebene.gibSchnittEntfernung(strahl);
    }

    @Override
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if (modus == Flaeche.MODUS_ABSORB) {
            cStrGng.strahlengangBeenden(position);
            return;
        }
        if (cStrGng.getAktuellerStrahl().getRichtungsVektor().getX() == 0)
            return; //Abbrechen, wenn Strahl parallel zur Hauptebene eintrifft

        double richtungsVZ = Math.signum(cStrGng.getAktuellerStrahl().getRichtungsVektor().getX()); //Gibt an von welcher Seite der Strahl kommt
        double gegenstandsweite = richtungsVZ * (mittelpunkt.getX() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getX()); //Gegenstandsweite gespiegelt bei Richtungswechsel
        double gegenstandshoehe = cStrGng.getAktuellerStrahl().gibQuellPunkt().getY() - mittelpunkt.getY();
        Vektor relativerSchnittpunkt = Vektor.subtrahiere(position, mittelpunkt);

        double reflFakt = 1;
        if (modus == Flaeche.MODUS_REFLEKT) {
            reflFakt = -1;
        }

        double brennweiteG; //Brennweite auf Gegenstandsseite
        double brennweiteB; //Brennweite auf Bildseite
        if (richtungsVZ > 0) {
            brennweiteG = brennweiteVor;
            brennweiteB = brennweiteHinter;
        } else {
            brennweiteG = reflFakt * brennweiteHinter;
            brennweiteB = reflFakt * brennweiteVor;
        }

        Vektor neueRichtung;
        boolean inUnendlich;
        boolean istVirtuell;
        double nQuellWeite = 0;

        if ((brennweiteB == 0 || brennweiteG == 0)) {
            //Ebener Spiegel oder Ebene Linse ohne Brennweite (Brennweite -> unendlich)
            if (cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) {
                double hoehe = cStrGng.getAktuellerStrahl().getRichtungsVektor().getY();
                double breite = richtungsVZ * cStrGng.getAktuellerStrahl().getRichtungsVektor().getX();
                neueRichtung = new Vektor(-richtungsVZ * reflFakt * breite, -hoehe);
                inUnendlich = true;
                istVirtuell = true;
                nQuellWeite = 0;
            } else {
                double bildweite = -gegenstandsweite;
                double bildgroesse = -gegenstandshoehe;
                Vektor bildPosition = new Vektor(bildweite, -bildgroesse);
                istVirtuell = (bildweite < 0);
                inUnendlich = false;
                bildPosition.setX(richtungsVZ * reflFakt * bildPosition.getX());
                bildPosition.subtrahiere(relativerSchnittpunkt);
                neueRichtung = bildPosition;
                nQuellWeite = neueRichtung.gibLaenge();
            }
        } else if (Math.abs(gegenstandsweite) < 0.000001) {
            //Spezialfall 3: Quellpunkt liegt genau auf der Linse -> uebernimmt Funktion einer Feldlinse
            double einfallswinkel = cStrGng.getAktuellerStrahl().getRichtungsVektor().gibRichtungsWinkel();
            if (richtungsVZ < 0) {
                einfallswinkel = Math.PI - einfallswinkel;
            }
            double brechungsWinkel = Math.atan(Math.tan(einfallswinkel) * (brennweiteG / brennweiteB) - (relativerSchnittpunkt.getY() / brennweiteB));
            neueRichtung = new Vektor(richtungsVZ * reflFakt, 0);
            neueRichtung.dreheUmWinkel(richtungsVZ * reflFakt * brechungsWinkel);
            inUnendlich = false;
            istVirtuell = false;
            nQuellWeite = 0;
        } else if (Math.abs(brennweiteG - gegenstandsweite) < Konstanten.TOLERANZ_ABBILDUNG_UNENDLICH && !cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) {
            //Spezialfall 1: (g ist ungefaehr f)
            neueRichtung = new Vektor(reflFakt * richtungsVZ * brennweiteB, -gegenstandshoehe);
            neueRichtung.multipliziere(Math.signum(brennweiteB));
            inUnendlich = true;
            istVirtuell = false;
            nQuellWeite = 0;
        } else {
            Vektor bildPosition;
            //Position des Bildes relativ zum Mittelpunkt der Hauptebene
            if (cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) { //Spezialfall 2 (Strahl aus dem Unendlichen)
                double einfallswinkel = cStrGng.getAktuellerStrahl().getRichtungsVektor().gibRichtungsWinkel();
                if (richtungsVZ < 0) {
                    einfallswinkel = Math.PI - einfallswinkel;
                }
                bildPosition = new Vektor(brennweiteB, brennweiteG * Math.tan(einfallswinkel));
                istVirtuell = (brennweiteB < 0);
                inUnendlich = false;
            } else {
                //Normalfall (g, G liefern direkt Werte b, B)
                double bildweite = (brennweiteB * gegenstandsweite) / (gegenstandsweite - brennweiteG);
                double bildgroesse = (gegenstandshoehe * brennweiteG) / (gegenstandsweite - brennweiteG);
                bildPosition = new Vektor(bildweite, -bildgroesse);
                istVirtuell = (bildweite < 0);
                inUnendlich = false;
            }
            bildPosition.setX(richtungsVZ * reflFakt * bildPosition.getX());
            bildPosition.subtrahiere(relativerSchnittpunkt);
            neueRichtung = bildPosition;
            nQuellWeite = neueRichtung.gibLaenge();
        }

        if (istVirtuell) {
            //Bild ist virtuell
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -1), -nQuellWeite, inUnendlich));
        } else {
            //Bild ist reell
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, neueRichtung, nQuellWeite, inUnendlich));
        }
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        hauptebene.verschiebeUm(verschiebung);
        mittelpunkt.addiere(verschiebung);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        hauptebene.paintComponent(g);
    }
}
