package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;

public class Grenzflaeche_Ebene extends Grenzflaeche{


    private Gerade ebene;

    public Grenzflaeche_Ebene (int modus, double n1, double n2, Vektor von_Vektor, Vektor bis_Vektor) {
        super(modus, n1, n2);
        this.ebene = new Gerade(von_Vektor, bis_Vektor);
    }

    public Grenzflaeche_Ebene(int modus, Vektor von_Vektor, Vektor bis_Vektor) {
        super(modus);
        this.ebene = new Gerade(von_Vektor, bis_Vektor);
    }

    @Override
    public Vektor gibTangentialVektor(Vektor position) {
        return ebene.getRichtungsVektor();
    }

    @Override
    public Vektor gibNormalenVektor(Vektor position) {
        return ebene.getRichtungsVektor().gibNormalenVektor();
    }

    @Override
    public float kollisionUeberpruefen(Strahlengang cStrGng) {
        return ebene.gibSchnittEntfernung(cStrGng.getAktuellerStrahl());
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
