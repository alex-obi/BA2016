package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

public class Schirm extends Bauelement implements KannKollision {

    private float hoehe;
    private float breite;

    private Grenzflaeche schirmFlaeche;

    public Schirm(OptischeBank optischeBank, Vektor mittelPunkt, float radius, float hoehe) {
        super(optischeBank, mittelPunkt, TYP_SCHIRM);
        if(radius != 0) {
            this.hoehe = Math.min(hoehe, radius);
        } else {
            this.hoehe = hoehe;
        }
        setRadius(radius);
    }

    public void setRadius(float radius) {
        if(radius == 0) {
            breite = 0;
            Vektor von = new VektorFloat(mittelPunkt.getXfloat(), mittelPunkt.getYfloat() + hoehe / 2);
            Vektor bis = new VektorFloat(mittelPunkt.getXfloat(), mittelPunkt.getYfloat() - hoehe / 2);
            schirmFlaeche = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_ABSORB, von, bis);

            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new VektorInt(-5, hoehe / 2));
            rahmen.rahmenErweitern(new VektorInt(+5 , hoehe / 2));
            rahmen.rahmenErweitern(new VektorInt(+5, -hoehe / 2));
            rahmen.rahmenErweitern(new VektorInt(-5, -hoehe / 2));
            setRahmen(rahmen);

        } else {
            double winkel = Math.asin(Math.abs(hoehe / (2* radius)));
            float c = (float)Math.sqrt(radius * radius - (hoehe * hoehe) / 4);
            breite = Math.abs(radius) - c;
            if(radius > 0) {
                Vektor mp = new VektorFloat(mittelPunkt.getXfloat() - radius + breite, mittelPunkt.getYfloat());
                schirmFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_ABSORB, mp, radius, Math.PI * 2 - winkel, 2 * winkel);

                Rahmen rahmen = new Rahmen(mittelPunkt);
                rahmen.rahmenErweitern(new VektorInt(-5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(breite + 5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(breite + 5, -hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(-5, -hoehe / 2));
                setRahmen(rahmen);

            } else {
                Vektor mp = new VektorFloat(mittelPunkt.getXfloat() - radius - breite, mittelPunkt.getYfloat());
                schirmFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_ABSORB, mp, -radius, Math.PI - winkel, 2 * winkel);

                Rahmen rahmen = new Rahmen(mittelPunkt);
                rahmen.rahmenErweitern(new VektorInt(5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(-breite - 5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(-breite - 5, -hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(5, -hoehe / 2));
                setRahmen(rahmen);

            }
        }


    }

    @Override
    public void waehleAus() {

    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setColor(new Color(62, 8, 0));
        schirmFlaeche.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        return schirmFlaeche.gibKollision(cStrGng);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        schirmFlaeche.verschiebeUm(verschiebung);
    }
}
