package de.hhu.alobe.ba2016.editor.aktionen;

/**
 * Interface Aktion stellt sicher, dass implementierende Klassen eine Aktion durchfuehren, aber auch wieder rueckgaengig machen koennen.
 */
public interface Aktion {

    /**
     * Fuehre die Aktion durch.
     */
    void aktionDurchfuehren();

    /**
     * Mache die Aktion wieder rueckgaengig.
     */
    void aktionRueckgaengig();

}
