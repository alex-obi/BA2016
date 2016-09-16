package de.hhu.alobe.ba2016.mathe;

import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Vektorklasse, die Point2D.Double um Funktionen zur Vektorarithmetik ergaenzt.
 */
public class Vektor extends Point2D.Double {

    /**
     * Name der X-Koordinate im XML-Dokument.
     */
    public static final String XML_XKOORDINATE = "x";

    /**
     * Name der Y-Koordinate im XML-Dokument.
     */
    public static final String XML_YKOORDINATE = "y";

    /**
     * Initialisiere Vektor ueber die Koordinaten eines Point2D.Double.
     *
     * @param point Point2D.Double.
     */
    public Vektor(Point2D.Double point) {
        super(point.getX(), point.getY());
    }

    /**
     * Initialisiere Vektor ueber die Koordinaten eines Point.
     *
     * @param point Point.
     */
    public Vektor(Point point) {
        super(point.getX(), point.getY());
    }

    /**
     * Initialisiere Vektor ueber double Werte.
     *
     * @param x X-Koordinate.
     * @param y Y-Koordinate.
     */
    public Vektor(double x, double y) {
        super(x, y);
    }

    /**
     * Initialisiere Vektor ueber int Werte.
     *
     * @param x X-Koordinate.
     * @param y Y-Koordinate.
     */
    public Vektor(int x, int y) {
        super((double) x, (double) y);
    }

    /**
     * Initialisiere Vektor ueber jdom-Element, das die benoetigten Werte enthaelt.
     *
     * @param xmlElement jdom-Element.
     * @throws Exception Exception, die geworfen wird, wenn bei der Initialisierung ein Fehler passiert.
     */
    public Vektor(Element xmlElement) throws Exception {
        x = xmlElement.getAttribute(XML_XKOORDINATE).getDoubleValue();
        y = xmlElement.getAttribute(XML_YKOORDINATE).getDoubleValue();
    }

    /**
     * Statische Methode um zu ueberpruefen, ob zwei Vektoren gleich sind.
     *
     * @param vektor1 Vektor 1.
     * @param vektor2 Vektor 2.
     * @return Wahrheitswert, ob beide Vektoren gleiche Werte besitzen.
     */
    public static boolean sindGleich(Vektor vektor1, Vektor vektor2) {
        return vektor1.sindGleich(vektor2);
    }

    /**
     * Gibt an, ob die Werte dieses Vektors mit dem uebergebenen Vektor uebereinstimmten.
     *
     * @param vergleichsVektor Vektor, mit dem die Werte verglichen werden sollen.
     * @return Wahrheitswert, ob beide Vektoren gleiche Werte besitzen.
     */
    public boolean sindGleich(Vektor vergleichsVektor) {
        return (vergleichsVektor.getX() == x && vergleichsVektor.getY() == y);
    }

    /**
     * Statische Methode, um den Abstand zwischen zwei Vektoren zu berechnen.
     *
     * @param v1 Vektor 1.
     * @param v2 Vektor 2.
     * @return Anstamd zwischen beiden Vektoren.
     */
    public static double gibAbstand(Vektor v1, Vektor v2) {
        return v1.distance(v2);
    }

    /**
     * Berechnet die Laenge des Vektors.
     *
     * @return Laenge des Vektors als double.
     */
    public double gibLaenge() {
        return Math.pow(x * x + y * y, 0.5);
    }

    /**
     * Statische Methode um zwei Vektoren zu addieren und als neuen Vektor auszugeben.
     *
     * @param addVektor1 Vektor 1.
     * @param addVektor2 Vektor 2.
     * @return Summe der beiden Vektoren als neuer Vektor.
     */
    public static Vektor addiere(Vektor addVektor1, Vektor addVektor2) {
        Vektor retVektor = addVektor1.kopiere();
        retVektor.addiere(addVektor2);
        return retVektor;
    }

    /**
     * Addiert diesen Vektor mit dem uebergebenen Vektor.
     *
     * @param addVektor Vektor, dessen Werte auf diesen Vektor addiert werden sollen.
     */
    public void addiere(Vektor addVektor) {
        x += addVektor.getX();
        y += addVektor.getY();
    }

    /**
     * Statische Methode um zwei Vektoren zu subtrahieren.
     *
     * @param vonVektor Vektor, von dem subtrahiert werden soll.
     * @param subVektor Vektor, der subtrahiert werden soll.
     * @return Resultierender Vektor aus der Subtraktion.
     */
    public static Vektor subtrahiere(Vektor vonVektor, Vektor subVektor) {
        Vektor retVektor = vonVektor.kopiere();
        retVektor.subtrahiere(subVektor);
        return retVektor;
    }

    /**
     * Subtrahiere einen Vektor von diesem Vektor.
     *
     * @param subVektor Vektor, der subtrahiert weden soll.
     */
    public void subtrahiere(Vektor subVektor) {
        x -= subVektor.getX();
        y -= subVektor.getY();
    }

    /**
     * Skalarmultiplikation eines Vektors mit einem Skalar als double Wert.
     *
     * @param vektor Vektor.
     * @param skalar Skalar als double.
     * @return Neuer, resultierender Vektor.
     */
    public static Vektor multipliziere(Vektor vektor, double skalar) {
        Vektor retVektor = vektor.kopiere();
        retVektor.multipliziere(skalar);
        return retVektor;
    }

    /**
     * Skalarmultiplikation dieses Vektors mit einem Skalar als double Wert.
     *
     * @param skalar Skalar als double.
     */
    public void multipliziere(double skalar) {
        x = x * skalar;
        y = y * skalar;
    }

