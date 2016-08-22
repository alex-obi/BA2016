package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.physik.flaechen.Hauptebene;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Menueleiste extends JMenuBar implements ActionListener, MenuListener {

    HauptFenster optikSimulator;

    JMenu datei;
    JMenuItem neu;
    JMenuItem laden;
    JMenu lokal;

    JMenuItem speichern;
    JMenuItem speichernUnter;

    JMenuItem schliessen;

    JMenu bearbeiten;

    JMenu ansicht;

    JMenu hilfe;

    public Menueleiste(HauptFenster optikSimulator) {
        this.optikSimulator = optikSimulator;

        datei = new JMenu("Datei");
        datei.addMenuListener(this);
        this.add(datei);
        neu = new JMenuItem("Neu...");
        neu.addActionListener(this);
        datei.add(neu);
        laden = new JMenuItem("Laden...");
        laden.addActionListener(this);
        datei.add(laden);
        lokal = new JMenu("Lokal");
        datei.add(lokal);
        aktualisiereLokaleDateien();
        datei.addSeparator();
        speichern = new JMenuItem("Speichern");
        speichern.addActionListener(this);
        datei.add(speichern);
        speichernUnter = new JMenuItem("Speichern unter...");
        datei.add(speichernUnter);

        bearbeiten = new JMenu("Bearbeiten");
        this.add(bearbeiten);


        ansicht = new JMenu("Ansicht");
        this.add(ansicht);


        hilfe = new JMenu("Hilfe");
        this.add(hilfe);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(neu)) {
            String nName = JOptionPane.showInputDialog(this, "Name fuer die neue Optische Bank:");
            while (nName != null && !istDateiNameGueltig(nName)) {
                nName = JOptionPane.showInputDialog(this, "Name nicht gueltig. Keine Sonderzeichen verwenden. Bitte neuen Namen eingeben:");
            }
            if(nName != null) {
                optikSimulator.neueOptischeBank(nName);
            }
        }
        if(e.getSource().equals(laden)) {

        }
        for(Component lokaleDatei : lokal.getMenuComponents()) {
            if(e.getSource().equals(lokaleDatei)) {
                //optikSimulator.wechseleOptischeBank(optikSimulator.ladeNeueOptischeBank(new File(Konstanten.SAVE_ORDNER + ((JMenuItem)lokaleDatei).getText() + ".xml")));
                OptischeBank optischeBank = optikSimulator.ladeNeueOptischeBank(new File(Konstanten.SAVE_ORDNER + ((JMenuItem)lokaleDatei).getText() + ".xml"));
                optikSimulator.wechseleOptischeBank(optischeBank);
            }
        }
        if(e.getSource().equals(speichern)) {
            OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();
            File pfad = new File(Konstanten.SAVE_ORDNER + optischeBank.getName() + ".xml");
            if(pfad.exists()) {
                int ergebnis = JOptionPane.showConfirmDialog(this, "Datei existiert bereits. Überschreiben?", "Speichern", JOptionPane.YES_NO_OPTION);
                if(ergebnis == JOptionPane.YES_OPTION) {
                    optikSimulator.speichereAktuelleOptischeBank(pfad);
                }
            } else {
                optikSimulator.speichereAktuelleOptischeBank(pfad);
            }
        }
        if(e.getSource().equals(speichernUnter)) {

        }
    }

    public static boolean istDateiNameGueltig(String datei) {
        if(datei == null) return false;
        File testDatei = new File(datei);
        try {
            testDatei.getCanonicalFile().isFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void aktualisiereLokaleDateien() {
        lokal.removeAll();
        for(File datei : getLokaleXmlDateien()) {
            JMenuItem nItem = new JMenuItem(datei.getName().substring(0, datei.getName().length() - 4));
            nItem.addActionListener(this);
            lokal.add(nItem);
        }
    }

    public File[] getLokaleXmlDateien() {
        File saveOrdner = new File(Konstanten.SAVE_ORDNER);
        File[] saves = saveOrdner.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isFile() && name.toLowerCase().endsWith( ".xml" );
            }
        });
        return saves;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        if(e.getSource().equals(datei)) {
            aktualisiereLokaleDateien();
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
