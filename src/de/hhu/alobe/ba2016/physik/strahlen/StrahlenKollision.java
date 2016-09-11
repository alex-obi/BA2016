package de.hhu.alobe.ba2016.physik.strahlen;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;

import java.util.ArrayList;

/**
 * Klasse zum Verwalten der Kollision eines Strahls mit einer Fläche
 */
public class StrahlenKollision {

    //Distanz, die der aktuelle Strahl des Strahlengangs bis zur Fläche zurückgelegt hat
    private double distanz;

    //Strahlengang, der betrachtet wird
    private Strahlengang strahlengang;

    //Fläche, mit dem der Strahlengang kollidiert ist
    private Flaeche kollFlaeche;

    /**
     * Initialisiert ein neues StrahlenKollisions Objekt
     *
     * @param distanz      Distanz, die der aktuelle Strahl des Strahlengangs bis zur Fläche zurueckgelegt hat
     * @param strahlengang Strahlengang, der betrachtet wird
     * @param kollFlaeche  Fläche, mit dem der Strahlengang kollidiert ist
     */
    public StrahlenKollision(double distanz, Strahlengang strahlengang, Flaeche kollFlaeche) {
        this.distanz = distanz;
        this.strahlengang = strahlengang;
        this.kollFlaeche = kollFlaeche;
    }

    /**
     * Führt die Kollision des Strahls mit der übergebenen Fläche aus. Es ist sinnvoll vor Aufruf dieser Funktion zu überprüfen, dass dise Kollision
     * wirklich die erste Kollision des aktuellen Strahls des Strahlengangs mit einer Fläche ist.
     */
    public void kollisionDurchfuehren() {
        Vektor position = Vektor.multipliziere(strahlengang.getAktuellerStrahl().getRichtungsVektor(), distanz);
        position.addiere(strahlengang.getAktuellerStrahl().getBasisVektor());
        kollFlaeche.kollisionDurchfuehren(strahlengang, position);
    }

    /**
     * Statische Methode, um aus einer Liste von Strahlen Kollisionen das Element zu finden, das die kleinste zurückgelegte Distanz des aktuellen Strahls des Strahlengangs hat.
     *
     * @param kollisionen Liste von StrahlenKollisions Objekten. null als Objekt in der Liste erlaubt.
     * @return Das StrahlenKollisions Objekt mit der kleinsten Distanz
     */
    public static StrahlenKollision getErsteKollision(ArrayList<StrahlenKollision> kollisionen) {
        if (kollisionen == null) return null;
        if (kollisionen.size() == 0) return null;
        StrahlenKollision cKHandler = kollisionen.get(0);
        for (int i = 1; i < kollisionen.size(); i++) {
            if (kollisionen.get(i) != null) {
                if (cKHandler == null || cKHandler.getDistanz() > kollisionen.get(i).getDistanz()) {
                    cKHandler = kollisionen.get(i);
                }
            }
        }
        return cKHandler;
    }

    /**
     * @return Distanz, die der aktuelle Strahl des Strahlengangs bis zur Fläche zurueckgelegt hat
     */
    public double getDistanz() {
        return distanz;
    }

    /**
     * @return Strahlengang, der betrachtet wird
     */
    public Strahlengang getStrahlengang() {
        return strahlengang;
    }

}
