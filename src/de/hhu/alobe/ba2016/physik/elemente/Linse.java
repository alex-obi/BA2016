package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.flaechen.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class Linse extends Bauelement implements KannKollision {

    private float hoehe;
    private float dicke;

    private Grenzflaeche linsenseite1;
    private float radius1; //Radius 0 bedeutet, dass die Grenzflaeche eine Ebene ist
    private Grenzflaeche linsenseite2;
    private float radius2;

    private Grenzflaeche obereBegrenzung;
    private Grenzflaeche untereBegrenzung;

    private double brechzahl;

    private float brennweite;

    private Hauptebene hauptebene;

    public Linse (OptischeBank optischeBank, Vektor mittelPunkt, float brennweite) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        this.brennweite = brennweite;
        radius1 = radius2 = brennweite;
        formatNeuBestimmen(1.5, Math.abs(radius1) / 2, 0, radius1, radius2);
        hauptebene = new Hauptebene(mittelPunkt, brennweite, hoehe);
    }

    public Linse (OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, float hoehe, float dicke, float radius1, float radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        this.brennweite = (float)Math.pow((brechzahl - 1) * (1 / radius1 + 1 / radius2), -1); //Keine dicke Linsen!
        this.radius1 = radius1;
        this.radius2 = radius2;
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, radius2);
        hauptebene = new Hauptebene(mittelPunkt, brennweite, hoehe);
    }

    public void formatNeuBestimmen(double brechzahl, float nHoehe, float nDicke, float r1, float r2) {
        this.brechzahl = brechzahl;

        if((nHoehe <= 2 * Math.abs(r1) || r1 == 0) && (nHoehe <= 2 * Math.abs(r2) || r2 == 0)) {
            this.hoehe = nHoehe;
        } else {
            this.hoehe = Math.min(2 * Math.abs(r1), 2 * Math.abs(r2));
        }

        float c1 = (float)Math.sqrt(r1 * r1 - (hoehe * hoehe) / 4);
        float c2 = (float)Math.sqrt(r2 * r2 - (hoehe * hoehe) / 4);

        float mindestDicke = -r1 + Math.signum(r1) * c1 - r2 + Math.signum(r2) * c2;
        if(nDicke <= mindestDicke) {
            this.dicke = mindestDicke + Konstanten.MIND_DICKE_LINSEN;
        } else {
            this.dicke = nDicke;
        }

        Vektor oben1 = Vektor.addiere(mittelPunkt, new VektorFloat(-dicke / 2, -hoehe / 2));
        Vektor oben2 = Vektor.addiere(mittelPunkt, new VektorFloat(dicke / 2, -hoehe / 2));
        Vektor unten1 = Vektor.addiere(mittelPunkt, new VektorFloat(-dicke / 2, hoehe / 2));
        Vektor unten2 = Vektor.addiere(mittelPunkt, new VektorFloat(dicke / 2, hoehe / 2));

        obereBegrenzung = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, 0, 0, oben1, oben2);
        untereBegrenzung = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, 0, 0, unten1, unten2);

        if(r1 != 0) {
            float r = Math.abs(r1);
            double phi = Math.abs(Math.asin(hoehe / (2 * r)));
            if(r1 > 0) {
                Vektor p = new VektorFloat(c1 - dicke / 2, 0);
                double vonWinkel = Math.PI - phi;
                double extWinkel = 2 * phi;
                linsenseite1 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            } else {
                Vektor p = new VektorFloat(-(c1 + dicke / 2), 0);
                double vonWinkel = Math.PI * 2 - phi;
                double extWinkel = 2 * phi;
                linsenseite1 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            }
        } else {
            linsenseite1 = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, unten1, oben1);
        }

        if(r2 != 0) {
            float r = Math.abs(r2);
            double phi = Math.asin(hoehe / (2 * r));
            if(r2 > 0) {
                Vektor p = new VektorFloat(-(c2 - dicke / 2), 0);
                double vonWinkel = Math.PI * 2 - phi;
                double extWinkel = 2 * phi;
                linsenseite2 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            } else {
                Vektor p = new VektorFloat(c2 + dicke / 2, 0);
                double vonWinkel = Math.PI - phi;
                double extWinkel = 2 * phi;
                linsenseite2 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            }
        } else {
            linsenseite2 = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, unten2, oben2);
        }

        float breite1, breite2;
        breite1 = breite2 = dicke / 2;
        if(r1 > 0) {
            breite1 += r1 - c1;
        }
        if(r2 > 0) {
        breite2 += r2 - c2;
        }

        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-breite1 - 2, -(hoehe / 2) - 2));
        rahmen.rahmenErweitern(new VektorInt(breite2 + 2, -(hoehe / 2) - 2));
        rahmen.rahmenErweitern(new VektorInt(breite2 + 2, (hoehe / 2) + 2));
        rahmen.rahmenErweitern(new VektorInt(-breite1 - 2, (hoehe / 2) + 2));
        setRahmen(rahmen);

    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                ArrayList<StrahlenKollision> kollisionen = new ArrayList();
                kollisionen.add(linsenseite1.gibKollision(cStrGng));
                kollisionen.add(linsenseite2.gibKollision(cStrGng));
                kollisionen.add(obereBegrenzung.gibKollision(cStrGng));
                kollisionen.add(untereBegrenzung.gibKollision(cStrGng));
                return StrahlenKollision.getErsteKollision(kollisionen);
            case OptischeBank.MODUS_HAUPTEBENE:
                return hauptebene.gibKollision(cStrGng);
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(Color.BLUE);
                linsenseite1.paintComponent(g);
                linsenseite2.paintComponent(g);
                g.setColor(Color.BLACK);
                obereBegrenzung.paintComponent(g);
                untereBegrenzung.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                g.setColor(Color.GRAY);
                linsenseite1.paintComponent(g);
                linsenseite2.paintComponent(g);
                obereBegrenzung.paintComponent(g);
                untereBegrenzung.paintComponent(g);
                g.setColor(Color.BLACK);
                hauptebene.paintComponent(g);
                break;
        }
    }

    @Override
    public void waehleAus() {

    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, radius2);
        hauptebene.getHauptebene().getBasisVektor().addiere(verschiebung);
    }

    public float getRadius1() {
        return radius1;
    }

    public float getRadius2() {
        return radius2;
    }

    public float getHoehe() {
        return hoehe;
    }

    public float getDicke() {
        return dicke;
    }

    public void neueHoehe(float nHoehe) {
        formatNeuBestimmen(brechzahl, nHoehe, dicke, radius1, radius2);
    }

    public void neueDicke(float nDicke) {
        formatNeuBestimmen(brechzahl, hoehe, nDicke, radius1, radius2);
    }

    public void neuerRadius1(float nRadius1) {
        formatNeuBestimmen(brechzahl, hoehe, dicke, nRadius1,radius2);
    }

    public void neuerRadius2(float nRadius2) {
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, nRadius2);
    }

    public double getBrechzahl() {
        return brechzahl;
    }

    public void setBrechzahl(double nBrechzahl) {
        formatNeuBestimmen(nBrechzahl, hoehe, dicke, radius1, radius2);
    }

}
