package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.jdom.Speicherbar;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Strahl aus Basis und normiertem Richtungsvektor.
 */
public class Strahl extends GeomertrischeFigur implements Speicherbar {

    /**
     * Name des Strahls im XML-Dokument
     */
    public static final String XML_STRAHL = "strahl";

    //Basisvektor des Strahls.
    Vektor basisVektor;

    /**
     * Name des Basisvektors im XML-Dokument
     */
    public static final String XML_BASISVEKTOR = "basisVektor";

    //Richtungsvektor des Strahls. Die Länge muss auf 1 normiert werden.
    Vektor richtungsVektor;

    /**
     * Name des Richtungsvektor im XML-Dokument
     */
    public static final String XML_RICHTUNGSVEKTOR = "richtungsVektor";

    //Variable zum Speichern der Entfernung, von welchem Bildpunkt (reell oder virtuell) aus der Strahl bei Berechnung durch Hauptebenen erzeugt wird
    double quellEntfernung;

    //Wahrheitswert, der angibt, ob dieser Strahl aus dem Unendlichen zu kommen scheint.
    private boolean ausDemUnendlichen;

    /**
     * Initialisiere Strahl mit Basisvektor und Richtung und setze Quellentfernung auf 0.
     *
     * @param basisVektor Basisvektor
     * @param richtung    Richtung des Strahls. Die Richtung wird automatisch auf 1 normiert.
     */
    public Strahl(Vektor basisVektor, Vektor richtung) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = 0;
        this.ausDemUnendlichen = false;
    }

    /**
     * Initialisiere Strahl mit Quelle, aus der der Strahl zu kommen scheint, für Abbildungen durch Hauptebenen.
     *
     * @param basisVektor       Basisvektor
     * @param richtung          Richtungsvektor. Die Richtung wird automatisch auf 1 normiert.
     * @param quellEntfernung   Distanz bezüglich des Basisvektors, aus dem der Strahl zu kommen scheint.
     * @param ausDemUnendlichen Gibt an, ob der Strahl aus dem Unendlichen zu kommen scheint.
     */
    public Strahl(Vektor basisVektor, Vektor richtung, double quellEntfernung, boolean ausDemUnendlichen) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = quellEntfernung;
        this.ausDemUnendlichen = ausDemUnendlichen;
    }

    /**
     * Initialisiert Strahl über ein jdom-Element.
     *
     * @param xmlElement Element, dass die Werte enthält.
     * @throws Exception Expection, die geworfen wird, wenn bei der Initialisierung ein Fehler passiert.
     */
    public Strahl(Element xmlElement) throws Exception {
        basisVektor = new Vektor(xmlElement.getChild(XML_BASISVEKTOR));
        richtungsVektor = new Vektor(xmlElement.getChild(XML_RICHTUNGSVEKTOR));
        quellEntfernung = 0;
        ausDemUnendlichen = false;
    }

    /**
     * Gibt an welche Strecke ein Strahl zurücklegen muss bis er den jeweils anderen trifft.
     *
     * @param s2 Strahl, mit dem Schnittpunkt überprüft werden soll.
     * @return Array, mit Element 0 = Entfernung dieser Strahl, Element 1 = Entfernung übergebener Strahl. Gibt null zurück, wenn Strahlen parallel verlaufen.
     */
    public double[] gibSchnittentfernungen(Strahl s2) {
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

    /**
     * Statische Methode um den Schnittpunkt zweier Strahlen auszuwerten. Die zurückgegebenen Werte entsprechen den Distanzen, die jeder Strahl bis zum dem jeweils anderen Strahl zurücklegen muss.
     *
     * @param s1 Strahl 1.
     * @param s2 Strahls 2.
     * @return Array, mit Element 0 = Entfernung Strahl 1, Element 1 = Entfernung Strahl 2. Gibt null zurück, wenn Strahlen parallel verlaufen.
     */
    public static double[] gibSchnittentfernungen(Strahl s1, Strahl s2) {
        return s1.gibSchnittentfernungen(s2);
    }

    /**
     * Gibt den Punkt, von dem aus dieser Strahl zu kommen scheint. Wird zur Abbildung durch Hauptebenen benötigt.
     *
     * @return Quellpunkt, aus dem dieser Strahl zu kommen scheint.
     */
    public Vektor gibQuellPunkt() {
        Vektor retVektor = basisVektor.kopiere();
        retVektor.addiere(Vektor.multipliziere(richtungsVektor, quellEntfernung));
        return retVektor;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        basisVektor.addiere(verschiebung);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = new Element(getXmlElementTyp());
        xmlElement.addContent(basisVektor.getXmlElement(XML_BASISVEKTOR));
        xmlElement.addContent(richtungsVektor.getXmlElement(XML_RICHTUNGSVEKTOR));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_STRAHL;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor bisVektor = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, 3000));
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        Line2D line = new Line2D.Double(basisVektor, bisVektor);
        g.draw(line);
        if ((quellEntfernung < 0 || isAusDemUnendlichen()) && HauptFenster.get().getAktuelleOptischeBank().isVirtuelleStrahlenAktiv()) {
            double cQuellEntfernung = quellEntfernung;
            if (isAusDemUnendlichen()) {
                cQuellEntfernung = -3000;
            }
            Vektor bisVektorVirtuell = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, cQuellEntfernung));
            g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 5.0f, new float[]{10.0f, 4.0f}, 0.0f));
            Line2D lineVirtuell = new Line2D.Double(basisVektor, bisVektorVirtuell);
            g.draw(lineVirtuell);
        }
    }

    /**
     * @return Basisvektor des Strahls.
     */
    public Vektor getBasisVektor() {
        return basisVektor;
    }

    /**
     * @return Richtungsvektor des Strahls.
     */
    public Vektor getRichtungsVektor() {
        return richtungsVektor;
    }

    /**
     * @return Entfernung bezüglich Basisvektor, aus der der Strahl zu kommen scheint. Wird zur Abbildung durch Hauptebenen benötigt.
     */
    public double getQuellEntfernung() {
        return quellEntfernung;
    }

    /**
     * @return Gibt an, ob der Strahl aus dem Unendlichen zu kommen scheint. Wird zur Abbildung durch Hauptebenen benötigt.
     */
    public boolean isAusDemUnendlichen() {
        return ausDemUnendlichen;
    }

    /**
     * @param ausDemUnendlichen Gibt an, ob der Strahl aus dem Unendlichen zu kommen scheint. Wird zur Abbildung durch Hauptebenen benötigt.
     */
    public void setAusDemUnendlichen(boolean ausDemUnendlichen) {
        this.ausDemUnendlichen = ausDemUnendlichen;
    }
}

