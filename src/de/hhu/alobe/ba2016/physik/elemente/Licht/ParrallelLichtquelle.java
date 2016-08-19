package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ParrallelLichtquelle extends Lichtquelle {

    private double breite = 20;
    private double hoehe;

    public static final int MIND_HOEHE = 100;
    public static final int MAX_HOEHE = 600;

    double neigungsWinkel;

    public static final double MIND_NEIGUNG = - Math.PI / 8;
    public static final double MAX_NEIGUNG = Math.PI / 8;

    public ParrallelLichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Color farbe, int hoehe, double neigungsWinkel) {
        super(optischeBank, mittelPunkt, farbe);
        this.hoehe = hoehe;
        this.neigungsWinkel = neigungsWinkel;

        setRahmen(generiereRahmen());
    }

    @Override
    public Strahlengang berechneNeuenStrahl(Vektor strahlPunkt) {
        Vektor richtung = new Vektor(Math.signum(strahlPunkt.getX() - mittelPunkt.getX()), 0);
        richtung.dreheUmWinkel(neigungsWinkel);
        Vektor basis = new Vektor(-Math.cos(neigungsWinkel) * (strahlPunkt.getX() - mittelPunkt.getX()), -Math.sin(neigungsWinkel) * (strahlPunkt.getX() - mittelPunkt.getX()));
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
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite / 2, hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-breite / 2, hoehe / 2));
        return rahmen;
    }

    @Override
    public void waehleAus() {
        super.waehleAus();

        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JCheckBox anAus = new JCheckBox("Lampe aktiv", isAktiv());
        anAus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(anAus.isSelected()) {
                    setAktiv(true);
                } else {
                    setAktiv(false);
                }
                optischeBank.aktualisieren();
            }
        });
        regler.add(new Eigenschaftenregler("", anAus));

        JComboBox farben_box = new JComboBox(Farbe.farbenpalette.keySet().toArray());
        farben_box.setSelectedItem(Farbe.gibFarbenName(getFarbe()));
        farben_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFarbe(Farbe.farbenpalette.get(farben_box.getSelectedItem()));
                optischeBank.aktualisieren();
            }
        });
        regler.add(new Eigenschaftenregler("Farbe", farben_box));

        JSlider slide_neigung = new JSlider ((int)(MIND_NEIGUNG * 100), (int)(MAX_NEIGUNG * 100), (int)(-neigungsWinkel * 100));
        slide_neigung.setPaintTicks(true);
        slide_neigung.setMajorTickSpacing(10);
        slide_neigung.addChangeListener(e -> {
            setNeigung( -(double)((JSlider) e.getSource()).getValue() / 100);
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Neigungswinkel", slide_neigung));

        JSlider slide_hoehe = new JSlider (MIND_HOEHE, MAX_HOEHE, (int)hoehe);
        slide_hoehe.setPaintTicks(true);
        slide_hoehe.setMajorTickSpacing(50);
        slide_hoehe.addChangeListener(e -> {
            setHoehe( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Höhe", slide_hoehe));

        optischeBank.getEigenschaften().setOptionen("Laser Lichtquelle", regler);

    }

    public void setNeigung(double nNeigungsWinkel) {
        neigungsWinkel = nNeigungsWinkel;
        for(Strahlengang cStrg : strahlengaenge) {
            cStrg.resetteStrahlengang();
            double relativeNeigung;
            if(cStrg.getAnfangsStrahl().getRichtungsVektor().getX() < 0) { //Strahl geht in Richtung linker Seite
                relativeNeigung = (nNeigungsWinkel + Math.PI) - cStrg.getAnfangsStrahl().getRichtungsVektor().gibRichtungsWinkel();
            } else {
                relativeNeigung = nNeigungsWinkel - cStrg.getAnfangsStrahl().getRichtungsVektor().gibRichtungsWinkel();
            }

            cStrg.getAnfangsStrahl().getRichtungsVektor().dreheUmWinkel(relativeNeigung);
        }
    }

    public void setHoehe(int nHoehe) {
        hoehe = nHoehe;
        Rectangle2D hitbox = new Rectangle.Double(mittelPunkt.getX() - breite / 2, mittelPunkt.getY() - hoehe / 2, breite, hoehe);
        for(int i = 0;i < strahlengaenge.size(); i++) {
            if(!hitbox.contains(strahlengaenge.get(i).getAnfangsStrahl().getBasisVektor())) {
                strahlengaenge.remove(i);
            }
        }
        rahmenAktualisieren();
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setColor(farbe);

        g.draw(new Rectangle2D.Double(mittelPunkt.getX() - breite / 2, mittelPunkt.getY() - hoehe / 2, breite, hoehe));
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
