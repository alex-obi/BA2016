package de.hhu.alobe.ba2016.grafik;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;

import java.awt.*;
import java.awt.geom.Line2D;

public class OptischeAchse implements Zeichenbar {

    private double hoehe;

    public OptischeAchse(double hoehe) {
        this.hoehe = hoehe;
    }

    public double getHoehe() {
        return hoehe;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(new Line2D.Double(0, hoehe, 100000, hoehe));
    }
}
