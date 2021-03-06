package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

/**
 * Aktion zum Loeschen eines Bauelements in der Optischen Bank.
 */
public class Aktion_BauelementLoeschen implements Aktion {

    //Referenz auf die Optische Bank:
    private OptischeBank optischeBank;

    //Bauelement, das geloescht werden soll.
    private Bauelement bauelement;

    /**
     * Initialisiert die Aktion BauelementLoeschen.
     *
     * @param optischeBank Optische Bank, von der das Bauelement geloescht werden soll.
     * @param bauelement   Bauelement, das geloescht werden soll.
     */
    public Aktion_BauelementLoeschen(OptischeBank optischeBank, Bauelement bauelement) {
        this.optischeBank = optischeBank;
        this.bauelement = bauelement;
    }

    @Override
    public void aktionDurchfuehren() {
        //Loescht das Bauelement:
        optischeBank.bauelementLoeschen(bauelement);
    }

    @Override
    public void aktionRueckgaengig() {
        //Stellt das Bauelement wieder her:
        optischeBank.bauelementHinzufuegen(bauelement);
    }
}
