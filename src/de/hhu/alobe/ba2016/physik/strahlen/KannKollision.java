package de.hhu.alobe.ba2016.physik.strahlen;


/**
 * Interface, damit implementierende Klassen mit einem Strahlengang kollidieren können.
 */
public interface KannKollision {

    /**
     * Gibt ein StrahlenKollisions Objekt zurück, um die Kollision mit der implementierenden Klasse zu verarbeiten.
     * Implementierende Klassen, die mehrere Flächen besitzen müssen zuerst auswählen mit welcher Fläche der übergebene Strahlengang zuerst kollidiert.
     * @param cStrGng Betrachteter Strahlengang
     * @return StrahlenKollisions Objekt
     */
    StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng);

}
