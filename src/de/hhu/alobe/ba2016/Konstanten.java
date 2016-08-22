package de.hhu.alobe.ba2016;

/**
 * Liste globaler Konstanten
 */
public abstract class Konstanten {

    public static final String SAVE_ORDNER = "saves/";

    public static final double LICHTGESCHW = 299792458; // m/s

    public static final double MIND_ENTFERNUNG_STRAHL = 0.1f; // Mindestdistanz, die ein Strahl zurueckgelegt haben muss um Kollidieren zu koennen

    public static final double MIND_DICKE_LINSEN = 1f; //Mindestdicke, die eine Linse haben muss am dünnsten Punkt zwischen ihren brechenden Flächen.

    public static final int MAX_STRAHLLAENGE = 50; //Maximalanzahl an Strahlenteile um Endlosschleifen zu vermeiden.

    public static final int LINIENDICKE = 1;

    public static final double ZOOM_STUFE = 0.2; //Zoomveränderung pro Zoomstufe in Prozent

    public static final int OPTISCHEACHSE_FANGENTFERNUNG = 25; //Distanz, bis Bauelemente direkt auf die Achse gesetzt werden

    public static final int HAUPTEBENE_MINDESTHOEHE = 80;

    /**
     * Legt die Groesse des Fensters beim Start fest
     */
    public static final int FENSTER_X = 1024;
    public static final int FENSTER_Y = 768;


}
