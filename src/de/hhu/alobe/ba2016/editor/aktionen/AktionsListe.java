package de.hhu.alobe.ba2016.editor.aktionen;

import java.util.ArrayList;

/**
 * Liste zum Verwalten aller Benutzeraktionen mit der optischen Bank. Elemente dieser Liste sind Aktionen, die rückgängig gemacht werden können.
 * Ein Springen innerhalb dieser Liste ermöglicht also das Zurücknehmen und Wiederherstellen von Aktionen.
 */
public class AktionsListe {

    //Liste der Benutzeraktionen:
    private ArrayList<Aktion> aktionen;

    //Aktuelle Position in der Liste. -1 bedeutet, dass die Liste leer ist:
    private int aktuellePosition;

    /**
     * Initialisiert eine neue, leere Aktions Liste.
     */
    public AktionsListe() {
        aktionen = new ArrayList<>();
        aktuellePosition = -1;
    }

    /**
     * Fügt eine neue Aktion der Liste hinzu.
     *
     * @param aktion Neue Aktion.
     */
    public void neueAktion(Aktion aktion) {
        if (aktuellePosition < 0) {
            aktionen = new ArrayList<>();
            aktuellePosition = 0;
        } else {
            aktionen.subList(aktuellePosition + 1, aktionen.size()).clear();
            aktuellePosition++;
        }
        aktionen.add(aktion);
    }

    /**
     * Überschreibt die Aktion an der aktuellen Position in der Liste.
     *
     * @param aktion Neue Aktion.
     */
    public void letzteAktionUeberschreiben(Aktion aktion) {
        undo();
        neueAktion(aktion);
    }

    /**
     * Macht die Aktion an der aktuellen Position in der Liste rückgängig.
     */
    public void undo() {
        if (aktuellePosition >= 0) {
            aktionen.get(aktuellePosition).aktionRueckgaengig();
            aktuellePosition--;
        }
    }

    /**
     * Führt die Aktion der aktuellen Position in der Liste erneut aus.
     */
    public void redo() {
        if (aktuellePosition < aktionen.size() - 1 && !aktionen.isEmpty()) {
            aktuellePosition++;
            aktionen.get(aktuellePosition).aktionDurchfuehren();
        }
    }


}
