package de.hhu.alobe.ba2016.editor.eigenschaften;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Eigenschaftenregler_Slider extends Eigenschaftenregler implements ChangeListener {

    ReglerEvent event;

    double minimum;
    double maximum;

    public Eigenschaftenregler_Slider(String name, String einheit, int anzahlWerte, double momentanerWert, double minimum, double maximum, ReglerEvent event) {
        super(name, einheit, "", new JSlider(0, anzahlWerte, 0));
        this.minimum = minimum;
        this.maximum = maximum;
        this.event = event;

        setWert(momentanerWert);

        ((JSlider)regler).setPaintTicks(true);
        ((JSlider)regler).setMajorTickSpacing(anzahlWerte / 10);
        ((JSlider)regler).addChangeListener(this);

    }

    public void setWert(double wert) {
        JSlider slider = getSlider();
        slider.setValue((int) (slider.getMaximum() * event.berechneReglerProzent(wert, minimum, maximum)));
        aktualisiereGroesse(event.berechnePhysikalischenWert(wert));
    }

    public double getProzent() {
        return getSlider().getValue() / (double)getSlider().getMaximum();
    }

    public JSlider getSlider() {
        return (JSlider)regler;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        event.reglerWurdeVerschoben(event.berechneReglerWert(getProzent(), minimum, maximum));
        aktualisiereGroesse(event.berechnePhysikalischenWert(event.berechneReglerWert(getProzent(), minimum, maximum)));
    }
}
