package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import org.jdom2.Element;

import java.awt.*;
import java.awt.geom.Line2D;


public class Augenlinse extends Linse {

    public static final String XML_AUGENLINSE = "augenlinse";

    public static final double HOEHE = 80;

    public Augenlinse(Auge auge, Vektor position, double brechzahl, double radius) {
        super(auge.getOptischeBank(), position, brechzahl, HOEHE, 0, radius, radius);
    }

    public Augenlinse(Auge auge, Element xmlElement) throws Exception {
        super(auge.getOptischeBank(), xmlElement);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_AUGENLINSE;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (optischeBank.getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(Color.BLUE);
                linsenseite1.paintComponent(g);
                linsenseite2.paintComponent(g);
                g.setColor(Color.BLACK);
                obereBegrenzung.paintComponent(g);
                untereBegrenzung.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                g.setColor(Color.GRAY);
                linsenseite1.paintComponent(g);
                linsenseite2.paintComponent(g);
                obereBegrenzung.paintComponent(g);
                untereBegrenzung.paintComponent(g);
                g.setColor(Color.BLACK);
                hauptebene.paintComponent(g);
                break;
        }
    }

}
