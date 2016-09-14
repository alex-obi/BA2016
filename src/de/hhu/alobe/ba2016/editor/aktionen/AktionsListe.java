package de.hhu.alobe.ba2016.editor.aktionen;

import java.util.ArrayList;

/**
 * Liste zum Verwalten aller Benutzeraktionen mit der Optischen Bank. Elemente dieser Liste sind Aktionen, die rueckgaengig gemacht werden koennen.
 * Ein Springen innerhalb dieser Liste ermoeglicht also das Zuruecknehmen und Wiederherstellen von Aktionen.
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
     * Fuegt eine neue Aktion der Liste hinzu.
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
     * Ueberschreibt die Aktion an der aktuellen Position in der Liste. Die alte Aktion wird zuvor rueckgaengig gemacht.
     *
     * @param aktion Neue Aktion.
     */
    public void letzteAktionUeberschreiben(Aktion aktion) {
        undo();
        neueAktion(aktion);
    }

    /**
     * Macht die Aktion an der aktuellen Position in der Liste rueckgaengig.
     */
    public void undo() {
        if (aktuellePosition >= 0) {
            aktionen.get(aktuellePosition).aktionRueckgaengig();
            aktuellePosition--;
        }
    }

    /**
     * Fuehrt die Aktion der aktuellen Position in der Liste erneut aus.
     */
    public void redo() {
        if (aktuellePosition < aktionen.size() - 1 && !aktionen.isEmpty()) {
            aktuellePosition++;
            aktionen.get(aktuellePosition).aktionDurchfuehren();
        }
    }


}
