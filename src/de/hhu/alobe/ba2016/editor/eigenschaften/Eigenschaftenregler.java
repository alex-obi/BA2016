package de.hhu.alobe.ba2016.editor.eigenschaften;


import javax.swing.*;
import java.awt.*;

/**
 * Repraesentiert einen Eigenschaftenregler. Dem Eigenschaftenregler kann ein JComponent als Manipulationswerkzeug uebergeben werden.
 */
public class Eigenschaftenregler extends JPanel {

    //Name der Regelgroesse:
    private String name;

    //Einheit der veraendernden Regelgroesse:
    private String einheit;

    //Wert der veraendernden Regelgroesse:
    private String groesse;

    //Titel des Eigenschaftenreglers zusammengesetzt aus Name, Einheit und Groesse:
    private JLabel label_titel;

    //Regler als JComponent. Es ist also jede Art von Regler moeglich, der von JComponent erbt:
    JComponent regler;

    /**
     * Initialisiere einen Eigenschaftenregler, der eine Regelgroesse mit einer bestimmten Einheit verwaltet.
     *
     * @param name    Name der Regelgroesse.
     * @param einheit Einheit der Regelgroesse.
     * @param groesse Momentaner Wert der Regelgroesse.
     * @param regler  Regler zur Manipulation der Regelgroesse.
     */
    public Eigenschaftenregler(String name, String einheit, String groesse, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, einheit, groesse, regler);
    }

    /**
     * Initialisiere einen Eigenschaftenregler, der eine Regelgroesse verwaltet, die keinen bestimmten Wert mit Einheit beistzt. Bspw. Farben.
     *
     * @param name   Name der Regelgroesse.
     * @param regler Regler zur Manipulation der Regelgroesse.
     */
    public Eigenschaftenregler(String name, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, "", "", regler);
    }

    /**
     * Methode zum initialisieren der Datenfelder
     *
     * @param nName    Name der Regelgroesse.
     * @param nEinheit Einheit der Regelgroesse.
     * @param nGroesse Wert der Regelgroesse.
     * @param nRegler  Regler zur Manipulation der Regelgroesse.
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
     * Aktualisiert den Titel des Reglers mit einer neuen Regelgroesse.
     *
     * @param nGroesse Neue Regelgroesse.
     */
    public void aktualisiereGroesse(String nGroesse) {
        groesse = nGroesse;
        label_titel.setText(name + ": " + nGroesse + einheit);
    }

}
