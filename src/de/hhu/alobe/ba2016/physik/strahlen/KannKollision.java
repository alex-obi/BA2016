package de.hhu.alobe.ba2016.physik.strahlen;


/**
 * Interface, damit implementierende Klassen mit einem Strahlengang kollidieren koennen.
 */
public interface KannKollision {

    /**
     * Gibt ein StrahlenKollisions Objekt zurueck, um die Kollision mit der implementierenden Klasse zu verarbeiten.
     * Implementierende Klassen, die mehrere Flaechen besitzen muessen zuerst auswaehlen mit welcher Flaeche der uebergebene Strahlengang zuerst kollidiert.
     *
     * @param cStrGng Betrachteter Strahlengang
     * @return StrahlenKollisions Objekt
     */
    StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng);

}
