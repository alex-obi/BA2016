package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.Konstanten;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Strahl mit Basis und normiertem Richtungsvektor
 */
public class Strahl extends GeomertrischeFigur {

    public static final String XML_STRAHL = "strahl";

    protected Vektor basisVektor;
    public static final String XML_BASISVEKTOR = "basisVektor";

    protected Vektor richtungsVektor;
    public static final String XML_RICHTUNGSVEKTOR = "richtungsVektor";

    //Variable zum Speichern der Entfernung, von welchem Bildpunkt (reell oder virtuell) aus der Strahl bei Berechnung durch Hauptebenen erzeugt wird
    protected double quellEntfernung;

    protected boolean ausDemUnendlichen;

    public Strahl(Vektor basisVektor, Vektor richtung) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = 0;
    }

    public Strahl(Vektor basisVektor, Vektor richtung, double quellEntfernung, boolean ausDemUnendlichen) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = quellEntfernung;
        this.ausDemUnendlichen = ausDemUnendlichen;
    }

    public Strahl(Element xmlElement) throws Exception {
        basisVektor = new Vektor(xmlElement.getChild(XML_BASISVEKTOR));
        richtungsVektor = new Vektor(xmlElement.getChild(XML_RICHTUNGSVEKTOR));
    }

    /**
     * Gibt an welche Strecke ein Strahl zurücklegen muss bis er den jeweils anderen trifft
     * @param s2
     * @return Array, mit Element 0 = Entfernung dieser Strahl, Element 1 = Entfernung übergebener Strahl
     */
    public double[] gibSchnittentfernungen (Strahl s2) {
        double b1x = basisVektor.getX();
        double b1y = basisVektor.getY();
        double r1x = richtungsVektor.getX();
        double r1y = richtungsVektor.getY();
        double b2x = s2.getBasisVektor().getX();
        double b2y = s2.getBasisVektor().getY();
        double r2x = s2.getRichtungsVektor().getX();
        double r2y = s2.getRichtungsVektor().getY();

        if (r1x * r2y == r1y * r2x) return null; //Strahlen sind parallel

        double lamda1 = ((b1y - b2y) * r2x - (b1x - b2x) * r2y) / (r1x * r2y - r1y * r2x);

        double lamda2 = 0;
        if (r2x != 0) lamda2 = (b1x - b2x + lamda1 * r1x) / r2x;
        if (r2y != 0) lamda2 = (b1y - b2y + lamda1 * r1y) / r2y;

        double[] lamdas = {lamda1, lamda2};
        return lamdas;
    }

    public Vektor gibQuellPunkt() {
        Vektor retVektor = basisVektor.kopiere();
        retVektor.addiere(Vektor.multipliziere(richtungsVektor, quellEntfernung));
        return retVektor;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor bisVektor = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, 10000));
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        Line2D line = new Line2D.Double(basisVektor, bisVektor);
        g.draw(line);
        if((quellEntfernung < 0 || isAusDemUnendlichen()) && HauptFenster.get().getAktuelleOptischeBank().isVirtuelleStrahlenAktiv()) {
            double cQuellEntfernung = quellEntfernung;
            if(isAusDemUnendlichen()) {
                cQuellEntfernung = -10000;
            }
            Vektor bisVektorVirtuell = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, cQuellEntfernung));
            g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 5.0f, new float[] {10.0f,4.0f}, 0.0f));
            Line2D lineVirtuell = new Line2D.Double(basisVektor, bisVektorVirtuell);
            g.draw(lineVirtuell);
        }
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        basisVektor.addiere(verschiebung);
    }

    public Element getXmlElement() {
        Element xmlElement = new Element(getXmlElementTyp());
        xmlElement.addContent(basisVektor.getXmlElement(XML_BASISVEKTOR));
        xmlElement.addContent(richtungsVektor.getXmlElement(XML_RICHTUNGSVEKTOR));
        return xmlElement;
    }

    public String getXmlElementTyp() {
        return XML_STRAHL;
    }

    public static double[] gibSchnittentfernungen (Strahl s1, Strahl s2) {
        return s1.gibSchnittentfernungen(s2);
    }

    public Vektor getBasisVektor() {
        return basisVektor;
    }

    public void setBasisVektor(Vektor basisVektor) {
        this.basisVektor = basisVektor;
    }

    public Vektor getRichtungsVektor() {
        return richtungsVektor;
    }

    public void setRichtungsVektor(Vektor richtungsVektor) {
        this.richtungsVektor = richtungsVektor.gibEinheitsVektor();
    }

    public double getQuellEntfernung() {
        return quellEntfernung;
    }

    public void setQuellEntfernung(double quellEntfernung) {
        this.quellEntfernung = quellEntfernung;
    }

    public boolean isAusDemUnendlichen() {
        return ausDemUnendlichen;
    }

    public void setAusDemUnendlichen(boolean ausDemUnendlichen) {
        this.ausDemUnendlichen = ausDemUnendlichen;
    }
}

