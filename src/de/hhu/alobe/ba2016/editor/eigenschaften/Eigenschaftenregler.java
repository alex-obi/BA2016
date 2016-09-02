package de.hhu.alobe.ba2016.editor.eigenschaften;


import javax.swing.*;
import java.awt.*;

/**
 * Repräsentiert einen Eigenschaftenregler. Dem Eigenschaftenregler kann ein JComponent als Manipulationswerkzeug übergeben werden.
 */
public class Eigenschaftenregler extends JPanel {

    //Name der Regelgröße:
    private String name;

    //Einheit der verändernden Regelgröße:
    private String einheit;

    //Wert der verändernden Regelgröße:
    private String groesse;

    //Titel des Eigenschaftenreglers zusammengesetzt aus Name, Einheit und Größe:
    private JLabel label_titel;

    //Regler als JComponent. Es ist also jede Art von Regler möglich:
    JComponent regler;

    /**
     * Initialisiere einen Eigenschaftenregler, der eine Regelgröße mit einer bestimmten Einheit verwaltet.
     *
     * @param name    Name der Regelgröße.
     * @param einheit Einheit der Regelgröße.
     * @param groesse Wert der Regelgröße.
     * @param regler  Regler zur Manipulation der Regelgröße.
     */
    public Eigenschaftenregler(String name, String einheit, String groesse, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, einheit, groesse, regler);
    }

    /**
     * Initialisiere einen Eigenschaftenregler, der eine Regelgröße verwaltet, die keinen bestimmten Wert mit Einheit beistzt. Bspw. Farben.
     *
     * @param name   Name der Regelgröße
     * @param regler Regler zur Manipulation der Regelgröße
     */
    public Eigenschaftenregler(String name, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, "", "", regler);
    }

    /**
     * Methode zum initialisieren der Datenfelder
     *
     * @param nName    Name der Regelgröße.
     * @param nEinheit Einheit der Regelgröße.
     * @param nGroesse Wert der Regelgröße.
     * @param nRegler  Regler zur Manipulation der Regelgröße.
     */
    private void initialisiere(String nName, String nEinheit, String nGroesse, JComponent nRegler) {
        name = nName;
        einheit = nEinheit;
        groesse = nGroesse;
        if (!nGroesse.equals("")) {
            label_titel = new JLabel(name + ": " + groesse + einheit);
        } else {
            label_titel = new JLabel(name);
        }
        label_titel.setPreferredSize(new Dimension(200, 15));
        label_titel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_titel, BorderLayout.NORTH);
        regler = nRegler;

        this.add(regler, BorderLayout.CENTER);
    }

    /**
     * Aktualisiert den Titel des Reglers mit einer neuen Regelgröße.
     *
     * @param nGroesse Neue Regelgröße.
     */
    public void aktualisiereGroesse(String nGroesse) {
        groesse = nGroesse;
        label_titel.setText(name + ": " + nGroesse + einheit);
    }

}
