package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Hauptebene extends Flaeche {

    Vektor mittelpunkt;

    Gerade hauptebene;

    double brennweite;

    public Hauptebene(int modus, Vektor mittelpunkt, double brennweite, double hoehe) {
        this.modus = modus;
        this.mittelpunkt = (Vektor) mittelpunkt.kopiere();
        this.brennweite = brennweite;
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
        double richtungsvorzeichen = Math.signum(cStrGng.getAktuellerStrahl().getRichtungsVektor().getX());
        double gegenstandsweite = richtungsvorzeichen * (mittelpunkt.getX() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getX());
        double gegenstandshoehe = richtungsvorzeichen * (mittelpunkt.getY() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getY());
        double bildweite = 0;
        double bildgroesse;
        Vektor neueRichtung;
        double quellLaenge;
        double cBrennweite = brennweite;
        if(modus == Flaeche.MODUS_REFLEKT) {
            if(cStrGng.getAktuellerStrahl().getRichtungsVektor().getX() < 0) {
                cBrennweite *= -1;
            }
        }
        double faktor = 1;
        if (modus == Flaeche.MODUS_REFLEKT) {
            faktor = -1;
        }
        if(Math.abs(cBrennweite - gegenstandsweite) > 7) {
            if (gegenstandsweite != 0 && cBrennweite != 0) {
                bildweite = (Math.pow((1 / cBrennweite) - (1 / gegenstandsweite), -1));
            } else if (cBrennweite == 0) {
                bildweite = -gegenstandsweite;
            } else {
                return;
            }
            bildgroesse = (bildweite / gegenstandsweite) * gegenstandshoehe;
            if(modus == Flaeche.MODUS_REFLEKT) {
                bildweite *= -1;
            }
            Vektor relativerSchnittpunkt = Vektor.subtrahiere(position, mittelpunkt);
            neueRichtung = Vektor.addiere(relativerSchnittpunkt, new Vektor(-(richtungsvorzeichen * bildweite), -(richtungsvorzeichen * bildgroesse)));
            if(cStrGng.getAktuellerStrahl().isAusDemUnendlichen()) {
                neueRichtung.multipliziere(Math.abs(brennweite / bildweite));
                //todo: position offset einbingen was wenn x- == bildweite ?
            }
            if(bildweite < 0) {
                if(!cStrGng.getAktuellerStrahl().isAusDemUnendlichen() || brennweite < 0) {
                    cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, faktor), -faktor * neueRichtung.gibLaenge(), false));
                } else {
                    cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, faktor), faktor * neueRichtung.gibLaenge(), false));
                }
            } else {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -faktor), faktor * neueRichtung.gibLaenge(), false));
            }
        } else { //gegenstand ungefähr in brennweite
            neueRichtung = new Vektor(faktor * cBrennweite, gegenstandshoehe);
            if(richtungsvorzeichen > 0) {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, neueRichtung, 0, true));
            } else {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -1), 0, true));
            }

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
        brennweite = nBrennweite;
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
