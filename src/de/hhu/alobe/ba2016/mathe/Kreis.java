package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

/**
 * Ein Kreis definiert durch einen Mittelpunkt und Radius
 * Es ist möglich durch Übergabe von 2 Winkeln nur einen Kreisabschnitt zu definieren
 */
public class Kreis extends GeomertrischeFigur implements KannStrahlenSchnitt {

    private Vektor mittelpunkt;
    private double radius;

    //Gibt den Bereich eines Kreisbogens an. vonWinkel gibt den Startwinkel an. extWinkel die Laenge als Bogenmass in math. pos. Richtung
    private double vonWinkel;
    private double extWinkel;

    public Kreis (Vektor mittelpunktVektor, double radius) {
        this.mittelpunkt = mittelpunktVektor;
        this.radius = radius;
        vonWinkel = 0;
        extWinkel = 2 * Math.PI;
    }

    public Kreis (Vektor mittelpunktVektor, double radius, double vonWinkel, double extWinkel) {
        this.mittelpunkt = mittelpunktVektor;
        this.radius = radius;
        this.vonWinkel = vonWinkel;
        this.extWinkel = extWinkel;
    }

    /**
     * Gibt ob und nach welcher Entfernung ein Strahl das erste mal auf den Kreis trifft
     * @return Entfernung von Basis des Strahls entlang der Richtung bis der Strahl auf den Kreis trifft
     * -1 als Rueckgabe bedeutet, dass kein Schnittpunkt gefunden wurde
     */
    @Override
    public double gibSchnittEntfernung (Strahl strahl) {
        Vektor normalenVektor = strahl.getRichtungsVektor().gibNormalenVektor();
        Strahl tempStrahl = new Strahl(mittelpunkt, normalenVektor);

        double tempLamdas[] = Strahl.gibSchnittentfernungen(tempStrahl, strahl);
        if (tempLamdas == null) return -1; //Bei der Berechnung ist ein Fehler passiert

        if(Math.abs(tempLamdas[0]) > radius) return -1; //Strahl passiert den Kreis ohne Beruehrung

        double offsetLamda = Math.pow(radius * radius - tempLamdas[0] * tempLamdas[0], 0.5);

        Vektor schnittpunkt1 = Vektor.addiere(Vektor.multipliziere(strahl.getRichtungsVektor(), tempLamdas[1] - offsetLamda), strahl.getBasisVektor());
        schnittpunkt1.subtrahiere(mittelpunkt);
        Vektor schnittpunkt2 = Vektor.addiere(Vektor.multipliziere(strahl.getRichtungsVektor(), tempLamdas[1] + offsetLamda), strahl.getBasisVektor());
        schnittpunkt2.subtrahiere(mittelpunkt);

        if(vonWinkel + extWinkel <= 2 * Math.PI) {
            if (schnittpunkt1.gibRichtungsWinkel() >= vonWinkel && schnittpunkt1.gibRichtungsWinkel() <= (vonWinkel + extWinkel)) {
                return (tempLamdas[1] - offsetLamda);
            } else if (schnittpunkt2.gibRichtungsWinkel() >= vonWinkel && schnittpunkt2.gibRichtungsWinkel() <= (vonWinkel + extWinkel)) {
                return (tempLamdas[1] + offsetLamda);
            } else {
                return -1;
            }
        } else {
            if (schnittpunkt1.gibRichtungsWinkel() >= vonWinkel || schnittpunkt1.gibRichtungsWinkel() <= (vonWinkel + extWinkel) - (2 * Math.PI)) {
                return (tempLamdas[1] - offsetLamda);
            } else if (schnittpunkt2.gibRichtungsWinkel() >= vonWinkel || schnittpunkt2.gibRichtungsWinkel() <= (vonWinkel + extWinkel) - (2 * Math.PI)) {
                return (tempLamdas[1] + offsetLamda);
            } else {
                return -1;
            }
        }
    }

    public boolean schneidetStrahl(Strahl strahl) {
        double entfernung = gibSchnittEntfernung(strahl);
        return (entfernung >= 0);
    }

    public boolean schneidetGerade(Gerade gerade) {
        double entfernung = gibSchnittEntfernung(gerade);
        return (entfernung >= 0 && entfernung <= gerade.getLaenge());
    }

    /**
     * Gibt den Schnittpunkt einer Geraden mit diesem Kreis zurueck, wenn dieser existiert
     * @param strahl Strahl
     * @return Schnittpunkt
     * null als Rueckgabe bedeutet, dass kein Schnittpunkt existiert
     */
    @Override
    public Vektor gibSchnittpunkt (Strahl strahl) {
        double entfernung = gibSchnittEntfernung(strahl);
        if (entfernung < 0) return null;
        Vektor schnittpunkt = Vektor.addiere(strahl.getBasisVektor(), Vektor.multipliziere(strahl.getRichtungsVektor(), entfernung));
        return schnittpunkt;
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelpunkt.addiere(verschiebung);
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor obenLinks = Vektor.addiere(mittelpunkt, new Vektor(-radius, -radius));
        double start = Math.toDegrees(vonWinkel);
        double extend = Math.toDegrees(extWinkel);
        Arc2D zeichenKreis = new Arc2D.Double(obenLinks.getX(), obenLinks.getY(), radius * 2, radius * 2, start, extend, Arc2D.OPEN);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(zeichenKreis);
    }

    public Vektor getMittelpunkt() {
        return mittelpunkt;
    }

    public void setMittelpunkt(Vektor mittelpunkt) {
        this.mittelpunkt = mittelpunkt;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}
