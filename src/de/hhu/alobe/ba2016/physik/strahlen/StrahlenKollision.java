package de.hhu.alobe.ba2016.physik.strahlen;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;

import java.util.ArrayList;

public class StrahlenKollision {

    //Distanz, die der aktuelle Strahl des Strahlengangs bis zur KannStrahlenSchnitt zurueckgelegt hat
    private float distanz;

    private Strahlengang strahlengang;

    private Flaeche kollFlaeche;


    public StrahlenKollision(float distanz, Strahlengang strahlengang, Flaeche kollFlaeche) {
        this.distanz = distanz;
        this.strahlengang = strahlengang;
        this.kollFlaeche = kollFlaeche;
    }

    public void kollisionDurchfuehren () {
        Vektor position = Vektor.multipliziere(strahlengang.getAktuellerStrahl().getRichtungsVektor(), distanz);
        position.addiere(strahlengang.getAktuellerStrahl().getBasisVektor());
        kollFlaeche.kollisionDurchfuehren(strahlengang, position);
    }

    public static StrahlenKollision getErsteKollision(ArrayList<StrahlenKollision> kollisionen) {
        if(kollisionen == null) return null;
        if(kollisionen.size() == 0) return null;
        StrahlenKollision cKHandler = kollisionen.get(0);
        for(int i = 1; i < kollisionen.size(); i++) {
            if(kollisionen.get(i) != null) {
                if (cKHandler == null || cKHandler.getDistanz() > kollisionen.get(i).getDistanz()) {
                    cKHandler = kollisionen.get(i);
                }
            }
        }
        return cKHandler;
    }

    public float getDistanz() {
        return distanz;
    }

    public void setDistanz(float distanz) {
        this.distanz = distanz;
    }

    public Flaeche getkollFlaeche() {
        return kollFlaeche;
    }

    public void setkollFlaeche(Flaeche kollFlaeche) {
        this.kollFlaeche = kollFlaeche;
    }

    public Strahlengang getStrahlengang() {
        return strahlengang;
    }

    public void setStrahlengang(Strahlengang strahlengang) {
        this.strahlengang = strahlengang;
    }

}
