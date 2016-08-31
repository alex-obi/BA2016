package de.hhu.alobe.ba2016.editor.eigenschaften;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Eigenschaften extends JPanel {

    OptischeBank optischeBank;

    JLabel titel;
    JPanel optionen;

    public Eigenschaften(OptischeBank optischeBank) {
        super(new BorderLayout());
        this.optischeBank = optischeBank;
        setPreferredSize(new Dimension(0, 100));

        titel = new JLabel("");
        titel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        titel.setPreferredSize(new Dimension(400, 25) );
        add(titel, BorderLayout.NORTH);

        optionen = new JPanel(new GridLayout(2, 2));
        add(optionen, BorderLayout.CENTER);

    }

    public void setOptionen(String nTitel, Eigenschaftenregler[] komponenten) {
        optionenLoeschen();

        titel.setText(nTitel);
        for(int i= 0; i < komponenten.length; i++) {
            optionen.add(komponenten[i]);
        }

    }

    public void optionenLoeschen() {
        titel.setText("");
        optionen.removeAll();
    }

    public static double radiusZuProzent(double radius, double mindRadius, double maxRadius) {
        if(radius == 0 || radius > maxRadius) return 0.5;
        double prozent = ((0.5 * mindRadius) / radius) + 0.5;
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    public static double prozentZuRadius(double prozent, double mindRadius) {
        if(prozent == 0.5) return 0;
        double radius = (0.5 * mindRadius) / (prozent - 0.5);
        radius = Math.signum(radius) * Math.max(mindRadius, Math.abs(radius));
        return radius;
    }

    public static double linearZuProzent(double wert, double minimum, double maximum) {
        double prozent = (wert - minimum) / (maximum - minimum);
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    public static double prozentZuLinear(double prozent, double minimum, double maximum) {
        double wert =  (((maximum - minimum) * prozent) + minimum);
        wert = Math.min(maximum, Math.max(minimum, wert));
        return wert;
    }

    public static double brennweiteZuProzent(double wert, double minimum, double maximum) {
        double prozent = 0.5;
        if(wert >= minimum) {
            prozent = Math.sqrt(0.25 / (maximum - minimum)) * Math.sqrt(wert - minimum) + 0.5;
        } else if(wert <= minimum) {
            prozent = -Math.sqrt(0.25 / (maximum - minimum)) * Math.sqrt(-wert - minimum) + 0.5;
        }
        prozent = Math.min(1, Math.max(0, prozent));
        return prozent;
    }

    public static double prozentZuBrennweite(double prozent, double minimum, double maximum) {
        double wert;
        if(prozent >= 0.5) {
             wert = (Math.pow(prozent - 0.5, 2) * ((maximum - minimum) / 0.25)) + minimum;
        } else {
            wert = -((Math.pow(prozent - 0.5, 2) * ((maximum - minimum) / 0.25)) + minimum);
        }
        wert = Math.signum(wert) * Math.min(maximum, Math.max(minimum, Math.abs(wert)));
        return wert;
    }

    public static String laengeZuCm(double laenge) {
        return String.format("%.1f", laenge / Konstanten.PIXEL_PRO_CM);
    }

    public static String radiusZuCm(double laenge) {
        if(laenge == 0) {
            return "unendlich ";
        } else {
            return String.format("%.1f", laenge / Konstanten.PIXEL_PRO_CM);
        }
    }

    public static String neigungsWinkelZuGrad(double neigungswinkel) {
        if(neigungswinkel > Math.PI) {
            neigungswinkel = neigungswinkel - Math.PI;
        }
        return String.format("%.0f", Math.toDegrees(neigungswinkel));
    }

    public static String zahlZuBrechzahl(double zahl) {
        return String.format("%.3f", zahl);
    }

    public static String abstandNetzhautSicht(double normalwert, double istWert, double tolleranzwert) {
        if(istWert - normalwert > tolleranzwert) {
            return "Kurzsichtig";
        }
        if(istWert - normalwert < - tolleranzwert) {
            return "Weitsichtig";
        }
        return "Normalsichtig";
    }

}
