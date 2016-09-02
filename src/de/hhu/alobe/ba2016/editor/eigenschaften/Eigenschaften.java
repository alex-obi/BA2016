package de.hhu.alobe.ba2016.editor.eigenschaften;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;

import javax.swing.*;
import java.awt.*;

/**
 * Optionsleiste zum Manipulieren von Eigenschaften von Elementen der Optische Bank.
 * Der Optionsleiste können Eigenschaftenregler übergeben werden, die durch diese Leiste angezeigt und verwaltet werden.
 * Diese Klasse stellt statische Methoden zur Verfügung um verschiedene Einheiten ineinander umzurechnen.
 */
public class Eigenschaften extends JPanel {

    //Höhe der Optionsleiste:
    private static final int HOEHE = 100;

    //Höhe für Titel:
    private static final int TITEL_HOEHE = 25;

    //Titel der Optionsleiste
    private JLabel titel;

    //Leinwand zum Anzeigen der Eigenschaftenregler
    private JPanel optionen;

    /**
     * Initialisiert eine neue Optionsleiste zum Verwalten von Eigenschaften
     */
    public Eigenschaften() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(0, HOEHE));

        titel = new JLabel("");
        titel.setFont(new Font("Arial", Font.PLAIN, 20));
        titel.setPreferredSize(new Dimension(400, TITEL_HOEHE));
        add(titel, BorderLayout.NORTH);

        optionen = new JPanel(new GridLayout(2, 4));
        add(optionen, BorderLayout.CENTER);
    }

    /**
     * Setzt die angezeigten Manipulationsoptionen auf eine neue Liste von Eigenschaftenreglern.
     *
     * @param nTitel      Titel der Optionsleiste. Bspw: Name des Bauelements.
     * @param komponenten Eigenschaftenregler
     */
    public void setOptionen(String nTitel, Eigenschaftenregler[] komponenten) {
        //Lösche alte Regler:
        optionenLoeschen();

        //Setze neue Regler:
        titel.setText(nTitel);
        for (int i = 0; i < komponenten.length; i++) {
            optionen.add(komponenten[i]);
        }
    }

    /**
     * Löscht die aktuellen Eigenschaftenregler und den Titel.
     */
    public void optionenLoeschen() {
        titel.setText("");
        optionen.removeAll();
    }

    /**
     * Berechnet einen prozentualen Wert für einen Radius zur Benutzung in einem Eigenschaftenregler_Sliders.
     * Rundet auf 0/ 1 bei Überschreiten.
     *
     * @param radius     Radius
     * @param mindRadius Mindest Radius
     * @param maxRadius  Maximal Radius
     * @return wert in Prozent
     */
    public static double radiusZuProzent(double radius, double mindRadius, double maxRadius) {
        if (radius == 0 || radius > maxRadius) return 0.5;
        double prozent = ((0.5 * mindRadius) / radius) + 0.5;
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    /**
     * Berechnet aus einem prozentualen Wert eines Eigenschaftenregler_Sliders einen Wert für einen Radius.
     *
     * @param prozent    Prozent des JSliders
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
     * Berechnet aus einem Wert den prozentualen Wert innerhalb des Intervalls [minimum, maximum]. Rundet auf 0/ 1 bei Überschreiten.
     *
     * @param wert    Wert
     * @param minimum Minimum
     * @param maximum Maximum
     * @return prozentualer Wert
     */
    public static double linearZuProzent(double wert, double minimum, double maximum) {
        double prozent = (wert - minimum) / (maximum - minimum);
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    /**
     * Berechnet zu einem Prozentwert den Wert innerhalb des Intervalls [minimum, maximum]. Rundet auf minimum/ maximum be Überschreiten.
     *
     * @param prozent prozentualer Wert
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Wert berechnet aus dem Intervall
     */
    public static double prozentZuLinear(double prozent, double minimum, double maximum) {
        double wert = (((maximum - minimum) * prozent) + minimum);
        wert = Math.min(maximum, Math.max(minimum, wert));
        return wert;
    }

    /**
     * Berechnet den prozentualen Wert einer Brennweite zum Benutzen in einem Eigenschaftenregler_Sliders. Rundet bei Überschreiten auf 0/ 1.
     *
     * @param wert    Brennweite
     * @param minimum Minimum
     * @param maximum Maximum
     * @return Wert in Prozent
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
     * Berechnet zu einem prozentualen Wert eines Eigenschaftenregler_Sliders.
     *
     * @param prozent prozentualer Wert
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
     * @return Länge in Zentimerter
     */
    public static String laengeZuCm(double laenge) {
        return String.format("%.1f", laenge / Konstanten.PIXEL_PRO_CM);
    }

    /**
     * Wandelt die Computerinterne Länge eines Radius in Zentimeter um. Rundet auf 1 Stelle.
     *
     * @param radius Radius
     * @return Radius in CM als String, 'unendlich', falls r = 0. Der Wert 0 ist extra hierfür reserviert, da er sonst physikalisch nicht möglich ist!
     */
    public static String radiusZuCm(double radius) {
        if (radius == 0) {
            return "unendlich ";
        } else {
            return String.format("%.1f", radius / Konstanten.PIXEL_PRO_CM);
        }
    }

    /**
     * Wandelt einen Neigungswinkel aus rad in Grad um. Rundet auf 1 Stelle.
     *
     * @param neigungswinkel Neigungswinkel
     * @return Liefert Werte von -180° bis 180° als String
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
