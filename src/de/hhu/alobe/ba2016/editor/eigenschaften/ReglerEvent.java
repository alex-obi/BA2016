package de.hhu.alobe.ba2016.editor.eigenschaften;

import de.hhu.alobe.ba2016.Konstanten;

/**
 * Abstrakte Klasse um Interaktion mit einem Eigenschaftenregler zu erleichtern. Hierbei wird davon ausgegangen, dass der Regler einen prozentualen Wert einstellen kann.
 * Erbende Klassen bieten dann Methoden um die Reglerstellung zu interpretieren und zu verändern.
 * Diese Klasse beinhaltet statische Methoden, die benutzt werden können, um die Regelgröß interpretieren und verändern zu könnnen.
 */
public abstract class ReglerEvent {

    /**
     * Hier sollen Aktionen implementiert werden, die bei einer Änderung der Reglerstellung ausgeführt werden sollen.
     *
     * @param wert Neuer Wert der Regelgröße
     */
    public abstract void reglerWurdeVerschoben(double wert);

    /**
     * Berechnet zu einer bestimmten Reglerstellung (reglerProzent: 0.0 bis 1.0) den Wert der resultierenden Regelgröße.
     *
     * @param reglerProzent Reglerstellung in Prozent
     * @param minimum       Minimum des Reglers
     * @param maximum       Maximum des Reglers
     * @return Wert der Regelgröße
     */
    public abstract double berechneReglerWert(double reglerProzent, double minimum, double maximum);

    /**
     * Gibt an welche Reglerstellung benötigt wird, um eine bestimmte Regelgröße zu erreichen. Es ist darauf zu achten, dass die
     * Berechnung der Umkehrfunktion von 'berechneReglerWert' entspricht.
     *
     * @param wert    Neuer Wert der Regelgröße
     * @param minimum Minimum des Reglers
     * @param maximum Maximum des Reglers
     * @return Neue Reglerstellung in Prozent
     */
    public abstract double berechneReglerProzent(double wert, double minimum, double maximum);

    /**
     * Berechnet zu der Regelgröße den korrekten physikalischen Wert und gibt diesen gerundet als Zeichenkette zurück.
     *
     * @param zahl Regelgröße
     * @return Physikalischer Wert der Regelgröße
     */
    public abstract String berechnePhysikalischenWert(double zahl);

