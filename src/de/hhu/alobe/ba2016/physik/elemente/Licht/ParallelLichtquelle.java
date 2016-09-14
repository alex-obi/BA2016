package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
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

/**
 * Lichtquelle, die parallele Strahlen erzeugt, die bei der Behandlung durch Hauptebenen aus dem Unendlichen kommen.
 */
public class ParallelLichtquelle extends Lichtquelle {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Parallele Lichtquelle";

    /**
     * Name der Parallellichtquelle im XML-Dokument.
     */
    public static final String XML_PARALLELLICHT = "parallel_licht";

    //Breite der Parallellichtquelle
    private double breite = 20;

    //Höhe der Parallellichtquelle
    private double hoehe;

    /**
     * Name für die Höhe im XML-Dokument.
     */
    public static final String XML_HOEHE = "hoehe";

    /**
     * Mindestwert für die Höhe.
     */
    public static final int MIND_HOEHE = 100;

    /**
     * Maximalwert für die Höhe.
     */
    public static final int MAX_HOEHE = 600;

    //Neigungswinkel, mit dem Strahlen erzeugt werden.
    private double neigungsWinkel;

    /**
     * Name für den Neigungswinkel im XML-Dokument.
     */
    public static final String XML_NEIGUNG = "neigung";

    /**
     * Mindestwert für den Neigungswinkel.
     */
    public static final double MIND_NEIGUNG = -Math.PI / 4;

    /**
     * Maximalwert für den Neigungswinkel.
     */
    public static final double MAX_NEIGUNG = Math.PI / 4;

    //Eigenschaftenregler zur Manipulation der Werte der Parallellichtquelle durch den Benutzer:
    private JCheckBox anAus;
    private JComboBox farben_box;
    private Eigenschaftenregler_Slider slide_neigung;
    private Eigenschaftenregler_Slider slide_hoehe;

    /**
     * Initialisiere Parallellichtquelle miz Farbe, Höhe und Neigungswinkel.
     *
     * @param optischeBank   Referenz auf Optische Bank.
     * @param mittelPunkt    Mittelpunkt der Parallellichtquelle.
     * @param farbe          Farbe der Parallellichtquelle.
     * @param hoehe          Höhe der Parallellichtquelle.
     * @param neigungsWinkel Neigungswinkel der Parallellichtquelle.
     */
    public ParallelLichtquelle(OptischeBank optischeBank, Vektor mittelPunkt, Farbe farbe, int hoehe, double neigungsWinkel) {
        super(optischeBank, mittelPunkt, farbe);
        initialisiere(hoehe, neigungsWinkel);
    }

    /**
     * Initialisiere Parallellichtquelle mit einem jdom2.Element
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement   jdom2.Element mit den benötigten Attributen.
     * @throws Exception Exception, die geworfen wird, wenn beim Initialisieren etwas schief läuft.
     */
    public ParallelLichtquelle(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement);
        initialisiere(xmlElement.getAttribute(XML_HOEHE).getDoubleValue(), xmlElement.getAttribute(XML_NEIGUNG).getDoubleValue());
    }

    //Initialisiere die Werte der Parallellichtquelle und erstelle Eigenschaftenregler
    private void initialisiere(double nHoehe, double nNeigungsWinkel) {
        formatAktualisieren(nHoehe, nNeigungsWinkel);

        anAus = new JCheckBox("Lampe aktiv", isAktiv());
        anAus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (anAus.isSelected()) {
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

        slide_neigung = new Eigenschaftenregler_Slider("Neigungswinkel", "°", 100, neigungsWinkel, MIND_NEIGUNG, MAX_NEIGUNG, new ReglerEvent() {
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

    //Aktualisiere das Format der Parallellichtquelle
    private void formatAktualisieren(double nHoehe, double nNeigungsWinkel) {
        this.hoehe = Math.min(MAX_HOEHE, Math.max(nHoehe, MIND_HOEHE));
        this.neigungsWinkel = Math.min(MAX_NEIGUNG, Math.max(nNeigungsWinkel, MIND_NEIGUNG));

        setRahmen(generiereRahmen());
    }

    /**
     * @param nNeigungsWinkel Neuer Neigungswinkel.
     */
    public void setNeigung(double nNeigungsWinkel) {
        neigungsWinkel = nNeigungsWinkel;
        for (Strahlengang cStrg : strahlengaenge) {
            cStrg.resetteStrahlengang();
            if (nNeigungsWinkel == 0) {
                cStrg.getAnfangsStrahl().getRichtungsVektor().setY(0);
                cStrg.getAnfangsStrahl().getRichtungsVektor().setX(Math.signum(cStrg.getAnfangsStrahl().getRichtungsVektor().getX()));
            } else {
                double relativeNeigung;
                if (cStrg.getAnfangsStrahl().getRichtungsVektor().getX() < 0) { //Strahl geht in Richtung linker Seite
                    relativeNeigung = (nNeigungsWinkel + Math.PI) - cStrg.getAnfangsStrahl().getRichtungsVektor().gibRichtungsWinkel();
                } else {
                    relativeNeigung = nNeigungsWinkel - cStrg.getAnfangsStrahl().getRichtungsVektor().gibRichtungsWinkel();
                }
                cStrg.getAnfangsStrahl().getRichtungsVektor().dreheUmWinkel(relativeNeigung);
            }

        }
    }

    /**
     * @param nHoehe Neue Höhe.
     */
    public void setHoehe(double nHoehe) {
        hoehe = nHoehe;
        Rectangle2D hitbox = new Rectangle.Double(mittelPunkt.getX() - breite / 2, mittelPunkt.getY() - hoehe / 2, breite, hoehe);
        for (int i = 0; i < strahlengaenge.size(); i++) {
            if (!hitbox.contains(strahlengaenge.get(i).getAnfangsStrahl().getBasisVektor())) {
                strahlengaenge.remove(i);
            }
        }
        rahmenAktualisieren();
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
        if (Math.abs(basis.getYint() - mittelPunkt.getYint()) < hoehe / 2) {
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
        Eigenschaftenregler[] komponenten = new Eigenschaftenregler[4];
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
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
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
