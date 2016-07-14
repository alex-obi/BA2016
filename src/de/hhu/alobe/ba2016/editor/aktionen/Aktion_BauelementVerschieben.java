package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;

public class Aktion_BauelementVerschieben extends Aktion {

    Bauelement bauelement;
    Vektor verschiebung;

    public Aktion_BauelementVerschieben(Bauelement bauelement, Vektor verschiebung) {
        this.bauelement = bauelement;
        this.verschiebung = verschiebung;
    }

    @Override
    public void aktionDurchfuehren() {
        bauelement.verschiebeUm(verschiebung);
    }

    @Override
    public void aktionRueckgaengig() {
        bauelement.verschiebeUm(Vektor.multipliziere(verschiebung, -1));
    }
}
