package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Bauelement Auge bestehend aus einer Hornhaut, Augenlinse und der Netzhaut.
 */
public class Auge extends Bauelement implements KannKollision {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Auge";

    /**
     * Name des Auges im XML-Dokument.
     */
    public static final String XML_AUGE = "auge";

    /**
     * Hoehe des Auges.
     */
    public static final double HOEHE_AUGE = 125;

    //Hornhaut
    private Hornhaut hornhaut;

    //Radius der Hornhaut
    private static final double HORNHAUT_RADIUS = 43.1;
    //Abstand der Hornhaut relativ zu Augenlinse
    private static final double HORNHAUT_ABSTAND = 40.3;

    //Augenlinse
    private Augenlinse augenlinse;
    //Brechzahl der Augenlinse
    private static final double AUGENLINSE_BRECHZAHL = 1.25;

    //Kruemmungsradius Augenlinse
    private double kruemmungsradius_linse;

    /**
     * Name des Kruemmungsradius im XML-Dokument.
     */
    public static final String XML_KRUEMMUNG_LINSE = "kruemmungLinse";

    /**
     * Mindestwert fuer den Kruemmungsradius.
     */
    public static final double MIND_RADIUS_LINSE = 60;

    /**
     * Maximalradius fuer den Kruemmungsradius.
     */
    public static final double MAX_RADIUS_LINSE = 535.8 / 2; //Radius bei entspanntem Auge

    //Netzhaut
    private Netzhaut netzhaut;
    //Hoehe der Netzhaut
    private static final double HOEHE_NETZHAUT = 64.65;

    //Abstand der Netzhaut relativ zu Augenlinse
    private double abstand_netzhaut;

    /**
     * Name des Netzhautabstand im XML-Dokument.
     */
    public static final String XML_NETZHAUT_ABSTAND = "abstandNetzhaut";

    /**
     * Mindestwert fuer den Abstand der Netzhaut.
     */
    public static final double MIND_ABSTAND_NETZHAUT = 46.3;

    /**
     * Wert fuer den Abstand der Netzhaut fuer Normalsichtigkeit.
     */
    public static final double ABSTAND_NETZHAUT_NORMAL = 96.3;

    /**
     * Maximalwert fuer den Abstand der Netzhaut.
     */
    public static final double MAX_ABSTAND_NETZHAUT = 146.3;

    //Begrenzungen des Auges
    private Grenzflaeche_Sphaerisch obereBegrenzung;
    private Grenzflaeche_Sphaerisch untereBegrenzung;

    //Eigenschaftenregler zur Manipulation der Werte des Auges durch den Benutzer
    private Eigenschaftenregler_Slider slide_netzhaut;
    private Eigenschaftenregler_Slider slide_radius;

    /**
     * Initialisiere neues Auge am uebergebenen Mittelpunkt.
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelpunkt Mittelpunkt des Auges.
     */
    public Auge(OptischeBank optischeBank, Vektor mittelpunkt) {
        super(optischeBank, mittelpunkt, Bauelement.TYP_AUGE);
        initialisiere(MAX_RADIUS_LINSE, ABSTAND_NETZHAUT_NORMAL);
    }

