package de.hhu.alobe.ba2016.physik.elemente.Licht;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Farbe {

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

}
