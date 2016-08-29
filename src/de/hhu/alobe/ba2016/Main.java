package de.hhu.alobe.ba2016;

import de.hhu.alobe.ba2016.editor.HauptFenster;
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
         * todo: Menüführung. Speichern unter...
         * todo: Maßstab und Einheiten
         *
         * todo: Schirm Hohlspiegel Linse Grafisch Abgrenzen
         * todo: Strahl und Bauelement bei erstellen schon mitzeichnen. Nur diesen aktualisieren

         Bugs:
         * Wichtig:
         * todo: Zwischen Hauptebene und Spiegel buggy
         * todo: Bei KonkavKonvexen werden bei r -> unendlich beide Seiten vertauscht
         * todo: Rückgängig bei Parallellicht buggy
         *
         * Zweitrangig:
         * todo: Bei Radiusänderung unter Höhe wird Spiegel erst nach loslassen des Reglers aktualisiert
         * todo: Rahmen werden nach scrollen ausgeblendet
         * todo: Bei Auge wird Brennweite mitgezeichnet
         * todo: Auswahl Cursor ändert sich nicht

         Optional:
         * todo: Parallel-, Zenit-, Brennpunktstrahl markieren und einfangen
         * todo: Strahlen nummerieren und oder farblich kennzeichnen

         * todo: Optische Achse verschiebbar
         * todo: Scrollen mit Maus und Ausschnitt optimieren
         * todo: Eigenschaftenänderung rücknehmbar

         * todo: get gib einheitlich

         Zweitrangig:
         * todo: Mehrere Tabs
         * todo: Spiegel schräg

         */


    }

}
