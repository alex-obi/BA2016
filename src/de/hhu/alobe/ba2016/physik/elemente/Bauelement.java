package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorInt;

import java.awt.*;

/**
 * Ein Bauelement hat eine Interaktion mit Strahlen
 *
 */
public abstract class Bauelement extends Auswahlobjekt {

    protected OptischeBank optischeBank;

    private int typ = 0;


    public static final int TYP_LAMPE = 1;
    public static final int TYP_LINSE = 2;
    public static final int TYP_SPIEGEL = 3;
    public static final int TYP_BLENDE = 4;
    public static final int TYP_SCHIRM = 5;
    public static final int TYP_AUGE = 6;


    public Bauelement(OptischeBank optischeBank, Vektor mittelPunkt, int typ) {
        super(mittelPunkt);
        this.optischeBank = optischeBank;
        this.mittelPunkt = mittelPunkt;
        this.typ = typ;
    }

    public boolean fangModusOptischeAchseAn() {
        return (typ != Bauelement.TYP_LAMPE);
    }

    public void setzeMittelpunktNeu(Vektor nMittelpunkt) {
        verschiebeUm(Vektor.subtrahiere(nMittelpunkt, mittelPunkt));
    }

    public abstract void verschiebeUm(Vektor verschiebung);

    public int getTyp() {
        return typ;
    }

    public OptischeBank getOptischeBank() {
        return optischeBank;
    }
}
