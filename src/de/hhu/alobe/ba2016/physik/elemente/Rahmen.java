package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorInt;

import java.awt.*;
import java.util.ArrayList;

public class Rahmen extends Polygon implements Zeichenbar {

    public static Color rahmenFarbe = Color.RED;

    Vektor mittelpunkt;

    public Rahmen() {
        mittelpunkt = new VektorInt(0, 0);
    }

    public Rahmen(Vektor nMittelpunkt) {
        mittelpunkt = nMittelpunkt;
    }

    public Rahmen(Vektor mittelpunkt, ArrayList<Vektor> punkte) {
        for(Vektor punkt : punkte) {
            addPoint(punkt.getXint(), punkt.getXint());
        }
    }

    public void rahmenErweitern(Vektor nPunkt) {
        addPoint(nPunkt.getXint(), nPunkt.getYint());
    }

    public boolean istVektorInRahmen(Vektor pruefVektor) {
        Vektor verschoben = Vektor.subtrahiere(pruefVektor, mittelpunkt);
        return this.contains(verschoben.getXint(), verschoben.getYint());
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
