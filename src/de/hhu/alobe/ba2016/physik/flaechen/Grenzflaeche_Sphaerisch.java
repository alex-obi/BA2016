package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Grenzflaeche_Sphaerisch extends Grenzflaeche {

    private Kreis kreis;

    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, double radius) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius);
    }

    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, double radius, double vonWinkel, double extWinkel) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius, vonWinkel, extWinkel);
    }

    public Grenzflaeche_Sphaerisch (int modus, Vektor mittelpunkt, double radius) {
        super(modus);
        kreis = new Kreis(mittelpunkt, radius);
    }

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

    public double getRadius() {
        return kreis.getRadius();
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
