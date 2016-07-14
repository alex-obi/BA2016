package de.hhu.alobe.ba2016.mathe;


import java.awt.*;

public class VektorInt extends Vektor {

    private int xKoordinate;
    private int yKoordinate;

    public VektorInt(Point point) {
        xKoordinate = point.x;
        yKoordinate = point.y;
    }

    public VektorInt(double x, double y) {
        xKoordinate = (int)x;
        yKoordinate = (int)y;
    }

    public VektorInt(float x, float y) {
        xKoordinate = (int)x;
        yKoordinate = (int)y;
    }

    public VektorInt(int x, int y) {
        xKoordinate = x;
        yKoordinate = y;
    }

    @Override
    public Vektor kopiere() {
        return new VektorInt(xKoordinate, yKoordinate);
    }

    @Override
    public boolean sindGleich(Vektor vergleichsVektor) {
        return (vergleichsVektor.getXint() == xKoordinate && vergleichsVektor.getYint() == yKoordinate);
    }

    @Override
    public float gibLaenge() {
        return (float)Math.pow((double)((xKoordinate * xKoordinate) + (yKoordinate * yKoordinate)), 0.5);
    }

    @Override
    public void addiere(Vektor addVektor) {
        xKoordinate += addVektor.getXint();
        yKoordinate += addVektor.getYint();
    }

    @Override
    public void subtrahiere(Vektor subVektor) {
        xKoordinate -= subVektor.getXint();
        yKoordinate -= subVektor.getYint();
    }

    @Override
    public void multipliziere(float skalar) {
        xKoordinate = (int)((float)xKoordinate * skalar);
        yKoordinate = (int)((float)yKoordinate * skalar);
    }

    public void multipliziere(int skalar) {
        xKoordinate = xKoordinate * skalar;
        yKoordinate = yKoordinate * skalar;
    }

    @Override
    public float skalarprodukt(Vektor vektor) {
        return (float)xKoordinate * vektor.getXfloat() + (float)yKoordinate * vektor.getYfloat();
    }

    @Override
    public VektorFloat gibEinheitsVektor() {
        VektorFloat retVektor = new VektorFloat(xKoordinate, yKoordinate);
        retVektor.zuEinheitsVektor();
        return retVektor;
    }

    @Override
    public Vektor gibNormalenVektor() {
        return new VektorInt(-yKoordinate, xKoordinate);
    }

    @Override
    public boolean istNullvektor() {
        return (xKoordinate == 0 && yKoordinate == 0);
    }

    @Override
    public void dreheUmWinkel(double winkel) {
        xKoordinate = (int)((double)xKoordinate * Math.cos(winkel) - (double)yKoordinate * Math.sin(winkel));
        yKoordinate = (int)((double)xKoordinate * Math.sin(winkel) + (double)yKoordinate * Math.cos(winkel));
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
        Vektor retVektor = new VektorInt(xKoordinate, yKoordinate);
        retVektor.dreheUmWinkel(winkel);
        return retVektor;
    }

    @Override
    public double gibSchnittwinkel(Vektor schnittMitVektor) {
        double skalar = (double)Vektor.skalarprodukt(this.gibEinheitsVektor(), schnittMitVektor.gibEinheitsVektor());
        return Math.acos(skalar);
    }


    public int getX() {
        return xKoordinate;
    }

    public int getY() {
        return yKoordinate;
    }

    @Override
    public int getXint() {
        return xKoordinate;
    }

    @Override
    public int getYint() {
        return yKoordinate;
    }

    @Override
    public float getXfloat() {
        return (float)xKoordinate;
    }

    @Override
    public float getYfloat() {
        return (float)yKoordinate;
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
        xKoordinate = x;
    }

    @Override
    public void setY(int y) {
        yKoordinate = y;
    }

    @Override
    public void setX(float x) {
        xKoordinate = (int)x;
    }

    @Override
    public void setY(float y) {
        yKoordinate = (int)y;
    }

    @Override
    public void setX(double x) {
        xKoordinate = (int)x;
    }

    @Override
    public void setY(double y) {
        yKoordinate = (int)y;
    }

}
