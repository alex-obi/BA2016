package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

/**
 * Aktion zum Verschieben eines Bauelements in der Optischen Bank.
 */
public class Aktion_BauelementVerschieben implements Aktion {

    //Referenz auf das Bauelement:
    private Bauelement bauelement;

    //Vektor, um den das Bauelement verschoben werden soll:
    private Vektor verschiebung;

    /**
     * Initialisiert die Aktion BauelmentVerschieben.
     *
     * @param bauelement   Bauelement, das verschoben werden soll.
     * @param verschiebung Vektor, um den das Bauelement verschoben werden soll.
     */
    public Aktion_BauelementVerschieben(Bauelement bauelement, Vektor verschiebung) {
        this.bauelement = bauelement;
        this.verschiebung = verschiebung;
    }

    @Override
    public void aktionDurchfuehren() {
        //Verschiebt das Bauelement um den Vektor verschiebung:
        bauelement.verschiebeUm(verschiebung);
    }

    @Override
    public void aktionRueckgaengig() {
        //Verschiebt das Bauelement entgegen der Richtung des Vektors verschiebung:
        bauelement.verschiebeUm(Vektor.multipliziere(verschiebung, -1));
    }
}
