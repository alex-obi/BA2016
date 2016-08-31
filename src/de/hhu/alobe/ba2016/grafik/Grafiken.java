package de.hhu.alobe.ba2016.grafik;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Grafiken {

    public static ImageIcon grafik_auge;
    public static ImageIcon grafik_blende;
    public static ImageIcon grafik_hohlspiegel;
    public static ImageIcon grafik_lampe;
    public static ImageIcon grafik_laser;
    public static ImageIcon grafik_linse;
    public static ImageIcon grafik_schirm;
    public static ImageIcon grafik_spiegel;

    public static ImageIcon grafik_auswahl;
    public static ImageIcon grafik_loeschen;
    public static ImageIcon grafik_zurueck;
    public static ImageIcon grafik_vorwaerts;
    public static ImageIcon grafik_zoomRein;
    public static ImageIcon grafik_zoomRaus;
    public static ImageIcon grafik_zoom100;

    public static void grafikenLaden() {
        grafik_auge = ladeIcon("gfx/Auge50.png");
        grafik_blende = ladeIcon("gfx/Blende50.png");
        grafik_hohlspiegel = ladeIcon("gfx/Hohlspiegel50.png");
        grafik_lampe = ladeIcon("gfx/Lampe50.png");
        grafik_laser = ladeIcon("gfx/Laser50.png");
        grafik_linse = ladeIcon("gfx/Linse50.png");
        grafik_schirm = ladeIcon("gfx/Schirm50.png");
        grafik_spiegel = ladeIcon("gfx/Spiegel50.png");

        grafik_auswahl = ladeIcon("gfx/Auswahl.png");
        grafik_loeschen = ladeIcon("gfx/Loeschen.png");
        grafik_zurueck = ladeIcon("gfx/Pfeil_links.png");
        grafik_vorwaerts = ladeIcon("gfx/Pfeil_rechts.png");
        grafik_zoomRein = ladeIcon("gfx/Zoom_rein.png");
        grafik_zoomRaus = ladeIcon("gfx/Zoom_raus.png");
        grafik_zoom100 = ladeIcon("gfx/Zoom_100.png");
    }

    private static BufferedImage ladeBild(String ort) {
        try {
            return ImageIO.read(new File(ort));
        } catch (IOException e) {
            System.out.println("Error: Grafikdatei " + ort + " nicht gefunden!");
            return null;
        }
    }

    private static ImageIcon ladeIcon(String ort) {
        URL pfad = ClassLoader.getSystemResource(ort);
        return new ImageIcon(pfad);
    }
}
