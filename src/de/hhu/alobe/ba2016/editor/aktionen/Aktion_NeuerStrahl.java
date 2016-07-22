package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

public class Aktion_NeuerStrahl extends Aktion{

    private Lichtquelle lichtquelle;
    private Strahlengang erzeugterStrahlengang;

    public Aktion_NeuerStrahl(Lichtquelle lichtquelle, Strahlengang erzeugterStrahlengang) {
        this.lichtquelle = lichtquelle;
        this.erzeugterStrahlengang = erzeugterStrahlengang;
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
