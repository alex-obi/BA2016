package de.hhu.alobe.ba2016.physik.strahlen;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Gerade;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Strahlengang implements Zeichenbar{

    //Speichert die Startwerte um Strahlengang zurücksetzen zu können
    private Strahl anfangsStrahl;

    /**
     * Speichert aktuelle Strahlenabschnitte und die Richtung des letzten, ungehinderten Abschnittes, der das Bild verlässt.
     * Das letzte Element der Liste entspricht dem Basis Vektor des aktuellen Strahls
     */
    private ArrayList<Gerade> strahlenAbschnitte;
    private Strahl aktuellerStrahl;

    public Strahlengang(Strahl anfangsStrahl) {
        this.anfangsStrahl = anfangsStrahl;
        this.aktuellerStrahl = anfangsStrahl;
        strahlenAbschnitte = new ArrayList<>();
    }

    public void neuenStrahlAnhaengen (Strahl neuerStrahl) {
        if (strahlenAbschnitte.size() >= Konstanten.MAX_STRAHLLAENGE) {
            strahlengangBeenden(neuerStrahl.getBasisVektor());
            return;
        }
        strahlenAbschnitte.add(new Gerade(aktuellerStrahl, Vektor.gibAbstand(neuerStrahl.getBasisVektor(), aktuellerStrahl.getBasisVektor())));
        this.aktuellerStrahl = neuerStrahl;
    }

    public void strahlengangBeenden (Vektor letztePosition) {
        //todo: Laenge besser als Vektor
        strahlenAbschnitte.add(new Gerade(aktuellerStrahl, Vektor.gibAbstand(letztePosition, aktuellerStrahl.getBasisVektor())));
        aktuellerStrahl = null;
    }

    public void resetteStrahlengang () {
        this.aktuellerStrahl = anfangsStrahl;
        strahlenAbschnitte = new ArrayList<>();
    }

    public ArrayList<Vektor> gibBildpunkte() {
        ArrayList<Vektor> retList = new ArrayList<>();
        for(Gerade g : strahlenAbschnitte) {
            if(g.getQuellEntfernung() != 0 && g.getQuellEntfernung() < g.getLaenge()) {
                retList.add(g.gibQuellPunkt());
            }
        }
        if(aktuellerStrahl != null) {
            if (aktuellerStrahl.getQuellEntfernung() != 0) {
                retList.add(aktuellerStrahl.gibQuellPunkt());
            }
        }
        return retList;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        for(int i = 0; i < strahlenAbschnitte.size(); i++) {
            strahlenAbschnitte.get(i).paintComponent(g);
        }
        if(aktuellerStrahl != null) {
            aktuellerStrahl.paintComponent(g);
        }
    }

    public Strahl getAnfangsStrahl() {
        return anfangsStrahl;
    }

    public void setAnfangsStrahl(Strahl anfangsStrahl) {
        this.anfangsStrahl = anfangsStrahl;
    }

    public ArrayList<Gerade> getStrahlenAbschnitte() {
        return strahlenAbschnitte;
    }

    public void setStrahlenAbschnitte(ArrayList<Gerade> strahlenAbschnitte) {
        this.strahlenAbschnitte = strahlenAbschnitte;
    }

    public Strahl getAktuellerStrahl() {
        return aktuellerStrahl;
    }

    public void setAktuellerStrahl(Strahl aktuellerStrahl) {
        this.aktuellerStrahl = aktuellerStrahl;
    }

}
