package de.hhu.alobe.ba2016.editor.werkzeuge;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Implementierende Klassen stellen Methoden zur Verfuegung, um die optische Bank zu bearbeiten.
 * Maus und Tastatur koennen hierzu mit einem Werkzeug interagieren.
 * Die Klasse verwaltet Mausaktionen als MouseAdapter.
 */
public abstract class Werkzeug extends MouseAdapter {

    //Referenz auf die zugehoerigen Optische Bank
    OptischeBank optischeBank;

    /**
     * Aufruf durch erbende Klassen garantiert die Initialisierung der Referenz auf die zugehoerige Optische Bank.
     *
     * @param optischeBank Referenz auf Optische Bank
     */
    public Werkzeug(OptischeBank optischeBank) {
        this.optischeBank = optischeBank;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Vektor positionAufZeichenbrett = new Vektor((double) e.getX() / optischeBank.getZoom(), (double) e.getY() / optischeBank.getZoom());
        mouseClicked(e, positionAufZeichenbrett);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Vektor positionAufZeichenbrett = new Vektor((double) e.getX() / optischeBank.getZoom(), (double) e.getY() / optischeBank.getZoom());
        mousePressed(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vektor positionAufZeichenbrett = new Vektor((double) e.getX() / optischeBank.getZoom(), (double) e.getY() / optischeBank.getZoom());
        mouseReleased(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vektor positionAufZeichenbrett = new Vektor((double) e.getX() / optischeBank.getZoom(), (double) e.getY() / optischeBank.getZoom());
        mouseDragged(e, positionAufZeichenbrett);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Vektor positionAufZeichenbrett = new Vektor((double) e.getX() / optischeBank.getZoom(), (double) e.getY() / optischeBank.getZoom());
        mouseMoved(e, positionAufZeichenbrett);
    }

    /**
     * Aufruf bei Mausklick.
     *
     * @param e             Maus Interaktion als MouseEvent.
     * @param realePosition Reale Position im Koordinatensystem der optischen Bank.
     */
    public abstract void mouseClicked(MouseEvent e, Vektor realePosition);

    /**
     * Aufruf bei Druck auf Maustaste
     *
     * @param e             Maus Interaktion als MouseEvent.
     * @param realePosition Reale Position im Koordinatensystem der optischen Bank.
     */
    public abstract void mousePressed(MouseEvent e, Vektor realePosition);

    /**
     * Aufruf bei Loslassen der Maustaste
     *
     * @param e             Maus Interaktion als MouseEvent.
     * @param realePosition Reale Position im Koordinatensystem der optischen Bank.
     */
    public abstract void mouseReleased(MouseEvent e, Vektor realePosition);

    /**
     * Aufruf bei Bewegung des Mauszeigers mit gedrueckter Maustaste.
     *
     * @param e             Maus Interaktion als MouseEvent.
     * @param realePosition Reale Position im Koordinatensystem der optischen Bank.
     */
    public abstract void mouseDragged(MouseEvent e, Vektor realePosition);

    /**
     * Aufruf bei Bewegung des Mauszeigers
     *
     * @param e             Maus Interaktion als MouseEvent.
     * @param realePosition Reale Position im Koordinatensystem der optischen Bank.
     */
    public abstract void mouseMoved(MouseEvent e, Vektor realePosition);

    /**
     * Implementiert Funktionen, die aufgerufen werden sollen, wenn ein Element mit diesem Werkzeug ausgewaehlt wird.
     */
    public abstract void auswahlAufheben();

    /**
     * Implementiert Funktionen, die aufgerufen werden sollen, wenn das ausgewaehlte Element nicht mehr mit diesem Werkzeug ausgewaehlt ist.
     */
    public abstract void auswaehlen();

}

