package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.HauptFenster;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Menueleiste extends JMenuBar {

    HauptFenster optikSimulator;

    JMenu datei;
    JMenu bearbeiten;
    JMenu ansicht;
    JMenu hilfe;

    public Menueleiste(HauptFenster optikSimulator) {
        datei = new JMenu("Datei");
        this.add(datei);
        bearbeiten = new JMenu("Bearbeiten");
        this.add(bearbeiten);
        ansicht = new JMenu("Ansicht");
        this.add(ansicht);
        hilfe = new JMenu("Hilfe");
        this.add(hilfe);
    }

    public void checkAction(ActionEvent e) {

    }


}
