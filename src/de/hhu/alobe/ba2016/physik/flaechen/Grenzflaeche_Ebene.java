package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;

/**
 * Stellt eine reale Grenzfläche als zweidimensionale Ebene, also einer Geraden, dar.
 */
public class Grenzflaeche_Ebene extends Grenzflaeche {

    //Fläche als Gerade
    private Gerade ebene;

    /**
     * Initialisiert Grenzfläche mit Brechzahlen. Die Ebene wird durch Anfangs- und Endpunkt definiert.
     *
     * @param modus      Berechnungsmodus der Fläche.
     * @param n1         Brechzahl aussen (In Richtung des Normalvektors).
     * @param n2         Brechzahl innen.
     * @param von_Vektor Anfangspunkt der Ebene.
     * @param bis_Vektor Endpunkt der Ebene.
     */
    public Grenzflaeche_Ebene(int modus, double n1, double n2, Vektor von_Vektor, Vektor bis_Vektor) {
        super(modus, n1, n2);
        this.ebene = new Gerade(von_Vektor, bis_Vektor);
    }

    /**
     * Initialisiert Grenzfäche ohne Brechzahlen (Für Reflektion und Absorbtion). Die Ebene wird durch Anfangs- und Endpunkt definiert.
     *
     * @param modus      Berechnungsmodus der Fläche.
     * @param von_Vektor Anfangspunkt der Ebene.
     * @param bis_Vektor Endpunkt der Ebene.
     */
    public Grenzflaeche_Ebene(int modus, Vektor von_Vektor, Vektor bis_Vektor) {
        super(modus);
        this.ebene = new Gerade(von_Vektor, bis_Vektor);
    }

    /**
     * @return Kollisionsgerade der Grenzflaeche
     */
    public Gerade getEbene() {
        return ebene;
    }

    @Override
    public Vektor gibNormalenVektor(Vektor position) {
        return ebene.getRichtungsVektor().gibNormalenVektor();
    }

    @Override
    public double kollisionUeberpruefen(Strahl strahl) {
        return ebene.gibSchnittEntfernung(strahl);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        ebene.paintComponent(g);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        ebene.verschiebeUm(verschiebung);
    }

}
