package de.hhu.alobe.ba2016.editor.eigenschaften;

/**
 * Interface um Interaktion mit einem Eigenschaftenregler zu erleichtern. Hierbei wird davon ausgegangen, dass der Regler einen prozentualen Wert einstellen kann.
 * Das Interface bietet dann Methoden um die Reglerstellung zu interpretieren und zu verändern.
 */
public interface ReglerEvent {

    /**
     * Hier sollen Aktionen implementiert werden, die bei einer Änderung der Reglerstellung ausgeführt werden sollen.
     *
     * @param wert Neuer wert der Regelgröße
     */
    void reglerWurdeVerschoben(double wert);

    /**
     * Berechnet zu einer bestimmten Reglerstellung (reglerProzent) den Wert der resultierenden Regelgröße.
     *
     * @param reglerProzent Reglerstellung in Prozent
     * @param minimum       Minimum des Reglers
     * @param maximum       Maximum des Reglers
     * @return Wert der Regelgröße
     */
    double berechneReglerWert(double reglerProzent, double minimum, double maximum);

    /**
     * Gibt an welche Reglerstellung benötigt wird, um eine bestimmte Regelgröße zu erreichen. Es ist darauf zu achten, dass die
     * Berechnung der Umkehrfunktion von 'berechneReglerWert' entspricht.
     *
     * @param wert    Neuer Wert der Regelgröße
     * @param minimum Minimum des Reglers
     * @param maximum Maximum des Reglers
     * @return Neue Reglerstellung in Prozent
     */
    double berechneReglerProzent(double wert, double minimum, double maximum);

    /**
     * Berechnet zu der Regelgröße den korrekten physikalischen Wert und gibt diesen gerundet als Zeichenkette zurück.
     *
     * @param zahl Regelgröße
     * @return Physikalischer Wert der Regelgröße
     */
    String berechnePhysikalischenWert(double zahl);
}
