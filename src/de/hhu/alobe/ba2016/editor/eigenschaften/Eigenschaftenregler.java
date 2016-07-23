package de.hhu.alobe.ba2016.editor.eigenschaften;


import javax.swing.*;
import java.awt.*;

public class Eigenschaftenregler extends JPanel {

    private JLabel label_titel;
    private JComponent regler;

    public Eigenschaftenregler(String titel, JComponent regler) {
        super(new BorderLayout());
        label_titel = new JLabel(titel);
        label_titel.setPreferredSize(new Dimension(200, 15));
        label_titel.setHorizontalAlignment(SwingConstants.CENTER);
        this.regler = regler;
        this.add(label_titel, BorderLayout.NORTH);
        this.add(regler, BorderLayout.CENTER);
    }
}
