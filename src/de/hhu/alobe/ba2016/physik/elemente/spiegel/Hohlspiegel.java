package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.flaechen.*;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Hohlspiegel extends Bauelement implements KannKollision{

    public static final String XML_HOHLSPIEGEL = "hohlspiegel";

    protected double hoehe;
    public static final String XML_HOEHE = "hoehe";
    public static final int MIND_HOEHE = 100;
    public static final int MAX_HOEHE = 510;

    protected double breite;


    protected double radius;
    public static final String XML_RADIUS = "radius";
    public static final int MIND_RADIUS = 50;
    public static final int MAX_RADIUS = 100000; //Maximaler Radius bis Kreis als ebene Fläche approximiert wird

    private Grenzflaeche spiegelFlaeche;

    private Hauptebene hauptebene;

    public Hohlspiegel(OptischeBank optischeBank, Vektor mittelPunkt, double radius, double hoehe) {
        super(optischeBank, mittelPunkt, TYP_SPIEGEL);
        this.radius = radius;
        hauptebene = new Hauptebene(Flaeche.MODUS_REFLEKT, mittelPunkt, radius / 2, hoehe);
        setHoehe(hoehe);
    }

    public Hohlspiegel(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement, TYP_SPIEGEL);
        this.hoehe = xmlElement.getChild(XML_HOEHE).getAttribute("wert").getDoubleValue();
        this.radius = xmlElement.getChild(XML_RADIUS).getAttribute("wert").getDoubleValue();
        hauptebene = new Hauptebene(Flaeche.MODUS_REFLEKT, mittelPunkt, radius / 2, hoehe);
        setHoehe(hoehe);
    }

    public void setHoehe(double nHoehe) {
        formatNeuBestimmen(nHoehe, radius);
    }

    public void setRadius(double nRadius) {
        formatNeuBestimmen(hoehe, nRadius);
    }

    public void formatNeuBestimmen(double nHoehe, double nRadius) {
        if(radius != 0) {
            this.hoehe = Math.min(nHoehe, Math.abs(radius * 2));
        } else {
            this.hoehe = nHoehe;
        }
        hauptebene.setHoehe(hoehe);
        this.radius = nRadius;
        hauptebene.setBrennweite(radius / 2);
        if(radius == 0 || radius > MAX_RADIUS) {
            breite = 0;
            Vektor von = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() + hoehe / 2);
            Vektor bis = new Vektor(mittelPunkt.getX(), mittelPunkt.getY() - hoehe / 2);
            spiegelFlaeche = new Grenzflaeche_Ebene(Grenzflaeche.MODUS_REFLEKT, von, bis);
        } else {
            double winkel = Math.asin(Math.abs(hoehe / (2* radius)));
            double c = Math.sqrt(radius * radius - (hoehe * hoehe) / 4);
            breite = Math.abs(radius) - c;
            if(radius > 0) {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, radius, Math.PI * 2 - winkel, 2 * winkel);
            } else {
                Vektor mp = new Vektor(mittelPunkt.getX() - radius, mittelPunkt.getY());
                spiegelFlaeche = new Grenzflaeche_Sphaerisch(Grenzflaeche.MODUS_REFLEKT, mp, -radius, Math.PI - winkel, 2 * winkel);
            }
        }
        setRahmen(generiereRahmen());
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

        JSlider slide_radius = new JSlider(-10000 / MIND_RADIUS, 10000 / MIND_RADIUS, (int)(10000 / radius));
        slide_radius.setPaintTicks(true);
        slide_radius.setMajorTickSpacing(20);
        slide_radius.addChangeListener(e -> {
            int wert = ((JSlider) e.getSource()).getValue();
            if(wert != 0) {
                setRadius(10000 / wert);
            } else {
                setRadius(0);
            }
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Radius", slide_radius));

        optischeBank.getEigenschaften().setOptionen("Hohlspiegel", regler);
    }

    @Override
    public void paintComponent(Graphics2D g) {

        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(new Color(0, 7, 244));
                spiegelFlaeche.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                g.setColor(Color.GRAY);
                spiegelFlaeche.paintComponent(g);
                g.setColor(Color.BLACK);
                hauptebene.paintComponent(g);
                break;
        }
        super.paintComponent(g);

    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                return spiegelFlaeche.gibKollision(cStrGng);
            case OptischeBank.MODUS_HAUPTEBENE:
                StrahlenKollision sK = spiegelFlaeche.gibKollision(cStrGng);
                if(sK != null) {
                    return new StrahlenKollision(sK.getDistanz(), cStrGng, hauptebene);
                }

        }
        return null;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        spiegelFlaeche.verschiebeUm(verschiebung);
        hauptebene.verschiebeUm(verschiebung);
    }

    @Override
    public Rahmen generiereRahmen() {
        if(radius > 0) {
            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new Point2D.Double(5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-breite - 5, -hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(5, -hoehe / 2));
            return  rahmen;
        } else {
            Rahmen rahmen = new Rahmen(mittelPunkt);
            rahmen.rahmenErweitern(new Point2D.Double(-5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(breite + 5, hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(breite + 5, -hoehe / 2));
            rahmen.rahmenErweitern(new Point2D.Double(-5, -hoehe / 2));
            return rahmen;
        }
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        xmlElement.addContent(new Element(XML_HOEHE).setAttribute("wert", String.valueOf(hoehe)));
        xmlElement.addContent(new Element(XML_RADIUS).setAttribute("wert", String.valueOf(radius)));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_HOHLSPIEGEL;
    }

}
