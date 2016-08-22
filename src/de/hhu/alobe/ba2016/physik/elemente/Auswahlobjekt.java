package de.hhu.alobe.ba2016.physik.elemente;


import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.*;

public abstract class Auswahlobjekt implements Zeichenbar {

    protected Vektor mittelPunkt;
    public static final String XML_MITTELPUNKT = "Mittelpunkt";

    protected boolean rahmenSichtbar;
    protected Rahmen rahmen;

    protected Cursor mausZeiger;

    public Auswahlobjekt(Vektor nMittelPunkt, Rahmen nRahmen, Cursor nMausZeiger) {
        rahmen = nRahmen;
        mittelPunkt = nMittelPunkt;
        mausZeiger = nMausZeiger;
    }

    public Auswahlobjekt(Vektor nMittelPunkt) {
        rahmen = new Rahmen();
        mittelPunkt = nMittelPunkt;
        mausZeiger = Cursor.getDefaultCursor();
    }

    public boolean istAngeklickt(Vektor pruefVektor) {
        return rahmen.istVektorInRahmen(pruefVektor);
    }

    public Cursor getMausZeiger() {
        return mausZeiger;
    }

    public void setMausZeiger(Cursor mausZeiger) {
        this.mausZeiger = mausZeiger;
    }

    public Rahmen getRahmen() {
        return rahmen;
    }

    public void setRahmen(Rahmen rahmen) {
        this.rahmen = rahmen;
    }

    public abstract void waehleAus();

    public void rahmenEinblenden() {
        rahmenSichtbar = true;
    }

    public void rahmenAusblenden() {
        rahmenSichtbar = false;
    }

    public Vektor getMittelPunkt() {
        return mittelPunkt;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        if(rahmenSichtbar) {
            rahmen.paintComponent(g);
        }
    }

}
