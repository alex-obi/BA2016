package de.hhu.alobe.ba2016.editor.eigenschaften;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Eigenschaftenregler, der automatisch einen JSlider als Regler initialisiert. Die Klasse verwaltet Änderungen an dem Regler als ChangeListener.
 */
public class Eigenschaftenregler_Slider extends Eigenschaftenregler implements ChangeListener {

    //Schnittstelle zur Interaktion mit der Optischen Bank als ReglerEvent
    private ReglerEvent event;

    //Minimum der Regelgröße bei Stellung auf 0.0 Prozent
    private double minimum;

    //Maximum der Regelgröße bei Stellung auf 1.0 (100%) Prozent
    private double maximum;

    /**
     * Initialisiere neuen Eigenschaftenregler mit einem JSlider als Regler
     *
     * @param name           Name der Regelgröße
     * @param einheit        Einheit der Regelgröße
     * @param anzahlWerte    Anzahl der diskreten Werte, die durch diesen Regler eingestellt werden können (ideal zwischen 100 und 500)
     * @param momentanerWert Momentaner Wert der Regelgröße zur Initialisierung des Reglers
     * @param minimum        Minimum der Regelgröße bei Stellung auf 0 Prozent
     * @param maximum        Maximum der Regelgröße bei Stellung auf 1.0 (100) Prozent
     * @param event          Schnittstelle als ReglerEvent
     */
    public Eigenschaftenregler_Slider(String name, String einheit, int anzahlWerte, double momentanerWert, double minimum, double maximum, ReglerEvent event) {
        super(name, einheit, "", new JSlider(0, anzahlWerte, 0));
        this.minimum = minimum;
        this.maximum = maximum;
        this.event = event;

        setWert(momentanerWert);

        ((JSlider) regler).setPaintTicks(true);
        ((JSlider) regler).setMajorTickSpacing(anzahlWerte / 10);
        ((JSlider) regler).setMinorTickSpacing(anzahlWerte / 100);
        ((JSlider) regler).addChangeListener(this);

    }

    /**
     * Setzt die Position des Reglers so, dass die eingestellte Regelgröße dem übergebenem Wert entspricht
     *
     * @param wert Neue Regelgröße
     */
    public void setWert(double wert) {
        JSlider slider = getSlider();
        slider.setValue((int) (slider.getMaximum() * event.berechneReglerProzent(wert, minimum, maximum)));
        aktualisiereGroesse(event.berechnePhysikalischenWert(wert));
    }

    /**
     * @return Stellung des Reglers in Prozent
     */
    public double getProzent() {
        return getSlider().getValue() / (double) getSlider().getMaximum();
    }

    /**
     * @return Regler als JSlider
     */
    public JSlider getSlider() {
        return (JSlider) regler;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        event.reglerWurdeVerschoben(event.berechneReglerWert(getProzent(), minimum, maximum));
        aktualisiereGroesse(event.berechnePhysikalischenWert(event.berechneReglerWert(getProzent(), minimum, maximum)));
    }
}
