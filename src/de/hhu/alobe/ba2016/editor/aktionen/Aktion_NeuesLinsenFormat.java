package de.hhu.alobe.ba2016.editor.aktionen;

import de.hhu.alobe.ba2016.physik.elemente.Linse;

public class Aktion_NeuesLinsenFormat extends Aktion {

    Linse linse;

    private double alt_brechzahl;
    private float alt_hoehe;
    private float alt_dicke;
    private float alt_radius1;
    private float alt_radius2;

    private double neu_brechzahl;
    private float neu_hoehe;
    private float neu_dicke;
    private float neu_radius1;
    private float neu_radius2;
    
    public Aktion_NeuesLinsenFormat (Linse linse, double brechzahl, float hoehe, float dicke, float radius1, float radius2) {
        this.linse = linse;

        alt_brechzahl = linse.getBrechzahl();
        alt_hoehe = linse.getHoehe();
        alt_dicke = linse.getDicke();
        alt_radius1 = linse.getRadius1();
        alt_radius2 = linse.getRadius2();

        neu_brechzahl = brechzahl;
        neu_hoehe = hoehe;
        neu_dicke = dicke;
        neu_radius1 = radius1;
        neu_radius2 = radius2;
    }
    @Override
    public void aktionDurchfuehren() {
        linse.formatNeuBestimmen(neu_brechzahl, neu_hoehe, neu_dicke, neu_radius1, neu_radius2);
    }

    @Override
    public void aktionRueckgaengig() {
        linse.formatNeuBestimmen(alt_brechzahl, alt_hoehe, alt_dicke, alt_radius1, alt_radius2);
    }
}
