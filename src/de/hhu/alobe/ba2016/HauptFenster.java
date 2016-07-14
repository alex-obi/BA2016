package de.hhu.alobe.ba2016;


import de.hhu.alobe.ba2016.editor.Fenster_Werkzeuge;
import de.hhu.alobe.ba2016.editor.Menueleiste;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.VektorInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Hauptfenster und Verwaltung des gesamten Programms
 */
public class HauptFenster extends JFrame implements ActionListener {

    //Statische Referenz zum globalen Aufruf von allen Klassen
    private static HauptFenster optikSimulator;

    private OptischeBank optBank;
    private Menueleiste menueleiste;
    private Fenster_Werkzeuge fenster_werkzeuge;

    public HauptFenster() {
        super("Optischer Baukasten");
        optikSimulator = this;

        this.setSize(Konstanten.FENSTER_X, Konstanten.FENSTER_Y);
        this.setMinimumSize(new Dimension(400, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        menueleiste = new Menueleiste(this);
        this.setJMenuBar(menueleiste);

        fenster_werkzeuge = new Fenster_Werkzeuge(this);

        optBank = new OptischeBank();
        optBank.setOpaque(true);
        this.setContentPane(optBank);

        fenster_werkzeuge.setVisible(true);
        this.setVisible(true);
    }

    public OptischeBank gibAktuelleOptischeBank() {
        return optBank;
    }

    private VektorInt gibMausPosition(MouseEvent e) {
        return new VektorInt(e.getX() - 4, e.getY() - 26);
    }

    public static HauptFenster get() {
        return optikSimulator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menueleiste.checkAction(e);
    }
}
