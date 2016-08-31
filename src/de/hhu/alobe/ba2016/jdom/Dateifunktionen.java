package de.hhu.alobe.ba2016.jdom;

import de.hhu.alobe.ba2016.Konstanten;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

public abstract class Dateifunktionen {

    public static String getDateiEndung(File datei) {
        String s = datei.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            String endung;
            endung = s.substring(i + 1);
            endung.toLowerCase();
            return endung;
        } else {
            return "";
        }
    }

    public static File[] getLokaleDateienMitEndung(File ordner, String endung) {
        File[] saves = ordner.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.toLowerCase().endsWith( "." + endung );
            }
        });
        return saves;
    }

    public static String getDateiNamen(File datei) {
        String s = datei.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            String endung;
            endung = s.substring(0, i);
            return endung;
        } else {
            return "";
        }
    }

    //Liefert true wenn Datei erfolgreich neu erstellt wurde oder zum Überschreiben freigegeben
    public static boolean erstelleDatei(Component parent, File pfad) {
        try {
            if(pfad.createNewFile()) {
                return true;
            } else {
                if(pfad.exists()) {
                    int ergebnis = JOptionPane.showConfirmDialog(parent, "Datei existiert bereits. Überschreiben?", "Speichern", JOptionPane.YES_NO_OPTION);
                    if (ergebnis == JOptionPane.YES_OPTION) return true;
                }
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

}


