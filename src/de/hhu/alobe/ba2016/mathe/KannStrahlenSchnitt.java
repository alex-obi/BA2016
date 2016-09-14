package de.hhu.alobe.ba2016.mathe;


/**
 * Interface, dass implementierende Klassen eine Funktion implementieren laesst um Schnitt mit einer Geraden zu ueberpruefen.
 */
public interface KannStrahlenSchnitt {

    /**
     * Gibt die Entfernung zurueck, die der uebergebene Strahl zuruecklegen muss, bis er auf dieses Objekt trifft. Dieser Funktion muss durch implementierende Klassen implementiert werden.
     * Existieren mehrere Schnittpunkte mit diesem Objekt, so ist die kleinste Entfernung des Strahls zu einem dieser Punkte zurueckzugeben. Der Wert muss aber >= 0 sein.
     * Existiert kein Schnittpunkt mit diesem Objekt, so ist -1 zurueckzugeben.
     *
     * @param strahl Strahl, der auf Schnittpunkte mit diesem Objekt ausgewertet werden soll.
     * @return Distanz, die der uebergebene Strahl bis zur Kollision mit diesem Objekt zuruecklegt. -1, wenn kein Schnittpunkt existiert.
     */
    double gibSchnittEntfernung(Strahl strahl);

}
