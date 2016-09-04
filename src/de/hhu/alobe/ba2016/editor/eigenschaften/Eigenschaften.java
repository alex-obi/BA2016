package de.hhu.alobe.ba2016.editor.eigenschaften;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;

import javax.swing.*;
import java.awt.*;

/**
 * Optionsleiste zum Manipulieren von Eigenschaften von Elementen der Optische Bank.
 * Der Optionsleiste können Eigenschaftenregler übergeben werden, die durch diese Leiste angezeigt und verwaltet werden.
 */
public class Eigenschaften extends JPanel {

    //Höhe der Optionsleiste:
    private static final int HOEHE = 100;

    //Höhe für Titel:
    private static final int TITEL_HOEHE = 25;

    //Titel der Optionsleiste
    private JLabel titel;

    //Leinwand zum Anzeigen der Eigenschaftenregler
    private JPanel optionen;

    /**
     * Initialisiert eine neue Optionsleiste zum Verwalten von Eigenschaften.
     */
    public Eigenschaften() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(0, HOEHE));

        titel = new JLabel("");
        titel.setFont(new Font("Arial", Font.PLAIN, 20));
        titel.setPreferredSize(new Dimension(400, TITEL_HOEHE));
        add(titel, BorderLayout.NORTH);

        optionen = new JPanel(new GridLayout(2, 4));
        add(optionen, BorderLayout.CENTER);
    }

    /**
     * Setzt die angezeigten Manipulationsoptionen auf eine neue Liste von Eigenschaftenreglern.
     *
     * @param nTitel      Titel der Optionsleiste. Bspw: Name des Bauelements.
     * @param komponenten Eigenschaftenregler als Array.
     */
    public void setOptionen(String nTitel, Eigenschaftenregler[] komponenten) {
        //Lösche alte Regler:
        optionenLoeschen();

        //Setze neue Regler:
        titel.setText(nTitel);
        for (int i = 0; i < komponenten.length; i++) {
            optionen.add(komponenten[i]);
        }
    }

    /**
     * Löscht die aktuellen Eigenschaftenregler und den Titel.
     */
    public void optionenLoeschen() {
        titel.setText("");
        optionen.removeAll();
    }

}
