package de.hhu.alobe.ba2016.editor.eigenschaften;

import de.hhu.alobe.ba2016.editor.OptischeBank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Eigenschaften extends JPanel {

    OptischeBank optischeBank;

    JLabel titel;
    JPanel optionen;

    public Eigenschaften(OptischeBank optischeBank) {
        super(new BorderLayout());
        this.optischeBank = optischeBank;
        setPreferredSize(new Dimension(0, 100));

        titel = new JLabel("");
        titel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        titel.setPreferredSize(new Dimension(400, 20) );
        add(titel, BorderLayout.NORTH);

        optionen = new JPanel(new GridLayout(2, 2));
        add(optionen, BorderLayout.CENTER);

    }

    public void setOptionen(String nTitel, ArrayList<Eigenschaftenregler> komponenten) {
        optionenLoeschen();

        titel.setText(nTitel);
        for(int i= 0; i < komponenten.size(); i++) {
            optionen.add(komponenten.get(i));
        }

    }

    public void optionenLoeschen() {
        titel.setText("");
        optionen.removeAll();
    }

}
