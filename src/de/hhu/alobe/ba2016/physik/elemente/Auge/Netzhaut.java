package de.hhu.alobe.ba2016.physik.elemente.Auge;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;

/**
 * Netzhaut eines Auges als Schirm.
 */
public class Netzhaut extends Schirm {

    /**
     * Initialisiere Netzhaut mit Position und Höhe.
     *
     * @param auge     Referenz auf zugehöriges Auge.
     * @param position Position der Netzhaut.
     * @param hoehe    Höhe der Netzhaut.
     */
    public Netzhaut(Auge auge, Vektor position, double hoehe) {
        super(auge.getOptischeBank(), position, hoehe);
    }

}
