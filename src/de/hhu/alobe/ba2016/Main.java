package de.hhu.alobe.ba2016;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.grafik.Grafiken;

import java.io.File;

/**
 * Klasse zum Programmeinstieg
 */
public class Main {

    public static void main(String[] args) {
        Grafiken.grafikenLaden();
        new HauptFenster();

        /*
        Todoliste:

         Bugs:
         * Wichtig:
         * todo: Parallellicht auf 0 Grad zurückstellen führt zu komischem Wackeln des Strahlengangs
         *
         * Zweitrangig:
         * todo: Rahmen werden nach scrollen ausgeblendet
         * todo: Auswahl Cursor ändert sich nicht

         Optional:
         * todo: Optische Achse verschiebbar
         * todo: Strahl und Bauelement bei erstellen schon mitzeichnen. Nur diesen aktualisieren

         * todo: Scrollen mit Maus und Ausschnitt optimieren

         * todo: Parallel-, Zenit-, Brennpunktstrahl markieren und einfangen
         * todo: Strahlen nummerieren und oder farblich kennzeichnen

         * todo: Eigenschaftenänderung rücknehmbar

         * todo: get gib einheitlich

         Zweitrangig:
         * todo: Mehrere Tabs
         * todo: Spiegel schräg
         * todo: Garbage vermeiden

         */


    }

}
