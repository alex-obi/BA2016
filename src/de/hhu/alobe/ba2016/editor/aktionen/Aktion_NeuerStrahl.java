package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Aktion zum Erstellen eines neuen Strahlengangs von einer Lichtquelle.
 */
public class Aktion_NeuerStrahl implements Aktion {

    //Zugehörige Lichtquelle:
    private Lichtquelle lichtquelle;

    //Strahlengang, der erzeugt wird:
    private Strahlengang erzeugterStrahlengang;

    /**
     * Initialisiert die Aktion NeuerStrahl
     *
     * @param lichtquelle           Lichtquelle, von der der Strahlengang erzeugt wird.
     * @param erzeugterStrahlengang Strahlengang, der erzeugt wird.
     */
    public Aktion_NeuerStrahl(Lichtquelle lichtquelle, Strahlengang erzeugterStrahlengang) {
        this.lichtquelle = lichtquelle;
        this.erzeugterStrahlengang = erzeugterStrahlengang;
    }

    @Override
    public void aktionDurchfuehren() {
        //Füge einen neuen Strahl zur Lichtquelle hinzu:
        lichtquelle.neuerStrahl(erzeugterStrahlengang);
    }

    @Override
    public void aktionRueckgaengig() {
        //Lösche den Strahl wieder aus der Lichtquelle:
        lichtquelle.loescheStrahl(erzeugterStrahlengang);
    }

}
