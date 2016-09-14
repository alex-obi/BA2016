package de.hhu.alobe.ba2016.mathe;


import de.hhu.alobe.ba2016.grafik.Zeichenbar;

/**
 * Abstrakte Klasse einer Geometrischesn Figur. Erbende Klassen koennen verschoben und gezeichnet werden.
 */
public abstract class GeomertrischeFigur implements Zeichenbar {

    /**
     * Die Geometrische Figur wird um den uebergebenen Vektor verschoben. Diese Funktion ist explizit durch erbende Klassen zu implementieren.
     *
     * @param verschiebung Vektor, um den die Geometrische Figur verschoben wird.
     */
    public abstract void verschiebeUm(Vektor verschiebung);

}
