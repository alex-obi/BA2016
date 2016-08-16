package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.HauptFenster;
import de.hhu.alobe.ba2016.editor.werkzeuge.*;
import de.hhu.alobe.ba2016.grafik.Grafiken;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Auge.Auge;
import de.hhu.alobe.ba2016.physik.elemente.Licht.ParrallelLichtquelle;
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

public class Fenster_Bauelemente extends JDialog implements ActionListener{

    public final int BUTTON_BREITE = 60;
    public final int BUTTON_HOEHE = 60;

    private JPanel fensterInhalt;

    private JButton werkzeug_linse;
    private JButton werkzeug_auge;
    private JButton werkzeug_spiegel;
    private JButton werkzeug_hohlspiegel;
    private JButton werkzeug_punktlicht;
    private JButton werkzeug_laser;
    private JButton werkzeug_blende;
    private JButton werkzeug_schirm;

    private HauptFenster optikSimulator;

    public Fenster_Bauelemente(HauptFenster optikSimulator) {
        super(optikSimulator);
        this.setTitle("Bauelemente");
        this.setIconImage(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.optikSimulator = optikSimulator;
        this.setLocation(optikSimulator.getX() - 135, optikSimulator.getY());

        fensterInhalt = new JPanel(new GridLayout(4, 2));
        //fensterInhalt.setPreferredSize(new Dimension(PANEL_X, PANEL_Y));

        werkzeug_linse = new JButton();
        werkzeug_linse.setIcon(Grafiken.grafik_linse);
        werkzeug_linse.setToolTipText("Linse");
        fensterInhalt.add(werkzeug_linse);

        werkzeug_auge = new JButton();
        werkzeug_auge.setIcon(Grafiken.grafik_auge);
        werkzeug_auge.setToolTipText("Auge");
        fensterInhalt.add(werkzeug_auge);

        werkzeug_spiegel = new JButton();
        werkzeug_spiegel.setIcon(Grafiken.grafik_spiegel);
        werkzeug_spiegel.setToolTipText("Spiegel");
        fensterInhalt.add(werkzeug_spiegel);

        werkzeug_hohlspiegel = new JButton();
        werkzeug_hohlspiegel.setIcon(Grafiken.grafik_hohlspiegel);
        werkzeug_hohlspiegel.setToolTipText("Hohlspiegel");
        fensterInhalt.add(werkzeug_hohlspiegel);

        werkzeug_punktlicht = new JButton();
        werkzeug_punktlicht.setIcon(Grafiken.grafik_lampe);
        werkzeug_punktlicht.setToolTipText("Punktlichtquelle");
        fensterInhalt.add(werkzeug_punktlicht);

        werkzeug_laser = new JButton();
        werkzeug_laser.setIcon(Grafiken.grafik_laser);
        werkzeug_laser.setToolTipText("Parallellichtquelle");
        fensterInhalt.add(werkzeug_laser);

        werkzeug_blende = new JButton();
        werkzeug_blende.setIcon(Grafiken.grafik_blende);
        werkzeug_blende.setToolTipText("Blende");
        fensterInhalt.add(werkzeug_blende);

        werkzeug_schirm = new JButton();
        werkzeug_schirm.setIcon(Grafiken.grafik_schirm);
        werkzeug_schirm.setToolTipText("Schirm");
        fensterInhalt.add(werkzeug_schirm);

        for(Component comp : fensterInhalt.getComponents()) {
            ((JButton)comp).addActionListener(this);
            comp.setPreferredSize(new Dimension(BUTTON_BREITE, BUTTON_HOEHE));
            comp.setBackground(Color.WHITE);
        }

        this.add(fensterInhalt);
        this.pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptischeBank optBank = optikSimulator.gibAktuelleOptischeBank();
        if(e.getSource().equals(werkzeug_linse)) {
            String[] typen = {"Sammellinse", "Streulinse"};
            String typString = (String)JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Linsentyp:",
                    "Neue Linse erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    typen,
                    "Sammellinse");
            if(typString != null) {
                Linse nLinse;
                if(typString.equals("Sammellinse")) {
                    nLinse = new Linse(optBank, new Vektor(0, 0), 150);
                } else if(typString.equals("Streulinse")) {
                    nLinse = new Linse(optBank, new Vektor(0, 0), -150);
                } else {
                    nLinse = new Linse(optBank, new Vektor(0, 0), -150); //Eventuell durch weitere Typen zu ersetzen
                }
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, nLinse));
            }
        }
        if(e.getSource().equals(werkzeug_auge)) {
            Auge auge = new Auge(optBank, new Vektor(0, 0));
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, auge));
        }

        if(e.getSource().equals(werkzeug_spiegel)) {
            Spiegel neuerSpiegel = new Spiegel(optBank, new Vektor(0, 0), Spiegel.MAX_HOEHE / 2);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, neuerSpiegel));
        }
        if(e.getSource().equals(werkzeug_hohlspiegel)) {
            Hohlspiegel hohlspiegel = new Hohlspiegel(optBank, new Vektor(0, 0), 400, Hohlspiegel.MAX_HOEHE / 2);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, hohlspiegel));
        }

        if(e.getSource().equals(werkzeug_punktlicht)) {
            String farbenString = (String)JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Farbe der Punktlichtquelle:",
                    "Neue Punktlichtquelle erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Farbe.farbenpalette.keySet().toArray(),
                    "Schwarz");
            if(farbenString != null) {
                Lichtquelle lampe = new PunktLichtquelle(optBank, new Vektor(0, 0), Farbe.getColor(farbenString));
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, lampe));
            }
        }
        if(e.getSource().equals(werkzeug_laser)) {
            String farbenString = (String)JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Farbe der Parallellichtquelle:",
                    "Neue Parallellichtquelle erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Farbe.farbenpalette.keySet().toArray(),
                    "Schwarz");
            if(farbenString != null) {
                ParrallelLichtquelle lampe = new ParrallelLichtquelle(optBank, new Vektor(0, 0), Farbe.getColor(farbenString), ParrallelLichtquelle.MAX_HOEHE  / 2, 0);
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, lampe));
            }
        }

        if(e.getSource().equals(werkzeug_blende)) {
            Blende blende = new Blende(optBank, new Vektor(0, 0), Blende.MAX_HOEHE / 2, Blende.MAX_HOEHE / 4);
            optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, blende));
        }
        if(e.getSource().equals(werkzeug_schirm)) {
                Schirm neuerSchirm = new Schirm(optBank, new Vektor(0, 0), Schirm.MAX_HOEHE / 2);
                optBank.werkzeugWechseln(new Werkzeug_NeuesBauelement(optBank, neuerSchirm));
        }

    }
}
