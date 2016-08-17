package de.hhu.alobe.ba2016;

import de.hhu.alobe.ba2016.grafik.Grafiken;
import de.hhu.alobe.ba2016.mathe.Vektor;

/**
 * Klasse zum Programmeinstieg
 */
public class Main {

    public static void main(String[] args) {
        Grafiken.grafikenLaden();
        new HauptFenster();

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
         * todo: Begrenzungen Auge. Abstände und Größen mit wirklichnkeit abgleichen und skalieren. (~ 5 Std)
         *
         * Brennweite und Position der Hauptebene bei dicken Linsen. Linse über kleine Brennweite zu klein dargestellt -> ab mindesthöhe brechzahl editieren(~ 4 Std) [X]
         *
         Donnerstag:

         * todo: Kommentieren und Funktionen überprüfen. Mit Formeln belegen und aufschreiben. (~ 8 Std)

         Freitag:

         * todo: Maßstab und Einheiten (~ 3 Std)
         * todo: Speichern und Laden xml (~ 6 Std)

         Samstag:
         * todo: Menüführung (~ 4 Std)
         * todo: Bugfixes1 (~ 2 Std)
         *
         Sonntag:
         * todo: Bugfixes2 (~ 6 Std)

         Bugs:
         * Wichtig:
         * todo: Strahlen machen kleinen Sprung an Hornhaut
         * todo: Hauptebene Brennweite -> unendlich Reflexion mit Bild?!
         * todo: Senkrechter Einfall bei Hauptebene -> Absturz!
         * todo: bilder nur noch bei spiegeln aus dem unendlichen nicht angezeigt und ins unendliche mehrere bilder
         * todo: spiegel bei unendlich keine reflexion
         * todo: Lichtquelle genau auf Spiegelhauptebene -> Absturz!
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
         * todo: Strahlen nummerieren
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
