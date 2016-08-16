package de.hhu.alobe.ba2016;


import de.hhu.alobe.ba2016.editor.Fenster_Bauelemente;
import de.hhu.alobe.ba2016.editor.Menueleiste;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.Panel_Werkzeuge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Hauptfenster und Verwaltung des gesamten Programms
 */
public class HauptFenster extends JFrame implements ActionListener {

    public static final int MIND_BREITE = 600;
    public static final int MIND_HOEHE = 400;

    //Statische Referenz zum globalen Aufruf von allen Klassen
    private static HauptFenster optikSimulator;

    private JPanel fensterInhalt;

    private Menueleiste menueleiste;
    private Panel_Werkzeuge werkzeuge;
    private OptischeBank optBank;

    private Fenster_Bauelemente fenster_bauelemente;



    public HauptFenster() {
        super("Optischer Baukasten");
        optikSimulator = this;

        this.setSize(Konstanten.FENSTER_X, Konstanten.FENSTER_Y);
        this.setMinimumSize(new Dimension(MIND_BREITE, MIND_HOEHE));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        fensterInhalt = new JPanel(new BorderLayout());
        fensterInhalt.setOpaque(true);

        optBank = new OptischeBank();
        fensterInhalt.add(optBank, BorderLayout.CENTER);

        fenster_bauelemente = new Fenster_Bauelemente(this);

        menueleiste = new Menueleiste(this);
        this.setJMenuBar(menueleiste);

        werkzeuge = new Panel_Werkzeuge(this);
        fensterInhalt.add(werkzeuge, BorderLayout.NORTH);

        this.setContentPane(fensterInhalt);

        fenster_bauelemente.setVisible(true);
        this.setVisible(true);
    }

    public OptischeBank gibAktuelleOptischeBank() {
        return optBank;
    }

    private Point gibMausPosition(MouseEvent e) {
        return new Point(e.getX() - 4, e.getY() - 26);
    }

    public static HauptFenster get() {
        return optikSimulator;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menueleiste.checkAction(e);
    }
}
