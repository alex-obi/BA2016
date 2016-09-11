package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;

/**
 * Eine Grenzfläche als Kreis oder Teilstück eines Kreises/ Kreisbogen.
 */
public class Grenzflaeche_Sphaerisch extends Grenzflaeche {

    //Fläche als Kreis
    private Kreis kreis;

    /**
     * Initialisiert Grenzfläche mit Brechzahlen als geschlossener Kreis.
     * @param modus Berechnungsmodus der Fläche.
     * @param n1         Brechzahl aussen (In Richtung des Normalvektors).
     * @param n2         Brechzahl innen.
     * @param mittelpunkt Mittelpunkt des Kreises.
     * @param radius Radius des Kreises.
     */
    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, double radius) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius);
    }

    /**
     * Initialisiert Grenzfläche mit Brechzahlen und als Kreisbogen.
     * @param modus Berechnungsmodus der Fläche.
     * @param n1         Brechzahl aussen (In Richtung des Normalvektors).
     * @param n2         Brechzahl innen.
     * @param mittelpunkt Mittelpunkt des Kreises.
     * @param radius Radius des Kreises.
     * @param vonWinkel Startwinkel des Kreises (in rad).
     * @param extWinkel Länge des Kreisbogens als Winkel (in rad).
     */
    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, double radius, double vonWinkel, double extWinkel) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius, vonWinkel, extWinkel);
    }

    /**
     * Initialisiert Grenzfläche als geschlossener Kreis und ohne Brechzahlen (für Reflexion und Absorbtion).
     * @param modus Berechnungsmodus der Fläche.
     * @param mittelpunkt Mittelpunkt des Kreises.
     * @param radius Radius des Kreises.
     */
    public Grenzflaeche_Sphaerisch (int modus, Vektor mittelpunkt, double radius) {
        super(modus);
        kreis = new Kreis(mittelpunkt, radius);
    }

    /**
     * Initialisiert Grenzfläche als Kreisbogen ohne Brechzahlen (für Reflexion und Absorbtion).
     * @param modus Berechnungsmodus der Fläche.
     * @param mittelpunkt Mittelpunkt des Kreises.
     * @param radius Radius des Kreises.
     * @param vonWinkel Startwinkel des Kreises (in rad).
     * @param extWinkel Länge des Kreisbogens als Winkel (in rad).
     */
    public Grenzflaeche_Sphaerisch (int modus, Vektor mittelpunkt, double radius, double vonWinkel, double extWinkel) {
        super(modus);
        kreis = new Kreis(mittelpunkt, radius, vonWinkel, extWinkel);
    }

    @Override
    public Vektor gibNormalenVektor(Vektor position) {
        Vektor normalVektor = Vektor.subtrahiere(position, kreis.getMittelpunkt());
        return normalVektor.gibEinheitsVektor();
    }

    @Override
    public double kollisionUeberpruefen(Strahl strahl) {
        return kreis.gibSchnittEntfernung(strahl);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        kreis.verschiebeUm(verschiebung);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        kreis.paintComponent(g);
    }
}
