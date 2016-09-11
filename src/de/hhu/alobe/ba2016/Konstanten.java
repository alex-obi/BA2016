package de.hhu.alobe.ba2016;

/**
 * Liste globaler Konstanten.
 */
public abstract class Konstanten {

    /**
     * Pfad zum Ordner, in dem lokale Optische Bänke gespeichert werden sollen.
     */
    public static final String SAVE_ORDNER = "saves/";

    /**
     * Dateiendung für Speicherdateien.
     */
    public static final String SAVE_ENDUNG = "xml";

    /**
     * Lichtgeschwindigkeit in Meter/ Sekunde.
     */
    public static final double LICHTGESCHW = 299792458;

    /**
     * Mindestentfernung, die ein Strahl zurückgelegt haben muss um mit einem neuen Objekt zu kollidieren.
     */
    public static final double MIND_ENTFERNUNG_STRAHL = 0.1f; // Mindestdistanz, die ein Strahl zurueckgelegt haben muss um Kollidieren zu koennen

    /**
     * Mindestdicke, die eine Linse haben muss am dünnsten Punkt zwischen ihren brechenden Flächen.
     * Dieser Wert muss zwingend größer sein als MIND_ENTFERNUNG_STRAHL!
     */
    public static final double MIND_DICKE_LINSEN = 1f; //

    /**
     * Maximale Anzahl an Teilgeraden eines Strahlengangs um Endlos-Strahlengänge zu vermeiden
     */
    public static final int MAX_STRAHLLAENGE = 50; //

    /**
     * Dicke, in der Standardlinien gezeichnet werden. Dünnere oder dickere Linien sollten Prozentual von diesem Wert abweichen.
     */
    public static final float LINIENDICKE = 1.5f;

    /**
     * Zoomfaktor in Prozent, um den bei jedem Zoom-Schritt hinein-/herausgezoomt wird.
     */
    public static final double ZOOM_STUFE = 0.2;

    /**
     * Distanz, bis Bauelemente direkt auf die Achse gesetzt werden.
     */
    public static final int OPTISCHEACHSE_FANGENTFERNUNG = 25;

    /**
     * Toleranzwert, ab dem g = f bei Abbildungen angenommen wird.
     */
    public static final double TOLERANZ_ABBILDUNG_UNENDLICH = 7;

    /**
     * Gibt an wieviele Pixel pro Zentimeter angezeigt werden zur Längenskalierung. Dieser Wert ist ein Schätzwert und kann von Gerät zu Gerät variieren.
     */
    public static final double PIXEL_PRO_CM = 36;

    /**
     * Legt die Breite des Fensters beim Start fest
     */
    public static final int FENSTER_X = 1024;

    /**
     * Legt die Höhe des Fensters beim Start fest
     */
    public static final int FENSTER_Y = 768;


}
