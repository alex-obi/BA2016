package de.hhu.alobe.ba2016.mathe;


import java.awt.*;

public class VektorFloat extends Vektor {

    private float xKoordinate;
    private float yKoordinate;

    public VektorFloat(Point point) {
        xKoordinate = point.x;
        yKoordinate = point.y;
    }

    public VektorFloat(double x, double y) {
        xKoordinate = (float)x;
        yKoordinate = (float)y;
    }

    public VektorFloat(float x, float y) {
        xKoordinate = x;
        yKoordinate = y;
    }

    public VektorFloat(int x, int y) {
        xKoordinate = (float)x;
        yKoordinate = (float)y;
    }

    @Override
    public Vektor kopiere() {
        return new VektorFloat(xKoordinate, yKoordinate);
    }

    @Override
    public boolean sindGleich(Vektor vergleichsVektor) {
        return (vergleichsVektor.getXfloat() == xKoordinate && vergleichsVektor.getYfloat() == yKoordinate);
    }

    @Override
    public float gibLaenge() {
        return (float)Math.pow((double)((xKoordinate * xKoordinate) + (yKoordinate * yKoordinate)), 0.5);
    }

    @Override
    public void addiere(Vektor addVektor) {
        xKoordinate += addVektor.getXfloat();
        yKoordinate += addVektor.getYfloat();
    }

    @Override
    public void subtrahiere(Vektor subVektor) {
        xKoordinate -= subVektor.getXfloat();
        yKoordinate -= subVektor.getYfloat();
    }

    @Override
    public void multipliziere(float skalar) {
        xKoordinate = xKoordinate * skalar;
        yKoordinate = yKoordinate * skalar;
    }

    public void multipliziere(int skalar) {
        xKoordinate = xKoordinate * (float)skalar;
        yKoordinate = yKoordinate * (float)skalar;
    }

    @Override
    public float skalarprodukt(Vektor vektor) {
        return xKoordinate * vektor.getXfloat() + yKoordinate * vektor.getYfloat();
    }

    public void zuEinheitsVektor() {
        float laenge = gibLaenge();
        if(laenge == 0) return;
        xKoordinate = xKoordinate / laenge;
        yKoordinate = yKoordinate / laenge;
    }

    @Override
    public VektorFloat gibEinheitsVektor() {
        VektorFloat retVektor = new VektorFloat(xKoordinate, yKoordinate);
        retVektor.zuEinheitsVektor();
        return retVektor;
    }

    @Override
    public Vektor gibNormalenVektor() {
        return new VektorFloat(-yKoordinate, xKoordinate);
    }

    @Override
    public boolean istNullvektor() {
        return (xKoordinate == 0 && yKoordinate == 0);
    }

    @Override
    public void dreheUmWinkel(double winkel) {
        xKoordinate = (float)((double)xKoordinate * Math.cos(winkel) - (double)yKoordinate * Math.sin(winkel));
        yKoordinate = (float)((double)xKoordinate * Math.sin(winkel) + (double)yKoordinate * Math.cos(winkel));
    }

    @Override
    public double gibRichtungsWinkel() {
        double richtungsWinkel = 0;
        double minWinkel = Math.acos(this.gibEinheitsVektor().getXdouble());
        if (yKoordinate > 0) {
            richtungsWinkel = minWinkel;
        } else {
            richtungsWinkel = 2 * Math.PI - minWinkel;
        }
        return richtungsWinkel;
    }

    @Override
    public Vektor gibGedrehtenVektor(double winkel) {
        Vektor retVektor = new VektorFloat(xKoordinate, yKoordinate);
        retVektor.dreheUmWinkel(winkel);
        return retVektor;
    }

    @Override
    public double gibSchnittwinkel(Vektor schnittMitVektor) {
        double skalar = (double)Vektor.skalarprodukt(this.gibEinheitsVektor(), schnittMitVektor.gibEinheitsVektor());
        return Math.acos(skalar);
    }


    public float getX() {
        return xKoordinate;
    }

    public float getY() {
        return yKoordinate;
    }

    @Override
    public int getXint() {
        return (int)xKoordinate;
    }

    @Override
    public int getYint() {
        return (int)yKoordinate;
    }

    @Override
    public float getXfloat() {
        return xKoordinate;
    }

    @Override
    public float getYfloat() {
        return yKoordinate;
    }

    @Override
    public double getXdouble() {
        return (double)xKoordinate;
    }

    @Override
    public double getYdouble() {
        return (double)yKoordinate;
    }

    @Override
    public void setX(int x) {
        xKoordinate = (float)x;
    }

    @Override
    public void setY(int y) {
        yKoordinate = (float)y;
    }

    @Override
    public void setX(float x) {
        xKoordinate = x;
    }

    @Override
    public void setY(float y) {
        yKoordinate = y;
    }

    @Override
    public void setX(double x) {
        xKoordinate = (float)x;
    }

    @Override
    public void setY(double y) {
        yKoordinate = (float)y;
    }

}
