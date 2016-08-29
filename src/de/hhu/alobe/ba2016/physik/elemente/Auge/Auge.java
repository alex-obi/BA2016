package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Auge extends Bauelement implements KannKollision {

    public static final String XML_AUGE = "auge";
    public static final double HOEHE_AUGE = 135;

    private Hornhaut hornhaut;
    private static final double HORNHAUT_RADIUS = 43.1;
    private static final double HORNHAUT_ABSTAND = 40.3; //Abstand relativ zu Augenlinse

    private Augenlinse augenlinse;
    private static final double AUGENLINSE_BRECHZAHL = 1.25;

    private double kruemmungsradius_linse;
    public static final String XML_KRUEMMUNG_LINSE = "kruemmungLinse";

    public static final double MIND_RADIUS_LINSE = 60;
    public static final double MAX_RADIUS_LINSE = 535.8 / 2;

    private Netzhaut netzhaut;
    private static final double HOEHE_NETZHAUT = 64.65;

    private double abstand_netzhaut; //Abstand relativ zu Augenlinse
    public static final String XML_NETZHAUT_ABSTAND = "abstandNetzhaut";

    public static final double MIND_ABSTAND_NETZHAUT = 46.3;
    public static final double MAX_ABSTAND_NETZHAUT = 146.3;

    private Grenzflaeche_Sphaerisch obereBegrenzung;
    private Grenzflaeche_Sphaerisch untereBegrenzung;

    double insgesamtHoehe;

    public Auge(OptischeBank optischeBank, Vektor mittelpunkt) {
        super(optischeBank, mittelpunkt, Bauelement.TYP_AUGE);
        initialisiere(mittelpunkt, 535.8 / 2, 96.3);
    }

    public Auge(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_AUGE);
        kruemmungsradius_linse = xmlElement.getAttribute(XML_KRUEMMUNG_LINSE).getDoubleValue();
        abstand_netzhaut = xmlElement.getAttribute(XML_NETZHAUT_ABSTAND).getDoubleValue();
        initialisiere(mittelPunkt, kruemmungsradius_linse, abstand_netzhaut);
    }

    private void initialisiere(Vektor mittelpunkt, double nKruemmungsradius_linse, double nAbstand_netzhaut) {
        kruemmungsradius_linse = nKruemmungsradius_linse;
        abstand_netzhaut = nAbstand_netzhaut;
        hornhaut = new Hornhaut(this, Vektor.addiere(mittelpunkt, new Vektor(-HORNHAUT_ABSTAND, 0)), HORNHAUT_RADIUS);
        augenlinse = new Augenlinse(this, mittelpunkt.kopiere(), AUGENLINSE_BRECHZAHL, 80, kruemmungsradius_linse);
        netzhaut = new Netzhaut(this, Vektor.addiere(mittelpunkt, new Vektor(abstand_netzhaut, 0)), HOEHE_NETZHAUT);
        generiereBegrenzungen();
        setRahmen(generiereRahmen());
    }

    private void generiereBegrenzungen() {
        double d = abstand_netzhaut + HORNHAUT_ABSTAND - 14.59;
        double a = netzhaut.getHoehe() / 2;
        double b = abstand_netzhaut - (d / 2);
        double e = HOEHE_AUGE / 2;
        double c = (e * e - a * a - (d * d) / 4) / (2 * (a - e));
        double r = e + c;
        double alpha = Math.atan((2 * (a + c)) / d);
        double ext = Math.PI - 2 * alpha;
        obereBegrenzung = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, new Vektor(b, -c)), r, alpha, ext);
        untereBegrenzung = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_ABSORB, Vektor.addiere(mittelPunkt, new Vektor(b, c)), r, Math.PI + alpha, ext);
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        augenlinse.verschiebeUm(verschiebung);
        hornhaut.verschiebeUm(verschiebung);
        netzhaut.verschiebeUm(verschiebung);
        obereBegrenzung.verschiebeUm(verschiebung);
        untereBegrenzung.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-HORNHAUT_ABSTAND - 5, 5 + insgesamtHoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double( abstand_netzhaut + 5, 5 + insgesamtHoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(+abstand_netzhaut + 5, -5 -insgesamtHoehe / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-HORNHAUT_ABSTAND - 5, -5 -insgesamtHoehe / 2));
        return rahmen;
    }

    @Override
    public void waehleAus() {
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JSlider slide_netzhaut = new JSlider ((int)MIND_ABSTAND_NETZHAUT, (int)MAX_ABSTAND_NETZHAUT, (int)abstand_netzhaut);
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
        generiereBegrenzungen();
        setRahmen(generiereRahmen());
    }

    public void setze_kruemmungsradius(double nKruemmungsradius) {
        kruemmungsradius_linse = nKruemmungsradius;
        augenlinse.setRadius1(nKruemmungsradius);
        augenlinse.setRadius2(nKruemmungsradius);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        hornhaut.paintComponent(g);
        augenlinse.paintComponent(g);
        netzhaut.paintComponent(g);
        obereBegrenzung.paintComponent(g);
        untereBegrenzung.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(hornhaut.kollisionUeberpruefen(cStrGng));
        kollisionen.add(augenlinse.kollisionUeberpruefen(cStrGng));
        kollisionen.add(netzhaut.kollisionUeberpruefen(cStrGng));
        kollisionen.add(obereBegrenzung.gibKollision(cStrGng));
        kollisionen.add(untereBegrenzung.gibKollision(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.setAttribute(XML_KRUEMMUNG_LINSE, String.valueOf(kruemmungsradius_linse));
        xmlElement.setAttribute(XML_NETZHAUT_ABSTAND, String.valueOf(abstand_netzhaut));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_AUGE;
    }

}
