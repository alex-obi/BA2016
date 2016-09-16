package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.editor.HauptFenster;
import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Gerade als Verbindung zweier Punkte.
 * Die Gerade entspricht einem Strahl mit endlicher Laenge.
 */
public class Gerade extends Strahl implements KannStrahlenSchnitt {

    //Laenge der Geraden
    private double laenge;

    /**
     * Initialisiere die Gerade ueber einen Strahl mit einer endlichen Laenge.
     *
     * @param strahl Strahl, aus dem Gerade gebaut wird.
     * @param laenge Laenge der neuen Gerade.
     */
    public Gerade(Strahl strahl, double laenge) {
        super(strahl.getBasisVektor(), strahl.getRichtungsVektor(), strahl.getQuellEntfernung(), strahl.isAusDemUnendlichen());
        this.laenge = laenge;
    }

    /**
     * Initialisiere die Gerade als Verbindung zweier Punkte.
     *
     * @param startVektor Verbindungspunkt 1.
     * @param zielVektor  Verbindungspunkt 2.
     */
    public Gerade(Vektor startVektor, Vektor zielVektor) {
        super(startVektor, Vektor.subtrahiere(zielVektor, startVektor));
        laenge = Vektor.subtrahiere(zielVektor, startVektor).gibLaenge();
    }

    /**
     * Gibt ob und nach welcher Entfernung ein Strahl auf die Gerade trifft.
     *
     * @return Entfernung von der Basis des Strahls bis zum dem Punkt, wo der Strahl auf die Gerade trifft.
     * -1 als Rueckgabe bedeutet, dass kein Schnittpunkt gefunden wurde.
     */
    @Override
    public double gibSchnittEntfernung(Strahl strahl) {
        double[] lamdas = gibSchnittentfernungen(strahl);
        if (lamdas == null) return -1; //Die Strahlen sind parallel

        //Gebe die Schnittentfernung nur zurueck, wenn der uebergebene Strahl diese Gerade innerhalb ihrer Grenzen schneidet. Ansonsten -1.
        if (lamdas[0] >= 0 && lamdas[0] <= laenge && lamdas[1] >= 0) {
            return lamdas[1];
        } else {
            return -1;
        }
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor bisVektor = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, laenge));
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        Line2D line = new Line2D.Double(basisVektor, bisVektor);
        g.draw(line);
        if (quellEntfernung < 0 && HauptFenster.get().getAktuelleOptischeBank().isVirtuelleStrahlenAktiv()) {
            //Zeichne virtuelle Strahlen
            Vektor bisVektorVirtuell = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, quellEntfernung));
            g.setStroke(new BasicStroke(Konstanten.LINIENDICKE, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 5.0f, new float[]{10.0f, 4.0f}, 0.0f));
            Line2D lineVirtuell = new Line2D.Double(basisVektor, bisVektorVirtuell);
            g.draw(lineVirtuell);
        }
    }

    /**
     * @return Laenge der Geraden.
     */
    public double getLaenge() {
        return laenge;
    }

    /**
     * @param laenge Neue Laenge der Geraden vom Basisvektor aus.
     */
    public void setLaenge(double laenge) {
        this.laenge = laenge;
    }


}
