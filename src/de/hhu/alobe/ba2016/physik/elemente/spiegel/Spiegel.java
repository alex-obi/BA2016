package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler_Slider;
import de.hhu.alobe.ba2016.editor.eigenschaften.ReglerEvent;
import de.hhu.alobe.ba2016.mathe.Vektor;
import org.jdom2.Element;

import javax.swing.*;
import java.util.ArrayList;

public class Spiegel extends Hohlspiegel{

    public static final String NAME = "Spiegel";
    public static final String XML_SPIEGEL = "spiegel";

    private Eigenschaftenregler_Slider slide_hoehe;

    public Spiegel(OptischeBank optischeBank, Vektor mittelPunkt, float hoehe) {
        super(optischeBank, mittelPunkt, 0, hoehe);
        initialisiere();
    }

    public Spiegel(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement);
        initialisiere();
    }

    private void initialisiere() {

        slide_hoehe = new Eigenschaftenregler_Slider("Hoehe", "cm", 100, hoehe, MIND_HOEHE, MAX_HOEHE, new ReglerEvent() {
            @Override
            public void reglerWurdeVerschoben(double wert) {
                setHoehe(wert);
                optischeBank.aktualisieren();
            }

            @Override
            public double berechneReglerWert(double reglerProzent, double minimum, double maximum) {
                return Eigenschaften.prozentZuLinear(reglerProzent, minimum, maximum);
            }

            @Override
            public double berechneReglerProzent(double wert, double minimum, double maximum) {
                return Eigenschaften.linearZuProzent(wert, minimum, maximum);
            }

            @Override
            public String berechnePhysikalischenWert(double zahl) {
                return Eigenschaften.laengeZuCm(zahl);
            }
        });
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten =  new Eigenschaftenregler[1];
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
