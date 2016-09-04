package de.hhu.alobe.ba2016.editor.aktionen;

/**
 * Interface Aktion stellt sicher, dass implementierende Klassen eine Aktion durchführen, aber auch wieder rückgängig machen können.
 */
public interface Aktion {

    /**
     * Führe die Aktion durch.
     */
    void aktionDurchfuehren();

    /**
     * Mache die Aktion wieder rückgängig.
     */
    void aktionRueckgaengig();

}
