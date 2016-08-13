package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.HauptFenster;
import de.hhu.alobe.ba2016.editor.werkzeuge.*;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
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

public class Fenster_Werkzeuge extends JDialog implements ActionListener{

    public final int FENSTER_X = 200;
    public final int FENSTER_Y = 400;

    private JButton werkzeug_linse_neu;
    private JButton werkzeug_spiegel_neu;
    private JButton werkzeug_schirm_neu;
    private JButton werkzeug_lampe_neu;

    private JButton werkzeug_bauelement_loeschen;
    private JButton werkzeug_bauelement_auswaehlen;

    HauptFenster optikSimulator;

    public Fenster_Werkzeuge(HauptFenster optikSimulator) {
        super(optikSimulator);
        this.setTitle("Werkzeuge");
        this.setIconImage(null);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.optikSimulator = optikSimulator;
        this.setSize(FENSTER_X, FENSTER_Y);
        this.setLocation(optikSimulator.getX() - FENSTER_X - 20, optikSimulator.getY());

        this.setLayout(new GridLayout(4,2));

        werkzeug_linse_neu = new JButton("Linse");
        werkzeug_linse_neu.addActionListener(this);
        add(werkzeug_linse_neu);
        werkzeug_spiegel_neu = new JButton("Hohlspiegel");
        werkzeug_spiegel_neu.addActionListener(this);
        add(werkzeug_spiegel_neu);
        werkzeug_schirm_neu = new JButton("Schirm");
        werkzeug_schirm_neu.addActionListener(this);
        add(werkzeug_schirm_neu);
        werkzeug_lampe_neu = new JButton("Lampe");
        werkzeug_lampe_neu.addActionListener(this);
        add(werkzeug_lampe_neu);
        werkzeug_bauelement_loeschen = new JButton("Löschen");
        werkzeug_bauelement_loeschen.addActionListener(this);
        add(werkzeug_bauelement_loeschen);
        werkzeug_bauelement_auswaehlen = new JButton("Auswählen");
        werkzeug_bauelement_auswaehlen.addActionListener(this);
        add(werkzeug_bauelement_auswaehlen);
        add(new JButton(" 7"));
        add(new JButton(" 8"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(werkzeug_linse_neu)) {
            float brennweite = 0;
            String eingabe = JOptionPane.showInputDialog(HauptFenster.get(), "Brennweite der Linse:", "Neue Linse erstellen", JOptionPane.QUESTION_MESSAGE);
            if(eingabe != null) {
                brennweite = Float.parseFloat(eingabe);
            }
            if(brennweite != 0) {
                Linse neueLinse = new Linse(optikSimulator.gibAktuelleOptischeBank(), new VektorFloat(0, 0), brennweite);
                optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_NeuesBauelement(optikSimulator.gibAktuelleOptischeBank(), neueLinse));
            }
        }
        if(e.getSource().equals(werkzeug_spiegel_neu)) {
            float hoehe = 0;
            String eingabe = JOptionPane.showInputDialog(HauptFenster.get(), "Hoehe des Spiegels:", "Neuen Hohlspiegel erstellen", JOptionPane.QUESTION_MESSAGE);
            if(eingabe != null) {
                hoehe = Float.parseFloat(eingabe);
            }
            if(hoehe != 0) {
                Spiegel neuerSpiegel = new Spiegel(optikSimulator.gibAktuelleOptischeBank(), new VektorFloat(0, 0), hoehe);
                optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_NeuesBauelement(optikSimulator.gibAktuelleOptischeBank(), neuerSpiegel));
            }
        }
        if(e.getSource().equals(werkzeug_schirm_neu)) {
            int hoehe = 0;
            String eingabe = JOptionPane.showInputDialog(HauptFenster.get(), "Hoehe des Schirms:", "Neuen Schirm erstellen", JOptionPane.QUESTION_MESSAGE);
            if(eingabe != null) {
                hoehe = Integer.parseInt(eingabe);
            }
            if(hoehe != 0) {
                Schirm neuerSchirm = new Schirm(optikSimulator.gibAktuelleOptischeBank(), new VektorFloat(0, 0), hoehe);
                optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_NeuesBauelement(optikSimulator.gibAktuelleOptischeBank(), neuerSchirm));
            }
        }
        if(e.getSource().equals(werkzeug_lampe_neu)) {
            String farbenString = (String)JOptionPane.showInputDialog(
                    HauptFenster.get(),
                    "Farbe der Lampe:",
                    "Neue Lampe erstellen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Farbe.farbenpalette.keySet().toArray(),
                    "Schwarz");
            if(farbenString != null) {

                Lichtquelle lampe = new PunktLichtquelle(optikSimulator.gibAktuelleOptischeBank(), new VektorFloat(0, 0), Farbe.getColor(farbenString));
                optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_NeuesBauelement(optikSimulator.gibAktuelleOptischeBank(), lampe));
            }
        }
        if(e.getSource().equals(werkzeug_bauelement_loeschen)) {
            optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_ElementLoeschen(optikSimulator.gibAktuelleOptischeBank()));
        }
        if(e.getSource().equals(werkzeug_bauelement_auswaehlen)) {
            optikSimulator.gibAktuelleOptischeBank().werkzeugWechseln(new Werkzeug_Auswahl(optikSimulator.gibAktuelleOptischeBank()));
        }

    }
}
