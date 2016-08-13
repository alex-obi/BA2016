package de.hhu.alobe.ba2016.physik.elemente.absorbtion;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Schirm extends Bauelement implements KannKollision {

    private float hoehe;
    private float breite;
    private float radius;

    private Grenzflaeche schirmFlaeche;

    public Schirm(OptischeBank optischeBank, Vektor mittelPunkt, float radius, float hoehe) {
        super(optischeBank, mittelPunkt, TYP_SCHIRM);
        this. radius = radius;
        setHoehe(hoehe);
    }

    public Schirm(OptischeBank optischeBank, Vektor mittelPunkt, float hoehe) {
        super(optischeBank, mittelPunkt, TYP_SCHIRM);
        this. radius = 0;
        setHoehe(hoehe);
    }

    public void setHoehe(float nHoehe) {
        if(radius != 0) {
            this.hoehe = Math.min(nHoehe, Math.abs(radius * 2));
        } else {
            this.hoehe = nHoehe;
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
            int c = (int)Math.sqrt(radius * radius - (hoehe * hoehe) / 4);
            breite = Math.abs(radius) - c;
            if(radius > 0) {
                Vektor mp = new VektorFloat(mittelPunkt.getXfloat() - radius, mittelPunkt.getYfloat());
                schirmFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_ABSORB, mp, radius, Math.PI * 2 - winkel, 2 * winkel);

                Rahmen rahmen = new Rahmen(mittelPunkt);
                rahmen.rahmenErweitern(new VektorInt(+5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(-breite - 5, hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(-breite - 5, -hoehe / 2));
                rahmen.rahmenErweitern(new VektorInt(+5, -hoehe / 2));
                setRahmen(rahmen);

            } else {
                Vektor mp = new VektorFloat(mittelPunkt.getXfloat() - radius, mittelPunkt.getYfloat());
                schirmFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_ABSORB, mp, -radius, Math.PI - winkel, 2 * winkel);


                setRahmen(generiereRahmen());

            }
        }


    }

    @Override
    public void waehleAus() {
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JSlider slide_hoehe = new JSlider (10, 510, (int)hoehe);
        slide_hoehe.setPaintTicks(true);
        slide_hoehe.setMajorTickSpacing(20);
        slide_hoehe.addChangeListener(e -> {
            setHoehe( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Höhe", slide_hoehe));

        optischeBank.getEigenschaften().setOptionen("Schirm", regler);
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

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-5, hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(+breite + 5, hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(+breite + 5, -hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(-5, -hoehe / 2));
        return rahmen;
    }

}