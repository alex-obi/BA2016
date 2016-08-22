package de.hhu.alobe.ba2016.physik.elemente.glasskoerper;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.flaechen.*;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Linse extends Bauelement implements KannKollision {

    public static final String XML_LINSE = "linse";
    private double hoeheLinse;
    public static final String XML_HOEHE_LINSE = "hoeheLinse";
    public static final int MIND_HOEHE_LINSE = 100;
    public static final int MAX_HOEHE_LINSE = 500;

    private double dicke;
    public static final String XML_DICKE = "dicke";

    private Grenzflaeche linsenseite1;
    private double radius1; //Radius 0 bedeutet, dass die Grenzflaeche eine Ebene ist
    public static final String XML_RADIUS1 = "radius1";
    private double breite1; //Abstand bezüglich Mittelpunkt von Radius1

    private Grenzflaeche linsenseite2;
    private double radius2;
    public static final String XML_RADIUS2 = "radius2";
    private double breite2;  //Abstand bezüglich Mittelpunkt von Radius2

    public static final int MIND_RADIUS = 50;
    public static final int MAX_RADIUS = 100000; //Maximaler Radius bis Kreis als ebene Fläche approximiert wird

    private Grenzflaeche obereBegrenzung;
    private Grenzflaeche untereBegrenzung;

    private double brechzahl;
    public static final String XML_BRECHZAHL = "brechzahl";
    public static final double MIND_BRECHZAHL = 1;
    public static final double MAX_BRECHZAHL = 4;

    private double brennweite;
    public static final int MIND_BRENNWEITE = -500;
    public static final int MAX_BRENNWEITE = 500;

    private Hauptebene hauptebene;
    private double hoeheHauptebene;
    public static final String XML_HOEHE_HAUPTEBENE = "hoeheHauptebene";
    public static final int MIND_HOEHE_HAUPTEBENE = 100;
    public static final int MAX_HOEHE_HAUPTEBENE = 500;

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brennweite) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        this.hoeheHauptebene = Math.max(MIND_HOEHE_HAUPTEBENE, Math.min(Math.abs(brennweite), MAX_HOEHE_HAUPTEBENE));
        setBrennweite(brennweite);
    }

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, double hoehe, double dicke, double radius1, double radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        formatNeuBestimmen(brechzahl, Math.min(hoehe, MIND_HOEHE_HAUPTEBENE), hoehe, dicke, radius1, radius2);
    }

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, double hoeheHauptebene, double hoeheLinse, double dicke, double radius1, double radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    public Linse(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_LINSE);
        brechzahl = xmlElement.getChild(XML_BRECHZAHL).getAttribute("wert").getDoubleValue();
        hoeheHauptebene = xmlElement.getChild(XML_HOEHE_HAUPTEBENE).getAttribute("wert").getDoubleValue();
        hoeheLinse = xmlElement.getChild(XML_HOEHE_LINSE).getAttribute("wert").getDoubleValue();
        dicke = xmlElement.getChild(XML_DICKE).getAttribute("wert").getDoubleValue();
        radius1 = xmlElement.getChild(XML_RADIUS1).getAttribute("wert").getDoubleValue();
        radius2 = xmlElement.getChild(XML_RADIUS2).getAttribute("wert").getDoubleValue();
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    public void formatNeuBestimmen(double brechzahl, double nMindHoeheHauptebene, double nMaxHoeheLinse, double nDicke, double r1, double r2) {
        this.radius1 = r1;
        this.radius2 = r2;
        this.brechzahl = brechzahl;

        if((nMaxHoeheLinse <= 2 * Math.abs(r1) || r1 == 0) && (nMaxHoeheLinse <= 2 * Math.abs(r2) || r2 == 0)) {
            this.hoeheLinse = nMaxHoeheLinse;
        } else {
            this.hoeheLinse = Math.min(2 * Math.abs(r1), 2 * Math.abs(r2));
        }

        if(radius1 == -radius2) {
            brennweite = 0; //Brennweite -> unendlich
        } else {
            this.brennweite = berechneBrennweite(brechzahl, radius1, radius2); //Keine dicke Linsen!
        }

        hoeheHauptebene = Math.max(hoeheLinse, Math.max(nMindHoeheHauptebene, Konstanten.HAUPTEBENE_MINDESTHOEHE));
        hauptebene = new Hauptebene(Flaeche.MODUS_BRECHUNG, mittelPunkt, brennweite, hoeheHauptebene);

        double c1 = Math.sqrt(r1 * r1 - (hoeheLinse * hoeheLinse) / 4);
        double c2 = Math.sqrt(r2 * r2 - (hoeheLinse * hoeheLinse) / 4);

        double mindestDicke = -r1 + Math.signum(r1) * c1 - r2 + Math.signum(r2) * c2;
        if(nDicke <= mindestDicke) {
            this.dicke = mindestDicke + Konstanten.MIND_DICKE_LINSEN;
        } else {
            this.dicke = nDicke;
        }

        Vektor oben1 = Vektor.addiere(mittelPunkt, new Vektor(-dicke / 2, -hoeheLinse / 2));
        Vektor oben2 = Vektor.addiere(mittelPunkt, new Vektor(dicke / 2, -hoeheLinse / 2));
        Vektor unten1 = Vektor.addiere(mittelPunkt, new Vektor(-dicke / 2, hoeheLinse / 2));
        Vektor unten2 = Vektor.addiere(mittelPunkt, new Vektor(dicke / 2, hoeheLinse / 2));

        obereBegrenzung = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, 0, 0, oben1, oben2);
        untereBegrenzung = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, 0, 0, unten1, unten2);

        if(r1 != 0 && r1 < MAX_RADIUS) {
            double r = Math.abs(r1);
            double phi = Math.abs(Math.asin(hoeheLinse / (2 * r)));
            if(r1 > 0) {
                Vektor p = new Vektor(c1 - dicke / 2, 0);
                double vonWinkel = Math.PI - phi;
                double extWinkel = 2 * phi;
                linsenseite1 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            } else {
                Vektor p = new Vektor(-(c1 + dicke / 2), 0);
                double vonWinkel = Math.PI * 2 - phi;
                double extWinkel = 2 * phi;
                linsenseite1 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            }
        } else {
            linsenseite1 = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, unten1, oben1);
        }

        if(r2 != 0 && r2 < MAX_RADIUS) {
            double r = Math.abs(r2);
            double phi = Math.asin(hoeheLinse / (2 * r));
            if(r2 > 0) {
                Vektor p = new Vektor(-(c2 - dicke / 2), 0);
                double vonWinkel = Math.PI * 2 - phi;
                double extWinkel = 2 * phi;
                linsenseite2 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            } else {
                Vektor p = new Vektor(c2 + dicke / 2, 0);
                double vonWinkel = Math.PI - phi;
                double extWinkel = 2 * phi;
                linsenseite2 = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_BRECHUNG, brechzahl, 1, Vektor.addiere(mittelPunkt, p), r, vonWinkel, extWinkel);
            }
        } else {
            linsenseite2 = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_BRECHUNG, 1, brechzahl, unten2, oben2);
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

    //r = 0 -> r gegen uendlich
    public static double berechneBrennweite(double brechzahl, double radius1, double radius2) {
        if(radius1 == 0) {
            return Math.pow((brechzahl - 1) / radius2, -1);
        }
        if(radius2 == 0) {
            return Math.pow((brechzahl - 1)  / radius1, -1);
        } else {
            return Math.pow((brechzahl - 1) * (1 / radius1 + 1 / radius2), -1);
        }
    }

    public void setMaxLinsenHoehe(int nHoehe) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, nHoehe, dicke, radius1, radius2);
    }

    public void setRadius1(double nRadius1) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, dicke, nRadius1, radius2);
    }

    public void setRadius2(double nRadius2) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, nRadius2);
    }

    public void setBrechzahl(double nBrechzahl) {
        formatNeuBestimmen(nBrechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    //Min Dicke: Formatänderung unter Einhaltung einer möglichst geringen Dicke

    public void setMaxLinsenHoeheHoehe_minDicke(int nHoehe) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, nHoehe, 0, radius1, radius2);
    }

    public void setRadius1_minDicke(double nRadius1) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, 0, nRadius1, radius2);
    }

    public void setRadius2_minDicke(double nRadius2) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, 0, radius1, nRadius2);
    }

    public void setBrechzahl_minDicke(double nBrechzahl) {
        formatNeuBestimmen(nBrechzahl, hoeheHauptebene, hoeheLinse, 0, radius1, radius2);
    }

    public void setDicke(double nDicke) {
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, nDicke, radius1, radius2);
    }

    public void setMindHauptebeneHoeheHoehe_minDicke(int nHoehe) {
        double maxHoeheLinseNeu = Math.min(nHoehe, Math.min(Math.abs(radius1), Math.abs(radius2)));
        formatNeuBestimmen(brechzahl, nHoehe, maxHoeheLinseNeu, 0, radius1, radius2);
    }

    public void setBrennweite(double nBrennweite) {
        radius1 = radius2 = 3 * nBrennweite;
        double maxHoeheLinseNeu = Math.min(hoeheHauptebene, Math.abs(radius1));
        formatNeuBestimmen(2.5, hoeheHauptebene, maxHoeheLinseNeu, 0, radius1, radius2);
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

        //Brennpunkt zeichnen:
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(new Line2D.Double(mittelPunkt.getX() + brennweite, mittelPunkt.getY() + 5, mittelPunkt.getX() + brennweite, mittelPunkt.getY() - 5));
        g.draw(new Line2D.Double(mittelPunkt.getX() - brennweite, mittelPunkt.getY() + 5, mittelPunkt.getX() - brennweite, mittelPunkt.getY() - 5));

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

        if(optischeBank.getModus() == OptischeBank.MODUS_HAUPTEBENE) {

            JSlider slide_hoehe = new JSlider (MIND_HOEHE_HAUPTEBENE, MAX_HOEHE_HAUPTEBENE, (int)Math.max(MIND_HOEHE_HAUPTEBENE, Math.min(hoeheHauptebene, MAX_HOEHE_HAUPTEBENE)));
            slide_hoehe.setPaintTicks(true);
            slide_hoehe.setMajorTickSpacing(20);
            slide_hoehe.addChangeListener(e -> {
                setMindHauptebeneHoeheHoehe_minDicke( ((JSlider) e.getSource()).getValue());
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Höhe Hauptebene", slide_hoehe));

            JSlider slide_brennweite = new JSlider(MIND_BRENNWEITE, MAX_BRENNWEITE, (int)Math.max(MIND_BRENNWEITE, Math.min(brennweite, MAX_BRENNWEITE)));
            slide_brennweite.setPaintTicks(true);
            slide_brennweite.setMajorTickSpacing(20);
            slide_brennweite.addChangeListener(e -> {
                setBrennweite( ((JSlider) e.getSource()).getValue());
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Brennweite", slide_brennweite));

        } else {

            JSlider slide_hoehe = new JSlider (MIND_HOEHE_LINSE, MAX_HOEHE_LINSE, (int)Math.max(MIND_HOEHE_LINSE, Math.min(hoeheLinse, MAX_HOEHE_LINSE)));
            slide_hoehe.setPaintTicks(true);
            slide_hoehe.setMajorTickSpacing(20);
            slide_hoehe.addChangeListener(e -> {
                setMaxLinsenHoeheHoehe_minDicke( ((JSlider) e.getSource()).getValue());
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Höhe Linse", slide_hoehe));

            JSlider slide_brechzahl = new JSlider((int)(MIND_BRECHZAHL * 100), (int)(MAX_BRECHZAHL * 100), (int)(brechzahl * 100));
            slide_brechzahl.setPaintTicks(true);
            slide_brechzahl.setMajorTickSpacing(20);
            slide_brechzahl.addChangeListener(e -> {
                setBrechzahl_minDicke((double)((JSlider) e.getSource()).getValue() / 100);
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Brechzahl", slide_brechzahl));

            int startwert1;
            if(radius1 == 0) {
                startwert1 = 0;
            } else {
                startwert1 = (int)(10000 / Math.max(MIND_RADIUS, radius1));
            }
            JSlider slide_radius1 = new JSlider(-10000 / MIND_RADIUS, 10000 / MIND_RADIUS, startwert1);
            slide_radius1.setPaintTicks(true);
            slide_radius1.setMajorTickSpacing(20);
            slide_radius1.addChangeListener(e -> {
                int wert = ((JSlider) e.getSource()).getValue();
                if(wert != 0) {
                    setRadius1_minDicke(10000 / wert);
                } else {
                    setRadius1_minDicke(0);
                }
                slide_hoehe.setValue((int)Math.max(MIND_HOEHE_LINSE, Math.min(hoeheLinse, MAX_HOEHE_LINSE)));
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Radius 1", slide_radius1));

            int startwert2;
            if(radius2 == 0) {
                startwert2 = 0;
            } else {
                startwert2 = (int)(10000 / Math.max(MIND_RADIUS, radius2));
            }
            JSlider slide_radius2 = new JSlider(-10000 / MIND_RADIUS, 10000 / MIND_RADIUS, startwert2);
            slide_radius2.setPaintTicks(true);
            slide_radius2.setMajorTickSpacing(20);
            slide_radius2.addChangeListener(e -> {
                int wert = ((JSlider) e.getSource()).getValue();
                if(wert != 0) {
                    setRadius2_minDicke(10000 / wert);
                } else {
                    setRadius2_minDicke(0);
                }
                slide_hoehe.setValue((int)Math.max(MIND_HOEHE_LINSE, Math.min(hoeheLinse, MAX_HOEHE_LINSE)));
                optischeBank.aktualisieren();
            });
            regler.add(new Eigenschaftenregler("Radius 2", slide_radius2));
        }


        optischeBank.getEigenschaften().setOptionen("Linse", regler);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        formatNeuBestimmen(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-breite1 - 2, -(hoeheHauptebene / 2) - 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite2 + 2, -(hoeheHauptebene / 2) - 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite2 + 2, (hoeheHauptebene / 2) + 2));
        rahmen.rahmenErweitern(new Point2D.Double(-breite1 - 2, (hoeheHauptebene / 2) + 2));
        return rahmen;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.addContent(new Element(XML_BRECHZAHL).setAttribute("wert", String.valueOf(brechzahl)));
        xmlElement.addContent(new Element(XML_DICKE).setAttribute("wert", String.valueOf(dicke)));
        xmlElement.addContent(new Element(XML_HOEHE_HAUPTEBENE).setAttribute("wert", String.valueOf(hoeheHauptebene)));
        xmlElement.addContent(new Element(XML_HOEHE_LINSE).setAttribute("wert", String.valueOf(hoeheLinse)));
        xmlElement.addContent(new Element(XML_RADIUS1).setAttribute("wert", String.valueOf(radius1)));
        xmlElement.addContent(new Element(XML_RADIUS2).setAttribute("wert", String.valueOf(radius2)));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_LINSE;
    }

    public double getRadius1() {
        return radius1;
    }

    public double getRadius2() {
        return radius2;
    }

    public double getHoeheLinse() {
        return hoeheLinse;
    }

    public double getHoeheHauptebene() {
        return hoeheHauptebene;
    }

    public double getDicke() {
        return dicke;
    }

    public double getBrechzahl() {
        return brechzahl;
    }

}
