package de.hhu.alobe.ba2016.grafik;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Abstrakte Klasse zum Laden und Verwalten der benutzten Grafikdateien
 */
public abstract class Grafiken {

    //Bauelemente:
    private static ImageIcon grafik_auge;
    private static ImageIcon grafik_blende;
    private static ImageIcon grafik_hohlspiegel;
    private static ImageIcon grafik_lampe;
    private static ImageIcon grafik_laser;
    private static ImageIcon grafik_linse;
    private static ImageIcon grafik_schirm;
    private static ImageIcon grafik_spiegel;

    //Buttons:
    private static ImageIcon grafik_auswahl;
    private static ImageIcon grafik_loeschen;
    private static ImageIcon grafik_zurueck;
    private static ImageIcon grafik_vorwaerts;
    private static ImageIcon grafik_zoomRein;
    private static ImageIcon grafik_zoomRaus;
    private static ImageIcon grafik_zoom100;

    /**
     * Laedt alle benutzten Grafiken und initialisiert ihre Datenfelder
     */
    public static void grafikenLaden() {
        //Bauelemente:
        grafik_auge = ladeIcon("gfx/Auge50.png");
        grafik_blende = ladeIcon("gfx/Blende50.png");
        grafik_hohlspiegel = ladeIcon("gfx/Hohlspiegel50.png");
        grafik_lampe = ladeIcon("gfx/Lampe50.png");
        grafik_laser = ladeIcon("gfx/Laser50.png");
        grafik_linse = ladeIcon("gfx/Linse50.png");
        grafik_schirm = ladeIcon("gfx/Schirm50.png");
        grafik_spiegel = ladeIcon("gfx/Spiegel50.png");

        //Buttons:
        grafik_auswahl = ladeIcon("gfx/Auswahl.png");
        grafik_loeschen = ladeIcon("gfx/Loeschen.png");
        grafik_zurueck = ladeIcon("gfx/Pfeil_links.png");
        grafik_vorwaerts = ladeIcon("gfx/Pfeil_rechts.png");
        grafik_zoomRein = ladeIcon("gfx/Zoom_rein.png");
        grafik_zoomRaus = ladeIcon("gfx/Zoom_raus.png");
        grafik_zoom100 = ladeIcon("gfx/Zoom_100.png");
    }

    /**
     * Laedt eine Grafikdatei als BufferedImage
     *
     * @param ort Pfad zu der Datei
     * @return Grafik als BufferedImage, null wenn Laden nicht moeglich war
     */
    private static BufferedImage ladeBild(String ort) {
        try {
            return ImageIO.read(new File(ort));
        } catch (IOException e) {
            System.out.println("Error: Grafikdatei " + ort + " nicht gefunden!");
            return null;
        }
    }

    /**
     * Laedt eine Grafikdatei als ImageIcon
     *
     * @param ort Pfad zu der Datei
     * @return Grafik als ImageIcon
     */
    private static ImageIcon ladeIcon(String ort) {
        URL pfad = ClassLoader.getSystemResource(ort);
        return new ImageIcon(pfad);
    }

    /**
     * @return Grafik fuer Bauelement Auge als ImageIcon
     */
    public static ImageIcon getGrafik_auge() {
        return grafik_auge;
    }

    /**
     * @return Grafik fuer Bauelement Blende als ImageIcon
     */
    public static ImageIcon getGrafik_blende() {
        return grafik_blende;
    }

    /**
     * @return Grafik fuer Bauelement Hohlspiegel als ImageIcon
     */
    public static ImageIcon getGrafik_hohlspiegel() {
        return grafik_hohlspiegel;
    }

    /**
     * @return Grafik fuer Bauelement Lampe als ImageIcon
     */
    public static ImageIcon getGrafik_lampe() {
        return grafik_lampe;
    }

    /**
     * @return Grafik fuer Bauelement Laser als ImageIcon
     */
    public static ImageIcon getGrafik_laser() {
        return grafik_laser;
    }

    /**
     * @return Grafik fuer Bauelement Linse als ImageIcon
     */
    public static ImageIcon getGrafik_linse() {
        return grafik_linse;
    }

    /**
     * @return Grafik fuer Bauelement Schirm als ImageIcon
     */
    public static ImageIcon getGrafik_schirm() {
        return grafik_schirm;
    }

    /**
     * @return Grafik fuer Bauelement Spiegel als ImageIcon
     */
    public static ImageIcon getGrafik_spiegel() {
        return grafik_spiegel;
    }

    /**
     * @return Grafik fuer Auswahl Button als ImageIcon
     */
    public static ImageIcon getGrafik_auswahl() {
        return grafik_auswahl;
    }

    /**
     * @return Grafik fuer Loeschen Button als ImageIcon
     */
    public static ImageIcon getGrafik_loeschen() {
        return grafik_loeschen;
    }

    /**
     * @return Grafik fuer Schritt Rueckwaerts Button als ImageIcon
     */
    public static ImageIcon getGrafik_zurueck() {
        return grafik_zurueck;
    }

    /**
     * @return Grafik fuer Schritt Vorwaerts Button als ImageIcon
     */
    public static ImageIcon getGrafik_vorwaerts() {
        return grafik_vorwaerts;
    }

    /**
     * @return rafik fuer Zoom Rein Button als ImageIcon
     */
    public static ImageIcon getGrafik_zoomRein() {
        return grafik_zoomRein;
    }

    /**
     * @return Grafik fuer Zoom Raus Button als ImageIcon
     */
    public static ImageIcon getGrafik_zoomRaus() {
        return grafik_zoomRaus;
    }

    /**
     * @return Grafik fuer Zoom100 Button als ImageIcon
     */
    public static ImageIcon getGrafik_zoom100() {
        return grafik_zoom100;
    }
}
