package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import org.jdom2.Element;

import java.awt.*;

/**
 * Linse eines Auges.
 */
public class Augenlinse extends Linse {

    /**
     * Name der Augenlinse im XML-Dokument.
     */
    public static final String XML_AUGENLINSE = "augenlinse";

    //Hoehe der Augenlinse
    private static final double HOEHE = 80;

    /**
     * Initialisiert eine neue Augenlinse mit Brechzahl und Radis
     *
     * @param auge      Referenz auf das zugehoerige Auge.
     * @param position  Position der Linse.
     * @param brechzahl Brechzahl der Augenlinse.
     * @param radius    Radius der Augenlinse.
     */
    public Augenlinse(Auge auge, Vektor position, double brechzahl, double radius) {
        super(auge.getOptischeBank(), position, brechzahl, HOEHE, 0, radius, radius);
    }

    /**
     * Initialisiert das Auge mit einem jdom2.Element, das die benoetigten Attribute enthaelt.
     *
     * @param auge       Referenz auf das zugehoerige Auge.
     * @param xmlElement jdom2.Element.
     * @throws Exception Exception, die geworfen wird, wenn beim Initialisieren ein Fehler passiert.
     */
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
