package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Linse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Eigenschaften extends JPanel implements ActionListener {

    private OptischeBank optischeBank;

    private JButton button_rueckgaengig;
    private JButton button_wiederholen;

    private JButton button_snellius_hauptebene;
    private boolean snellius = false;

    private JButton button_zoom_rein;
    private JButton button_zoom_raus;
    private JButton button_zoom_100;

    public Eigenschaften(OptischeBank optischeBank) {
        this.optischeBank = optischeBank;
        setPreferredSize(new Dimension(200, 200));

        button_rueckgaengig =  new JButton("Zurück");
        button_rueckgaengig.addActionListener(this);
        this.add(button_rueckgaengig);
        button_wiederholen = new JButton("Vorwärts");
        button_wiederholen.addActionListener(this);
        this.add(button_wiederholen);

        button_snellius_hauptebene = new JButton("Snellius");
        button_snellius_hauptebene.addActionListener(this);
        this.add(button_snellius_hauptebene);

        button_zoom_rein = new JButton("Zoom +");
        button_zoom_rein.addActionListener(this);
        this.add(button_zoom_rein);
        button_zoom_raus = new JButton("Zoom -");
        button_zoom_raus.addActionListener(this);
        this.add(button_zoom_raus);
        button_zoom_100 = new JButton("Zoom 100%");
        button_zoom_100.addActionListener(this);
        this.add(button_zoom_100);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(button_rueckgaengig)) {
            optischeBank.aktionRueckgaengig();
        }
        if(e.getSource().equals(button_wiederholen)) {
            optischeBank.aktionWiederholen();
        }
        if(e.getSource().equals(button_snellius_hauptebene)) {
            if(snellius) {
                snellius = false;
                optischeBank.setModus(OptischeBank.MODUS_HAUPTEBENE);
                button_snellius_hauptebene.setText("Snellius aktivieren");
            } else {
                snellius = true;
                optischeBank.setModus(OptischeBank.MODUS_SNELLIUS);
                button_snellius_hauptebene.setText("Hauptebene aktivieren");
            }
            optischeBank.aktualisieren();
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