    /**
     * Berechnet einen prozentualen Wert für einen Radius zur Benutzung in einem Eigenschaftenregler.
     * Rundet auf 0.0/ 1.0 bei Überschreiten des gültigen Prozentbereichs.
     *
     * @param radius     Radius
     * @param mindRadius Mindest Radius
     * @param maxRadius  Maximal Radius
     * @return Prozentualer Wert (0.0 bis 1.0)
     */
    public static double radiusZuProzent(double radius, double mindRadius, double maxRadius) {
        if (radius == 0 || radius > maxRadius) return 0.5;
        double prozent = ((0.5 * mindRadius) / radius) + 0.5;
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    /**
     * Berechnet aus einem Prozentualer Wert (0.0 bis 1.0) eines Eigenschaftenreglers den Wert für einen Radius.
     *
     * @param prozent    Prozentualer Wert (0.0 bis 1.0) des JSliders
     * @param mindRadius Mindest Radius
     * @return Wert für Radius
     */
    public static double prozentZuRadius(double prozent, double mindRadius) {
        if (prozent == 0.5) return 0;
        double radius = (0.5 * mindRadius) / (prozent - 0.5);
        radius = Math.signum(radius) * Math.max(mindRadius, Math.abs(radius));
        return radius;
    }

    /**
     * Berechnet aus einem Wert den Prozentualer Wert (0.0 bis 1.0) innerhalb des Intervalls [minimum, maximum].
     * Rundet auf 0.0/ 1.0 bei Überschreitendes gültigen Prozentbereichs.
     *
     * @param wert    Wert
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Prozentualer Wert (0.0 bis 1.0)
     */
    public static double linearZuProzent(double wert, double minimum, double maximum) {
        double prozent = (wert - minimum) / (maximum - minimum);
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    /**
     * Berechnet zu einem Prozentualer Wert (0.0 bis 1.0) den Wert innerhalb des Intervalls [minimum, maximum]. Rundet auf minimum/ maximum bei Überschreiten.
     *
     * @param prozent Prozentualer Wert (0.0 bis 1.0)
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Wert berechnet aus dem Intervall [minimum, maximum]
     */
    public static double prozentZuLinear(double prozent, double minimum, double maximum) {
        double wert = (((maximum - minimum) * prozent) + minimum);
        wert = Math.min(maximum, Math.max(minimum, wert));
        return wert;
    }

    /**
     * Berechnet den Prozentualer Wert (0.0 bis 1.0) einer Brennweite zum Benutzen in einem Eigenschaftenregler. Rundet bei Überschreiten auf 0/ 1.
     *
     * @param wert    Brennweite
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Prozentualer Wert (0.0 bis 1.0)
     */
    public static double brennweiteZuProzent(double wert, double minimum, double maximum) {
        double prozent = 0.5;
        if (wert >= minimum) {
            prozent = Math.sqrt(0.25 / (maximum - minimum)) * Math.sqrt(wert - minimum) + 0.5;
        } else if (wert <= minimum) {
            prozent = -Math.sqrt(0.25 / (maximum - minimum)) * Math.sqrt(-wert - minimum) + 0.5;
        }
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    /**
     * Berechnet zu einem Prozentualer Wert (0.0 bis 1.0) eines Eigenschaftenreglers die Brennweite.
     *
     * @param prozent Prozentualer Wert (0.0 bis 1.0)
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Brennweite aus dem Intervall
     */
    public static double prozentZuBrennweite(double prozent, double minimum, double maximum) {
        double wert;
        if (prozent >= 0.5) {
            wert = (Math.pow(prozent - 0.5, 2) * ((maximum - minimum) / 0.25)) + minimum;
        } else {
            wert = -((Math.pow(prozent - 0.5, 2) * ((maximum - minimum) / 0.25)) + minimum);
        }
        wert = Math.signum(wert) * Math.min(maximum, Math.max(minimum, Math.abs(wert)));
        return wert;
    }

    /**
     * Wandelt die Computerinterne Länge (Pixel) in Zentimeter um. Rundet auf 1 Stelle.
     *
     * @param laenge Länge in Pixel
     * @return Länge in Zentimeter
     */
    public static String laengeZuCm(double laenge) {
        return String.format("%.1f", laenge / Konstanten.PIXEL_PRO_CM);
    }

    /**
     * Wandelt die Computerinterne Länge eines Radius in Zentimeter um. Rundet auf 1 Stelle.
     *
     * @param radius Radius
     * @return Radius in Zentimeter als String. 'unendlich', falls r = 0. Der Wert 0 ist extra hierfür reserviert, da er sonst physikalisch nicht möglich ist!
     */
    public static String radiusZuCm(double radius) {
        if (radius == 0) {
            return "unendlich ";
        } else {
            return String.format("%.1f", radius / Konstanten.PIXEL_PRO_CM);
        }
    }

    /**
     * Wandelt einen Neigungswinkel aus Rad in Grad um. Rundet auf 1 Stelle.
     *
     * @param neigungswinkel Neigungswinkel
     * @return Liefert Werte von -180 bis 180 als String
     */
    public static String neigungsWinkelZuGrad(double neigungswinkel) {
        if (neigungswinkel > Math.PI) {
            neigungswinkel = neigungswinkel - Math.PI;
        }
        return String.format("%.1f", Math.toDegrees(neigungswinkel));
    }

    /**
     * Wandelt einen double Wert in eine auf 3 Stellen gerundete Brechzahl um.
     *
     * @param zahl double Brechzahl
     * @return Gerundete Brechzahl als String
     */
    public static String zahlZuBrechzahl(double zahl) {
        return String.format("%.3f", zahl);
    }

    /**
     * Gibt zu einem Abstand der Netzhaut an, ob das Auge hierdurch Weit-/Normal- oder Kurzsichtig wird.
     *
     * @param normalwert    Normalwert des Abstands
     * @param istWert       Tatsächlicher Abstand
     * @param tolleranzwert Tolleranzwert um den normalwert+-tolleranzwert als Normalsichtig angesehen wird.
     * @return Sichtigkeit als String
     */
    public static String abstandNetzhautSicht(double normalwert, double istWert, double tolleranzwert) {
        if (istWert - normalwert > tolleranzwert) {
            return "Kurzsichtig";
        }
        if (istWert - normalwert < -tolleranzwert) {
            return "Weitsichtig";
        }
        return "Normalsichtig";
    }
}
