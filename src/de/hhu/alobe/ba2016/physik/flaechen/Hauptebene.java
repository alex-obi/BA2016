package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Hauptebene extends Flaeche {

    Vektor mittelpunkt;

    Gerade hauptebene;

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
    public double kollisionUeberpruefen(Strahlengang cStrGng) {
        return hauptebene.gibSchnittEntfernung(cStrGng.getAktuellerStrahl());
    }

    @Override
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if(modus == Flaeche.MODUS_ABSORB) {
            cStrGng.strahlengangBeenden(position);
            return;
        }
        if(cStrGng.getAktuellerStrahl().getRichtungsVektor().getX() == 0) return; //Abbrechen, wenn Strahl parallel zur Hauptebene eintrifft

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
        double nQuellWeite;

        if (Math.abs(brennweiteG - gegenstandsweite) < 7){ //Spezialfall 1 (g ist ungefähr f)
            neueRichtung = new Vektor(reflFakt * richtungsVZ * brennweiteB, -gegenstandshoehe);
            inUnendlich = true;
            istVirtuell = (brennweiteB < 0);
            nQuellWeite = 0;
        } else {
            Vektor bildPosition; //Position des Bildes relativ zum Mittelpunkt der Hauptebene
            if (cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) { //Spezialfall 2 (g, G -> unendlich)
                double hoehe = -richtungsVZ * cStrGng.getAktuellerStrahl().getRichtungsVektor().getY();
                double breite = cStrGng.getAktuellerStrahl().getRichtungsVektor().getX();
                bildPosition = new Vektor(brennweiteB, -brennweiteG * (hoehe / breite));
                istVirtuell = (brennweiteB < 0);
            } else { //Normalfall
                double bildweite;
                if (gegenstandsweite != 0 && brennweiteB != 0 && brennweiteG != 0) {
                    bildweite = (brennweiteB * gegenstandsweite) / (gegenstandsweite - brennweiteG);
                } else if (brennweiteB == 0) { //Strahl wird unverändert durchgelassen
                    return; //todo: Spezialfall behandeln
                } else if (brennweiteG == 0) {
                    return; //todo: Spezialfall behandeln
                } else {
                    return; //todo: Spezialfall behandeln
                }

                double bildgroesse = (gegenstandshoehe * brennweiteG) / (gegenstandsweite - brennweiteG);

                bildPosition = new Vektor(bildweite, -bildgroesse);
                istVirtuell = (bildweite < 0);
            }
            bildPosition.setX(richtungsVZ * reflFakt * bildPosition.getX());
            bildPosition.subtrahiere(relativerSchnittpunkt);
            neueRichtung = bildPosition;
            inUnendlich = false;
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
