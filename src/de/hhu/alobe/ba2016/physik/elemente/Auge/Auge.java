package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Auge extends Bauelement implements KannKollision {

    public static final double BRECHZAHL_KAMMERWASSER = 1.333;

    private Hornhaut hornhaut;
    private double abstand_hornhaut; //Abstand relativ zu Augenlinse

    private Augenlinse augenlinse;
    private double kruemmungsradius_linse;

    public static final int MIND_RADIUS_LINSE = 100;
    public static final int MAX_RADIUS_LINSE = 200;

    private Netzhaut netzhaut;
    private double abstand_netzhaut; //Abstand relativ zu Augenlinse

    public static final int MIND_ABSTAND_NETZHAUT = 100;
    public static final int MAX_ABSTAND_NETZHAUT = 300;

    public Auge(OptischeBank optischeBank, Vektor mittelpunkt) {
        super(optischeBank, mittelpunkt, Bauelement.TYP_AUGE);
        abstand_hornhaut = 40;
        hornhaut = new Hornhaut(this, Vektor.addiere(mittelpunkt, new Vektor(-abstand_hornhaut, 0)), 60);
        kruemmungsradius_linse = 700;
        //augenlinse = new Augenlinse(this, mittelpunkt.kopiere(), 1.5, 120, kruemmungsradius_linse);
        abstand_netzhaut = 150;
        netzhaut = new Netzhaut(this, Vektor.addiere(mittelpunkt, new Vektor(abstand_netzhaut, 0)), 120);
        setRahmen(generiereRahmen());
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        //augenlinse.verschiebeUm(verschiebung);
        hornhaut.verschiebeUm(verschiebung);
        netzhaut.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-abstand_hornhaut - 5, 80));
        rahmen.rahmenErweitern(new Point2D.Double( abstand_netzhaut + 5, 80));
        rahmen.rahmenErweitern(new Point2D.Double(+abstand_netzhaut + 5, -80));
        rahmen.rahmenErweitern(new Point2D.Double(-abstand_hornhaut - 5, -80));
        return rahmen;
    }

    @Override
    public void waehleAus() {
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JSlider slide_netzhaut = new JSlider (MIND_ABSTAND_NETZHAUT, MAX_ABSTAND_NETZHAUT, (int)abstand_netzhaut);
        slide_netzhaut.setPaintTicks(true);
        slide_netzhaut.setMajorTickSpacing(20);
        slide_netzhaut.addChangeListener(e -> {
            setze_abstand_netzhaut( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Abstand Netzhaut", slide_netzhaut));

        JSlider slide_radius = new JSlider((int)MIND_RADIUS_LINSE, (int)MAX_RADIUS_LINSE, (int)augenlinse.getRadius1());
        slide_radius.setPaintTicks(true);
        slide_radius.setMajorTickSpacing(10);
        slide_radius.addChangeListener(e -> {
            setze_kruemmungsradius( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Brennweite Augenlinse", slide_radius));

        optischeBank.getEigenschaften().setOptionen("Auge", regler);
    }

    public void setze_abstand_netzhaut(int n_Abstand) {
        netzhaut.verschiebeUm(new Vektor(n_Abstand - abstand_netzhaut, 0));
        abstand_netzhaut = n_Abstand;
        setRahmen(generiereRahmen());
    }

    public void setze_kruemmungsradius(double nKruemmungsradius) {
        augenlinse.setRadius1(nKruemmungsradius);
        augenlinse.setRadius2(nKruemmungsradius);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        hornhaut.paintComponent(g);
        //augenlinse.paintComponent(g);
        netzhaut.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(hornhaut.kollisionUeberpruefen(cStrGng));
        //kollisionen.add(augenlinse.kollisionUeberpruefen(cStrGng));
        kollisionen.add(netzhaut.kollisionUeberpruefen(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }
}
