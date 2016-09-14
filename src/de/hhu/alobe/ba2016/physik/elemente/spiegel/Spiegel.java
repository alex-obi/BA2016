package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Vektor;
import org.jdom2.Element;


/**
 * Spiegel als Hohlspiegel mit Radius -> unendlich.
 */
public class Spiegel extends Hohlspiegel {

    /**
     * Name des Bauelements.
     */
    public static final String NAME = "Spiegel";

    /**
     * Name des Spiegels im XML-Dokument.
     */
    public static final String XML_SPIEGEL = "spiegel";

    //Eigenschaftenregler zur Manipulation des Spiegels durch den Benutzer:
    private Eigenschaftenregler_Slider slide_hoehe;

    /**
     * Initialisiere Spiegel mit Hoehe.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelPunkt  Mittelpunkt des Spiegels.
     * @param hoehe        Hoehe des Spiegels.
     */
    public Spiegel(OptischeBank optischeBank, Vektor mittelPunkt, float hoehe) {
        super(optischeBank, mittelPunkt, 0, hoehe);
        initialisiere();
    }

    /**
     * Initialisiere Spiegel ueber jdom2.Element.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement   jdom2.Element, das benoetigte Attribute enthaelt.
     * @throws Exception Expection, die geworfen wird, wenn bei der Initialisierung ein Fehler passiert.
     */
    public Spiegel(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement);
        initialisiere();
    }

    //Initialisiere die Werte des Spiegels und erstelle Eigenschaftenregler zur Manipulation des Spiegels durch den Benutzer.
    private void initialisiere() {

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

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten = new Eigenschaftenregler[1];
        komponenten[0] = slide_hoehe;
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_SPIEGEL;
    }

}
