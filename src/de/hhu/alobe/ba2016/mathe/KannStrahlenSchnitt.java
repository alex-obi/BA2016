package de.hhu.alobe.ba2016.mathe;


/**
 * Interface, dass implementierende Klassen eine Funktion implementieren lässt um Schnitt mit einer Geraden zu überprüfen.
 */
public interface KannStrahlenSchnitt {

    /**
     * Gibt die Entfernung zurück, die der übergebene Strahl zurücklegen muss, bis er auf dieses Objekt trifft. Dieser Funktion muss durch implementierende Klassen implementiert werden.
     * Existieren mehrere Schnittpunkte mit diesem Objekt, so ist die kleinste Entfernung des Strahls zu einem dieser Punkte zurückzugeben. Der Wert muss aber >= 0 sein.
     * Existiert kein Schnittpunkt mit diesem Objekt, so ist -1 zurückzugeben.
     *
     * @param strahl Strahl, der auf Schnittpunkte mit diesem Objekt ausgewertet werden soll.
     * @return Distanz, die der übergebene Strahl bis zur Kollision mit diesem Objekt zurücklegt. -1, wenn kein Schnittpunkt existiert.
     */
    double gibSchnittEntfernung(Strahl strahl);

}
