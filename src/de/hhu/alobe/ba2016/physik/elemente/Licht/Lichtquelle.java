package de.hhu.alobe.ba2016.physik.elemente.Licht;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public abstract class Lichtquelle extends Bauelement {

    private boolean aktiv;

    //Strahlengaenge, die durch diese Lichtquelle erzeugt werden
    protected ArrayList<Strahlengang> strahlengaenge;

    //Farbe dieser Lichtquelle
    protected Color farbe;

    public Lichtquelle (OptischeBank optischeBank, Vektor mittelPunkt, Color farbe) {
        super(optischeBank, mittelPunkt, TYP_LAMPE);
        aktiv = true;
        strahlengaenge = new ArrayList<>();
        this.farbe = farbe;
    }

    public abstract Strahlengang berechneNeuenStrahl (Vektor strahlPunkt);

    public void neuerStrahl(Strahlengang strahl) {
        strahlengaenge.add(strahl);
    }

    public void loescheStrahl(Strahlengang strahl) {
        strahlengaenge.remove(strahl);
    }

    public void strahlenZeichnen(Graphics2D g) {
        g.setColor(farbe);
        for(Strahlengang cStrahl : strahlengaenge) {
            cStrahl.paintComponent(g);
        }

    }

    public ArrayList<Vektor> gibBildpunkte(boolean auchVirtuell) {
        ArrayList<Vektor> pruefList = new ArrayList<>();
        ArrayList<Vektor> retList = new ArrayList<>();
        for(Strahlengang cStrG : strahlengaenge) {
            for(Vektor cBildpunkt : cStrG.gibBildpunkte(auchVirtuell)) {
                int anzahlGleicherPunkte = 1;
                for(Vektor cVergleichsBildpunkt : pruefList) {
                    if(Vektor.gibAbstand(cVergleichsBildpunkt, cBildpunkt) < 1) {
                        anzahlGleicherPunkte++;
                    }
                }
                pruefList.add(cBildpunkt);
                if(anzahlGleicherPunkte == 2) {
                    retList.add(cBildpunkt);
                }
            }
        }
        return retList;
    }

    @Override
    public void waehleAus() {
        optischeBank.werkzeugWechseln(new Werkzeug_NeuerStrahl(optischeBank, this) {
        });
    }

    @Override
    public abstract void verschiebeUm(Vektor verschiebung);

    @Override
    public void paintComponent(Graphics2D g) {
        if(aktiv) {
            strahlenZeichnen(g);
            //Bilder Zeichnen, die von dieser Lampe entstehen
                for (Vektor bildPunkt : gibBildpunkte(optischeBank.isVirtuelleStrahlenAktiv())) {
                    g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
                    g.draw(new Line2D.Double(bildPunkt.getX(), optischeBank.getOptischeAchse().getHoehe(), bildPunkt.getX(), bildPunkt.getY()));
            }
        }
        super.paintComponent(g);
    }

    public Color getFarbe() {
        return farbe;
    }

    public void setFarbe(Color farbe) {
        this.farbe = farbe;
    }

    public ArrayList<Strahlengang> getStrahlengaenge() {
        return strahlengaenge;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public void setStrahlengaenge(ArrayList<Strahlengang> strahlengaenge) {
        this.strahlengaenge = strahlengaenge;
    }

}
