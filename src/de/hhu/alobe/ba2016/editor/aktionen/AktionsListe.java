package de.hhu.alobe.ba2016.editor.aktionen;


import de.hhu.alobe.ba2016.editor.OptischeBank;

import java.util.ArrayList;

public class AktionsListe {

    private ArrayList<Aktion> aktionen;
    private int aktuellePosition;

    public AktionsListe() {
        aktionen = new ArrayList<>();
        aktuellePosition = -1;
    }

    public void neueAktion(Aktion aktion) {
        if(aktuellePosition < 0) {
            aktionen = new ArrayList<>();
            aktuellePosition = 0;
        } else {
            aktionen.subList(aktuellePosition + 1, aktionen.size()).clear();
            aktuellePosition++;
        }
        aktionen.add(aktion);
    }

    public void letzteAktionUeberschreiben(Aktion aktion) {
        undo();
        neueAktion(aktion);
    }

    public void undo() {
        if(aktuellePosition >= 0) {
            aktionen.get(aktuellePosition).aktionRueckgaengig();
            aktuellePosition--;
        }
    }

    public void redo() {
        if(aktuellePosition < aktionen.size() - 1 && !aktionen.isEmpty()) {
            aktuellePosition++;
            aktionen.get(aktuellePosition).aktionDurchfuehren();
        }
    }


}
