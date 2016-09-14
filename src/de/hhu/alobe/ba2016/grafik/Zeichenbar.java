package de.hhu.alobe.ba2016.grafik;


import java.awt.*;

/**
 * Interface zum Bereitstellen der Methode paintComponent fuer alle Objekte die gezeichnet werden koennen
 */
public interface Zeichenbar {

    /**
     * Zeichne das Objekt
     *
     * @param g Graphics2D Element
     */
    void paintComponent(Graphics2D g);

}
