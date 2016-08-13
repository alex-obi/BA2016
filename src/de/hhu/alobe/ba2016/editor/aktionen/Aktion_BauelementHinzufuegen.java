package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

public class Aktion_BauelementHinzufuegen extends Aktion {

    private OptischeBank optischeBank;
    private Bauelement bauelement;

    public Aktion_BauelementHinzufuegen(OptischeBank optischeBank, Bauelement bauelement) {
        this.optischeBank = optischeBank;
        this.bauelement = bauelement;
    }

    @Override
    public void aktionDurchfuehren() {
        optischeBank.bauelementHinzufuegen(bauelement);
    }

    @Override
    public void aktionRueckgaengig() {
        optischeBank.bauelementLoeschen(bauelement);
    }
}
