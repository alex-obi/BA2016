package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.flaechen.Hauptebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;
import java.util.ArrayList;

public class Hornhaut implements KannKollision, Zeichenbar {

    private Auge auge;

    private Grenzflaeche grenzflaeche;

    private Hauptebene hauptebene;

    private Vektor position;

    private float radius;

    public Hornhaut(Auge auge, Vektor position, float radius) {
        this.auge = auge;
        this.radius = radius;
        this.position = position;
        this.hauptebene = new Hauptebene(Hauptebene.MODUS_BRECHUNG, position, 248, radius * 1.5f);
        this.grenzflaeche = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_BRECHUNG, 1, Konstanten.BRECHZAHL_KAMMERWASSER, new VektorFloat(position.getXfloat() + radius, position.getYfloat()), radius, 2.36, 1.57);
    }

    public void verschiebeUm(Vektor verschiebung) {
        position.addiere(verschiebung);
        grenzflaeche.verschiebeUm(verschiebung);
        hauptebene.verschiebeUm(verschiebung);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        switch (auge.getOptischeBank().getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                return grenzflaeche.gibKollision(cStrGng);
            case OptischeBank.MODUS_HAUPTEBENE:
                StrahlenKollision sK = grenzflaeche.gibKollision(cStrGng);
                if(sK != null) {
                    return new StrahlenKollision(sK.getDistanz(), cStrGng, hauptebene);
                }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (auge.getOptischeBank().getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(new Color(0, 7, 244));
                grenzflaeche.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                g.setColor(Color.GRAY);
                grenzflaeche.paintComponent(g);
                g.setColor(Color.BLACK);
                hauptebene.paintComponent(g);
                break;
        }
    }
}
