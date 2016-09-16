package de.hhu.alobe.ba2016.physik.elemente.absorbtion;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Bauelement Blende als Gerade mit einer Oeffnung, durch die Strahlen hindurch kommen.
 */
public class Blende extends Bauelement implements KannKollision {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Blende";

    /**
     * Name der Blende im XML-Dokument.
     */
    public static final String XML_BLENDE = "blende";

    //Durchmesser der Oeffnung
    private double durchmesser;

    /**
     * Name des Durchmessers im XML-Dokument.
     */
    public static final String XML_DURCHMESSER = "durchmesser";

    /**
     * Mindestwert fuer Durchmesser.
     */
    public static final double MIND_DURCHMESSER = 0;

    /**
     * Mindestlaenge der Aussenbegrenzungen der Blende.
     */
    public static final double MIND_ABSTAND = 10;

    //Hoehe der gesamten Blende.
    private double hoehe;

    /**
     * Name der Hoehe im XML-Dokument.
     */
    public static final String XML_HOEHE = "hoehe";

    /**
     * Mindestwert fuer die Hoehe.
     */
    public static final double MIND_HOEHE = 20;

    /**
     * Maximalwert fuer die Hoehe.
     */
    public static final double MAX_HOEHE = 500;

    //Obere Haelfte der Blende als Grenzflaeche.
    private Grenzflaeche obereHaelfte;

    //Untere Haelfte der Blende als Grenzflaeche.
    private Grenzflaeche untereHaelfte;

    //Eigenschaftenregler zur Manipulation des Schirms durch den Benutzer.
    private Eigenschaftenregler_Slider slide_hoehe;
    private Eigenschaftenregler_Slider slide_durchmesser;

    /**
     * Initialisiert neue Blende mit Hoehe und Durchmesser.
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelPunkt Mittelpunkt der Blende.
     * @param hoehe Hoehe der Blende.
     * @param durchmesser Durchmesser der Oeffnung der Blende.
     */
    public Blende(OptischeBank optischeBank, Vektor mittelPunkt, double hoehe, double durchmesser) {
        super(optischeBank, mittelPunkt, Bauelement.TYP_BLENDE);
        initialisiere(hoehe, durchmesser);
    }

    /**
     * Initialisiert neue Blende mit einem jdom-Element.
     * @param optischeBank Referenz auf Optische Bank
     * @param xmlElement jdom2.Element, was benoetigte Werte enthaelt.
     * @throws Exception Exception, die geworfen wird, wenn ein Fehler bei der Initialisierung passiert.
     */
    public Blende(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_BLENDE);
        initialisiere(xmlElement.getAttribute(XML_HOEHE).getDoubleValue(), xmlElement.getAttribute(XML_DURCHMESSER).getDoubleValue());
    }

    //Funktion um die Werte der Blende zu initialisieren und die Manipulationsregler zu erstellen.
    private void initialisiere(double nHoehe, double nDurchmesser) {
        formatAktualisieren(nHoehe, nDurchmesser);

        slide_hoehe = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoehe, MIND_HOEHE, MAX_HOEHE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setHoehe(wert);
                slide_durchmesser.setWert(durchmesser);
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

        slide_durchmesser = new Eigenschaftenregler_Slider("Durchmesser", "cm", 100, durchmesser, MIND_DURCHMESSER, MAX_HOEHE - MIND_ABSTAND * 2, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setDurchmesser(wert);
                slide_durchmesser.setWert(durchmesser);
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
    }

    //Aktualisiert das Format dieser Blende.
    private void formatAktualisieren(double nHoehe, double nDurchmesser) {
        hoehe = Math.min(MAX_HOEHE, Math.max(MIND_HOEHE, nHoehe));
        durchmesser = Math.max(MIND_DURCHMESSER, Math.min(hoehe - MIND_ABSTAND * 2, nDurchmesser));

        Vektor vonVekt = new Vektor(0, durchmesser / 2);
        Vektor bisVekt = new Vektor(0, hoehe / 2);
        obereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, vonVekt), Vektor.addiere(mittelPunkt, bisVekt));
        untereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.subtrahiere(mittelPunkt, vonVekt), Vektor.subtrahiere(mittelPunkt, bisVekt));

        setRahmen(generiereRahmen());
    }

    /**
     * @param nDurchmesser Neuer Durchmesser der Oeffnung der Blende.
     */
    public void setDurchmesser(double nDurchmesser) {
        formatAktualisieren(hoehe, nDurchmesser);
    }

    /**
     * @param nHoehe Neue Hoehe der Blende.
     */
    public void setHoehe(double nHoehe) {
        formatAktualisieren(nHoehe, durchmesser);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        obereHaelfte.verschiebeUm(verschiebung);
        untereHaelfte.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-5, hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5 , hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-5, -hoehe / 2));
        return rahmen;
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[2];
        komponenten[0] = slide_hoehe;
        komponenten[1] = slide_durchmesser;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setColor(new Color(62, 8, 0));
        obereHaelfte.paintComponent(g);
        untereHaelfte.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(obereHaelfte.gibKollision(cStrGng));
        kollisionen.add(untereHaelfte.gibKollision(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_DURCHMESSER, String.valueOf(durchmesser));
        xmlElement.setAttribute(XML_HOEHE, String.valueOf(hoehe));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_BLENDE;
    }

}