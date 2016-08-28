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
        HauptFenster fenster = new HauptFenster();
        //fenster.wechseleOptischeBank(fenster.ladeNeueOptischeBank(new File("jdom/saves/test.jdom")));
        //fenster.speichereAktuelleOptischeBank(new File("jdom/saves/test.jdom"));

        /*
        Todoliste:

        Features:

        Freitag:
         * Eigenschaftenpanel für alle Bauelemente.(~ 4 Std) [X]
         *

         Dienstag:
         * Werkzeugfenster für alle Bauelemente. Geeignete Startwerte. Grafische Aufarbeitung (~ 2 Std) [X]
         *
         * Strahlen einzeln aktualisieren (~ 1 Std) [X]

         * Virtuelle Bilder an/aus (~ 1 Std) [X]



         Mittwoch:

         *
         * Brennweite und Position der Hauptebene bei dicken Linsen. Linse über kleine Brennweite zu klein dargestellt -> ab mindesthöhe brechzahl editieren(~ 4 Std) [X]
         *
         Donnerstag:

         * Kommentieren und Funktionen überprüfen. Mit Formeln belegen und aufschreiben. (~ 8 Std) [X]

         Freitag:
         * Begrenzungen Auge. Abstände und Größen mit wirklichnkeit abgleichen und skalieren. (~ 5 Std) [X]

         *

         Samstag:


         Sonntag:
         * todo: Auge mehr in Länge als in allgemeine Größe skalieren
         * todo: Feldlinse (g -> 0)
         * todo: Menüführung. Speichern unter...
         * todo: Maßstab und Einheiten



         Bugs:
         * Wichtig:
         * todo: Bei KonkavKonvexen werden bei r -> unendlich beide Seiten vertauscht
         * todo: Rückgängig bei Parallellicht buggy
         *
         * Zweitrangig:
         * todo: Bei Radiusänderung unter Höhe wird Spiegel erst nach loslassen des Reglers aktualisiert
         * todo: Rahmen werden nach scrollen ausgeblendet
         * todo: Bei Auge wird Brennweite mitgezeichnet
         * todo: Auswahl Cursor ändert sich nicht


         Optional:
         * todo: Parallel-, Zenit-, Brennpunktstrahl markieren
         * todo: Strahlen nummerieren und oder farblich kennzeichnen
         * todo: Eigenschaftenänderung rücknehmbar
         * todo: Strahl und Bauelement bei erstellen schon mitzeichnen. Nur diesen aktualisieren
         * todo: Scrollen mit Maus
         * todo: Schirm Hohlspiegel Linse Grafisch Abgrenzen
         * todo: Optische Achse verschiebbar
         *
         * todo: Mehrere Tabs
         * todo: Ausschnitt und Scrollen optimieren
         * todo: Spiegel schräg

         * todo: Nullpointer verhindern
         * todo: this vermeiden
         * todo: get gib einheitlich
         */


    }

}
