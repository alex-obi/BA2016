package de.hhu.alobe.ba2016.physik.elemente.glasskoerper;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
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
    public static final int MIND_HOEHE = 100;
    public static final int MAX_HOEHE = 600;

    private float dicke;

    private Grenzflaeche linsenseite1;
    private float radius1; //Radius 0 bedeutet, dass die Grenzflaeche eine Ebene ist
    private int breite1; //Abstand bezüglich Mittelpunkt von Radius1
    private Grenzflaeche linsenseite2;
    private float radius2;
    private int breite2;  //Abstand bezüglich Mittelpunkt von Radius2

    public static final float MIND_RADIUS = 50;
    public static final float MAX_RADIUS = 100000; //Maximaler Radius bis Kreis als ebene Fläche approximiert wird

    private Grenzflaeche obereBegrenzung;
    private Grenzflaeche untereBegrenzung;

    private double brechzahl;
    public static final double MIND_BRECHZAHL = 1;
    public static final double MAX_BRECHZAHL = 4;

    private float brennweite;
    public static final float MIND_BRENNWEITE = -500;
    public static final float MAX_BRENNWEITE = 500;

    private Hauptebene hauptebene;

    public Linse (OptischeBank optischeBank, Vektor mittelPunkt, float brennweite) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        this.hoehe = Math.abs(radius1 / 2);
        setBrennweite(brennweite);
    }

    public Linse (OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, float hoehe, float dicke, float radius1, float radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, radius2);
    }

    public void formatNeuBestimmen(double brechzahl, float nHoehe, float nDicke, float r1, float r2) {
        this.radius1 = r1;
        this.radius2 = r2;
        this.brechzahl = brechzahl;

        if((nHoehe <= 2 * Math.abs(r1) || r1 == 0) && (nHoehe <= 2 * Math.abs(r2) || r2 == 0)) {
            this.hoehe = nHoehe;
        } else {
            this.hoehe = Math.min(2 * Math.abs(r1), 2 * Math.abs(r2));
        }

        if(radius1 == -radius2) {
            brennweite = 0;
        } else {
            this.brennweite = (float) Math.pow((brechzahl - 1) * (1 / radius1 + 1 / radius2), -1); //Keine dicke Linsen!
        }
        hauptebene = new Hauptebene(Flaeche.MODUS_BRECHUNG, mittelPunkt, brennweite, Math.max(hoehe, Konstanten.HAUPTEBENE_MINDESTHOEHE));

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

        if(r1 != 0 && r1 < MAX_RADIUS) {
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

        if(r2 != 0 && r2 < MAX_RADIUS) {
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

        breite1 = breite2 = (int)(dicke / 2);
        if(r1 > 0) {
            breite1 += r1 - c1;
        }
        if(r2 > 0) {
        breite2 += r2 - c2;
        }

        setRahmen(generiereRahmen());

    }

    public void setHoehe(int nHoehe) {
        formatNeuBestimmen(brechzahl, nHoehe, dicke, radius1, radius2);
    }

    public void setRadius1(float nRadius1) {
        formatNeuBestimmen(brechzahl, hoehe, dicke, nRadius1, radius2);
    }

    public void setRadius2(float nRadius2) {
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, nRadius2);
    }

    public void setBrechzahl(double nBrechzahl) {
        formatNeuBestimmen(nBrechzahl, hoehe, dicke, radius1, radius2);
    }

    //Min Dicke: Formatänderung unter Einhaltung einer möglichst geringen Dicke

    public void setHoehe_minDicke(int nHoehe) {
        formatNeuBestimmen(brechzahl, nHoehe, 0, radius1, radius2);
    }

    public void setRadius1_minDicke(float nRadius1) {
        formatNeuBestimmen(brechzahl, hoehe, 0, nRadius1, radius2);
    }

    public void setRadius2_minDicke(float nRadius2) {
        formatNeuBestimmen(brechzahl, hoehe, 0, radius1, nRadius2);
    }

    public void setBrechzahl_minDicke(double nBrechzahl) {
        formatNeuBestimmen(nBrechzahl, hoehe, 0, radius1, radius2);
    }

    public void setDicke(float nDicke) {
        formatNeuBestimmen(brechzahl, hoehe, nDicke, radius1, radius2);
    }

    public void setBrennweite(float nBrennweite) {
        radius1 = radius2 = nBrennweite;
        formatNeuBestimmen(1.5, hoehe, 0, radius1, radius2);
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
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.drawLine(mittelPunkt.getXint() + (int)brennweite, mittelPunkt.getYint() + 5, mittelPunkt.getXint() + (int)brennweite, mittelPunkt.getYint() - 5);
        g.drawLine(mittelPunkt.getXint() - (int)brennweite, mittelPunkt.getYint() + 5, mittelPunkt.getXint() - (int)brennweite, mittelPunkt.getYint() - 5);
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
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        if(hoehe < MIND_HOEHE) hoehe = MIND_HOEHE;
        JSlider slide_hoehe = new JSlider (MIND_HOEHE, MAX_HOEHE, (int)hoehe);
        slide_hoehe.setPaintTicks(true);
        slide_hoehe.setMajorTickSpacing(20);
        slide_hoehe.addChangeListener(e -> {
            setHoehe_minDicke( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Höhe", slide_hoehe));

        if(optischeBank.getModus() == OptischeBank.MODUS_HAUPTEBENE) {

            JSlider slide_brennweite = new JSlider((int)MIND_BRENNWEITE, (int)MAX_BRENNWEITE, (int)brennweite);
            slide_brennweite.setPaintTicks(true);
            slide_brennweite.setMajorTickSpacing(20);
            slide_brennweite.addChangeListener(e -> {
                setBrennweite((float) ((JSlider) e.getSource()).getValue());
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Brennweite", slide_brennweite));

        } else {

            JSlider slide_brechzahl = new JSlider((int) (MIND_BRECHZAHL * 100), (int) (MAX_BRECHZAHL * 100), (int) (brechzahl * 100));
            slide_brechzahl.setPaintTicks(true);
            slide_brechzahl.setMajorTickSpacing(20);
            slide_brechzahl.addChangeListener(e -> {
                setBrechzahl_minDicke((double) ((JSlider) e.getSource()).getValue() / 100);
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Brechzahl", slide_brechzahl));

            JSlider slide_radius1 = new JSlider(-(int) (10000 / MIND_RADIUS), (int) (10000 / MIND_RADIUS), (int) (10000 / radius1));
            slide_radius1.setPaintTicks(true);
            slide_radius1.setMajorTickSpacing(20);
            slide_radius1.addChangeListener(e -> {
                setRadius1_minDicke(10000 / ((float) ((JSlider) e.getSource()).getValue()));
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Radius 1", slide_radius1));

            JSlider slide_radius2 = new JSlider(-(int) (10000 / MIND_RADIUS), (int) (10000 / MIND_RADIUS), (int) (10000 / radius2));
            slide_radius2.setPaintTicks(true);
            slide_radius2.setMajorTickSpacing(20);
            slide_radius2.addChangeListener(e -> {
                setRadius2_minDicke(10000 / ((float) ((JSlider) e.getSource()).getValue()));
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Radius 2", slide_radius2));
        }


        optischeBank.getEigenschaften().setOptionen("Hohlspiegel", regler);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        formatNeuBestimmen(brechzahl, hoehe, dicke, radius1, radius2);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-breite1 - 2, -(hoehe / 2) - 2));
        rahmen.rahmenErweitern(new VektorInt(breite2 + 2, -(hoehe / 2) - 2));
        rahmen.rahmenErweitern(new VektorInt(breite2 + 2, (hoehe / 2) + 2));
        rahmen.rahmenErweitern(new VektorInt(-breite1 - 2, (hoehe / 2) + 2));
        return rahmen;
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

    public double getBrechzahl() {
        return brechzahl;
    }

}
