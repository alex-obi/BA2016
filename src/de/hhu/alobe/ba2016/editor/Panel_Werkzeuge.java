package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_ElementLoeschen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Panel_Werkzeuge extends JPanel implements ActionListener, ItemListener {

    private HauptFenster optikSimulator;

    private JButton werkzeug_bauelement_auswaehlen;
    private JButton werkzeug_bauelement_loeschen;

    private JButton button_rueckgaengig;
    private JButton button_wiederholen;

    private JButton button_zoom_rein;
    private JButton button_zoom_raus;
    private JButton button_zoom_100;

    private JButton button_snellius_hauptebene;
    private JCheckBox button_virtuellAnAus;
    private boolean snellius = false;


    public Panel_Werkzeuge(HauptFenster optikSimulator) {
        this.optikSimulator = optikSimulator;
        setPreferredSize(new Dimension(0, 35));

        werkzeug_bauelement_auswaehlen = new JButton("Auswählen");
        werkzeug_bauelement_auswaehlen.addActionListener(this);
        this.add(werkzeug_bauelement_auswaehlen);
        werkzeug_bauelement_loeschen = new JButton("Löschen");
        werkzeug_bauelement_loeschen.addActionListener(this);
        this.add(werkzeug_bauelement_loeschen);

        button_rueckgaengig =  new JButton("Zurück");
        button_rueckgaengig.addActionListener(this);
        this.add(button_rueckgaengig);
        button_wiederholen = new JButton("Vorwärts");
        button_wiederholen.addActionListener(this);
        this.add(button_wiederholen);

        button_zoom_rein = new JButton("Zoom +");
        button_zoom_rein.addActionListener(this);
        this.add(button_zoom_rein);
        button_zoom_raus = new JButton("Zoom -");
        button_zoom_raus.addActionListener(this);
        this.add(button_zoom_raus);
        button_zoom_100 = new JButton("Zoom 100%");
        button_zoom_100.addActionListener(this);
        this.add(button_zoom_100);

        button_snellius_hauptebene = new JButton("Snellius");
        button_snellius_hauptebene.addActionListener(this);
        this.add(button_snellius_hauptebene);
        button_virtuellAnAus = new JCheckBox("Virtuelle Strahlen", optikSimulator.gibAktuelleOptischeBank().isVirtuelleStrahlenAktiv());
        button_virtuellAnAus.addItemListener(this);
        this.add(button_virtuellAnAus);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();
        if(e.getSource().equals(werkzeug_bauelement_loeschen)) {
           optischeBank.werkzeugWechseln(new Werkzeug_ElementLoeschen(optikSimulator.gibAktuelleOptischeBank()));
        }
        if(e.getSource().equals(werkzeug_bauelement_auswaehlen)) {
           optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optikSimulator.gibAktuelleOptischeBank()));
        }
        if(e.getSource().equals(button_rueckgaengig)) {
           optischeBank.aktionRueckgaengig();
        }
        if(e.getSource().equals(button_wiederholen)) {
           optischeBank.aktionWiederholen();
        }
        if(e.getSource().equals(button_zoom_raus)) {
           optischeBank.zoomStufeRaus();
           optischeBank.aktualisieren();
        }
        if(e.getSource().equals(button_zoom_rein)) {
           optischeBank.zoomStufeRein();
           optischeBank.aktualisieren();
        }
        if(e.getSource().equals(button_zoom_100)) {
           optischeBank.setZoom(1);
           optischeBank.aktualisieren();
        }
        if(e.getSource().equals(button_snellius_hauptebene)) {
            if(snellius) {
                snellius = false;
               optischeBank.setModus(OptischeBank.MODUS_HAUPTEBENE);
                button_snellius_hauptebene.setText("Snellius");
            } else {
                snellius = true;
               optischeBank.setModus(OptischeBank.MODUS_SNELLIUS);
                button_snellius_hauptebene.setText("Hauptebene");
            }
           optischeBank.aktualisieren();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();
        if(e.getSource().equals(button_virtuellAnAus)) {
            optischeBank.setVirtuelleStrahlenAktiv(button_virtuellAnAus.isSelected());
            optischeBank.aktualisieren();
        }
    }
}
