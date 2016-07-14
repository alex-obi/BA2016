package de.hhu.alobe.ba2016.editor.aktionen;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

/**
 * Created by Alexander on 18.06.2016.
 */
public class Aktion_BauelementLoeschen extends Aktion {

    OptischeBank optischeBank;
    Bauelement bauelement;

    public Aktion_BauelementLoeschen(OptischeBank optischeBank, Bauelement bauelement) {
        this.optischeBank = optischeBank;
        this.bauelement = bauelement;
    }

    @Override
    public void aktionDurchfuehren() {
        optischeBank.bauelementLoeschen(bauelement);
    }

    @Override
    public void aktionRueckgaengig() {
        optischeBank.bauelementHinzufuegen(bauelement);
    }
}
