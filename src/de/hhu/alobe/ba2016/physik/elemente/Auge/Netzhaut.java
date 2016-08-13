package de.hhu.alobe.ba2016.physik.elemente.Auge;


import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;

public class Netzhaut extends Schirm{

    public Netzhaut(Auge auge, Vektor position, int hoehe) {
        super(auge.getOptischeBank(), position, 0, hoehe);
    }

}
