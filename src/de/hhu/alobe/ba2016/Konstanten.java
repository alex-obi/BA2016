package de.hhu.alobe.ba2016;

/**
 * Liste globaler Konstanten
 */
public abstract class Konstanten {

    public static final float LICHTGESCHW = 299792458; // m/s

    public static final float MIND_ENTFERNUNG_STRAHL = 0.5f; // Mindestdistanz, die ein Strahl zurueckgelegt haben muss um Kollidieren zu koennen

    public static final float MIND_DICKE_LINSEN = 1f; //Mindestdicke, die eine Linse haben muss am dünnsten Punkt zwischen ihren brechenden Flächen.

    public static final int MAX_STRAHLLAENGE = 50; //Maximalanzahl an Strahlenteile um Endlosschleifen zu vermeiden.

    public static final int LINIENDICKE = 1;

    public static final double ZOOM_STUFE = 0.1; //Zoomveränderung pro Zoomstufe in Prozent

    public static final int OPTISCHEACHSE_FANGENTFERNUNG = 25; //Distanz, bis Bauelemente direkt auf die Achse gesetzt werden

    public static final double BRECHZAHL_KAMMERWASSER = 1.336;
    /**
     * Legt die Groesse des Fensters beim Start fest
     */
    public static final int FENSTER_X = 1024;
    public static final int FENSTER_Y = 768;


}
