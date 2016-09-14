package de.hhu.alobe.ba2016.physik.elemente.Licht;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Lichtquelle als abstrakte Klasse. Erbende Klassen erhalten Funtkionen um ihre Strahlengänge zu verwalten.
 */
public abstract class Lichtquelle extends Bauelement {

    /**
     * Name der Lichtquelle im XML-Dokument.
     */
    public static final String XML_LICHTQUELLE = "lichtquelle";

    //Wahrheitswert, ob Lichtquelle aktiv ist und ihre Strahlen gezeichnet werden.
    private boolean aktiv;

    /**
     * Name für Aktivitätswert im XML-Dokument.
     */
    public static final String XML_ISTAKTIV = "istAktiv";

    //Strahlengaenge, die durch diese Lichtquelle erzeugt werden
    protected ArrayList<Strahlengang> strahlengaenge;

    //Farbe dieser Lichtquelle
    protected Farbe farbe;

    /**
     * Name für die Farbe im XML-Dokument.
     */
    public static final String XML_FARBE = "farbe";

    /**
     * Initialisiere Lichtquelle mit Mittelpunkt und Farbe.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelPunkt  Mittelpunkt der Lichtquelle.
     * @param farbe        Farbe der Lichtquelle.
     */
    public Lichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Farbe farbe) {
        super(optischeBank, mittelPunkt, TYP_LAMPE);
        initialisieren(true, farbe);
        strahlengaenge = new ArrayList<>();
    }

    /**
     * Initialisiere Lichtquelle über ein jdom2.Element
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement   jdom2.Element, was die benötigten Attribute enthält.
     * @throws Exception Exception, die geworfen wird, wenn beim Initialisieren ein Fehler passiert.
     */
    public Lichtquelle(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_LAMPE);
        initialisieren(xmlElement.getAttribute(XML_ISTAKTIV).getBooleanValue(), new Farbe(xmlElement.getChild(XML_FARBE)));
        strahlengaenge = new ArrayList<>();
        Iterator<?> strahlen = xmlElement.getChildren(Strahl.XML_STRAHL).iterator();
        while (strahlen.hasNext()) {
            neuerStrahl(new Strahlengang(new Strahl((Element) strahlen.next())));
        }
    }

    //Initialisiert die Werte der Lichtquelle
    private void initialisieren(boolean nAktiv, Farbe nFarbe) {
        aktiv = nAktiv;
        farbe = nFarbe;
    }

    /**
     * Berechnet neuen Strahlengang, der abhängig von der erbenden Lichtquelle durch den übergebenen Strahlenpunkt verlaufen soll.
     *
     * @param strahlPunkt Punkt, durch den der erste Strahl des Strahlengangs verlaufen soll.
     * @return Strahlengang in Richtung des übergebenen Punktes.
     */
    public abstract Strahlengang berechneNeuenStrahl(Vektor strahlPunkt);

    /**
     * Fügt einen neuen Strahlengang der Lichtquelle hinzu.
     *
     * @param strahl Neuer Strahlengang.
     */
    public void neuerStrahl(Strahlengang strahl) {
        strahlengaenge.add(strahl);
    }

    /**
     * Entfernt einen Strahlengang von der Lichtquelle.
     *
     * @param strahl Zu entfernender Strahlengang.
     */
    public void loescheStrahl(Strahlengang strahl) {
        strahlengaenge.remove(strahl);
    }

    /**
     * Zeichnet die Strahlen dieser Lichtquelle im übergebenen Graphics Element.
     *
     * @param g Grafikelement, in das gezeichnet werden soll.
     */
    public void strahlenZeichnen(Graphics2D g) {
        g.setColor(farbe);
        for (Strahlengang cStrahl : strahlengaenge) {
            cStrahl.paintComponent(g);
        }

    }

    /**
     * Gibt alle Bildpunkte, die von dieser Lichtquelle durch die Strahlengänge entstehen. Voraussetzung ist, dass sich an jedem Bildpunkt mindestens 2 Strahlengänge kreuzen.
     *
     * @param auchVirtuell Gibt an, ob auch virtuelle Bildpunkte gesammelt werden sollen.
     * @return Liste der Bildpunkte dieser Lichtquelle.
     */
    public ArrayList<Vektor> gibBildpunkte(boolean auchVirtuell) {
        ArrayList<Vektor> pruefList = new ArrayList<>();
        ArrayList<Vektor> retList = new ArrayList<>();
        for (Strahlengang cStrG : strahlengaenge) {
            for (Vektor cBildpunkt : cStrG.gibBildpunkte(auchVirtuell)) {
                int anzahlGleicherPunkte = 1;
                for (Vektor cVergleichsBildpunkt : pruefList) {
                    if (Vektor.gibAbstand(cVergleichsBildpunkt, cBildpunkt) < 1) {
                        anzahlGleicherPunkte++;
                    }
                }
                pruefList.add(cBildpunkt);
                if (anzahlGleicherPunkte == 2) {
                    retList.add(cBildpunkt);
                }
            }
        }
        return retList;
    }

    @Override
    public void waehleAus() {
        super.waehleAus();
        optischeBank.werkzeugWechseln(new Werkzeug_NeuerStrahl(this) {
        });
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        for (Strahlengang cStrG : strahlengaenge) {
            cStrG.getAnfangsStrahl().getBasisVektor().addiere(verschiebung);
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        if (aktiv) {
            strahlenZeichnen(g);
            //Bilder Zeichnen, die von dieser Lampe entstehen
            for (Vektor bildPunkt : gibBildpunkte(optischeBank.isVirtuelleStrahlenAktiv())) {
                boolean aufSchirm = false;
                for (Schirm schirm : optischeBank.getSchirme()) {
                    Vektor schirmPunkt = schirm.gibKollisionsPunkt(bildPunkt);
                    if (schirmPunkt != null) {
                        aufSchirm = true;
                        g.fillArc(schirmPunkt.getXint() - 5, schirmPunkt.getYint() - 5, 10, 10, 0, 360);
                    }
                }
                if (!aufSchirm) {
                    g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                    g.draw(new Line2D.Double(bildPunkt.getX(), optischeBank.getOptischeAchse().getHoehe(), bildPunkt.getX(), bildPunkt.getY()));
                }
            }
        }
        super.paintComponent(g);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_ISTAKTIV, String.valueOf(aktiv));
        xmlElement.addContent(farbe.getXmlElement(XML_FARBE));
        for (Strahlengang strG : strahlengaenge) {
            xmlElement.addContent(strG.getAnfangsStrahl().getXmlElement());
        }
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_LICHTQUELLE;
    }

    /**
     * @return Farbe der Lichtquelle.
     */
    public Color getFarbe() {
        return farbe;
    }

    /**
     * @param farbe Neue Farbe der Lichtquelle.
     */
    public void setFarbe(Color farbe) {
        this.farbe = new Farbe(farbe);
    }

    /**
     * @return Alle Strahlengänge, die durch diese Lichtquelle erzeugt werden.
     */
    public ArrayList<Strahlengang> getStrahlengaenge() {
        return strahlengaenge;
    }

    /**
     * @return Wahrheitswert, ob Lampe aktiv ist.
     */
    public boolean isAktiv() {
        return aktiv;
    }

    /**
     * @param aktiv Neuer Wert, ob Lampe aktiv ist.
     */
    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }


}
