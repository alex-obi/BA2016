package de.hhu.alobe.ba2016.physik.elemente.spiegel;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Auge.Hornhaut;

import javax.swing.*;
import java.util.ArrayList;

public class Spiegel extends Hohlspiegel{

    public Spiegel(OptischeBank optischeBank, Vektor mittelPunkt, float hoehe) {
        super(optischeBank, mittelPunkt, 0, hoehe);
    }

    @Override
    public void waehleAus() {
        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JSlider slide_hoehe = new JSlider (MIND_HOEHE, MAX_HOEHE, (int)hoehe);
        slide_hoehe.setPaintTicks(true);
        slide_hoehe.setMajorTickSpacing(20);
        slide_hoehe.addChangeListener(e -> {
            setHoehe( ((JSlider) e.getSource()).getValue());
            optischeBank.aktualisieren();
        });
        regler.add(new Eigenschaftenregler("Höhe", slide_hoehe));

        optischeBank.getEigenschaften().setOptionen("Spiegel", regler);
    }

}
