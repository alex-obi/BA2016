package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_ElementLoeschen;
import de.hhu.alobe.ba2016.grafik.Grafiken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Panel_Werkzeuge extends JPanel implements ActionListener {

    private HauptFenster optikSimulator;

    private JButton werkzeug_bauelement_auswaehlen;
    private JButton werkzeug_bauelement_loeschen;

    private JButton button_rueckgaengig;
    private JButton button_wiederholen;

    private JButton button_zoom_rein;
    private JButton button_zoom_raus;
    private JButton button_zoom_100;


    public Panel_Werkzeuge(HauptFenster optikSimulator) {
        this.optikSimulator = optikSimulator;
        setPreferredSize(new Dimension(0, 35));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        werkzeug_bauelement_auswaehlen = new JButton();
        werkzeug_bauelement_auswaehlen.setPreferredSize(new Dimension(26, 27));
        werkzeug_bauelement_auswaehlen.setIcon(Grafiken.grafik_auswahl);
        werkzeug_bauelement_auswaehlen.setToolTipText("Auswaehlen");
        werkzeug_bauelement_auswaehlen.addActionListener(this);
        this.add(werkzeug_bauelement_auswaehlen);

        werkzeug_bauelement_loeschen = new JButton();
        werkzeug_bauelement_loeschen.setPreferredSize(new Dimension(26, 27));
        werkzeug_bauelement_loeschen.setIcon(Grafiken.grafik_loeschen);
        werkzeug_bauelement_loeschen.setToolTipText("Loeschen");
        werkzeug_bauelement_loeschen.addActionListener(this);
        this.add(werkzeug_bauelement_loeschen);

        button_rueckgaengig =  new JButton();
        button_rueckgaengig.setPreferredSize(new Dimension(26, 27));
        button_rueckgaengig.setIcon(Grafiken.grafik_zurueck);
        button_rueckgaengig.setToolTipText("Zurueck");
        button_rueckgaengig.addActionListener(this);
        this.add(button_rueckgaengig);

        button_wiederholen = new JButton();
        button_wiederholen.setPreferredSize(new Dimension(26, 27));
        button_wiederholen.setIcon(Grafiken.grafik_vorwaerts);
        button_wiederholen.setToolTipText("Vorwaerts");
        button_wiederholen.addActionListener(this);
        this.add(button_wiederholen);

        button_zoom_rein = new JButton();
        button_zoom_rein.setPreferredSize(new Dimension(26, 27));
        button_zoom_rein.setIcon(Grafiken.grafik_zoomRein);
        button_zoom_rein.setToolTipText("Reinzoomen");
        button_zoom_rein.addActionListener(this);
        this.add(button_zoom_rein);
        button_zoom_raus = new JButton();
        button_zoom_raus.setPreferredSize(new Dimension(26, 27));
        button_zoom_raus.setIcon(Grafiken.grafik_zoomRaus);
        button_zoom_raus.setToolTipText("Rauszoomen");
        button_zoom_raus.addActionListener(this);
        this.add(button_zoom_raus);
        button_zoom_100 = new JButton();
        button_zoom_100.setPreferredSize(new Dimension(26, 27));
        button_zoom_100.setIcon(Grafiken.grafik_zoom100);
        button_zoom_100.setToolTipText("Originalgroesse");
        button_zoom_100.addActionListener(this);
        this.add(button_zoom_100);

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
    }

}
