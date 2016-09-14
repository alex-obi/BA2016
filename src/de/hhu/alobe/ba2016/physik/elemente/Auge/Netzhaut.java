package de.hhu.alobe.ba2016.physik.elemente.Auge;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;

/**
 * Netzhaut eines Auges als Schirm.
 */
public class Netzhaut extends Schirm {

    /**
     * Initialisiere Netzhaut mit Position und Hoehe.
     *
     * @param auge     Referenz auf zugehoeriges Auge.
     * @param position Position der Netzhaut.
     * @param hoehe    Hoehe der Netzhaut.
     */
    public Netzhaut(Auge auge, Vektor position, double hoehe) {
        super(auge.getOptischeBank(), position, hoehe);
    }

}
