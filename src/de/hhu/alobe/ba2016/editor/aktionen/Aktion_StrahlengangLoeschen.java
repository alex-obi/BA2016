package de.hhu.alobe.ba2016.editor.aktionen;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

public class Aktion_StrahlengangLoeschen extends Aktion {

    OptischeBank optischeBank;
    Strahlengang strahlengang;
    Lichtquelle lichtquelle;

    public Aktion_StrahlengangLoeschen(OptischeBank optischeBank, Lichtquelle lichtquelle, Strahlengang strahlengang) {
        this.optischeBank = optischeBank;
        this.strahlengang = strahlengang;
        this.lichtquelle = lichtquelle;
    }

    @Override
    public void aktionDurchfuehren() {
        lichtquelle.loescheStrahl(strahlengang);
    }

    @Override
    public void aktionRueckgaengig() {
        lichtquelle.neuerStrahl(strahlengang);
    }

}
