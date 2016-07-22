package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Hauptebene extends Flaeche {

    Vektor mittelpunkt;

    Gerade hauptebene;

    float brennweite;

    public Hauptebene(int modus, Vektor mittelpunkt, float brennweite, float hoehe) {
        this.modus = modus;
        this.mittelpunkt = mittelpunkt.kopiere();
        this.brennweite = brennweite;
        hauptebene = new Gerade(new VektorFloat(mittelpunkt.getXfloat(), mittelpunkt.getYfloat() + hoehe / 2), new VektorFloat(mittelpunkt.getXfloat(), mittelpunkt.getYfloat() - hoehe / 2));
    }

    @Override
    public float kollisionUeberpruefen(Strahlengang cStrGng) {
        return hauptebene.gibSchnittEntfernung(cStrGng.getAktuellerStrahl());
    }

    @Override
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if(modus == Flaeche.MODUS_ABSORB) {
            cStrGng.strahlengangBeenden(position);
            return;
        }
        float richtungsvorzeichen = Math.signum(cStrGng.getAktuellerStrahl().getRichtungsVektor().getXfloat());
        float gegenstandsweite = richtungsvorzeichen * (mittelpunkt.getXfloat() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getXfloat());
        float gegenstandshoehe = richtungsvorzeichen * (mittelpunkt.getYfloat() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getYfloat());
        float bildweite = 0;
        float bildgroesse;
        Vektor neueRichtung;
        float quellLaenge;
        float cBrennweite = brennweite;
        if(modus == Flaeche.MODUS_REFLEKT) {
            if(cStrGng.getAktuellerStrahl().getRichtungsVektor().getXfloat() < 0) {
                cBrennweite *= -1;
            }
        }
        float faktor = 1;
        if (modus == Flaeche.MODUS_REFLEKT) {
            faktor = -1;
        }
        if(Math.abs((double)(cBrennweite - gegenstandsweite)) > 7) {
            if (gegenstandsweite != 0 && cBrennweite != 0) {
                bildweite = (float)(Math.pow((double) ((1 / cBrennweite) - (1 / gegenstandsweite)), -1));
            } else if (cBrennweite == 0) {
                bildweite = -gegenstandsweite;
            } else {
                return;
            }
            bildgroesse = (bildweite / gegenstandsweite) * gegenstandshoehe;
            if(modus == Flaeche.MODUS_REFLEKT) {
                bildweite *= -1;
            }
            neueRichtung = Vektor.addiere(Vektor.subtrahiere(position, mittelpunkt), new VektorFloat(-(richtungsvorzeichen * bildweite), -(richtungsvorzeichen * bildgroesse)));

            if(bildweite < 0) {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, faktor), -faktor * neueRichtung.gibLaenge()));
            } else {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -faktor), faktor * neueRichtung.gibLaenge()));
            }
        } else { //gegenstand ungefähr in brennweite

            neueRichtung = new VektorFloat(faktor * cBrennweite, gegenstandshoehe);
            if(richtungsvorzeichen > 0) {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, neueRichtung, 0));
            } else {
                cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -1), 0));
            }

        }
    }

    public Gerade getHauptebene() {
        return hauptebene;
    }

    public void setHauptebene(Gerade hauptebene) {
        this.hauptebene = hauptebene;
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
