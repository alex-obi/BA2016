package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Aktion zum Loeschen eines Strahlengangs aus einer Lichtquelle.
 */
public class Aktion_StrahlengangLoeschen implements Aktion {

    //Zu loeschender Strahlengang:
    private Strahlengang strahlengang;

    //Zugehoerige Lichtquelle:
    private Lichtquelle lichtquelle;

    /**
     * Initialisiert die Aktion StrahlengangLoeschen.
     *
     * @param lichtquelle  Zum Strahlengang zugehoerige Lichtquelle.
     * @param strahlengang Strahlengang, der geloescht werden soll.
     */
    public Aktion_StrahlengangLoeschen(Lichtquelle lichtquelle, Strahlengang strahlengang) {
        this.strahlengang = strahlengang;
        this.lichtquelle = lichtquelle;
    }

    @Override
    public void aktionDurchfuehren() {
        //Loesche den Strahlengang aus der Lichtquelle:
        lichtquelle.loescheStrahl(strahlengang);
    }

    @Override
    public void aktionRueckgaengig() {
        //Fuege den Strahlengang wieder in die Lichtquelle ein:
        lichtquelle.neuerStrahl(strahlengang);
    }

}
