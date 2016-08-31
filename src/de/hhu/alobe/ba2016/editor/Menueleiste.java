package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_ElementLoeschen;
import de.hhu.alobe.ba2016.jdom.Dateifunktionen;
import de.hhu.alobe.ba2016.jdom.MeinDateiendungsFilter;
import de.hhu.alobe.ba2016.physik.flaechen.Hauptebene;
import org.jcp.xml.dsig.internal.dom.Utils;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
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

    JMenu bearbeiten;
    JMenuItem rueckgaengig;
    JMenuItem wiederholen;
    JMenuItem elAuswahl;
    JMenuItem elLoeschen;

    JMenu ansicht;
    JMenuItem vergroessern;
    JMenuItem verkleinern;
    JMenuItem originalgroesse;
    JCheckBoxMenuItem virtuelleStrahlen;
    JRadioButtonMenuItem snelliusAktivieren;
    JRadioButtonMenuItem hauptebenenAktivieren;

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
        speichernUnter.addActionListener(this);
        datei.add(speichernUnter);

        bearbeiten = new JMenu("Bearbeiten");
        this.add(bearbeiten);
        rueckgaengig = new JMenuItem("Rueckgaengig");
        rueckgaengig.addActionListener(this);
        bearbeiten.add(rueckgaengig);
        wiederholen = new JMenuItem("Wiederholen");
        wiederholen.addActionListener(this);
        bearbeiten.add(wiederholen);
        bearbeiten.addSeparator();
        elAuswahl = new JMenuItem("Element auswaehlen");
        elAuswahl.addActionListener(this);
        bearbeiten.add(elAuswahl);
        elLoeschen = new JMenuItem("Element loeschen");
        elLoeschen.addActionListener(this);
        bearbeiten.add(elLoeschen);

        ansicht = new JMenu("Ansicht");
        ansicht.addMenuListener(this);
        this.add(ansicht);
        vergroessern = new JMenuItem("Vergroessern");
        vergroessern.addActionListener(this);
        ansicht.add(vergroessern);
        verkleinern = new JMenuItem("Verkleinern");
        verkleinern.addActionListener(this);
        ansicht.add(verkleinern);
        originalgroesse = new JMenuItem("Originale Groesse");
        originalgroesse.addActionListener(this);
        ansicht.add(originalgroesse);
        ansicht.addSeparator();
        virtuelleStrahlen = new JCheckBoxMenuItem("Virtuelle Bilder", false);
        virtuelleStrahlen.addActionListener(this);
        ansicht.add(virtuelleStrahlen);
        ansicht.addSeparator();
        ButtonGroup gruppe = new ButtonGroup();
        snelliusAktivieren = new JRadioButtonMenuItem("Reale Brechung");
        snelliusAktivieren.addActionListener(this);
        hauptebenenAktivieren = new JRadioButtonMenuItem("Hauptebenen");
        hauptebenenAktivieren.setSelected(true);
        hauptebenenAktivieren.addActionListener(this);
        gruppe.add(snelliusAktivieren);
        gruppe.add(hauptebenenAktivieren);
        ansicht.add(snelliusAktivieren);
        ansicht.add(hauptebenenAktivieren);

        /*hilfe = new JMenu("Hilfe");
        this.add(hilfe);*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();
        if (e.getSource().equals(neu)) {
            optikSimulator.neueOptischeBank();
        }
        if (e.getSource().equals(laden)) {
            JFileChooser fileChooser = new JFileChooser(Konstanten.SAVE_ORDNER);
            fileChooser.setFileFilter(new MeinDateiendungsFilter(Konstanten.SAVE_ENDUNG));
            int retWert = fileChooser.showOpenDialog(optikSimulator);
            if (retWert == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                OptischeBank nOptischeBank = optikSimulator.ladeNeueOptischeBank(file);
                if (nOptischeBank != null) {
                    optikSimulator.wechseleOptischeBank(nOptischeBank);
                }
            }
        }
        for (Component lokaleDatei : lokal.getMenuComponents()) {
            if (e.getSource().equals(lokaleDatei)) {
                OptischeBank nOptischeBank = optikSimulator.ladeNeueOptischeBank(new File(Konstanten.SAVE_ORDNER + ((JMenuItem) lokaleDatei).getText() + "." + Konstanten.SAVE_ENDUNG));
                if (nOptischeBank != null) {
                    optikSimulator.wechseleOptischeBank(nOptischeBank);
                }
            }
        }
        if (e.getSource().equals(speichern)) {
            File pfad = optischeBank.getDateiPfad();
            if (pfad == null) {
                String name = JOptionPane.showInputDialog(optikSimulator, "Name fuer die neue Optische Bank:", "Speichern", JOptionPane.PLAIN_MESSAGE);
                while (name != null && !Dateifunktionen.erstelleDatei(optikSimulator, new File(Konstanten.SAVE_ORDNER + name + "." + Konstanten.SAVE_ENDUNG))) {
                    name = JOptionPane.showInputDialog(optikSimulator, "Name nicht gueltig. Keine Sonderzeichen (<>: \"\\ / | *?) verwenden.\n Bitte neuen Namen eingeben:", "Speichern", JOptionPane.PLAIN_MESSAGE);
                }
                if (name == null) {
                    return;
                } else {
                    pfad = new File(Konstanten.SAVE_ORDNER + name + "." + Konstanten.SAVE_ENDUNG);
                }
            }
            optikSimulator.speichereAktuelleOptischeBank(pfad);
        }
        if (e.getSource().equals(speichernUnter)) {
            File aktDatei = optischeBank.getDateiPfad();
            if (aktDatei == null) aktDatei = new File(Konstanten.SAVE_ORDNER);
            JFileChooser fileChooser = new JFileChooser(aktDatei);
            fileChooser.setSelectedFile(aktDatei);
            fileChooser.setFileFilter(new MeinDateiendungsFilter(Konstanten.SAVE_ENDUNG));
            int retWert = fileChooser.showSaveDialog(optikSimulator);
            if (retWert == JFileChooser.APPROVE_OPTION) {
                File dateiPfad = fileChooser.getSelectedFile();
                if (!Dateifunktionen.getDateiEndung(dateiPfad).equals(Konstanten.SAVE_ENDUNG)) {
                    dateiPfad = new File(fileChooser.getSelectedFile().getPath() + "." + Konstanten.SAVE_ENDUNG);
                }
                if (Dateifunktionen.erstelleDatei(optikSimulator, dateiPfad)) {
                    optikSimulator.speichereAktuelleOptischeBank(dateiPfad);
                }
            }
        }
        if (e.getSource().equals(rueckgaengig)) {
            optischeBank.aktionRueckgaengig();
        }
        if (e.getSource().equals(wiederholen)) {
            optischeBank.aktionWiederholen();
        }
        if (e.getSource().equals(elAuswahl)) {
            optischeBank.werkzeugWechseln(new Werkzeug_Auswahl(optischeBank));
        }
        if (e.getSource().equals(elLoeschen)) {
            optischeBank.werkzeugWechseln(new Werkzeug_ElementLoeschen(optischeBank));
        }
        if (e.getSource().equals(vergroessern)) {
            optischeBank.zoomStufeRein();
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(verkleinern)) {
            optischeBank.zoomStufeRaus();
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(originalgroesse)) {
            optischeBank.setZoom(1);
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(virtuelleStrahlen)) {
            optischeBank.setVirtuelleStrahlenAktiv(virtuelleStrahlen.isSelected());
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(snelliusAktivieren)) {
            optischeBank.setModus(OptischeBank.MODUS_SNELLIUS);
            optischeBank.aktualisieren();
        }
        if (e.getSource().equals(hauptebenenAktivieren)) {
            optischeBank.setModus(OptischeBank.MODUS_HAUPTEBENE);
            optischeBank.aktualisieren();
        }
    }

    public void aktualisiereLokaleDateien() {
        lokal.removeAll();
        if (new File(Konstanten.SAVE_ORDNER).exists()) {
            for (File datei : Dateifunktionen.getLokaleDateienMitEndung(new File(Konstanten.SAVE_ORDNER), Konstanten.SAVE_ENDUNG)) {
                JMenuItem nItem = new JMenuItem(datei.getName().substring(0, datei.getName().length() - 4));
                nItem.addActionListener(this);
                lokal.add(nItem);
            }
        }
    }

    @Override
    public void menuSelected(MenuEvent e) {
        OptischeBank optischeBank = optikSimulator.gibAktuelleOptischeBank();
        if (e.getSource().equals(datei)) {
            aktualisiereLokaleDateien();
        }
        if(e.getSource().equals(ansicht)) {
            virtuelleStrahlen.setSelected(optischeBank.isVirtuelleStrahlenAktiv());
            if(optischeBank.getModus() == OptischeBank.MODUS_HAUPTEBENE) {
                hauptebenenAktivieren.setSelected(true);
                snelliusAktivieren.setSelected(false);
            } else if (optischeBank.getModus() == OptischeBank.MODUS_SNELLIUS) {
                hauptebenenAktivieren.setSelected(false);
                snelliusAktivieren.setSelected(true);
            }
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
