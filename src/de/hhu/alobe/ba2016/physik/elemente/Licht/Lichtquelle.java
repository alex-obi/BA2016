package de.hhu.alobe.ba2016.physik.elemente.Licht;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Lichtquelle extends Bauelement {

    public static final String XML_LICHTQUELLE = "lichtquelle";

    private boolean aktiv;
    public static final String XML_ISTAKTIV = "istAktiv";

    //Strahlengaenge, die durch diese Lichtquelle erzeugt werden
    protected ArrayList<Strahlengang> strahlengaenge;

    //Farbe dieser Lichtquelle
    protected Farbe farbe;
    public static final String XML_FARBE = "farbe";

    public Lichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Farbe farbe) {
        super(optischeBank, mittelPunkt, TYP_LAMPE);
        initialisieren(true, farbe);
        strahlengaenge = new ArrayList<>();
    }

    public Lichtquelle(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_LAMPE);
        initialisieren(xmlElement.getAttribute(XML_ISTAKTIV).getBooleanValue(), new Farbe(xmlElement.getChild(XML_FARBE)));
        strahlengaenge = new ArrayList<>();
        Iterator<?> strahlen = xmlElement.getChildren(Strahl.XML_STRAHL).iterator();
        while(strahlen.hasNext()) {
            neuerStrahl(new Strahlengang(new Strahl((Element) strahlen.next())));
        }
    }

    private void initialisieren(boolean nAktiv, Farbe nFarbe) {
        aktiv = nAktiv;
        farbe = nFarbe;
    }

    public abstract Strahlengang berechneNeuenStrahl (Vektor strahlPunkt);

    public void neuerStrahl(Strahlengang strahl) {
        strahlengaenge.add(strahl);
    }

    public void loescheStrahl(Strahlengang strahl) {
        strahlengaenge.remove(strahl);
    }

    public void strahlenZeichnen(Graphics2D g) {
        g.setColor(farbe);
        for(Strahlengang cStrahl : strahlengaenge) {
            cStrahl.paintComponent(g);
        }

    }

    public ArrayList<Vektor> gibBildpunkte(boolean auchVirtuell) {
        ArrayList<Vektor> pruefList = new ArrayList<>();
        ArrayList<Vektor> retList = new ArrayList<>();
        for(Strahlengang cStrG : strahlengaenge) {
            for(Vektor cBildpunkt : cStrG.gibBildpunkte(auchVirtuell)) {
                int anzahlGleicherPunkte = 1;
                for(Vektor cVergleichsBildpunkt : pruefList) {
                    if(Vektor.gibAbstand(cVergleichsBildpunkt, cBildpunkt) < 1) {
                        anzahlGleicherPunkte++;
                    }
                }
                pruefList.add(cBildpunkt);
                if(anzahlGleicherPunkte == 2) {
                    retList.add(cBildpunkt);
                }
            }
        }
        return retList;
    }

    @Override
    public void waehleAus() {
        super.waehleAus();
        optischeBank.werkzeugWechseln(new Werkzeug_NeuerStrahl(optischeBank, this) {
        });
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        for(Strahlengang cStrG : strahlengaenge) {
            cStrG.getAnfangsStrahl().getBasisVektor().addiere(verschiebung);
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        if(aktiv) {
            strahlenZeichnen(g);
            //Bilder Zeichnen, die von dieser Lampe entstehen
                for (Vektor bildPunkt : gibBildpunkte(optischeBank.isVirtuelleStrahlenAktiv())) {
                    g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                    g.draw(new Line2D.Double(bildPunkt.getX(), optischeBank.getOptischeAchse().getHoehe(), bildPunkt.getX(), bildPunkt.getY()));
            }
        }
        super.paintComponent(g);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_ISTAKTIV, String.valueOf(aktiv));
        xmlElement.addContent(farbe.getXmlElement(XML_FARBE));
        for(Strahlengang strG : strahlengaenge) {
            xmlElement.addContent(strG.getAnfangsStrahl().getXmlElement());
        }
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_LICHTQUELLE;
    }

    public Color getFarbe() {
        return farbe;
    }

    public void setFarbe(Farbe farbe) {
        this.farbe = farbe;
    }

    public void setFarbe(Color farbe) {
        this.farbe = new Farbe(farbe);
    }

    public ArrayList<Strahlengang> getStrahlengaenge() {
        return strahlengaenge;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public void setStrahlengaenge(ArrayList<Strahlengang> strahlengaenge) {
        this.strahlengaenge = strahlengaenge;
    }


}
