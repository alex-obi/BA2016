package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
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

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Hohlspiegel als sphaerischer Kreisbogen, der Strahlen reflektiert.
 */
public class Hohlspiegel extends Bauelement implements KannKollision {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Hohlspiegel";

    /**
     * Name des Hohlspiegels im XML-Dokument.
     */
    public static final String XML_HOHLSPIEGEL = "hohlspiegel";

    //Hoehe des Spiegels
    double hoehe;

    /**
     * Name der Hoehe im XML-Dokument.
     */
    public static final String XML_HOEHE = "hoehe";

    /**
     * Mindestwert fuer die Hoehe.
     */
    public static final double MIND_HOEHE = 100;

    /**
     * Maximalwert fuer die Hoehe.
     */
    public static final double MAX_HOEHE = 510;

    //Breite des Hohlspiegels
    double breite;

    //Radius des Hohspiegels
    double radius;

    /**
     * Name des Radius im XML-Dokument.
     */
    public static final String XML_RADIUS = "radius";

    /**
     * Mindestwert fuer den Radius.
     */
    public static final double MIND_RADIUS = 50;

    /**
     * Maximalerwert fuer Radius bis Kreis als ebene Flaeche approximiert wird
     */
    public static final double MAX_RADIUS = 100000;

    //Grenzflaeche des Hohlspiegels
    private Grenzflaeche spiegelFlaeche;

    //Hauptebene des Hohlspiegels
    private Hauptebene hauptebene;

    //Eigenschaftenregler zur Manipulation des Hohlspiegels durch den Benutzer:
    private Eigenschaftenregler_Slider slide_hoehe;
    private Eigenschaftenregler_Slider slide_radius;

    /**
     * Initialisiere Hohlspiegel ueber Radius und Hoehe.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelPunkt  Mittelpunkt des Hohlspiegels.
     * @param radius       Radius des Hohlspiegels.
     * @param hoehe        Hoehe des Hohlspiegels.
     */
    public Hohlspiegel(OptischeBank optischeBank, Vektor mittelPunkt, double radius, double hoehe) {
        super(optischeBank, mittelPunkt, TYP_SPIEGEL);
        initialisiere(radius, hoehe);
    }

    /**
     * Initialisiere Hohlspiegel ueber ein jdom2.Element.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement   jdom2.Element, das die benoetigten Attribute enthaelt.
     * @throws Exception Exception, die geworfen wird, wenn bei der Initialisierung ein Fehler passiert.
     */
    public Hohlspiegel(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_SPIEGEL);
        initialisiere(xmlElement.getAttribute(XML_RADIUS).getDoubleValue(), xmlElement.getAttribute(XML_HOEHE).getDoubleValue());
    }

    //Initialisiere die Werte des Hohlspiegels und erstelle die Eigenschaftenregler
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
                return ReglerEvent.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return ReglerEvent.linearZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return ReglerEvent.laengeZuCm(zahl);
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
                return ReglerEvent.prozentZuRadius(reglerProzent, minimum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return ReglerEvent.radiusZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return ReglerEvent.radiusZuCm(zahl);
            }
        });
    }

    //Aktualisiere das Format des Hohlspiegels
    private void formatAktualisieren(double nHoehe, double nRadius) {
        this.radius = nRadius;
        if (radius != 0) {
            this.hoehe = Math.min(nHoehe, Math.abs(radius * 2));
        } else {
            this.hoehe = nHoehe;
        }
        hauptebene.setHoehe(hoehe);
        hauptebene.setBrennweite(radius / 2);
        if (radius == 0 || radius > MAX_RADIUS) {
            breite = 0;
            Vektor von = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() + hoehe / 2);
            Vektor bis = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() - hoehe / 2);
            spiegelFlaeche = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_REFLEKT, von, bis);
        } else {
            double winkel = Math.asin(Math.abs(hoehe / (2 * radius)));
            double c = Math.sqrt(radius * radius - (hoehe * hoehe) / 4);
            breite = Math.abs(radius) - c;
            if (radius > 0) {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, radius, Math.PI * 2 - winkel, 2 * winkel);
            } else {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, -radius, Math.PI - winkel, 2 * winkel);
            }
        }
        setRahmen(generiereRahmen());
    }

    /**
     * @param nHoehe Neue Hoehe des Hohlspiegels.
     */
    public void setHoehe(double nHoehe) {
        formatAktualisieren(nHoehe, radius);
    }

    /**
     * @param nRadius Neuer Radius des Hohlspiegels.
     */
    public void setRadius(double nRadius) {
        formatAktualisieren(hoehe, nRadius);
    }

    /**
     * @return Brennweite des Hohlspiegels.
     */
    public double getBrennweite() {
        return radius / 2;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
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
                if (sK != null) {
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
        if (radius > 0) {
            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new Point2D.Double(5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, -hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(5, -hoehe / 2));
            return rahmen;
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
        Eigenschaftenregler[] komponenten = new Eigenschaftenregler[2];
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
