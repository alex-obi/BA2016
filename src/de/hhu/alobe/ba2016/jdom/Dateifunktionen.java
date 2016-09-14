package de.hhu.alobe.ba2016.jdom;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Klasse bietet Funktionen zum Arbeiten mit Dateien.
 */
public abstract class Dateifunktionen {

    /**
     * Gibt die Dateiendung ohne "."  der uebergebnen Datei.
     *
     * @param datei Datei, dessen Dateiendung erfragt werden soll.
     * @return Dateiendung der Datei.
     */
    public static String getDateiEndung(File datei) {
        String s = datei.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            String endung;
            endung = s.substring(i + 1);
            endung.toLowerCase();
            return endung;
        } else {
            return "";
        }
    }

    /**
     * Gibt eine Liste aller Dateien mit der uebergebenen Dateiendung im uebergebenen Ordner
     *
     * @param ordner Ordner, in dem gesucht werden soll.
     * @param endung Dateiendung, nach der gefiltert werden soll.
     * @return Array mit allen Dateien mit der uebergebenen Dateiendung als File[].
     */
    public static File[] getLokaleDateienMitEndung(File ordner, String endung) {
        File[] saves = ordner.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.toLowerCase().endsWith("." + endung);
            }
        });
        return saves;
    }

    /**
     * Gibt den Namen der Datei ohne "."
     *
     * @param datei Datei, dessen Namen erfragt werden soll.
     * @return Name der Datei.
     */
    public static String getDateiNamen(File datei) {
        String s = datei.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            String endung;
            endung = s.substring(0, i);
            return endung;
        } else {
            return "";
        }
    }

    /**
     * Liefert true wenn Datei erfolgreich neu erstellt wurde oder zum Ueberschreiben freigegeben
     *
     * @param parent Komponente, zu der eine moegliche Meldung ausgegeben werden soll.
     * @param pfad   Pfad zu der Datei, die erstellt werden soll.
     * @return Wahrheitswert, ob Datei erfolgreich erstellt wurde oder ueberschrieben werden kann.
     */
    public static boolean erstelleDatei(Component parent, File pfad) {
        try {
            if (pfad.createNewFile()) {
                return true;
            } else {
                if (pfad.exists()) {
                    int ergebnis = JOptionPane.showConfirmDialog(parent, "Datei existiert bereits. Ueberschreiben?", "Speichern", JOptionPane.YES_NO_OPTION);
                    if (ergebnis == JOptionPane.YES_OPTION) return true;
                }
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

}


