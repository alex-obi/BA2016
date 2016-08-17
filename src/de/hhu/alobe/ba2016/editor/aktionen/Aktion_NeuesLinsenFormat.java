package de.hhu.alobe.ba2016.editor.aktionen;

import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;

public class Aktion_NeuesLinsenFormat extends Aktion {

    Linse linse;

    private double alt_brechzahl;
    private double alt_hoeheHaupt;
    private double alt_hoeheLinse;
    private double alt_dicke;
    private double alt_radius1;
    private double alt_radius2;

    private double neu_brechzahl;
    private double neu_hoeheHaupt;
    private double neu_hoeheLinse;
    private double neu_dicke;
    private double neu_radius1;
    private double neu_radius2;
    
    public Aktion_NeuesLinsenFormat (Linse linse, double brechzahl, double hoeheHaupt, double hoeheLinse, double dicke, double radius1, double radius2) {
        this.linse = linse;

        alt_brechzahl = linse.getBrechzahl();
        alt_hoeheHaupt = linse.getHoeheHauptebene();
        alt_hoeheLinse = linse.getHoeheLinse();
        alt_dicke = linse.getDicke();
        alt_radius1 = linse.getRadius1();
        alt_radius2 = linse.getRadius2();

        neu_brechzahl = brechzahl;
        neu_hoeheHaupt = hoeheHaupt;
        neu_hoeheLinse = hoeheLinse;
        neu_dicke = dicke;
        neu_radius1 = radius1;
        neu_radius2 = radius2;
    }
    @Override
    public void aktionDurchfuehren() {
        linse.formatNeuBestimmen(neu_brechzahl, neu_hoeheHaupt, neu_hoeheLinse, neu_dicke, neu_radius1, neu_radius2);
    }

    @Override
    public void aktionRueckgaengig() {
        linse.formatNeuBestimmen(alt_brechzahl, alt_hoeheHaupt, alt_hoeheLinse, alt_dicke, alt_radius1, alt_radius2);
    }
}
