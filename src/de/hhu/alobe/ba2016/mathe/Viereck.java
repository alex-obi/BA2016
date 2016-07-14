package de.hhu.alobe.ba2016.mathe;

import java.awt.*;
import java.util.ArrayList;

public class Viereck extends GeomertrischeFigur implements KannStrahlenSchnitt {

    ArrayList<Gerade> geraden;

    public Viereck(Vektor pos1, Vektor pos2, Vektor pos3, Vektor pos4) {
        geraden = new ArrayList<Gerade>();
        geraden.add(new Gerade(pos1, pos2));
        geraden.add(new Gerade(pos2, pos3));
        geraden.add(new Gerade(pos3, pos4));
        geraden.add(new Gerade(pos4, pos1));
    }

    @Override
    public float gibSchnittEntfernung(Strahl strahl) {
        float minimumEntfernung = geraden.get(0).gibSchnittEntfernung(strahl);
        for(int i = 1; i < geraden.size(); i++) {
            float neueEntfernung = geraden.get(i).gibSchnittEntfernung(strahl);
            if (neueEntfernung >= 0) {
                if(minimumEntfernung >= 0) {
                    minimumEntfernung = Math.min(neueEntfernung, minimumEntfernung);
                } else {
                    minimumEntfernung = neueEntfernung;
                }
            }
        }
        return minimumEntfernung;
    }

    @Override
    public Vektor gibSchnittpunkt(Strahl strahl) {
        float entfernung = gibSchnittEntfernung(strahl);
        if (entfernung < 0) return null;
        Vektor schnittpunkt = Vektor.addiere(strahl.getBasisVektor(), Vektor.multipliziere(strahl.getRichtungsVektor(), entfernung));
        return schnittpunkt;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        for(Gerade cGerade : geraden) {
            cGerade.verschiebeUm(verschiebung);
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        for(Gerade cGerade : geraden) {
            cGerade.paintComponent(g);
        }
    }
}
