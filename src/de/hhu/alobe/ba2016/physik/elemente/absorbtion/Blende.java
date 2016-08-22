package de.hhu.alobe.ba2016.physik.elemente.absorbtion;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Ebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Blende extends Bauelement implements KannKollision {

    public static final String XML_BLENDE = "blende";

    double durchmesser;
    public static final String XML_DURCHMESSER = "Durchmesser";
    public static final int MIND_DURCHMESSER = 0;
    public static final int MAX_DURCHMESSER = 500;

    double hoehe;
    public static final String XML_HOEHE = "Hoehe";
    public static final int MIND_HOEHE = 10;
    public static final int MAX_HOEHE = 510;

    Grenzflaeche obereHaelfte;
    Grenzflaeche untereHaelfte;

    public Blende(OptischeBank optischeBank, Vektor mittelPunkt, int hoehe, int durchmesser) {
        super(optischeBank, mittelPunkt, Bauelement.TYP_BLENDE);
        this.hoehe = Math.max(10, hoehe);
        setzeDurchmesser(durchmesser);
    }

    public Blende(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_BLENDE);
        this.durchmesser = xmlElement.getChild(XML_DURCHMESSER).getAttribute("wert").getDoubleValue();
        this.hoehe = xmlElement.getChild(XML_HOEHE).getAttribute("wert").getDoubleValue();
        setzeDurchmesser(durchmesser);
    }

    public void setzeDurchmesser(double nDurchmesser) {
        if(Math.abs(nDurchmesser) + 10 > hoehe) {
            durchmesser = hoehe - 10;
        } else {
            durchmesser = Math.abs(nDurchmesser);
        }
        formatAktualisieren();
    }

    public void setHoehe(double nHoehe) {
        if(nHoehe > durchmesser + 10) {
            hoehe = nHoehe;
        } else {
            hoehe = durchmesser + 10;
        }
        formatAktualisieren();
    }

    public void formatAktualisieren() {
        Vektor vonVekt = new Vektor(0, durchmesser / 2);
        Vektor bisVekt = new Vektor(0, hoehe / 2);
        obereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, vonVekt), Vektor.addiere(mittelPunkt, bisVekt));
        untereHaelfte = new Grenzflaeche_Ebene(Flaeche.MODUS_ABSORB, Vektor.subtrahiere(mittelPunkt, vonVekt), Vektor.subtrahiere(mittelPunkt, bisVekt));


        setRahmen(generiereRahmen());
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        obereHaelfte.verschiebeUm(verschiebung);
        untereHaelfte.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-5, hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5 , hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+5, -hoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-5, -hoehe / 2));
        return rahmen;
    }

    @Override
    public void waehleAus() {
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JSlider slide_hoehe = new JSlider (MIND_HOEHE, MAX_HOEHE, (int)hoehe);
        slide_hoehe.setPaintTicks(true);
        slide_hoehe.setMajorTickSpacing(20);
        slide_hoehe.addChangeListener(e -> {
            setHoehe( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Höhe", slide_hoehe));

        JSlider slide_durchmesser = new JSlider (MIND_DURCHMESSER, MAX_DURCHMESSER, (int)durchmesser );
        slide_durchmesser.setPaintTicks(true);
        slide_durchmesser.setMajorTickSpacing(20);
        slide_durchmesser.addChangeListener(e -> {
            setzeDurchmesser( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Durchmesser", slide_durchmesser));

        optischeBank.getEigenschaften().setOptionen("Blende", regler);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setColor(new Color(62, 8, 0));
        obereHaelfte.paintComponent(g);
        untereHaelfte.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(obereHaelfte.gibKollision(cStrGng));
        kollisionen.add(untereHaelfte.gibKollision(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.addContent(new Element(XML_DURCHMESSER).setAttribute("wert", String.valueOf(durchmesser)));
        xmlElement.addContent(new Element(XML_HOEHE).setAttribute("wert", String.valueOf(hoehe)));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_BLENDE;
    }

}