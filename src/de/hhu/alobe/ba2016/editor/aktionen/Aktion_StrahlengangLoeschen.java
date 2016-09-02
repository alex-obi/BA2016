package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Aktion zum Löschen eines Strahlengangs aus einer Lichtquelle
 */
public class Aktion_StrahlengangLoeschen implements Aktion {

    //Zu löschender Strahlengang:
    private Strahlengang strahlengang;

    //Zugehörige Lichtquelle:
    private Lichtquelle lichtquelle;

    /**
     * Initialisiert die Aktion StrahlengangLoeschen.
     *
     * @param lichtquelle  Zum Strahlengang zugehörige Lichtquelle.
     * @param strahlengang Strahlengang, der gelöscht werden soll.
     */
    public Aktion_StrahlengangLoeschen(Lichtquelle lichtquelle, Strahlengang strahlengang) {
        this.strahlengang = strahlengang;
        this.lichtquelle = lichtquelle;
    }

    @Override
    public void aktionDurchfuehren() {
        //Lösche den Strahlengang aus der Lichtquelle:
        lichtquelle.loescheStrahl(strahlengang);
    }

    @Override
    public void aktionRueckgaengig() {
        //Füge den Strahlengang wieder in die Lichtquelle ein:
        lichtquelle.neuerStrahl(strahlengang);
    }

}
