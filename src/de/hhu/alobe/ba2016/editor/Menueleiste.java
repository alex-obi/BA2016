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
        datei.addSeparator();
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
            optikSimulator.neueOptischeBank(null);
        }
        if(e.getSource().equals(laden)) {

        }
        for(Component lokaleDatei : lokal.getMenuComponents()) {
            if(e.getSource().equals(lokaleDatei)) {
                OptischeBank optischeBank = optikSimulator.ladeNeueOptischeBank(new File(Konstanten.SAVE_ORDNER + ((JMenuItem)lokaleDatei).getText() + ".xml"));
                optikSimulator.wechseleOptischeBank(optischeBank);
            }
        }
        if(e.getSource().equals(speichern)) {
            OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();

            String nName = optischeBank.getName();
            File pfad;
            if(nName == null) {
                nName = JOptionPane.showInputDialog(this, "Name fuer die neue Optische Bank:", "Speichern");
                pfad = new File(Konstanten.SAVE_ORDNER + nName + ".xml");
                while (nName != null && !erstelleDatei(pfad)) {
                    nName = JOptionPane.showInputDialog(this, "Name nicht gueltig. Keine Sonderzeichen (<>: \"\\ / | *?) verwenden. Bitte neuen Namen eingeben:", "Speichern");
                }
                if(nName == null) {
                    return;
                } else {
                    optischeBank.setName(nName);
                    optikSimulator.setTitle("Optischer Baukasten - " + nName);
                }
            } else {
                pfad = new File(Konstanten.SAVE_ORDNER + nName + ".xml");
            }
            optikSimulator.speichereAktuelleOptischeBank(pfad);
        }
        if(e.getSource().equals(speichernUnter)) {

        }
    }

    //Liefert true wenn Datei erfolgreich neu erstellt wurde oder zum Überschreiben freigegeben
    public boolean erstelleDatei(File pfad) {
        try {
            if(pfad.createNewFile()) {
                return true;
            } else {
                if(pfad.exists()) {
                    int ergebnis = JOptionPane.showConfirmDialog(this, "Datei existiert bereits. Überschreiben?", "Speichern", JOptionPane.YES_NO_OPTION);
                    if (ergebnis == JOptionPane.YES_OPTION) return true;
                }
                return false;
            }
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
