package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Rahmen extends Polygon implements Zeichenbar {

    /**
     * Farbe aller Rahmen.
     */
    public static Color rahmenFarbe = Color.RED;

    //Mittelpunkt des Rahmens
    private Vektor mittelpunkt;

    /**
     * Initialisiert neuen Rahmen ohne Kanten.
     */
    public Rahmen() {
        mittelpunkt = new Vektor(0, 0);
    }

    /**
     * Initialisiere neuen Rahmen ohne Kanten mit Mittelpunkt.
     *
     * @param nMittelpunkt Mittelpunkt des Rahmens.
     */
    public Rahmen(Vektor nMittelpunkt) {
        mittelpunkt = nMittelpunkt;
    }

    /**
     * Erstelle Rahmen ueber Verbindung von Punktes zu einem Polygon.
     *
     * @param mittelpunkt Mittelpunkt des Rahmens.
     * @param punkte      Punkte des Polygons.
     */
    public Rahmen(Vektor mittelpunkt, ArrayList<Point2D.Double> punkte) {
        this.mittelpunkt = mittelpunkt;
        for (Point2D.Double punkt : punkte) {
            addPoint((int) punkt.x, (int) punkt.y);
        }
    }

    /**
     * Erweitert den Rahmen mit einem neuen Punkt.
     *
     * @param nPunkt Neuer Punkt.
     */
    public void rahmenErweitern(Point2D.Double nPunkt) {
        addPoint((int) nPunkt.x, (int) nPunkt.y);
    }

    /**
     * Gibt an, ob der uebergebene Vektor in diesem Rahmen liegt.
     *
     * @param pruefVektor Position, an dem der Mauscursor liegt.
     * @return Wahrheitswert, ob Rahmen angeklickt wurde.
     */
    public boolean istVektorInRahmen(Vektor pruefVektor) {
        Vektor verschoben = Vektor.subtrahiere(pruefVektor, mittelpunkt);
        return this.contains(verschoben.getX(), verschoben.getY());
    }

    @Override
    public void paintComponent(Graphics2D g) {
        this.translate(mittelpunkt.getXint(), mittelpunkt.getYint());
        g.setColor(rahmenFarbe);
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(this);
        this.translate(-mittelpunkt.getXint(), -mittelpunkt.getYint());
    }
}
