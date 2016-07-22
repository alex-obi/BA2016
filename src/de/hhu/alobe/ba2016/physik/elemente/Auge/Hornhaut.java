package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.flaechen.Hauptebene;

public class Hornhaut {

    private Auge auge;

    private Grenzflaeche grenzflaeche;

    private Hauptebene hauptebene;

    private Vektor position;

    private float radius;

    public Hornhaut(Auge auge, Vektor position, float radius) {
        this.auge = auge;
        this.radius = radius;
        this.position = position;
        this.hauptebene = new Hauptebene(Hauptebene.MODUS_BRECHUNG, position, 124, radius);
        this.grenzflaeche = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_BRECHUNG, 1, Konstanten.BRECHZAHL_KAMMERWASSER, new VektorFloat(position.getXfloat() + radius, position.getYfloat()), radius, 2.36, 1.57);
    }
}
