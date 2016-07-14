package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_NeuerStrahl;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;
import java.util.ArrayList;

public class Lichtquelle extends Bauelement {

    public static int groesse = 20;

    //Strahlengaenge, die durch diese Lichtquelle erzeugt werden
    private ArrayList<Strahlengang> strahlengaenge;

    //Farbe dieser Lichtquelle
    private Color farbe;

    public Lichtquelle (OptischeBank optischeBank, Vektor mittelPunkt, Color farbe) {
        super(optischeBank, mittelPunkt, TYP_LAMPE);

        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-groesse / 2, -groesse / 2));
        rahmen.rahmenErweitern(new VektorInt(groesse / 2, -groesse / 2));
        rahmen.rahmenErweitern(new VektorInt(groesse / 2, groesse / 2));
        rahmen.rahmenErweitern(new VektorInt(-groesse / 2, groesse / 2));
        setRahmen(rahmen);

        strahlengaenge = new ArrayList<>();
        this.farbe = farbe;
    }

    public Strahlengang berechneNeuenStrahl (Vektor strahlPunkt) {
        VektorFloat richtungsPunkt = new VektorFloat(strahlPunkt.getXfloat(), strahlPunkt.getYfloat());
        Vektor richtungsVektor = Vektor.subtrahiere(richtungsPunkt, mittelPunkt);
        return new Strahlengang(new Strahl(mittelPunkt, richtungsVektor));
    }

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

    public ArrayList<Vektor> gibBildpunkte() {
        ArrayList<Vektor> retList = new ArrayList<>();
        for(Strahlengang cStrG : strahlengaenge) {
            for(Vektor cBildpunkt : cStrG.gibBildpunkte()) {
                boolean gibtEsSchon = false;
                for(Vektor cVergleichsBildpunkt : retList) {
                    if(Vektor.gibAbstand(cVergleichsBildpunkt, cBildpunkt) < 1) {
                        gibtEsSchon = true;
                        break;
                    }
                }
                if(!gibtEsSchon) {
                    retList.add(cBildpunkt);
                }
            }
        }
        return retList;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        strahlenZeichnen(g);
        g.setColor(farbe);
        g.fillArc(mittelPunkt.getXint() - groesse / 2, mittelPunkt.getYint() - groesse / 2, groesse, groesse, 0, 360);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.drawLine(mittelPunkt.getXint(), optischeBank.getOptischeAchse().getHoehe(), mittelPunkt.getXint(), mittelPunkt.getYint());
        for(Vektor bildPunkt : gibBildpunkte()) {
            g.fillArc(bildPunkt.getXint() - groesse / 2, bildPunkt.getYint() - groesse / 2, groesse, groesse, 0, 360);
            g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
            g.drawLine(bildPunkt.getXint(), optischeBank.getOptischeAchse().getHoehe(), bildPunkt.getXint(), bildPunkt.getYint());
        }

        super.paintComponent(g);
    }

    @Override
    public void waehleAus() {
        optischeBank.werkzeugWechseln(new Werkzeug_NeuerStrahl(optischeBank, this) {
        });
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
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

    public void setStrahlengaenge(ArrayList<Strahlengang> strahlengaenge) {
        this.strahlengaenge = strahlengaenge;
    }

}
