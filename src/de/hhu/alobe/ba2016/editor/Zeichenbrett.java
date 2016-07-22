package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.grafik.Zeichenbar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Zeichenbrett extends JPanel {

    public ArrayList<Zeichenbar> zeichenObjekte;

    OptischeBank optischeBank;

    public Zeichenbrett(OptischeBank optischeBank) {
        this.optischeBank = optischeBank;
        zeichenObjekte = new ArrayList<>();
        this.setBackground(Color.WHITE);
        this.setLayout(null);
    }

    public void neuesZeichenObjekt(Zeichenbar zeichenObjekt) {
        zeichenObjekte.add(zeichenObjekt);
    }

    public void zeichenObjektLoeschen(Zeichenbar zeichenObjekt) {
        zeichenObjekte.remove(zeichenObjekt);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(optischeBank.getZoom(), optischeBank.getZoom());
        for(Zeichenbar zeichenObjekt: zeichenObjekte) {
            zeichenObjekt.paintComponent(g2D);
        }
    }

}
