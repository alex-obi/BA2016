package de.hhu.alobe.ba2016.physik.elemente.Auge;


import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Schirm;

public class Netzhaut extends Schirm{

    public Netzhaut(Auge auge, Vektor position, float hoehe) {
        super(auge.getOptischeBank(), position, 0, hoehe);
    }

}
