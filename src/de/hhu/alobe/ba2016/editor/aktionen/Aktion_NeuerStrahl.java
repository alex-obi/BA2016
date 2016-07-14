package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

public class Aktion_NeuerStrahl extends Aktion{

    private Lichtquelle lichtquelle;
    private Strahlengang erzeugterStrahlengang;

    public Aktion_NeuerStrahl(Lichtquelle lichtquelle, Vektor richtung) {
        this.lichtquelle = lichtquelle;
        erzeugterStrahlengang = lichtquelle.berechneNeuenStrahl(richtung);
    }

    @Override
    public void aktionDurchfuehren() {
        lichtquelle.neuerStrahl(erzeugterStrahlengang);
    }

    @Override
    public void aktionRueckgaengig() {
        lichtquelle.loescheStrahl(erzeugterStrahlengang);
    }

}
