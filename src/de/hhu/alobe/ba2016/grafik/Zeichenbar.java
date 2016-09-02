package de.hhu.alobe.ba2016.grafik;


import java.awt.*;

/**
 * Interface zum Bereitstellen der Methode paintComponent für alle Objekte die gezeichnet werden können
 */
public interface Zeichenbar {

    /**
     * Zeichne das Objekt
     *
     * @param g Graphics2D Element
     */
    void paintComponent(Graphics2D g);

}
