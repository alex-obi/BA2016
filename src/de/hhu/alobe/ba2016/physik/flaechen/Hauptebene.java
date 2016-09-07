package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Hauptebene extends Flaeche {

    Vektor mittelpunkt;

    Gerade hauptebene;

    public static final int HAUPTEBENE_MINDESTHOEHE = 80;

    //Brennweite vor der Hauptebene
    double brennweiteVor;

    //Brennweite hinter der Hauptebene
    double brennweiteHinter;

    public Hauptebene(int modus, Vektor nMittelpunkt, double nBrennweite, double hoehe) {
        this.modus = modus;
        mittelpunkt = nMittelpunkt.kopiere();
        brennweiteVor = this.brennweiteHinter = nBrennweite;
        hauptebene = new Gerade(new Vektor(mittelpunkt.getX(), mittelpunkt.getY() + hoehe / 2), new Vektor(mittelpunkt.getX(), mittelpunkt.getY() - hoehe / 2));
    }

    public Hauptebene(int modus, Vektor nMittelpunkt, double nBrennweiteVor, double nBrennweiteHinter, double hoehe) {
        this.modus = modus;
        mittelpunkt = nMittelpunkt.kopiere();
        brennweiteVor = nBrennweiteVor;
        brennweiteHinter = nBrennweiteHinter;
        hauptebene = new Gerade(new Vektor(mittelpunkt.getX(), mittelpunkt.getY() + hoehe / 2), new Vektor(mittelpunkt.getX(), mittelpunkt.getY() - hoehe / 2));
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
        if (cStrGng.getAktuellerStrahl().getRichtungsVektor().getX() == 0) return; //Abbrechen, wenn Strahl parallel zur Hauptebene eintrifft

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

        if ((brennweiteB == 0 || brennweiteG == 0)){ //Ebener Spiegel oder Ebene Linse ohne Brennweite (Brennweite -> unendlich)
            if(cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) {
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
        } else if (Math.abs(gegenstandsweite) < 0.000001) { //Quellpunkt liegt genau auf der Linse -> übernimmt Funktion einer Feldlinse
            double einfallswinkel = cStrGng.getAktuellerStrahl().getRichtungsVektor().gibRichtungsWinkel();
            if(richtungsVZ < 0) {
                einfallswinkel = Math.PI - einfallswinkel;
            }
            double ausfallswinkel = Math.atan(-(relativerSchnittpunkt.getY() / brennweiteB) + Math.tan(einfallswinkel) * (brennweiteG / brennweiteB));
            neueRichtung = new Vektor(richtungsVZ * reflFakt, 0);
            neueRichtung.dreheUmWinkel(richtungsVZ * reflFakt * ausfallswinkel);
            inUnendlich = false;
            istVirtuell = false;
            nQuellWeite = 0;
        } else if (Math.abs(brennweiteG - gegenstandsweite) < 7 && !cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) { //Spezialfall 1 (g ist ungefähr f)
            neueRichtung = new Vektor(reflFakt * richtungsVZ * brennweiteB, -gegenstandshoehe);
            inUnendlich = true;
            istVirtuell = (brennweiteB < 0);
            nQuellWeite = 0;
        } else {
            Vektor bildPosition; //Position des Bildes relativ zum Mittelpunkt der Hauptebene
            if (cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) { //Spezialfall 2 (Strahl aus dem Unendlichen)
                double hoehe = cStrGng.getAktuellerStrahl().getRichtungsVektor().getY();
                double breite = richtungsVZ * cStrGng.getAktuellerStrahl().getRichtungsVektor().getX();
                bildPosition = new Vektor(brennweiteB, -brennweiteG * (-hoehe / breite));
                istVirtuell = (brennweiteB < 0);
                inUnendlich = false;
            } else { //Normalfall (g, G liefern direkt Werte b, B)
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

        if (istVirtuell) { //Bild ist virtuell
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -1), -nQuellWeite, inUnendlich));
        } else { //Bild ist reell
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, neueRichtung, nQuellWeite, inUnendlich));
        }
    }

    public void setHoehe(double nHoehe) {
        hauptebene.verschiebeUm(new Vektor(0, (nHoehe - hauptebene.getLaenge()) / 2));
        hauptebene.setLaenge(nHoehe);
    }

    public Gerade getHauptebene() {
        return hauptebene;
    }

    public void setHauptebene(Gerade hauptebene) {
        this.hauptebene = hauptebene;
    }

    public void setBrennweite(double nBrennweite) {
        brennweiteVor = brennweiteHinter = nBrennweite;
    }

    public double getBrennweiteHinter() {
        return brennweiteHinter;
    }

    public void setBrennweiteHinter(double brennweiteHinter) {
        this.brennweiteHinter = brennweiteHinter;
    }

    public double getBrennweiteVor() {
        return brennweiteVor;
    }

    public void setBrennweiteVor(double brennweiteVor) {
        this.brennweiteVor = brennweiteVor;
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
