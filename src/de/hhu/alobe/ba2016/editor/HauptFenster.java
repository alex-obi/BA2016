package de.hhu.alobe.ba2016.editor;


import com.sun.corba.se.pept.transport.ReaderThread;
import de.hhu.alobe.ba2016.Konstanten;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Hauptfenster und Verwaltung des gesamten Programms
 */
public class HauptFenster extends JFrame {

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

        optBank = new OptischeBank((String)null);
        wechseleOptischeBank(optBank);

        fenster_bauelemente = new Fenster_Bauelemente(this);

        menueleiste = new Menueleiste(this);
        this.setJMenuBar(menueleiste);

        werkzeuge = new Panel_Werkzeuge(this);
        fensterInhalt.add(werkzeuge, BorderLayout.NORTH);

        this.setContentPane(fensterInhalt);
        fenster_bauelemente.setVisible(true);
        this.setVisible(true);
    }

    public void neueOptischeBank(String name) {
        wechseleOptischeBank(new OptischeBank(name));
    }

    public OptischeBank ladeNeueOptischeBank(File datei) {
        Document xmlDatei;
        try {
            xmlDatei = new SAXBuilder().build(datei);
            return new OptischeBank(xmlDatei.getRootElement(), datei.getName().substring(0, datei.getName().length() - 4));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Datei: " + datei.getPath(), "Fehler", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void speichereAktuelleOptischeBank(File datei) {
        Document xmlDatei = new Document(gibAktuelleOptischeBank().getXmlElement());

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            FileWriter fileWriter = new FileWriter(datei);
            xmlOutput.output(xmlDatei, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, datei.getPath() + " konnte nicht gespeichert werden!");
            System.out.println(e.getMessage());
        }
    }

    public void wechseleOptischeBank(OptischeBank optischeBank) {
        fensterInhalt.remove(optBank);
        this.optBank = optischeBank;
        fensterInhalt.add(optBank, BorderLayout.CENTER);
        String name = optischeBank.getName();
        if(name == null) name = "Neue Optische Bank";
        this.setTitle("Optischer Baukasten - " + name);
        optBank.aktualisieren();
        this.revalidate();
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

}
