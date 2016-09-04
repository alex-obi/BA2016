package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class ParallelLichtquelle extends Lichtquelle {

    public static final String NAME = "Parallele Lichtquelle";
    public static final String XML_PARALLELLICHT = "parallel_licht";

    private double breite = 20;

    private double hoehe;
    public static final String XML_HOEHE = "hoehe";
    public static final int MIND_HOEHE = 100;
    public static final int MAX_HOEHE = 600;

    double neigungsWinkel;
    public static final String XML_NEIGUNG = "neigung";
    public static final double MIND_NEIGUNG = - Math.PI / 4;
    public static final double MAX_NEIGUNG = Math.PI / 4;

    private JCheckBox anAus;
    private JComboBox farben_box;
    private Eigenschaftenregler_Slider slide_neigung;
    private Eigenschaftenregler_Slider slide_hoehe;

    public ParallelLichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Farbe farbe, int hoehe, double neigungsWinkel) {
        super(optischeBank, mittelPunkt, farbe);
        initialisiere(hoehe, neigungsWinkel);
    }

    public ParallelLichtquelle(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement);
        initialisiere(xmlElement.getAttribute(XML_HOEHE).getDoubleValue(), xmlElement.getAttribute(XML_NEIGUNG).getDoubleValue());
    }

    private void initialisiere(double nHoehe, double nNeigungsWinkel) {
        formatAktualisieren(nHoehe, nNeigungsWinkel);

        anAus = new JCheckBox("Lampe aktiv", isAktiv());
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

        farben_box = new JComboBox(Farbe.farbenpalette.keySet().toArray());
        farben_box.setSelectedItem(Farbe.gibFarbenName(getFarbe()));
        farben_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFarbe(Farbe.farbenpalette.get(farben_box.getSelectedItem()));
                optischeBank.aktualisieren();
            }
        });

        slide_neigung = new Eigenschaftenregler_Slider("Neigungswinkel", "°", 500, neigungsWinkel, MIND_NEIGUNG, MAX_NEIGUNG, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setNeigung(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return -ReglerEvent.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return ReglerEvent.linearZuProzent(-wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return ReglerEvent.neigungsWinkelZuGrad(-neigungsWinkel);
            }
        });

        slide_hoehe = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoehe, MIND_HOEHE, MAX_HOEHE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setHoehe(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return ReglerEvent.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return ReglerEvent.linearZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return ReglerEvent.laengeZuCm(zahl);
            }
        });
    }

    private void formatAktualisieren(double nHoehe, double nNeigungsWinkel) {
        this.hoehe = Math.min(MAX_HOEHE, Math.max(nHoehe, MIND_HOEHE));
        this.neigungsWinkel = Math.min(MAX_NEIGUNG, Math.max(nNeigungsWinkel, MIND_NEIGUNG));

        setRahmen(generiereRahmen());
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

    public void setHoehe(double nHoehe) {
        hoehe = nHoehe;
        Rectangle2D hitbox = new Rectangle.Double(mittelPunkt.getX() - breite / 2, mittelPunkt.getY() - hoehe / 2, breite, hoehe);
        for(int i = 0;i < strahlengaenge.size(); i++) {
            if(!hitbox.contains(strahlengaenge.get(i).getAnfangsStrahl().getBasisVektor())) {
                strahlengaenge.remove(i);
            }
        }
        rahmenAktualisieren();
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

    @Override
    public void neuerStrahl(Strahlengang strahl) {
        strahl.getAnfangsStrahl().setAusDemUnendlichen(true);
        strahlengaenge.add(strahl);
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
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite / 2, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(breite / 2, hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-breite / 2, hoehe / 2));
        return rahmen;
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[4];
        komponenten[0] = new Eigenschaftenregler("", anAus);
        komponenten[1] = new Eigenschaftenregler("Farbe", farben_box);
        komponenten[2] = slide_neigung;
        komponenten[3] = slide_hoehe;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.draw(new Rectangle2D.Double(mittelPunkt.getX() - breite / 2, mittelPunkt.getY() - hoehe / 2, breite, hoehe));
        g.setColor(farbe);
        super.paintComponent(g);

    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_HOEHE, String.valueOf(hoehe));
        xmlElement.setAttribute(XML_NEIGUNG, String.valueOf(neigungsWinkel));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_PARALLELLICHT;
    }

}
