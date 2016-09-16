package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_ElementLoeschen;
import de.hhu.alobe.ba2016.grafik.Grafiken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Werkzeugleiste zum schnellen Auswaehlen durch den Benutzer. Verwaltet Buttons als ActionListener
 */
public class Panel_Werkzeuge extends JPanel implements ActionListener {

     //Referenz auf das Hauptfenster
    private HauptFenster hauptFenster;

    //Werkzeugbuttons:
    private JButton werkzeug_bauelement_auswaehlen;
    private JButton werkzeug_bauelement_loeschen;
    private JButton button_rueckgaengig;
    private JButton button_wiederholen;
    private JButton button_zoom_rein;
    private JButton button_zoom_raus;
    private JButton button_zoom_100;

    /**
     * Initialisiert eine neue Werkzeugleiste.
     *
     * @param hauptFenster Zugehoeriges Hauptfenster.
     */
    public Panel_Werkzeuge(HauptFenster hauptFenster) {
        this.hauptFenster = hauptFenster;
        //Setze Format der Werkzeugleiste:
        setPreferredSize(new Dimension(0, 35));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        //Initialisiere alle Werkzeugbuttons:
        werkzeug_bauelement_auswaehlen = new JButton();
        werkzeug_bauelement_auswaehlen.setPreferredSize(new Dimension(26, 27));
        werkzeug_bauelement_auswaehlen.setIcon(Grafiken.getGrafik_auswahl());
        werkzeug_bauelement_auswaehlen.setToolTipText("Auswaehlen");
        werkzeug_bauelement_auswaehlen.addActionListener(this);
        this.add(werkzeug_bauelement_auswaehlen);

        werkzeug_bauelement_loeschen = new JButton();
        werkzeug_bauelement_loeschen.setPreferredSize(new Dimension(26, 27));
        werkzeug_bauelement_loeschen.setIcon(Grafiken.getGrafik_loeschen());
        werkzeug_bauelement_loeschen.setToolTipText("Loeschen");
        werkzeug_bauelement_loeschen.addActionListener(this);
        this.add(werkzeug_bauelement_loeschen);

        button_rueckgaengig = new JButton();
        button_rueckgaengig.setPreferredSize(new Dimension(26, 27));
        button_rueckgaengig.setIcon(Grafiken.getGrafik_zurueck());
        button_rueckgaengig.setToolTipText("Zurueck");
        button_rueckgaengig.addActionListener(this);
        this.add(button_rueckgaengig);

        button_wiederholen = new JButton();
        button_wiederholen.setPreferredSize(new Dimension(26, 27));
        button_wiederholen.setIcon(Grafiken.getGrafik_vorwaerts());
        button_wiederholen.setToolTipText("Vorwaerts");
        button_wiederholen.addActionListener(this);
        this.add(button_wiederholen);

        button_zoom_rein = new JButton();
        button_zoom_rein.setPreferredSize(new Dimension(26, 27));
        button_zoom_rein.setIcon(Grafiken.getGrafik_zoomRein());
        button_zoom_rein.setToolTipText("Reinzoomen");
        button_zoom_rein.addActionListener(this);
        this.add(button_zoom_rein);

        button_zoom_raus = new JButton();
        button_zoom_raus.setPreferredSize(new Dimension(26, 27));
        button_zoom_raus.setIcon(Grafiken.getGrafik_zoomRaus());
        button_zoom_raus.setToolTipText("Rauszoomen");
        button_zoom_raus.addActionListener(this);
        this.add(button_zoom_raus);

        button_zoom_100 = new JButton();
        button_zoom_100.setPreferredSize(new Dimension(26, 27));
        button_zoom_100.setIcon(Grafiken.getGrafik_zoom100());
        button_zoom_100.setToolTipText("Originalgroesse");
        button_zoom_100.addActionListener(this);
        this.add(button_zoom_100);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptischeBank optischeBank = hauptFenster.getAktuelleOptischeBank();
        if (e.getSource().equals(werkzeug_bauelement_loeschen)) {
            optischeBank.werkzeugWechseln(new Werkzeug_ElementLoeschen(hauptFenster.getAktuelleOptischeBank()));
        }
        if (e.getSource().equals(werkzeug_bauelement_auswaehlen)) {
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(hauptFenster.getAktuelleOptischeBank()));
        }
        if (e.getSource().equals(button_rueckgaengig)) {
            optischeBank.aktionRueckgaengig();
        }
        if (e.getSource().equals(button_wiederholen)) {
            optischeBank.aktionWiederholen();
        }
        if (e.getSource().equals(button_zoom_raus)) {
            optischeBank.zoomStufeRaus();
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(button_zoom_rein)) {
            optischeBank.zoomStufeRein();
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(button_zoom_100)) {
            optischeBank.setZoom(1);
            optischeBank.aktualisieren();
        }
    }

}
