package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;

/**
 * Klasse entspricht einem Objekt, dass gezeichnet und innerhalb eines Rahmens angeklickt werden kann.
 */
public abstract class Auswahlobjekt implements Zeichenbar {

    /**
     * Mittelpunkt des Objekts. Bestandteile werden relativ zu diesem Punkt erstellt.
     */
    protected Vektor mittelPunkt;

    /**
     * Name des Mittelpunkts im XML-Dokument.
     */
    public static final String XML_MITTELPUNKT = "Mittelpunkt";

    //Gibt an, ob der Rahmen sichtbar ist, also gezeichnet werden soll.
    private boolean rahmenSichtbar;

    //Rahmen, der um das Objekt gezeichnet werden kann und innerhalb dessen das Objekt angeklickt werden kann.
    private Rahmen rahmen;

    /**
     * Initialisiert das Auswahlobjekt mit einem Mittelpunkt.
     *
     * @param nMittelPunkt Mittelpunkt des Auswahlobjekts.
     */
    public Auswahlobjekt(Vektor nMittelPunkt) {
        rahmen = new Rahmen();
        mittelPunkt = nMittelPunkt;
    }

    /**
     * Gibt an, ob der übergebene Punkt innerhalb des Rahmens liegt.
     *
     * @param pruefVektor Position, an dem geklickt wurde.
     * @return Wahrheitswert, ob Punkt innerhalb des Rahmens liegt.
     */
    public boolean istAngeklickt(Vektor pruefVektor) {
        return rahmen.istVektorInRahmen(pruefVektor);
    }

    /**
     * @return Rahmen dieses Objekts.
     */
    public Rahmen getRahmen() {
        return rahmen;
    }

    /**
     * @param rahmen Neuer Rahmen dieses Objekts.
     */
    public void setRahmen(Rahmen rahmen) {
        this.rahmen = rahmen;
    }

    /**
     * Abstrakte Funktion, die durch erbende Klassen implementiert wird und ausgeführt wird, wenn das Objekt angeklickt wurde.
     */
    public abstract void waehleAus();

    /**
     * Macht den Rahmen dieses Objekts sichtbar.
     */
    public void rahmenEinblenden() {
        rahmenSichtbar = true;
    }

    /**
     * Blendet den Rahmen dieses Objekts aus.
     */
    public void rahmenAusblenden() {
        rahmenSichtbar = false;
    }

    /**
     * @return Mittelpunkt dieses Objekts.
     */
    public Vektor getMittelPunkt() {
        return mittelPunkt;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        if (rahmenSichtbar) {
            rahmen.paintComponent(g);
        }
    }

}
