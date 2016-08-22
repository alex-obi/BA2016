package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import org.jdom2.Element;


public class Augenlinse extends Linse {

    public static final String XML_AUGENLINSE = "augenlinse";

    public Augenlinse(Auge auge, Vektor position, double brechzahl, double hoehe, double radius) {
        super(auge.getOptischeBank(), position, brechzahl, hoehe, 0, radius, radius);
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

}
