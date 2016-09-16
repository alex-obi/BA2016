package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.editor.werkzeuge.*;
import de.hhu.alobe.ba2016.grafik.Grafiken;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Auge.Auge;
import de.hhu.alobe.ba2016.physik.elemente.Licht.ParallelLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Blende;
import de.hhu.alobe.ba2016.physik.elemente.spiegel.Hohlspiegel;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Farbe;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.Licht.PunktLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;
import de.hhu.alobe.ba2016.physik.elemente.spiegel.Spiegel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenster zum Auswaehlen und Erstellen von neuen Bauelementen.
 * Verwaltet die Buttons als ActionListener.
 */
public class Fenster_Bauelemente extends JDialog implements ActionListener {

    /**
     * Breite der Buttons.
     */
    public final int BUTTON_BREITE = 60;

    /**
     * Hoehe der Buttons.
     */
    public final int BUTTON_HOEHE = 60;

    //Buttons zu jedem Bauelement:
    private JButton werkzeug_linse;
    private JButton werkzeug_auge;
    private JButton werkzeug_spiegel;
    private JButton werkzeug_hohlspiegel;
    private JButton werkzeug_punktlicht;
    private JButton werkzeug_laser;
    private JButton werkzeug_blende;
    private JButton werkzeug_schirm;

    //Referenz auf das Hauptfenster:
    private HauptFenster hauptFenster;

    /**
     * Initialisiert das Fenster mit vorgegebenen Werten.
     *
     * @param hauptFenster Referenz auf das Hauptfenster.
     */
    public Fenster_Bauelemente(HauptFenster hauptFenster) {
        super(hauptFenster);
        this.setTitle("Bauelemente");
        this.setIconImage(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.hauptFenster = hauptFenster;

        //Position relativ zum Hauptfenster
        this.setLocation(hauptFenster.getX() - 135, hauptFenster.getY());

        //Buttons initialisieren und anzeigen:
        JPanel fensterInhalt = new JPanel(new GridLayout(4, 2));

        werkzeug_linse = new JButton();
        werkzeug_linse.setIcon(Grafiken.getGrafik_linse());
        werkzeug_linse.setToolTipText("Linse");
        fensterInhalt.add(werkzeug_linse);

        werkzeug_auge = new JButton();
        werkzeug_auge.setIcon(Grafiken.getGrafik_auge());
        werkzeug_auge.setToolTipText("Auge");
        fensterInhalt.add(werkzeug_auge);

        werkzeug_spiegel = new JButton();
        werkzeug_spiegel.setIcon(Grafiken.getGrafik_spiegel());
        werkzeug_spiegel.setToolTipText("Spiegel");
        fensterInhalt.add(werkzeug_spiegel);

        werkzeug_hohlspiegel = new JButton();
        werkzeug_hohlspiegel.setIcon(Grafiken.getGrafik_hohlspiegel());
        werkzeug_hohlspiegel.setToolTipText("Hohlspiegel");
        fensterInhalt.add(werkzeug_hohlspiegel);

        werkzeug_punktlicht = new JButton();
        werkzeug_punktlicht.setIcon(Grafiken.getGrafik_lampe());
        werkzeug_punktlicht.setToolTipText("Punktlichtquelle");
        fensterInhalt.add(werkzeug_punktlicht);

        werkzeug_laser = new JButton();
        werkzeug_laser.setIcon(Grafiken.getGrafik_laser());
        werkzeug_laser.setToolTipText("Parallellichtquelle");
        fensterInhalt.add(werkzeug_laser);

        werkzeug_blende = new JButton();
        werkzeug_blende.setIcon(Grafiken.getGrafik_blende());
        werkzeug_blende.setToolTipText("Blende");
        fensterInhalt.add(werkzeug_blende);

        werkzeug_schirm = new JButton();
        werkzeug_schirm.setIcon(Grafiken.getGrafik_schirm());
        werkzeug_schirm.setToolTipText("Schirm");
        fensterInhalt.add(werkzeug_schirm);

        for (Component comp : fensterInhalt.getComponents()) {
            ((JButton) comp).addActionListener(this);
            comp.setPreferredSize(new Dimension(BUTTON_BREITE, BUTTON_HOEHE));
            comp.setBackground(Color.WHITE);
        }

        this.add(fensterInhalt);
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptischeBank optBank = hauptFenster.getAktuelleOptischeBank();
        if (e.getSource().equals(werkzeug_linse)) {
            String[] typen = {"Sammellinse", "Streulinse"};
            String typString = (String) JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Linsentyp:",
                    "Neue Linse erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    typen,
                    "Sammellinse");
            if (typString != null) {
                Linse nLinse;
                if (typString.equals("Sammellinse")) {
                    nLinse = new Linse(optBank, new Vektor(0, 0), 200);
                } else if (typString.equals("Streulinse")) {
                    nLinse = new Linse(optBank, new Vektor(0, 0), -200);
                } else {
                    nLinse = new Linse(optBank, new Vektor(0, 0), -200);
                }
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(nLinse));
            }
        }
        if (e.getSource().equals(werkzeug_auge)) {
            Auge auge = new Auge(optBank, new Vektor(0, 0));
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(auge));
        }

        if (e.getSource().equals(werkzeug_spiegel)) {
            Spiegel neuerSpiegel = new Spiegel(optBank, new Vektor(0, 0), (int) (Spiegel.MAX_HOEHE / 2));
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(neuerSpiegel));
        }
        if (e.getSource().equals(werkzeug_hohlspiegel)) {
            Hohlspiegel hohlspiegel = new Hohlspiegel(optBank, new Vektor(0, 0), 400, Hohlspiegel.MAX_HOEHE / 2);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(hohlspiegel));
        }

        if (e.getSource().equals(werkzeug_punktlicht)) {
            String farbenString = (String) JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Farbe der Punktlichtquelle:",
                    "Neue Punktlichtquelle erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Farbe.farbenpalette.keySet().toArray(),
                    "Schwarz");
            if (farbenString != null) {
                Lichtquelle lampe = new PunktLichtquelle(optBank, new Vektor(0, 0), new Farbe(farbenString));
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(lampe));
            }
        }
        if (e.getSource().equals(werkzeug_laser)) {
            String farbenString = (String) JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Farbe der Parallellichtquelle:",
                    "Neue Parallellichtquelle erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Farbe.farbenpalette.keySet().toArray(),
                    "Schwarz");
            if (farbenString != null) {
                ParallelLichtquelle lampe = new ParallelLichtquelle(optBank, new Vektor(0, 0), new Farbe(farbenString), ParallelLichtquelle.MAX_HOEHE / 2, 0);
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(lampe));
            }
        }

        if (e.getSource().equals(werkzeug_blende)) {
            Blende blende = new Blende(optBank, new Vektor(0, 0), Blende.MAX_HOEHE / 2, Blende.MAX_HOEHE / 4);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(blende));
        }
        if (e.getSource().equals(werkzeug_schirm)) {
            Schirm neuerSchirm = new Schirm(optBank, new Vektor(0, 0), Schirm.MAX_HOEHE / 2);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(neuerSchirm));
        }

    }
}
