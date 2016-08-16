package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;


public class Augenlinse extends Linse {

    public Augenlinse(Auge auge, Vektor position, double brechzahl, double hoehe, double radius) {
        super(auge.getOptischeBank(), position, brechzahl, hoehe, 0, radius, radius);
    }

}
