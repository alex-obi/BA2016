package de.hhu.alobe.ba2016.physik.flaechen;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.mathe.GeomertrischeFigur;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Abstrake Klasse zum Bereitstellen von Methoden um Strahlenkollision an Flaechen zu verarbeiten.
 * Eine Flaeche ist ein Zweidimendionales Objekt, das aus einer (gekruemmten) Linie besteht.
 */
public abstract class Flaeche extends GeomertrischeFigur {

    /**
     * Speichert die Art des Berechnungsmodus, mit dem eintreffende Strahlen veraendert werden.
     */
    protected int modus;

    /**
     * Modus, in dem die Flaeche eintreffende Strahlen absorbiert, also den Strahlengang beendet.
     */
    public static final int MODUS_ABSORB = 1;

    /**
     * Modus, in dem die Flaeche eintreffende Strahlen reflektiert.
     */
    public static final int MODUS_REFLEKT = 2;

    /**
     * Modus, in dem die Flaeche eintreffende Strahlen bricht.
     */
    public static final int MODUS_BRECHUNG = 3;

    /**
     *
     */
    public static final int MODUS_SCHIRM = 4;

    /**
     * Gibt ein StrahlenKollisions Objekt zurueck, das zum Verwalten der Kollision eines Strahlengangs mit dieser Flaeche benutzt wird.
     *
     * @param cStrGng Zu ueberpruefender Strahl
     * @return StrahlenKollisions Objekt, falls Kollision existiert. null sonst.
     */
    public StrahlenKollision gibKollision(Strahlengang cStrGng) {
        if (cStrGng.getAktuellerStrahl() == null) return null;
        double entfernung = kollisionUeberpruefen(cStrGng.getAktuellerStrahl());
        if (entfernung >= Konstanten.MIND_ENTFERNUNG_STRAHL) {
            return new StrahlenKollision(entfernung, cStrGng, this);
        } else {
            return null;
        }
    }

    /**
     * Gibt die Entfernung zurueck, die der uebergebene Strahl zurueckgelegt hat, bis er auf diese Flaeche trifft.
     *
     * @param strahl Strahl, mit dem Kollision ueberprueft werden soll
     * @return Distanz, die der Strahl bis zum Auftreffen zurueckgelegt hat. Ein negativer Wert bedeutet, dass der Strahl nicht mit dieser Flaeche zusammentrifft!
     */
    public abstract double kollisionUeberpruefen(Strahl strahl);

    /**
     * Fuehrt die Kollision eines Strahlengangs auf Basis eines Kollisionspunktes aus.
     *
     * @param cStrGng  Strahlengang, der betrachtet wird
     * @param position Punkt, an dem der Strahlengang auf diese Flaeche getroffen ist
     */
    public abstract void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position);

}
