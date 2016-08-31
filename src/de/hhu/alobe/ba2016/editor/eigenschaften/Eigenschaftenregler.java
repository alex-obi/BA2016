package de.hhu.alobe.ba2016.editor.eigenschaften;


import javax.swing.*;
import java.awt.*;

public class Eigenschaftenregler extends JPanel {

    private String name;
    private String einheit;
    private String groesse;


    private JLabel label_titel;
    JComponent regler;

    public Eigenschaftenregler(String name, String einheit, String groesse, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, einheit, groesse, regler);
    }

    public Eigenschaftenregler(String name, JComponent regler) {
        super(new BorderLayout());
        initialisiere(name, "", "", regler);
    }

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

    public void aktualisiereGroesse(String nGroesse) {
        groesse = nGroesse;
        label_titel.setText(name + ": " + nGroesse + einheit);
    }

}
