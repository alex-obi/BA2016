package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Kreis;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;

public class Grenzflaeche_Sphaerisch extends Grenzflaeche {

    private Kreis kreis;

    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, float radius) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius);
    }

    public Grenzflaeche_Sphaerisch (int modus, double n1, double n2, Vektor mittelpunkt, float radius, double vonWinkel, double extWinkel) {
        super(modus, n1, n2);
        kreis = new Kreis(mittelpunkt, radius, vonWinkel, extWinkel);
    }

    public Grenzflaeche_Sphaerisch (int modus, Vektor mittelpunkt, float radius) {
        super(modus);
        kreis = new Kreis(mittelpunkt, radius);
    }

    public Grenzflaeche_Sphaerisch (int modus, Vektor mittelpunkt, float radius, double vonWinkel, double extWinkel) {
        super(modus);
        kreis = new Kreis(mittelpunkt, radius, vonWinkel, extWinkel);
    }

    @Override
    public Vektor gibTangentialVektor(Vektor position) {
        return Vektor.multipliziere(this.gibNormalenVektor(position).gibNormalenVektor(), -1);
    }

    @Override
    public Vektor gibNormalenVektor(Vektor position) {
        Vektor normalVektor = Vektor.subtrahiere(position, kreis.getMittelpunkt());
        return normalVektor.gibEinheitsVektor();
    }

    @Override
    public float kollisionUeberpruefen(Strahlengang cStrGng) {
        return kreis.gibSchnittEntfernung(cStrGng.getAktuellerStrahl());
    }

    public float getRadius() {
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
