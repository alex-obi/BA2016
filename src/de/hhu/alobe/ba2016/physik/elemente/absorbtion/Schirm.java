package de.hhu.alobe.ba2016.physik.elemente.absorbtion;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Bauelement Schirm als Gerade, die Strahlen absorbiert. Zeigt an, wenn ein Bild auf diesen Schirm projeziert wird.
 */
public class Schirm extends Bauelement implements KannKollision {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Schirm";

    /**
     * Name des Schirms im XML-Dokument.
     */
    public static final String XML_SCHIRM = "schirm";

    //Hoehe des Schirms
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

    //Flaeche des Schirms
    private Grenzflaeche_Ebene schirmFlaeche;

    //Eigenschaftenregler zur Manipulation des Schirms durch den Benutzer.
    private Eigenschaftenregler_Slider slide_hoehe;

    public Schirm(OptischeBank optischeBank, Vektor mittelPunkt, double hoehe) {
        super(optischeBank, mittelPunkt, TYP_SCHIRM);
        initialisiere(hoehe);
    }

    public Schirm(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_SCHIRM);
        initialisiere(xmlElement.getAttribute(XML_HOEHE).getDoubleValue());

    }

    private void initialisiere(double nHoehe) {
        formatAktualisieren(nHoehe);

        slide_hoehe = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoehe, MIND_HOEHE, MAX_HOEHE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setHoehe(wert);
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

    public void formatAktualisieren(double nHoehe) {
        hoehe = Math.min(MAX_HOEHE, Math.max(MIND_HOEHE, nHoehe));

        Vektor von = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() + hoehe / 2);
        Vektor bis = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() - hoehe / 2);
        schirmFlaeche = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, von, bis);

        setRahmen(generiereRahmen());
    }

    public void setHoehe(double nHoehe) {
        formatAktualisieren(nHoehe);
    }

    /**
     * Prueft, ob der uebergebene Punkt in der Naehe dieses Schirms liegt und gebe einen Punkt zurueck, der auf dem Schirm in der Naehe liegt.
     * @param bildpunkt
     * @return
     */
    public Vektor gibKollisionsPunkt(Vektor bildpunkt) {
        Kreis k = new Kreis(bildpunkt, 3);
        if(k.schneidetGerade(schirmFlaeche.getEbene())) {
            return new Vektor(mittelPunkt.getX(), bildpunkt.getY());
        } else {
            return null;
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setColor(new Color(62, 8, 0));
        schirmFlaeche.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        return schirmFlaeche.gibKollision(cStrGng);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        schirmFlaeche.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-5, hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5 , hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-5, -hoehe / 2));
        setRahmen(rahmen);
        return rahmen;
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[1];
        komponenten[0] =slide_hoehe;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    public double getHoehe() {
        return hoehe;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_HOEHE, String.valueOf(hoehe));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_SCHIRM;
    }

}
