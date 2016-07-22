package de.hhu.alobe.ba2016.physik.flaechen;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.mathe.GeomertrischeFigur;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

public abstract class Flaeche extends GeomertrischeFigur {

    protected int modus;

    public static final int MODUS_ABSORB = 1; //Absorbiert den Strahl
    public static final int MODUS_REFLEKT = 2; //Reflektiert den Strahl nur
    public static final int MODUS_BRECHUNG = 3; //Bricht den Strahl nur

    public StrahlenKollision gibKollision(Strahlengang cStrGng) {
        if(cStrGng.getAktuellerStrahl() == null) return null;
        float entfernung = kollisionUeberpruefen(cStrGng);
        if (entfernung >= Konstanten.MIND_ENTFERNUNG_STRAHL) {
            return new StrahlenKollision(entfernung, cStrGng, this);
        } else {
            return null;
        }
    }

    public abstract float kollisionUeberpruefen(Strahlengang cStrGng);

    public abstract void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position);

}
