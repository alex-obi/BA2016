package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.*;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Hohlspiegel extends Bauelement implements KannKollision{

    public static final String NAME = "Hohlspiegel";
    public static final String XML_HOHLSPIEGEL = "hohlspiegel";

    protected double hoehe;
    public static final String XML_HOEHE = "hoehe";
    public static final double MIND_HOEHE = 100;
    public static final double MAX_HOEHE = 510;

    protected double breite;


    protected double radius;
    public static final String XML_RADIUS = "radius";
    public static final double MIND_RADIUS = 50;
    public static final double MAX_RADIUS = 100000; //Maximaler Radius bis Kreis als ebene Fläche approximiert wird

    private Grenzflaeche spiegelFlaeche;

    private Hauptebene hauptebene;

    private Eigenschaftenregler_Slider slide_hoehe;
    private Eigenschaftenregler_Slider slide_radius;

    public Hohlspiegel(OptischeBank optischeBank, Vektor mittelPunkt, double radius, double hoehe) {
        super(optischeBank, mittelPunkt, TYP_SPIEGEL);
        initialisiere(radius, hoehe);
    }

    public Hohlspiegel(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_SPIEGEL);
        initialisiere(xmlElement.getAttribute(XML_RADIUS).getDoubleValue(), xmlElement.getAttribute(XML_HOEHE).getDoubleValue());
    }

    private void initialisiere(double nRadius, double nHoehe) {
        hauptebene = new Hauptebene(Flaeche.MODUS_REFLEKT, mittelPunkt, nRadius / 2, nHoehe);

        radius = nRadius;
        setHoehe(nHoehe);

        slide_hoehe = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoehe, MIND_HOEHE, MAX_HOEHE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setHoehe(wert);
                slide_hoehe.setWert(hoehe);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.linearZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.laengeZuCm(zahl);
            }
        });

        slide_radius = new Eigenschaftenregler_Slider("Radius", "cm", 200, radius, MIND_RADIUS, MAX_RADIUS, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setRadius(wert);
                slide_hoehe.setWert(hoehe);
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

    public void formatAktualisieren(double nHoehe, double nRadius) {
        this.radius = nRadius;
        if(radius != 0) {
            this.hoehe = Math.min(nHoehe, Math.abs(radius * 2));
        } else {
            this.hoehe = nHoehe;
        }
        hauptebene.setHoehe(hoehe);
        hauptebene.setBrennweite(radius / 2);
        if(radius == 0 || radius > MAX_RADIUS) {
            breite = 0;
            Vektor von = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() + hoehe / 2);
            Vektor bis = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() - hoehe / 2);
            spiegelFlaeche = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_REFLEKT, von, bis);
        } else {
            double winkel = Math.asin(Math.abs(hoehe / (2* radius)));
            double c = Math.sqrt(radius * radius - (hoehe * hoehe) / 4);
            breite = Math.abs(radius) - c;
            if(radius > 0) {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, radius, Math.PI * 2 - winkel, 2 * winkel);
            } else {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, -radius, Math.PI - winkel, 2 * winkel);
            }
        }
        setRahmen(generiereRahmen());
    }

    public void setHoehe(double nHoehe) {
        formatAktualisieren(nHoehe, radius);
    }

    public void setRadius(double nRadius) {
        formatAktualisieren(hoehe, nRadius);
    }

    public double getBrennweite() {
        return radius / 2;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(new Color(62, 195, 221));
                spiegelFlaeche.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                //Brennpunkt zeichnen:
                g.setColor(Color.GRAY);
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                g.draw(new Line2D.Double(mittelPunkt.getX() - getBrennweite(), mittelPunkt.getY() + 5, mittelPunkt.getX() - getBrennweite(), mittelPunkt.getY() - 5));
                g.setColor(Color.BLACK);
                hauptebene.paintComponent(g);
                g.setColor(new Color(62, 195, 221));
                spiegelFlaeche.paintComponent(g);
        }
        super.paintComponent(g);

    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                return spiegelFlaeche.gibKollision(cStrGng);
            case OptischeBank.MODUS_HAUPTEBENE:
                StrahlenKollision sK = spiegelFlaeche.gibKollision(cStrGng);
                if(sK != null) {
                    return new StrahlenKollision(sK.getDistanz(), cStrGng, hauptebene);
                }

        }
        return null;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        spiegelFlaeche.verschiebeUm(verschiebung);
        hauptebene.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        if(radius > 0) {
            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new Point2D.Double(5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, -hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(5, -hoehe / 2));
            return  rahmen;
        } else {
            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new Point2D.Double(-5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(breite + 5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(breite + 5, -hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-5, -hoehe / 2));
            return rahmen;
        }
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[2];
        komponenten[0] = slide_hoehe;
        komponenten[1] = slide_radius;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_HOEHE, String.valueOf(hoehe));
        xmlElement.setAttribute(XML_RADIUS, String.valueOf(radius));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_HOHLSPIEGEL;
    }

}
