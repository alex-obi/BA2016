package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.jdom.Speicherbar;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import org.jdom2.Element;

/**
 * Ein Bauelement hat eine Interaktion mit Strahlen
 *
 */
public abstract class Bauelement extends Auswahlobjekt implements Speicherbar{

    public static final String XML_BAUELEMENT = "bauelement";

    protected OptischeBank optischeBank;

    private int typ = 0;

    public static final int TYP_LAMPE = 1;
    public static final int TYP_LINSE = 2;
    public static final int TYP_SPIEGEL = 3;
    public static final int TYP_BLENDE = 4;
    public static final int TYP_SCHIRM = 5;
    public static final int TYP_AUGE = 6;


    public Bauelement(OptischeBank optischeBank, Vektor mittelPunkt, int typ) {
        super(mittelPunkt);
        this.optischeBank = optischeBank;
        this.typ = typ;
    }

    public Bauelement(OptischeBank optischeBank, Element xmlElement, int typ) throws Exception {
        super(new Vektor(xmlElement.getChild(XML_MITTELPUNKT)));
        this.optischeBank = optischeBank;
        this.typ = typ;
    }

    public boolean fangModusOptischeAchseAn() {
        return (typ != Bauelement.TYP_LAMPE);
    }

    public void setzeMittelpunktNeu(Vektor nMittelpunkt) {
        verschiebeUm(Vektor.subtrahiere(nMittelpunkt, mittelPunkt));
    }

    public abstract void verschiebeUm(Vektor verschiebung);

    public int getTyp() {
        return typ;
    }

    public OptischeBank getOptischeBank() {
        return optischeBank;
    }

    public void rahmenAktualisieren() {
        setRahmen(generiereRahmen());
    }

    public abstract Rahmen generiereRahmen();

    @Override
    public Element getXmlElement() {
        Element xmlElement = new Element(getXmlElementTyp());
        xmlElement.addContent(mittelPunkt.getXmlElement(XML_MITTELPUNKT));
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_BAUELEMENT;
    }

}
