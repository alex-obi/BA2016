package de.hhu.alobe.ba2016.physik.elemente.Licht;

import org.jdom2.Element;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Farbe, die über deutsche Namen ausgewählt werden kann.
 */
public class Farbe extends Color {

    /**
     * Name für Rotanteil im XML-Dokument.
     */
    public static final String XML_RED = "r";

    /**
     * Name für Grünanteil im XML-Dokument.
     */
    public static final String XML_GREEN = "g";

    /**
     * Name für Grünanteil im XML-Dokument.
     */
    public static final String XML_BLUE = "b";

    /**
     * Hashmap um Namen den entsprechenden Farben zuzuordnen. Wird statisch initialisiert.
     */
    public static HashMap<String, Color> farbenpalette;

    //Initialisiere Farbenpalette bei Programmstart:
    static {
        farbenpalette = new HashMap<>();
        farbenpalette.put("Schwarz", Color.BLACK);
        farbenpalette.put("Rot", Color.RED);
        farbenpalette.put("Orange", Color.ORANGE);
        farbenpalette.put("Gelb", Color.YELLOW);
        farbenpalette.put("Gruen", Color.GREEN);
        farbenpalette.put("Blau", Color.BLUE);

    }

    /**
     * Initialisiert Farbe über Farbanteile.
     *
     * @param r Rotanteil.
     * @param g Grünanteil.
     * @param b Blauanteil.
     */
    public Farbe(int r, int g, int b) {
        super(r, g, b);
    }

    /**
     * Initialisiert Farbe über Color.
     *
     * @param farbe Farbe als Color.
     */
    public Farbe(Color farbe) {
        super(farbe.getRGB());
    }

    /**
     * Initialisiert Farbe über Namen der Farbe aus der Farbenpalette
     *
     * @param name Name der Farbe aus der Farbenpalette.
     */
    public Farbe(String name) {
        super(getColor(name).getRGB());
    }

    /**
     * Initialisiert Farbe über ein jdom2.Element.
     *
     * @param xmlElement jdom2.Element mit benötigten Attributen.
     * @throws Exception Expection, die geworfen wird, wenn bei der Initialisierung ein Fehler passiert.
     */
    public Farbe(Element xmlElement) throws Exception {
        super(xmlElement.getAttribute(XML_RED).getIntValue(), xmlElement.getAttribute(XML_GREEN).getIntValue(), xmlElement.getAttribute(XML_BLUE).getIntValue());
    }

    /**
     * Gibt zu einer bestimmten Farbe den Namen (wenn diese in der Farbenpalette existiert).
     *
     * @param farbe Farbe, dessen Name bestimmt werden soll.
     * @return Name der Farbe.
     */
    public static String gibFarbenName(Color farbe) {
        for (Map.Entry<String, Color> f : farbenpalette.entrySet()) {
            if (f.getValue().equals(farbe)) {
                return f.getKey();
            }
        }
        return "Unbekannte Farbe";
    }

    /**
     * Gibt die Farbe als Color zu einem bestimmten Namen, wenn dieser in der Farbenpalette existiert.
     *
     * @param farbenName Name der Farbe.
     * @return Farbe als Color.
     */
    public static Color getColor(String farbenName) {
        return farbenpalette.get(farbenName);
    }

    /**
     * Erstellt ein jdom2.Element mit dem übergebenen Namen. So können Farben beliebig mit Namen versehen werden, um diese identifizieren zu können.
     *
     * @param name Name der Farbe im XML-Dokument.
     * @return jdom2.Element dieses Vektors.
     */
    public Element getXmlElement(String name) {
        Element xmlElement = new Element(name);
        xmlElement.setAttribute(XML_RED, String.valueOf(getRed()));
        xmlElement.setAttribute(XML_GREEN, String.valueOf(getGreen()));
        xmlElement.setAttribute(XML_BLUE, String.valueOf(getBlue()));
        return xmlElement;
    }

}
