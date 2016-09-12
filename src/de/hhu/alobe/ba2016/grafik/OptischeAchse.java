package de.hhu.alobe.ba2016.grafik;

import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Optische Achse der Optischen Bank.
 */
public class OptischeAchse implements Zeichenbar {

    /**
     * Modus um Optische Achse durchgezogen zu zeichnen.
     */
    public static final int MODUS_DURCHGEZOGEN = 1;

    /**
     * Modus um Optische Achse gestrichelt zu zeichnen.
     */
    public static final int MODUS_GESTRICHELT = 2;

    /**
     * Modus um Optische Achse gepunktet zu zeichnen.
     */
    public static final int MODUS_GEPUNKTET = 3;

    /**
     * Modus, in dem die Optische Achse gezeichnet wird.
     */
    private int modus;

    //Höhe der Optischen Bank vom oberen Fensterpunkt aus
    private double hoehe;

    /**
     * Initialisiere Optische Achse
     *
     * @param hoehe Neue Höhe
     */
    public OptischeAchse(double hoehe, int modus) {
        this.modus = modus;
        this.hoehe = hoehe;
    }

    /**
     * @return Höhe der Optischen Achse als double
     */
    public double getHoehe() {
        return hoehe;
    }

    /**
     * @return Modus zum Zeichnen
     */
    public int getModus() {
        return modus;
    }

    /**
     * @param modus Modus zum Zeichnen
     */
    public void setModus(int modus) {
        this.modus = modus;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (modus) {
            case MODUS_DURCHGEZOGEN:
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                break;
            case MODUS_GESTRICHELT:
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 5.0f, new float[] {10.0f,8.0f}, 0.0f));
                break;
            case MODUS_GEPUNKTET:
                g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 5.0f, new float[] {Konstanten.LINIENDICKE}, 0.0f));
        }
        g.draw(new Line2D.Double(0, hoehe, 3000, hoehe));
    }
}
