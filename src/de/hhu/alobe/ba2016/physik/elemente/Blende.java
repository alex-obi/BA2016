package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Blende extends Bauelement implements KannKollision {

    float durchmesser;
    float hoehe;

    public static final float MIND_HOEHE = 10;

    Grenzflaeche obereHaelfte;
    Grenzflaeche untereHaelfte;

    public Blende(OptischeBank optischeBank, Vektor mittelPunkt, float hoehe, float durchmesser) {
        super(optischeBank, mittelPunkt, Bauelement.TYP_BLENDE);
        this.hoehe = Math.max(10, hoehe);

        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-5, hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(+5 , hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(+5, -hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(-5, -hoehe / 2));
        setRahmen(rahmen);

        setzeDurchmesser(durchmesser);
    }

    public void setzeDurchmesser(float nDurchmesser) {
        if(Math.abs(nDurchmesser) + 10 > hoehe) {
            durchmesser = hoehe - 10;
        } else {
            durchmesser = Math.abs(nDurchmesser);
        }
        Vektor vonVekt = new VektorFloat(0, durchmesser / 2);
        Vektor bisVekt = new VektorFloat(0, hoehe / 2);
        obereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, vonVekt), Vektor.addiere(mittelPunkt, bisVekt));
        untereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.subtrahiere(mittelPunkt, vonVekt), Vektor.subtrahiere(mittelPunkt, bisVekt));
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        obereHaelfte.verschiebeUm(verschiebung);
        untereHaelfte.verschiebeUm(verschiebung);
    }

    @Override
    public void waehleAus() {

    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setColor(new Color(62, 8, 0));
        obereHaelfte.paintComponent(g);
        untereHaelfte.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(obereHaelfte.gibKollision(cStrGng));
        kollisionen.add(untereHaelfte.gibKollision(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }
}