package de.hhu.alobe.ba2016.physik.elemente.glasskoerper;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
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

    public static final String NAME = "Linse";
    public static final String XML_LINSE = "linse";

    private double hoeheLinse;
    public static final String XML_HOEHE_LINSE = "hoeheLinse";
    public static final int MIND_HOEHE_LINSE = 100;
    public static final int MAX_HOEHE_LINSE = 500;

    private double dicke;
    public static final String XML_DICKE = "dicke";

    protected Grenzflaeche linsenseite1;
    private double radius1; //Radius 0 bedeutet, dass die Grenzflaeche eine Ebene ist
    public static final String XML_RADIUS1 = "radius1";
    private double breite1; //Abstand bezüglich Mittelpunkt von Radius1

    protected Grenzflaeche linsenseite2;
    private double radius2;
    public static final String XML_RADIUS2 = "radius2";
    private double breite2;  //Abstand bezüglich Mittelpunkt von Radius2

    public static final double MIND_RADIUS = 50;
    public static final double MAX_RADIUS = 1000;

    protected Grenzflaeche obereBegrenzung;
    protected Grenzflaeche untereBegrenzung;

    private double brechzahl;
    public static final String XML_BRECHZAHL = "brechzahl";
    public static final double MIND_BRECHZAHL = 1;
    public static final double MAX_BRECHZAHL = 4;

    private double brennweite;
    public static final double MIND_BRENNWEITE = 25;
    public static final double MAX_BRENNWEITE = 2000;

    protected Hauptebene hauptebene;
    private double hoeheHauptebene;
    public static final String XML_HOEHE_HAUPTEBENE = "hoeheHauptebene";
    public static final int MIND_HOEHE_HAUPTEBENE = 80;
    public static final int MAX_HOEHE_HAUPTEBENE = 500;

    private Eigenschaftenregler_Slider slide_hoeheHauptebene;
    private Eigenschaftenregler_Slider slide_brennweite;

    private Eigenschaftenregler_Slider slide_hoeheLinse;
    private Eigenschaftenregler_Slider slide_brechzahl;
    private Eigenschaftenregler_Slider slide_radius1;
    private Eigenschaftenregler_Slider slide_radius2;

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brennweite) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        radius1 = radius2 = 3 * brennweite;
        this.hoeheHauptebene = Math.max(MIND_HOEHE_HAUPTEBENE, Math.min(Math.abs(brennweite), MAX_HOEHE_HAUPTEBENE));
        double maxHoeheLinseNeu = Math.min(hoeheHauptebene, Math.abs(radius1));
        initialisieren(2.5, hoeheHauptebene, maxHoeheLinseNeu, 0, radius1, radius2);;
    }

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, double hoehe, double dicke, double radius1, double radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        initialisieren(brechzahl, Math.min(hoehe, MIND_HOEHE_HAUPTEBENE), hoehe, dicke, radius1, radius2);
    }

    public Linse(OptischeBank optischeBank, Vektor mittelPunkt, double brechzahl, double hoeheHauptebene, double hoeheLinse, double dicke, double radius1, double radius2) {
        super(optischeBank, mittelPunkt, TYP_LINSE);
        initialisieren(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    public Linse(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_LINSE);
        brechzahl = xmlElement.getAttribute(XML_BRECHZAHL).getDoubleValue();
        hoeheHauptebene = xmlElement.getAttribute(XML_HOEHE_HAUPTEBENE).getDoubleValue();
        hoeheLinse = xmlElement.getAttribute(XML_HOEHE_LINSE).getDoubleValue();
        dicke = xmlElement.getAttribute(XML_DICKE).getDoubleValue();
        radius1 = xmlElement.getAttribute(XML_RADIUS1).getDoubleValue();
        radius2 = xmlElement.getAttribute(XML_RADIUS2).getDoubleValue();
        initialisieren(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    private void initialisieren(double nBrechzahl, double nMindHoeheHauptebene, double nMaxHoeheLinse, double nDicke, double nR1, double nR2) {
        formatAktualisieren(nBrechzahl, nMindHoeheHauptebene, nMaxHoeheLinse, nDicke, nR1, nR2);

        slide_hoeheHauptebene = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoeheHauptebene, MIND_HOEHE_HAUPTEBENE, MAX_HOEHE_HAUPTEBENE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setMindHauptebeneHoeheHoehe_minDicke(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.linearZuProzent(hoeheHauptebene, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.laengeZuCm(zahl);
            }
        });

        slide_brennweite = new Eigenschaftenregler_Slider("Brennweite", "cm", 500, brennweite, MIND_BRENNWEITE, MAX_BRENNWEITE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setBrennweite(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuBrennweite(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.brennweiteZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.laengeZuCm(zahl);
            }
        });

        slide_hoeheLinse = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoeheLinse, MIND_HOEHE_LINSE, MAX_HOEHE_LINSE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setMaxLinsenHoeheHoehe_minDicke(wert);
                slide_hoeheLinse.setWert(hoeheLinse);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.linearZuProzent(hoeheLinse, MIND_HOEHE_LINSE, MAX_HOEHE_LINSE);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.laengeZuCm(zahl);
            }
        });

        slide_brechzahl = new Eigenschaftenregler_Slider("Brechzahl", "", 100, brechzahl, MIND_BRECHZAHL, MAX_BRECHZAHL, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setBrechzahl_minDicke(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.linearZuProzent(brechzahl, MIND_BRECHZAHL, MAX_BRECHZAHL);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.zahlZuBrechzahl(zahl);
            }
        });

        slide_radius1 = new Eigenschaftenregler_Slider("Radius 1", "cm", 100, radius1, MIND_RADIUS, MAX_RADIUS, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setRadius1_minDicke(wert);
                slide_hoeheLinse.setWert(hoeheLinse);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuRadius(reglerProzent, minimum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.radiusZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.radiusZuCm(zahl);
            }
        });

        slide_radius2 = new Eigenschaftenregler_Slider("Radius 2", "cm", 100, radius2, MIND_RADIUS, MAX_RADIUS, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setRadius2_minDicke(wert);
                slide_hoeheLinse.setWert(hoeheLinse);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuRadius(reglerProzent, minimum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.radiusZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.radiusZuCm(zahl);
            }
        });
    }

    public void formatAktualisieren(double brechzahl, double nMindHoeheHauptebene, double nMaxHoeheLinse, double nDicke, double r1, double r2) {
        if(r1 != 0) {
            this.radius1 = Math.signum(r1) * Math.max(MIND_RADIUS, Math.abs(r1));
        } else {
            radius1 = 0;
        }
        if(r2 != 0) {
            this.radius2 = Math.signum(r2) * Math.max(MIND_RADIUS, Math.abs(r2));
        } else {
            radius2 = 0;
        }
        this.brechzahl = Math.min(MAX_BRECHZAHL, Math.max(MIND_BRECHZAHL, brechzahl));;
        if((nMaxHoeheLinse <= 2 * Math.abs(radius1) || radius1 == 0) && (nMaxHoeheLinse <= 2 * Math.abs(radius2) || radius2 == 0)) {
            this.hoeheLinse = nMaxHoeheLinse;
        } else {
            this.hoeheLinse = Math.min(2 * Math.abs(radius1), 2 * Math.abs(r2));
        }
        hoeheHauptebene = Math.max(hoeheLinse, Math.max(nMindHoeheHauptebene, Konstanten.HAUPTEBENE_MINDESTHOEHE));

        if(radius1 == -radius2) {
            brennweite = 0; //Brennweite -> unendlich
        } else {
            this.brennweite = berechneBrennweite(brechzahl, radius1, radius2); //Keine dicke Linsen!
        }

        hauptebene = new Hauptebene(Flaeche.MODUS_BRECHUNG, mittelPunkt, brennweite, hoeheHauptebene);

        double c1;
        if(Math.abs(radius1) >= Math.abs(hoeheLinse / 2)) {
            c1 = Math.sqrt(radius1 * radius1 - (hoeheLinse * hoeheLinse) / 4);
        } else {
            c1 = 0;
        }
        double c2;
        if(Math.abs(radius2) >= Math.abs(hoeheLinse / 2)) {
            c2 = Math.sqrt(radius2 * radius2 - (hoeheLinse * hoeheLinse) / 4);
        } else {
            c2 = 0;
        }

        double mindestDicke = -radius1 + Math.signum(radius1) * c1 - radius2 + Math.signum(radius2) * c2;
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

        if(radius1 != 0) {
            double r = Math.abs(radius1);
            double phi = Math.abs(Math.asin(hoeheLinse / (2 * r)));
            if(radius1 > 0) {
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

        if(radius2 != 0) {
            double r = Math.abs(radius2);
            double phi = Math.asin(hoeheLinse / (2 * r));
            if(radius2 > 0) {
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
        if(radius1 > 0) {
            breite1 += radius1 - c1;
        }
        if(radius2 > 0) {
        breite2 += radius2 - c2;
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

    public void setMaxLinsenHoehe(double nHoehe) {
        formatAktualisieren(brechzahl, hoeheHauptebene, nHoehe, dicke, radius1, radius2);
    }

    public void setRadius1(double nRadius1) {
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, dicke, nRadius1, radius2);
    }

    public void setRadius2(double nRadius2) {
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, nRadius2);
    }

    public void setBrechzahl(double nBrechzahl) {
        formatAktualisieren(nBrechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
    }

    //Min Dicke: Formatänderung unter Einhaltung einer möglichst geringen Dicke

    public void setMaxLinsenHoeheHoehe_minDicke(double nHoehe) {
        formatAktualisieren(brechzahl, hoeheHauptebene, nHoehe, 0, radius1, radius2);
    }

    public void setRadius1_minDicke(double nRadius1) {
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, 0, nRadius1, radius2);
    }

    public void setRadius2_minDicke(double nRadius2) {
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, 0, radius1, nRadius2);
    }

    public void setBrechzahl_minDicke(double nBrechzahl) {
        formatAktualisieren(nBrechzahl, hoeheHauptebene, hoeheLinse, 0, radius1, radius2);
    }

    public void setDicke(double nDicke) {
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, nDicke, radius1, radius2);
    }

    public void setMindHauptebeneHoeheHoehe_minDicke(double nHoehe) {
        double maxHoeheLinseNeu = Math.min(nHoehe, Math.min(Math.abs(radius1), Math.abs(radius2)));
        formatAktualisieren(brechzahl, nHoehe, maxHoeheLinseNeu, 0, radius1, radius2);
    }

    public void setBrennweite(double nBrennweite) {
        radius1 = radius2 = 3 * nBrennweite;
        double maxHoeheLinseNeu = Math.min(hoeheHauptebene, Math.abs(radius1));
        formatAktualisieren(2.5, hoeheHauptebene, maxHoeheLinseNeu, 0, radius1, radius2);
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
                //Brennpunkt zeichnen:
                g.setColor(Color.GRAY);
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                g.draw(new Line2D.Double(mittelPunkt.getX() + brennweite, mittelPunkt.getY() + 5, mittelPunkt.getX() + brennweite, mittelPunkt.getY() - 5));
                g.draw(new Line2D.Double(mittelPunkt.getX() - brennweite, mittelPunkt.getY() + 5, mittelPunkt.getX() - brennweite, mittelPunkt.getY() - 5));
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
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        formatAktualisieren(brechzahl, hoeheHauptebene, hoeheLinse, dicke, radius1, radius2);
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
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten;
        if(optischeBank.getModus() == OptischeBank.MODUS_HAUPTEBENE) {
            komponenten = new Eigenschaftenregler[2];
            komponenten[0] = slide_hoeheHauptebene;
            komponenten[1] = slide_brennweite;
        } else {
            komponenten = new Eigenschaftenregler[4];
            komponenten[0] = slide_hoeheLinse;
            komponenten[1] = slide_brechzahl;
            komponenten[2] = slide_radius1;
            komponenten[3] = slide_radius2;
        }
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_BRECHZAHL, String.valueOf(brechzahl));
        xmlElement.setAttribute(XML_DICKE, String.valueOf(dicke));
        xmlElement.setAttribute(XML_HOEHE_HAUPTEBENE, String.valueOf(hoeheHauptebene));
        xmlElement.setAttribute(XML_HOEHE_LINSE, String.valueOf(hoeheLinse));
        xmlElement.setAttribute(XML_RADIUS1, String.valueOf(radius1));
        xmlElement.setAttribute(XML_RADIUS2, String.valueOf(radius2));
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
