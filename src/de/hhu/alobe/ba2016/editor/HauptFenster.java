package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.jdom.Dateifunktionen;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Hauptfenster und Verwaltung des gesamten Programms.
 */
public class HauptFenster extends JFrame {
    
    //Mindestbreite des Fensters
    private static final int MIND_BREITE = 600;

    //Mindesthöhe des Fensters
    private static final int MIND_HOEHE = 400;

    //Statische Referenz zum Aufruf von jeder Programmstelle aus
    private static HauptFenster hauptFenster;

    //Inhalt des gesamten Fensters/ Content Pane.
    private JPanel fensterInhalt;

    //Menüleiste des Fensters
    private Menueleiste menueleiste;

    //Werkzeugleiste am oberen Fensterrand zum Schnellen Auswählen durch den Benutzer
    private Panel_Werkzeuge werkzeuge;

    //Optische Bank als Leinwand für alle Manipulations- und Zeichenmethoden
    private OptischeBank optBank;

    //Fenster zum Erstellen von Bauelementen
    private Fenster_Bauelemente fenster_bauelemente;

    /**
     * Konstruktor zum Erstellen eines neuen Fensters. Alle Objekte werden durch vorgegebene Parameter erzeugt.
     */
    public HauptFenster() {
        super("Optischer Baukasten");
        hauptFenster = this;

        this.setSize(Konstanten.FENSTER_X, Konstanten.FENSTER_Y);
        this.setMinimumSize(new Dimension(MIND_BREITE, MIND_HOEHE));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        fensterInhalt = new JPanel(new BorderLayout());
        fensterInhalt.setOpaque(true);

        optBank = new OptischeBank();
        wechseleOptischeBank(optBank);

        fenster_bauelemente = new Fenster_Bauelemente(this);
        fenster_bauelemente.setVisible(true);

        menueleiste = new Menueleiste(this);
        this.setJMenuBar(menueleiste);

        werkzeuge = new Panel_Werkzeuge(this);
        fensterInhalt.add(werkzeuge, BorderLayout.NORTH);

        this.setContentPane(fensterInhalt);
        this.setVisible(true);
    }

    /**
     * @return Referenz auf dieses (einzige) Hauptfenster
     */
    public static HauptFenster get() {
        return hauptFenster;
    }

    /**
     * Wechselt zu einer neuen, leeren Optischen Bank.
     * Die aktuelle Optische Bank geht unwiederruflich verloren!
     */
    public void neueOptischeBank() {
        wechseleOptischeBank(new OptischeBank());
    }

    /**
     * Lädt eine neue Optische Bank aus einer Datei.
     *
     * @param datei Referenz auf die Datei als File.
     * @return Neue Optische Bank. Liefert null bei Fehlschlag (Datei falsches Format, nicht leesbar, etc.) und gibt Fehlermeldung aus.
     */
    public OptischeBank ladeNeueOptischeBank(File datei) {
        Document xmlDatei;
        try {
            xmlDatei = new SAXBuilder().build(datei);
            return new OptischeBank(xmlDatei.getRootElement(), datei);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Datei: " + datei.getPath(), "Fehler", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Speichert die aktuelle Optische Bank als XML-Datei in die übergebene Datei.
     * Setzt den Dateipfad der aktuellen Optische Bank auf die neue Datei und aktualisiert den Titel des Fensters auf den neuen Namen der Datei.
     * Liefert Fehlermeldung, falls dies nicht möglich war.
     *
     * @param datei Datei als File
     */
    public void speichereAktuelleOptischeBank(File datei) {
        Document xmlDatei = new Document(getAktuelleOptischeBank().getXmlElement());

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            FileWriter fileWriter = new FileWriter(datei);
            xmlOutput.output(xmlDatei, fileWriter);
            fileWriter.close();
            getAktuelleOptischeBank().setDateiPfad(datei);
            this.setTitle("Optischer Baukasten - " + Dateifunktionen.getDateiNamen(datei));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, datei.getPath() + " konnte nicht gespeichert werden!");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Wechselt die aktuelle Optische Bank zu der übergebenen, neuen Optischen Bank.
     * Aktualisiert den Namen des Fensters.
     * Alte Optische Bank geht unwiederruflich verloren!
     *
     * @param optischeBank Neue Optische Bank, die angezeigt werden soll.
     */
    public void wechseleOptischeBank(OptischeBank optischeBank) {
        fensterInhalt.remove(optBank);
        this.optBank = optischeBank;
        fensterInhalt.add(optBank, BorderLayout.CENTER);
        String name = optischeBank.getName();
        if (name == null) name = "Neue Optische Bank";
        this.setTitle("Optischer Baukasten - " + name);
        optBank.aktualisieren();

        this.revalidate();
    }

    /**
     * @return Aktuell angezeigt Optische Bank
     */
    public OptischeBank getAktuelleOptischeBank() {
        return optBank;
    }

}
