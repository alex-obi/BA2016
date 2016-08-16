package de.hhu.alobe.ba2016.grafik;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Grafiken {

    public static ImageIcon grafik_auge;
    public static ImageIcon grafik_blende;
    public static ImageIcon grafik_hohlspiegel;
    public static ImageIcon grafik_lampe;
    public static ImageIcon grafik_laser;
    public static ImageIcon grafik_linse;
    public static ImageIcon grafik_schirm;
    public static ImageIcon grafik_spiegel;

    public static void grafikenLaden() {
        grafik_auge = ladeIcon("gfx/Auge50.png");
        grafik_blende = ladeIcon("gfx/Blende50.png");
        grafik_hohlspiegel = ladeIcon("gfx/Hohlspiegel50.png");
        grafik_lampe = ladeIcon("gfx/Lampe50.png");
        grafik_laser = ladeIcon("gfx/Laser50.png");
        grafik_linse = ladeIcon("gfx/Linse50.png");
        grafik_schirm = ladeIcon("gfx/Schirm50.png");
        grafik_spiegel = ladeIcon("gfx/Spiegel50.png");
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
        try {
            return new ImageIcon(ImageIO.read(new File(ort)));
        } catch (IOException e) {
            System.out.println("Error: Grafikdatei " + ort + " nicht gefunden!");
            return null;
        }
    }
}
