package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;
import java.awt.geom.Arc2D;

public class ParrallelLichtquelle extends Lichtquelle {

    private int breite = 20;
    private int hoehe;

    double neigungsWinkel;

    public ParrallelLichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Color farbe, int hoehe, double neigungsWinkel) {
        super(optischeBank, mittelPunkt, farbe);
        this.hoehe = hoehe;
        this.neigungsWinkel = neigungsWinkel;
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(breite / 2, hoehe / 2));
        rahmen.rahmenErweitern(new VektorInt(-breite / 2, hoehe / 2));
        setRahmen(rahmen);
    }

    @Override
    public Strahlengang berechneNeuenStrahl(Vektor strahlPunkt) {
        Vektor richtung = new VektorFloat(Math.signum(strahlPunkt.getXfloat() - mittelPunkt.getXfloat()), 0);
        richtung.dreheUmWinkel(neigungsWinkel);
        Vektor basis = new VektorFloat(-Math.cos(neigungsWinkel) * (strahlPunkt.getXfloat() - mittelPunkt.getXfloat()), -Math.sin(neigungsWinkel) * (strahlPunkt.getXfloat() - mittelPunkt.getXfloat()));
        basis.addiere(strahlPunkt);
        if(Math.abs(basis.getYint() - mittelPunkt.getYint()) < hoehe / 2) {
            return new Strahlengang(new Strahl(basis, richtung, 0, true));
        } else {
            return null;
        }
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        for(Strahlengang cStrG : strahlengaenge) {
            cStrG.getAnfangsStrahl().getBasisVektor().addiere(verschiebung);
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        strahlenZeichnen(g);
        g.setColor(farbe);
        g.fillRect(mittelPunkt.getXint() - breite / 2, mittelPunkt.getYint() - hoehe / 2, breite, hoehe);
        super.paintComponent(g);
    }

    public double getNeigungsWinkel() {
        return neigungsWinkel;
    }

    public void setNeigungsWinkel(double nNeigungsWinkel) {
        double aenderung = nNeigungsWinkel - neigungsWinkel;
        for(Strahlengang cStrG : strahlengaenge) {
            cStrG.getAktuellerStrahl().getRichtungsVektor().dreheUmWinkel(nNeigungsWinkel);
        }
    }
}