    /**
     * Initialisiere neues Auge ueber ein jdom2.Element.
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement jdom2.Element, das die benoetigten Attribute enthaelt.
     * @throws Exception Expection, die geworfen wird, wenn beim Initialisieren ein Fehler passiert.
     */
    public Auge(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_AUGE);
        initialisiere(xmlElement.getAttribute(XML_KRUEMMUNG_LINSE).getDoubleValue(),  xmlElement.getAttribute(XML_NETZHAUT_ABSTAND).getDoubleValue());
    }

    //Initialisiert das Auge mit neuen Werten und erstellt die Eigenschaftenregler
    private void initialisiere(double nKruemmungsradius_linse, double nAbstand_netzhaut) {
        kruemmungsradius_linse = Math.max(MIND_RADIUS_LINSE, Math.min(MAX_RADIUS_LINSE, nKruemmungsradius_linse));
        abstand_netzhaut = Math.max(MIND_ABSTAND_NETZHAUT, Math.min(MAX_ABSTAND_NETZHAUT, nAbstand_netzhaut));
        hornhaut = new Hornhaut(this, Vektor.addiere(mittelPunkt, new Vektor(-HORNHAUT_ABSTAND, 0)), HORNHAUT_RADIUS);
        augenlinse = new Augenlinse(this, mittelPunkt.kopiere(), AUGENLINSE_BRECHZAHL, kruemmungsradius_linse);
        netzhaut = new Netzhaut(this, Vektor.addiere(mittelPunkt, new Vektor(abstand_netzhaut, 0)), HOEHE_NETZHAUT);

        formatAktualisieren();

        slide_netzhaut = new Eigenschaftenregler_Slider("Abstand Netzhaut", "", 100, abstand_netzhaut, MIND_ABSTAND_NETZHAUT, MAX_ABSTAND_NETZHAUT, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setze_abstand_netzhaut(wert);
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
                return ReglerEvent.abstandNetzhautSicht(ABSTAND_NETZHAUT_NORMAL, zahl, 5);
            }
        });

        slide_radius = new Eigenschaftenregler_Slider("Anspannung Augenlinse", "", 100, kruemmungsradius_linse, MIND_RADIUS_LINSE, MAX_RADIUS_LINSE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setze_kruemmungsradius(wert);
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
                if(zahl - 10 < MIND_RADIUS_LINSE) {
                    return "angespannt";
                } else if (zahl + 10 > MAX_RADIUS_LINSE) {
                    return "entspannt";
                } else {
                    return "";
                }
            }
        });
    }

    //Aktualisiert das Format des Auges
    private void formatAktualisieren() {
        generiereBegrenzungen();
        setRahmen(generiereRahmen());
    }

    /**
     * @param nAbstand Neuer Abstand der Netzhaut zur Augenlinse.
     */
    public void setze_abstand_netzhaut(double nAbstand) {
        double abstand_alt = abstand_netzhaut;
        abstand_netzhaut = Math.max(MIND_ABSTAND_NETZHAUT, Math.min(MAX_ABSTAND_NETZHAUT, nAbstand));
        netzhaut.verschiebeUm(new Vektor(abstand_netzhaut - abstand_alt, 0));
        formatAktualisieren();
    }

    /**
     * @param nKruemmungsradius Neuer Kruemmungsradius der Augenlinse.
     */
    public void setze_kruemmungsradius(double nKruemmungsradius) {
        kruemmungsradius_linse = Math.max(MIND_RADIUS_LINSE, Math.min(MAX_RADIUS_LINSE, nKruemmungsradius));
        augenlinse.setRadius1(nKruemmungsradius);
        augenlinse.setRadius2(nKruemmungsradius);
    }

    //Generiert die sphaerischen Begrenzungslinien des Auges als absorbierende Grenzflaechen
    private void generiereBegrenzungen() {
        double d = abstand_netzhaut + HORNHAUT_ABSTAND - 14.59;
        double a = netzhaut.getHoehe() / 2;
        double b = abstand_netzhaut - (d / 2);
        double e = HOEHE_AUGE / 2;
        double c = (e * e - a * a - (d * d) / 4) / (2 * (a - e));
        double r = e + c;
        double alpha = Math.atan((2 * (a + c)) / d);
        double ext = Math.PI - 2 * alpha;
        obereBegrenzung = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, new Vektor(b, -c)), r, alpha, ext);
        untereBegrenzung = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, new Vektor(b, c)), r, Math.PI + alpha, ext);
    }

    /**
     * @return Netzhaut des Auges.
     */
    public Netzhaut getNetzhaut() {
        return netzhaut;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        augenlinse.verschiebeUm(verschiebung);
        hornhaut.verschiebeUm(verschiebung);
        netzhaut.verschiebeUm(verschiebung);
        obereBegrenzung.verschiebeUm(verschiebung);
        untereBegrenzung.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-HORNHAUT_ABSTAND - 5, 5 + HOEHE_AUGE / 2));
        rahmen.rahmenErweitern(new Point2D.Double( abstand_netzhaut + 5, 5 + HOEHE_AUGE / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+abstand_netzhaut + 5, -5 -HOEHE_AUGE / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-HORNHAUT_ABSTAND - 5, -5 -HOEHE_AUGE / 2));
        return rahmen;
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[2];
        komponenten[0] = slide_netzhaut;
        komponenten[1] = slide_radius;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        hornhaut.paintComponent(g);
        augenlinse.paintComponent(g);
        netzhaut.paintComponent(g);
        obereBegrenzung.paintComponent(g);
        untereBegrenzung.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(hornhaut.kollisionUeberpruefen(cStrGng));
        kollisionen.add(augenlinse.kollisionUeberpruefen(cStrGng));
        kollisionen.add(netzhaut.kollisionUeberpruefen(cStrGng));
        kollisionen.add(obereBegrenzung.gibKollision(cStrGng));
        kollisionen.add(untereBegrenzung.gibKollision(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_KRUEMMUNG_LINSE, String.valueOf(kruemmungsradius_linse));
        xmlElement.setAttribute(XML_NETZHAUT_ABSTAND, String.valueOf(abstand_netzhaut));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_AUGE;
    }

}
