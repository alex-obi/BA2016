package de.hhu.alobe.ba2016.mathe;

public abstract class Vektor {

    public abstract Vektor kopiere();

    public abstract boolean sindGleich(Vektor vergleichsVektor);
    public static boolean sindGleich(Vektor vektor1, Vektor vektor2) {
        return vektor1.sindGleich(vektor2);
    }

    public abstract float gibLaenge();
    public static float gibAbstand(Vektor v1, Vektor v2) {
        Vektor editVektor = v1.kopiere();
        editVektor.subtrahiere(v2);
        return editVektor.gibLaenge();
    }

    public abstract void addiere(Vektor addVektor);
    public static Vektor addiere(Vektor addVektor1, Vektor addVektor2) {
        Vektor retVektor = addVektor1.kopiere();
        retVektor.addiere(addVektor2);
        return retVektor;
    }

    public abstract void subtrahiere(Vektor subVektor);
    public static Vektor subtrahiere(Vektor vonVektor, Vektor subVektor) {
        Vektor retVektor = vonVektor.kopiere();
        retVektor.subtrahiere(subVektor);
        return retVektor;
    }

    public abstract void multipliziere(float skalar);
    public static Vektor multipliziere(Vektor vektor, float skalar) {
        Vektor retVektor = vektor.kopiere();
        retVektor.multipliziere(skalar);
        return retVektor;
    }

    public abstract float skalarprodukt(Vektor vektor);
    public static float skalarprodukt(Vektor vektor1, Vektor vektor2) {
        return vektor1.skalarprodukt(vektor2);
    }

    public abstract Vektor gibNormalenVektor();

    public abstract VektorFloat gibEinheitsVektor();

    public abstract boolean istNullvektor();

    public abstract void dreheUmWinkel(double winkel);

    public abstract double gibRichtungsWinkel();

    public abstract Vektor gibGedrehtenVektor(double winkel);

    public abstract double gibSchnittwinkel(Vektor schnitMitVektor);
    public static double gibSchnittwinkel(Vektor vonVektor, Vektor zuVektor) {
        return vonVektor.gibSchnittwinkel(zuVektor);
    }

    public abstract int getXint();
    public abstract int getYint();
    public abstract float getXfloat();
    public abstract float getYfloat();
    public abstract double getXdouble();
    public abstract double getYdouble();

    public abstract void setX(int x);
    public abstract void setY(int y);
    public abstract void setX(float x);
    public abstract void setY(float y);
    public abstract void setX(double x);
    public abstract void setY(double y);

}
