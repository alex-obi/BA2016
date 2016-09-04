package de.hhu.alobe.ba2016.grafik;

import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Optische Achse der Optischen Bank.
 */
public class OptischeAchse implements Zeichenbar {

    //Höhe der Optischen Bank vom oberen Fensterpunkt aus
    private double hoehe;

    /**
     * Initialisiere Optische Achse
     *
     * @param hoehe Neue Höhe
     */
    public OptischeAchse(double hoehe) {
        this.hoehe = hoehe;
    }

    /**
     * @return Höhe der Optischen Achse als double
     */
    public double getHoehe() {
        return hoehe;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(new Line2D.Double(0, hoehe, 100000, hoehe));
    }
}
