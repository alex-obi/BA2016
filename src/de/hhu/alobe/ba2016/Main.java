package de.hhu.alobe.ba2016;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.grafik.Grafiken;

import javax.swing.*;
import java.io.File;

/**
 * Klasse zum Programmeinstieg.
 */
public class Main {

    /**
     * Programmeinstieg. Laden von Ordnern und Dateien.
     *
     * @param args Uebergabeparameter durch Konsole.
     */
    public static void main(String[] args) {
        saveOrdnerErstellen();
        Grafiken.grafikenLaden();
        new HauptFenster();
    }

    /**
     * Ueberprueft ob der SAVE_ORDNER existiert und versucht ihn bei Nichtexistenz zu erstellen.
     *
     * @return Konnte Ordner erstellt werden oder existiert bereits.
     */
    private static boolean saveOrdnerErstellen() {
        if (!new File(Konstanten.SAVE_ORDNER).exists()) {
            boolean geschafft = new File(Konstanten.SAVE_ORDNER).mkdir();
            if (!geschafft) {
                JOptionPane.showMessageDialog(null, "Speicherordner \"" + Konstanten.SAVE_ORDNER + "\" konnte nicht erstellt werden. Lokales Speichern nicht moeglich!");
                return false;
            }
        }
        return true;
    }

}