    /**
     * Skalarmultiplikation diese Vektors mit einem Skalar als int Wert.
     *
     * @param skalar Skalar als int.
     */
    public void multipliziere(int skalar) {
        x = x * (double) skalar;
        y = y * (double) skalar;
    }

    /**
     * Skalarprodukt zweier Vektoren.
     *
     * @param vektor1 Vektor 1.
     * @param vektor2 Vektor 2.
     * @return Ergebnis des Skalarprodukts als double.
     */
    public static double skalarprodukt(Vektor vektor1, Vektor vektor2) {
        return vektor1.skalarprodukt(vektor2);
    }

    /**
     * Skalarprodukt dieses Vektors mit dem uebergebenen Vektor.
     *
     * @param vektor Vektor, mit dem Skalarprodukt berechnet werden soll.
     * @return Ergebnis des Skalarprodukts als double.
     */
    public double skalarprodukt(Vektor vektor) {
        return x * vektor.getX() + y * vektor.getY();
    }

    /**
     * Erstellt einen neuen Vektor mit Werten dieses Vektors.
     *
     * @return Neuer Vektor mit gleichen Werten.
     */
    public Vektor kopiere() {
        return new Vektor(x, y);
    }

    /**
     * Normiert diesen Vektor auf Laenge 1.
     */
    public void zuEinheitsVektor() {
        double laenge = gibLaenge();
        if (laenge == 0) return;
        x = x / laenge;
        y = y / laenge;
    }

    /**
     * Erstellt den Vektor, der dem Einheitsvektor dieses Vektors entpsricht.
     *
     * @return Einheitsvektor zu diesem Vektor.
     */
    public Vektor gibEinheitsVektor() {
        Vektor retVektor = this.kopiere();
        retVektor.zuEinheitsVektor();
        return retVektor;
    }

    /**
     * Normalenvektor zu diesem Vektor als Tangentialvektor in linke Richtung.
     *
     * @return Normalenvektor zu diesem Vektor.
     */
    public Vektor gibNormalenVektor() {
        return new Vektor(-y, x);
    }

    /**
     * Gibt an, ob dieser Vektor dem Nullvektor entspricht.
     *
     * @return Wahrheitswert, ob dieser Vektor Nullvektor ist.
     */
    public boolean istNullvektor() {
        return (x == 0 && y == 0);
    }

    /**
     * Gibt den Vektor zurueck, diesem Vektor gedreht entspricht.
     *
     * @param winkel Winkel, um den Vektor in mathematisch positive Richtung gedreht werden soll.
     * @return Vektor, der durch die Drehung entsteht.
     */
    public Vektor gibGedrehtenVektor(double winkel) {
        Vektor retVektor = new Vektor(x, y);
        retVektor.dreheUmWinkel(winkel);
        return retVektor;
    }

    /**
     * Dreht diesen Vektor um den uebergebenen Winkel.
     *
     * @param winkel Winkel, um den Vektor in mathematisch positive Richtung gedreht werden soll.
     */
    public void dreheUmWinkel(double winkel) {
        double xAlt = x;
        double yAlt = y;
        x = xAlt * Math.cos(winkel) - yAlt * Math.sin(winkel);
        y = xAlt * Math.sin(winkel) + yAlt * Math.cos(winkel);
    }

    /**
     * Gibt den Winkel dieses Vektor bezueglich des Einheitsvektors in X-Richtung (1,0).
     *
     * @return Richtungswinkel.
     */
    public double gibRichtungsWinkel() {
        double richtungsWinkel = 0;
        double minWinkel = Math.acos(this.gibEinheitsVektor().getXdouble());
        if (y >= 0) {
            richtungsWinkel = minWinkel;
        } else {
            richtungsWinkel = 2 * Math.PI - minWinkel;
        }
        return richtungsWinkel;
    }

    /**
     * @return Koordinaten dieses Vektors als Point mit int Werten.
     */
    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    /**
     * @return X-Koordinate als int Wert.
     */
    public int getXint() {
        return (int) x;
    }

    /**
     * @return Y-Koordinate als int Wert.
     */
    public int getYint() {
        return (int) y;
    }

    /**
     * @return X-Koordinate als float Wert.
     */
    public float getXfloat() {
        return (float) x;
    }

    /**
     * @return Y-Koordinate als float Wert.
     */
    public float getYfloat() {
        return (float) y;
    }

    /**
     * @return X-Koordinate als double Wert.
     */
    public double getXdouble() {
        return x;
    }

    /**
     * @return Y-Koordinate als double Wert.
     */
    public double getYdouble() {
        return y;
    }

    /**
     * @param x Neue X-Koordinate als int Wert.
     */
    public void setX(int x) {
        this.x = (double) x;
    }

    /**
     * @param y Neue Y-Koordinate als int Wert.
     */
    public void setY(int y) {
        this.y = (double) y;
    }

    /**
     * @param x Neue X-Koordinate als float Wert.
     */
    public void setX(float x) {
        this.x = (double) x;
    }

    /**
     * @param y Neue Y-Koordinate als float Wert.
     */
    public void setY(float y) {
        this.y = (double) y;
    }

    /**
     * @param x Neue X-Koordinate als double Wert.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y Neue Y-Koordinate als double Wert.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Erstellt ein jdom-Element mit dem uebergebenen Namen. So koennen Vektor beliebig mit Namen versehen werden, um diese identifizieren zu koennen.
     *
     * @param name Name des Vektors im XML-Dokument.
     * @return jdom-Element dieses Vektors.
     */
    public Element getXmlElement(String name) {
        Element xmlElement = new Element(name);
        xmlElement.setAttribute(XML_XKOORDINATE, String.valueOf(x));
        xmlElement.setAttribute(XML_YKOORDINATE, String.valueOf(y));
        return xmlElement;
    }

}
