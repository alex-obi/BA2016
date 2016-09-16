package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

/**
 * Aktion zum Hinzufuegen eines neuen Bauelements zur Optischen Bank.
 */
public class Aktion_BauelementHinzufuegen implements Aktion {

    //Referenz auf Optische Bank.
    private OptischeBank optischeBank;

    //Bauelement, dass hinzugefuegt werden soll.
    private Bauelement bauelement;

    /**
     * Initialisiert die Aktion BauelementHinzufuegen.
     *
     * @param optischeBank Optische Bank, zu der das Bauelement hinzugefuegt werden soll.
     * @param bauelement   Bauelement, das hinzugefuegt werden soll.
     */
    public Aktion_BauelementHinzufuegen(OptischeBank optischeBank, Bauelement bauelement) {
        this.optischeBank = optischeBank;
        this.bauelement = bauelement;
    }

    @Override
    public void aktionDurchfuehren() {
        //Fuegt das Bauelement der Optische Bank hinzu:
        optischeBank.bauelementHinzufuegen(bauelement);
    }

    @Override
    public void aktionRueckgaengig() {
        //Loescht das Bauelement wieder aus der Optischen Bank:
        optischeBank.bauelementLoeschen(bauelement);
    }
}
