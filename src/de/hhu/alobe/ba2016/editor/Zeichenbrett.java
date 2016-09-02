package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.grafik.Zeichenbar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Zeichenbrett einer Optischen Bank, der alle Objekte übergeben werden, die Zeichenbar sind.
 * Das Zeichenbrett verwaltet das Zeichnen dieser Objekte.
 */
public class Zeichenbrett extends JPanel {

    //Referenz auf die zugehörige optische Bank:
    private OptischeBank optischeBank;

    //Liste aller Objekte der Optischen Bank, die gezeichnet werden sollen:
    private ArrayList<Zeichenbar> zeichenObjekte;

    /**
     * Initialisiere neues Zeichenbrett.
     *
     * @param optischeBank Referenz auf zugehörige Optische Bank.
     */
    public Zeichenbrett(OptischeBank optischeBank) {
        this.optischeBank = optischeBank;
        zeichenObjekte = new ArrayList<>();
        this.setBackground(Color.WHITE);
        this.setLayout(null);
    }

    /**
     * Fügt ein neues Zeichenobjekt der Liste hinzu, welches gezeichnet werden soll.
     *
     * @param zeichenObjekt Objekt als Zeichenbar.
     */
    public void neuesZeichenObjekt(Zeichenbar zeichenObjekt) {
        zeichenObjekte.add(zeichenObjekt);
    }

    /**
     * Entfernt ein Zeichenobjekt aus der Liste, damit es nicht mehr gezeichnet wird.
     *
     * @param zeichenObjekt Zu löschendes Objekt als Zeichenbar.
     */
    public void zeichenObjektLoeschen(Zeichenbar zeichenObjekt) {
        zeichenObjekte.remove(zeichenObjekt);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        //Aktiviere Kantenglättung:
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Stelle Zoom ein:
        g2D.scale(optischeBank.getZoom(), optischeBank.getZoom());

        //Zeichne alle Objekte in der Liste zeichenObjekte:
        for (Zeichenbar zeichenObjekt : zeichenObjekte) {
            zeichenObjekt.paintComponent(g2D);
        }
    }

}
