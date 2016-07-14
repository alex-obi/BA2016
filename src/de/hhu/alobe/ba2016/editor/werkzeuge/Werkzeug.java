package de.hhu.alobe.ba2016.editor.werkzeuge;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion_BauelementVerschieben;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Implementierende Klassen stellen Methoden zur Verfügung, um die optische Bank zu bearbeiten
 * Maus und Tastatur können hierzu mit einem Werkzeug interagieren
 */
public abstract class Werkzeug extends MouseAdapter {

    OptischeBank optischeBank;

    public Werkzeug(OptischeBank optischeBank) {
        this.optischeBank = optischeBank;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Vektor positionAufZeichenbrett = new VektorFloat((double)e.getX() / optischeBank.getZoom(), (double)e.getY() / optischeBank.getZoom());
        mouseClicked(e, positionAufZeichenbrett);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Vektor positionAufZeichenbrett = new VektorFloat((double)e.getX() / optischeBank.getZoom(), (double)e.getY() / optischeBank.getZoom());
        mousePressed(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vektor positionAufZeichenbrett = new VektorFloat((double)e.getX() / optischeBank.getZoom(), (double)e.getY() / optischeBank.getZoom());
        mouseReleased(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vektor positionAufZeichenbrett = new VektorFloat((double)e.getX() / optischeBank.getZoom(), (double)e.getY() / optischeBank.getZoom());
        mouseDragged(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Vektor positionAufZeichenbrett = new VektorFloat((double)e.getX() / optischeBank.getZoom(), (double)e.getY() / optischeBank.getZoom());
        mouseMoved(e, positionAufZeichenbrett);
    }

    public abstract void mouseClicked(MouseEvent e, Vektor realePosition);
    public abstract void mousePressed(MouseEvent e, Vektor realePosition);
    public abstract void mouseReleased(MouseEvent e, Vektor realePosition);
    public abstract void mouseDragged(MouseEvent e, Vektor realePosition);
    public abstract void mouseMoved(MouseEvent e, Vektor realePosition);


    public abstract void auswahlAufheben();

    public abstract void auswaehlen();

}

