package de.hhu.alobe.ba2016.physik.elemente;

import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.jdom.Speicherbar;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import org.jdom2.Element;

/**
 * Bauelement als Oberklasse fuer alle Objekte, die durch den Benutzer der Optischen Bank hinzugefuegt werden koennen und mit den Strahlengaengen interagieren.
 */
public abstract class Bauelement extends Auswahlobjekt implements Speicherbar {

    /**
     * Name fuer untypisierte Bauelemente im XML-Dokumente
     */
    public static final String XML_BAUELEMENT = "bauelement";

    /**
     * Referenz auf die zugehoerige Optische Bank.
     */
    protected OptischeBank optischeBank;

    //Typ des Bauelements.
    private int typ = 0;

    /**
     * Wert fuer Typ Lampe.
     */
    public static final int TYP_LAMPE = 1;

    /**
     * Wert fuer Typ Linse.
     */
    public static final int TYP_LINSE = 2;

    /**
     * Wert fuer Typ Spiegel.
     */
    public static final int TYP_SPIEGEL = 3;

    /**
     * Wert fuer Typ Blende.
     */
    public static final int TYP_BLENDE = 4;

    /**
     * Wert fuer Typ Schirm.
     */
    public static final int TYP_SCHIRM = 5;

    /**
     * Wert fuer Typ Auge.
     */
    public static final int TYP_AUGE = 6;

    /**
     * Initialisiere Bauelement mit Referenz auf Optische Bank und dem entsprechenden Typ.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelPunkt  Mittelpunkt des Elements.
     * @param typ          Typ des Elements.
     */
    public Bauelement(OptischeBank optischeBank, Vektor mittelPunkt, int typ) {
        super(mittelPunkt);
        this.optischeBank = optischeBank;
        this.typ = typ;
    }

    /**
     * Initialisiere Bauelement ueber ein jdom-Element.
     *
     * @param optischeBank Referenz auf Optische Bank
     * @param xmlElement   Element als jdom2.Element.
     * @param typ          Typ des Elements
     * @throws Exception Exception, die geworfen wird, wenn beim Initialisieren ein Fehler passiert.
     */
    public Bauelement(OptischeBank optischeBank, Element xmlElement, int typ) throws Exception {
        super(new Vektor(xmlElement.getChild(XML_MITTELPUNKT)));
        this.optischeBank = optischeBank;
        this.typ = typ;
    }

    /**
     * Verschiebt das Bauelement um den uebergebenen Vektor.
     *
     * @param verschiebung Verschiebungsvektor.
     */
    public abstract void verschiebeUm(Vektor verschiebung);

    /**
     * Generiert einen neuen Rahmen, der an die Groesse des Elements angepasst ist.
     *
     * @return Neuer Rahmen.
     */
    public abstract Rahmen generiereRahmen();

    /**
     * Gibt eine Liste der Eigenschaftenregler, die benoetigt werden um die Werte des Bauelements durch den Benutzer zu manipulieren.
     *
     * @return Liste der Eigenschaftenregler.
     */
    public abstract Eigenschaftenregler[] gibEigenschaftenregler();

    /**
     * Gibt den Namen des Bauelements.
     *
     * @return Name des Bauelements.
     */
    public abstract String gibBauelementNamen();

    /**
     * Gibt an, ob dieses Element durch Verschieben in der Naehe der Optischen Achse auf diese gesetzt werden soll.
     *
     * @return Soll das Element auf die Optische Achse zentriert werden.
     */
    public boolean fangModusOptischeAchseAn() {
        return (typ != Bauelement.TYP_LAMPE);
    }

    /**
     * Aendere den Mittelpunkt dieses Elments.
     *
     * @param nMittelpunkt Neuer Mittelpunkt.
     */
    public void setzeMittelpunktNeu(Vektor nMittelpunkt) {
        verschiebeUm(Vektor.subtrahiere(nMittelpunkt, mittelPunkt));
    }

    /**
     * Aktualisiere den Rahmen dieses Elements durch einen neuen Rahmen, der an die Groesse des Elements angepasst ist.
     */
    public void rahmenAktualisieren() {
        setRahmen(generiereRahmen());
    }

    @Override
    public void waehleAus() {
        optischeBank.getEigenschaften().setOptionen(gibBauelementNamen(), gibEigenschaftenregler());
    }

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

    /**
     * @return Typ des Bauelements.
     */
    public int getTyp() {
        return typ;
    }

    /**
     * @return Referenz auf die zugehoerige Optische Bank.
     */
    public OptischeBank getOptischeBank() {
        return optischeBank;
    }

}
