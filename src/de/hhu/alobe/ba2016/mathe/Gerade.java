package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Gerade, definiert durch Verbindung zweier Punkte
 * Die Gerade entspricht einem Strahl mit endlicher Länge
 */
public class Gerade extends Strahl implements KannStrahlenSchnitt {

    private double laenge;

    public Gerade(Strahl strahl, double laenge) {
        super(strahl.getBasisVektor(), strahl.getRichtungsVektor(), strahl.getQuellEntfernung(), strahl.isAusDemUnendlichen());
        this.laenge = laenge;
    }

    public Gerade(Vektor startVektor, Vektor zielVektor) {
        super(startVektor, Vektor.subtrahiere(zielVektor, startVektor));
        laenge = Vektor.subtrahiere(zielVektor, startVektor).gibLaenge();
    }

    /**
     * Gibt ob und nach welcher Entfernung ein Strahl auf die Gerade trifft
     * @return Entfernung von Basis des Strahls entlang der Richtung bis der Strahl auf die Gerade trifft
     * -1 als Rueckgabe bedeutet, dass kein Schnittpunkt gefunden wurde
     */
    @Override
    public double gibSchnittEntfernung(Strahl strahl) {
        double[] lamdas = gibSchnittentfernungen(strahl);
        if (lamdas == null) return -1; //Die Strahlen sind parallel

        if (lamdas[0] >= 0 && lamdas[0] <= laenge && lamdas[1] >= 0) {
            return lamdas[1];
        } else {
            return -1;
        }
    }

    /**
     * Gibt den Schnittpunkt einer Geraden mit einem Strahl zurueck, wenn dieser existiert
     * @param strahl Strahl
     * @return Schnittpunkt
     * null als Rueckgabe bedeutet, dass kein Schnittpunkt existiert
     */
    @Override
    public Vektor gibSchnittpunkt(Strahl strahl) {
        double entfernung = gibSchnittEntfernung(strahl);
        if (entfernung < 0) return null;
        Vektor schnittpunkt = Vektor.addiere(strahl.getBasisVektor(), Vektor.multipliziere(strahl.getRichtungsVektor(), entfernung));
        return schnittpunkt;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor bisVektor = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, laenge));
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        Line2D line = new Line2D.Double(basisVektor, bisVektor);
        g.draw(line);
        if(quellEntfernung < 0 && HauptFenster.get().gibAktuelleOptischeBank().isVirtuelleStrahlenAktiv()) {
            Vektor bisVektorVirtuell = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, quellEntfernung));
            g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 5.0f, new float[] {10.0f,4.0f}, 0.0f));
            Line2D lineVirtuell = new Line2D.Double(basisVektor, bisVektorVirtuell);
            g.draw(lineVirtuell);
        }
    }

    public double getLaenge() {
        return laenge;
    }

    public void setLaenge(double laenge) {
        this.laenge = laenge;
    }


}
