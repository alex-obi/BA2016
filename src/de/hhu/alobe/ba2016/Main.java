package de.hhu.alobe.ba2016;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.grafik.Grafiken;

import javax.swing.*;
import java.io.File;

/**
 * Klasse zum Programmeinstieg
 */
public class Main {

    /*
        Todoliste:
         * todo: Optische Achse verschiebbar. Format änderbar.
         * todo: Bild auf Schirm besser darstellen
         * todo: Auswahlwerkzeug überarbeiten
         *
         *
         Bugs:
         * Wichtig:
         * todo: Parallellicht auf 0 Grad zurückstellen führt zu komischem Wackeln des Strahlengangs
         *
         * Zweitrangig:
         * todo: Rahmen werden nach scrollen ausgeblendet

         Optional:
         * todo: Synchronized kritische Abschnitte
         * todo: Scrollen mit Maus und Ausschnitt optimieren

         * todo: Parallel-, Zenit-, Brennpunktstrahl markieren und einfangen
         * todo: Strahlen nummerieren und oder farblich kennzeichnen

         * todo: Eigenschaftenänderung rücknehmbar

         Zweitrangig:
         * todo: Garbage vermeiden

    */

    /**
     * Programmeinstieg. Laden von Ordnern und Dateien.
     *
     * @param args Übergabeparameter
     */
    public static void main(String[] args) {
        saveOrdnerErstellen();
        Grafiken.grafikenLaden();
        new HauptFenster();
    }

    /**
     * Überprüft ob der SAVE_ORDNER existiert und versucht ihn bei Nichtexistenz zu erstellen.
     *
     * @return Konnte Ordner erstellt werden oder existiert bereits.
     */
    private static boolean saveOrdnerErstellen() {
        if (!new File(Konstanten.SAVE_ORDNER).exists()) {
            boolean geschafft = new File(Konstanten.SAVE_ORDNER).mkdir();
            if (!geschafft) {
                JOptionPane.showMessageDialog(null, "Speicherordner \"" + Konstanten.SAVE_ORDNER + "\" konnte nicht erstellt werden");
                return false;
            }
        }
        return true;
    }

}
