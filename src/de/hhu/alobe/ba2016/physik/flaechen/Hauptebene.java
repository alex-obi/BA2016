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

    public Hauptebene(Vektor mittelpunkt, float brennweite, float hoehe) {
        this.mittelpunkt = mittelpunkt;
        this.brennweite = brennweite;
        hauptebene = new Gerade(new VektorFloat(mittelpunkt.getXfloat(), mittelpunkt.getYfloat() + hoehe / 2), new VektorFloat(mittelpunkt.getXfloat(), mittelpunkt.getYfloat() - hoehe / 2));
    }

    @Override
    public float kollisionUeberpruefen(Strahlengang cStrGng) {
        return hauptebene.gibSchnittEntfernung(cStrGng.getAktuellerStrahl());
    }

    @Override
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        float richtungsvorzeichen = Math.signum(cStrGng.getAktuellerStrahl().getRichtungsVektor().getXfloat());
        float gegenstandsweite = richtungsvorzeichen * (mittelpunkt.getXfloat() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getXfloat());
        float gegenstandshoehe = richtungsvorzeichen * (mittelpunkt.getYfloat() - cStrGng.getAktuellerStrahl().gibQuellPunkt().getYfloat());
        float bildweite;
        float bildgroesse;
        Vektor neueRichtung;
        float quellLaenge;
        if(Math.abs((double)(brennweite - gegenstandsweite)) > 7) {
            if (gegenstandsweite != 0 && brennweite != 0) {
                bildweite = (float)(Math.pow((double) ((1 / brennweite) - (1 / gegenstandsweite)), -1));
            } else {
                return;
            }
            bildgroesse = (bildweite / gegenstandsweite) * gegenstandshoehe;
            neueRichtung = Vektor.addiere(Vektor.subtrahiere(position, mittelpunkt), new VektorFloat(-(richtungsvorzeichen * bildweite), -(richtungsvorzeichen * bildgroesse)));
            float relHoehe = position.getYfloat() - mittelpunkt.getYfloat();
            quellLaenge = (float)Math.sqrt(bildweite * bildweite + (bildgroesse - relHoehe) * (bildgroesse - relHoehe));
        } else { //gegenstand ungefähr in brennweite
            neueRichtung = new VektorFloat(brennweite, gegenstandshoehe);
            quellLaenge = 0;
        }

        if(neueRichtung.getXfloat() > 0 ^ cStrGng.getAktuellerStrahl().getRichtungsVektor().getXfloat() < 0) {
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, neueRichtung, -quellLaenge));
        } else {
            cStrGng.neuenStrahlAnhaengen(new Strahl(position, Vektor.multipliziere(neueRichtung, -1), quellLaenge));
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
        mittelpunkt.addiere(verschiebung);
        hauptebene.verschiebeUm(verschiebung);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        hauptebene.paintComponent(g);
    }
}
