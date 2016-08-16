package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Rahmen extends Polygon implements Zeichenbar {

    public static Color rahmenFarbe = Color.RED;

    Vektor mittelpunkt;

    public Rahmen() {
        mittelpunkt = new Vektor(0, 0);
    }

    public Rahmen(Vektor nMittelpunkt) {
        mittelpunkt = nMittelpunkt;
    }

    public Rahmen(Vektor mittelpunkt, ArrayList<Point2D.Double> punkte) {
        for(Point2D.Double punkt : punkte) {
            addPoint((int)punkt.x, (int)punkt.y);
        }
    }

    public void rahmenErweitern(Point2D.Double nPunkt) {
        addPoint((int)nPunkt.x, (int)nPunkt.y);
    }

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
