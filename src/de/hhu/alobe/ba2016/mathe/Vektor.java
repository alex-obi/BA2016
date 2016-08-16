package de.hhu.alobe.ba2016.mathe;

import java.awt.*;
import java.awt.geom.Point2D;

public class Vektor extends Point2D.Double{

    public Vektor(Point2D.Double point) {
        super(point.getX(), point.getY());
    }

    public Vektor(Point point) {
        super(point.getX(), point.getY());
    }

    public Vektor(double x, double y) {
        super(x, y);
    }

    /*public Vektor(float x, float y) {
        super((double)x, (double)y);
    }*/

    public Vektor(int x, int y) {
        super((double)x, (double)y);
    }

    public static boolean sindGleich(Vektor vektor1, Vektor vektor2) {
        return vektor1.sindGleich(vektor2);
    }
    public boolean sindGleich(Vektor vergleichsVektor) {
        return (vergleichsVektor.getX() == x && vergleichsVektor.getY() == y);
    }

    public static double gibAbstand(Vektor v1, Vektor v2) {
        return v1.distance(v2);
    }

    public double gibLaenge() {
        return Math.pow(x * x + y * y, 0.5);
    }

    public static Vektor addiere(Vektor addVektor1, Vektor addVektor2) {
        Vektor retVektor = addVektor1.kopiere();
        retVektor.addiere(addVektor2);
        return retVektor;
    }
    public void addiere(Vektor addVektor) {
        x += addVektor.getX();
        y += addVektor.getY();
    }

    public static Vektor subtrahiere(Vektor vonVektor, Vektor subVektor) {
        Vektor retVektor = vonVektor.kopiere();
        retVektor.subtrahiere(subVektor);
        return retVektor;
    }
    public void subtrahiere(Vektor subVektor) {
        x -= subVektor.getX();
        y -= subVektor.getY();
    }

    public static Vektor multipliziere(Vektor vektor, double skalar) {
        Vektor retVektor = vektor.kopiere();
        retVektor.multipliziere(skalar);
        return retVektor;
    }
    public void multipliziere(double skalar) {
        x = x * skalar;
        y = y * skalar;
    }
    public void multipliziere(int skalar) {
        x = x * (double)skalar;
        y = y * (double)skalar;
    }

    public static double skalarprodukt(Vektor vektor1, Vektor vektor2) {
        return vektor1.skalarprodukt(vektor2);
    }
    public double skalarprodukt(Vektor vektor) {
        return x * vektor.getX() + y * vektor.getY();
    }

    public static double gibSchnittwinkel(Vektor vonVektor, Vektor zuVektor) {
        return vonVektor.gibSchnittwinkel(zuVektor);
    }
    public double gibSchnittwinkel(Vektor schnittMitVektor) {
        double skalar = Vektor.skalarprodukt(this.gibEinheitsVektor(), schnittMitVektor.gibEinheitsVektor());
        return Math.acos(skalar);
    }

    public Vektor kopiere() {
        return new Vektor(x, y);
    }

    public void zuEinheitsVektor() {
        double laenge = gibLaenge();
        if(laenge == 0) return;
        x = x / laenge;
        y = y / laenge;
    }
    public Vektor gibEinheitsVektor() {
        Vektor retVektor = this.kopiere();
        retVektor.zuEinheitsVektor();
        return retVektor;
    }

    public Vektor gibNormalenVektor() {
        return new Vektor(-y, x);
    }

    public boolean istNullvektor() {
        return (x == 0 && y == 0);
    }

    public Vektor gibGedrehtenVektor(double winkel) {
        Vektor retVektor = new Vektor(x, y);
        retVektor.dreheUmWinkel(winkel);
        return retVektor;
    }
    public void dreheUmWinkel(double winkel) {
        x = x * Math.cos(winkel) - y * Math.sin(winkel);
        y = x * Math.sin(winkel) + y * Math.cos(winkel);
    }

    public double gibRichtungsWinkel() {
        double richtungsWinkel = 0;
        double minWinkel = Math.acos(this.gibEinheitsVektor().getXdouble());
        if (y >= 0) {
            richtungsWinkel = minWinkel;
        } else {
            richtungsWinkel = 2 * Math.PI - minWinkel;
        }
        return richtungsWinkel;
    }

    public Point toPoint() {
        return new Point((int)x, (int)y);
    }

    public int getXint() {
        return (int)x;
    }

    public int getYint() {
        return (int)y;
    }

    public float getXfloat() {
        return (float)x;
    }

    public float getYfloat() {
        return (float)y;
    }

    public double getXdouble() {
        return x;
    }

    public double getYdouble() {
        return y;
    }

    public void setX(int x) {
        this.x = (double)x;
    }

    public void setY(int y) {
        this.y = (double)y;
    }

    public void setX(float x) {
        this.x = (double)x;
    }

    public void setY(float y) {
        this.y = (double)y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
