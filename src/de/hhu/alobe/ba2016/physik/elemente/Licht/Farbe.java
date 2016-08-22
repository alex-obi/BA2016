package de.hhu.alobe.ba2016.physik.elemente.Licht;

import org.jdom2.Element;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Farbe extends Color {

    public static final String XML_RED = "r";
    public static final String XML_GREEN = "g";
    public static final String XML_BLUE = "b";

    public static HashMap<String, Color> farbenpalette;

    static {
        farbenpalette = new HashMap<>();
        farbenpalette.put("Schwarz", Color.BLACK);
        farbenpalette.put("Rot", Color.RED);
        farbenpalette.put("Orange", Color.ORANGE);
        farbenpalette.put("Gelb", Color.YELLOW);
        farbenpalette.put("Gruen", Color.GREEN);
        farbenpalette.put("Blau", Color.BLUE);

    }

    public Farbe(int r, int g, int b) {
        super(r, g, b);
    }

    public Farbe(Color farbe) {
        super(farbe.getRGB());
    }

    public Farbe(String name) {
        super(getColor(name).getRGB());
    }

    public Farbe(Element xmlElement) throws Exception {
        super(xmlElement.getAttribute(XML_RED).getIntValue(), xmlElement.getAttribute(XML_GREEN).getIntValue(), xmlElement.getAttribute(XML_BLUE).getIntValue());
    }

    public static String gibFarbenName(Color farbe) {
        for(Map.Entry<String, Color> f : farbenpalette.entrySet()) {
            if(f.getValue().equals(farbe)) {
                return f.getKey();
            }
        }
        return "Unbekannte Farbe";
    }

    public static Color getColor(String farbenName) {
        return farbenpalette.get(farbenName);
    }

    public Element getXmlElement(String name) {
        Element xmlElement = new Element(name);
        xmlElement.setAttribute(XML_RED, String.valueOf(getRed()));
        xmlElement.setAttribute(XML_GREEN, String.valueOf(getGreen()));
        xmlElement.setAttribute(XML_BLUE, String.valueOf(getBlue()));
        return xmlElement;
    }

}
