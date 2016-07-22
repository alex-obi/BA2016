package de.hhu.alobe.ba2016.mathe;

import de.hhu.alobe.ba2016.Konstanten;

import java.awt.*;

/**
 * Strahl mit Basis und normiertem Richtungsvektor
 */
public class Strahl extends GeomertrischeFigur {

    protected Vektor basisVektor;
    protected VektorFloat richtungsVektor;

    //Variable zum Speichern der Entfernung, von welchem Bildpunkt (reell oder virtuell) aus der Strahl bei Berechnung durch Hauptebenen erzeugt wird
    protected float quellEntfernung;
    protected boolean ausDemUnendlichen;

    public Strahl(Vektor basisVektor, Vektor richtung) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = 0;
    }

    public Strahl(Vektor basisVektor, Vektor richtung, float quellEntfernung, boolean ausDemUnendlichen) {
        this.basisVektor = basisVektor;
        this.richtungsVektor = richtung.gibEinheitsVektor();
        this.quellEntfernung = quellEntfernung;
        this.ausDemUnendlichen = ausDemUnendlichen;
    }

    /**
     * Gibt an welche Strecke ein Strahl zurücklegen muss bis er den jeweils anderen trifft
     * @param s2
     * @return Array, mit Element 0 = Entfernung dieser Strahl, Element 1 = Entfernung übergebener Strahl
     */
    public float[] gibSchnittentfernungen (Strahl s2) {
        float b1x = basisVektor.getXfloat();
        float b1y = basisVektor.getYfloat();
        float r1x = richtungsVektor.getXfloat();
        float r1y = richtungsVektor.getYfloat();
        float b2x = s2.getBasisVektor().getXfloat();
        float b2y = s2.getBasisVektor().getYfloat();
        float r2x = s2.getRichtungsVektor().getXfloat();
        float r2y = s2.getRichtungsVektor().getYfloat();

        if (r1x * r2y == r1y * r2x) return null; //Strahlen sind parallel

        float lamda1 = ((b1y - b2y) * r2x - (b1x - b2x) * r2y) / (r1x * r2y - r1y * r2x);

        float lamda2 = 0;
        if (r2x != 0) lamda2 = (b1x - b2x + lamda1 * r1x) / r2x;
        if (r2y != 0) lamda2 = (b1y - b2y + lamda1 * r1y) / r2y;

        float[] lamdas = {lamda1, lamda2};
        return lamdas;
    }

    public Vektor gibQuellPunkt() {
        Vektor retVektor = basisVektor.kopiere();
        retVektor.addiere(Vektor.multipliziere(richtungsVektor, quellEntfernung));
        return retVektor;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        Vektor bisVektor = Vektor.addiere(basisVektor, Vektor.multipliziere(richtungsVektor, 10000));
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.drawLine(basisVektor.getXint(),
                basisVektor.getYint(),
                bisVektor.getXint(),
                bisVektor.getYint());
        if(quellEntfernung < 0 && !isAusDemUnendlichen()) {
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 5.0f, new float[] {10.0f,4.0f}, 0.0f));
            g.drawLine(basisVektor.getXint(),
                    basisVektor.getYint(),
                    basisVektor.getXint() + (int)((richtungsVektor.getXfloat() * quellEntfernung)),
                    basisVektor.getYint() + (int)((richtungsVektor.getYfloat() * quellEntfernung)));
        }
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        basisVektor.addiere(verschiebung);
    }

    public static float[] gibSchnittentfernungen (Strahl s1, Strahl s2) {
        return s1.gibSchnittentfernungen(s2);
    }

    public Vektor getBasisVektor() {
        return basisVektor;
    }

    public void setBasisVektor(Vektor basisVektor) {
        this.basisVektor = basisVektor;
    }

    public Vektor getRichtungsVektor() {
        return richtungsVektor;
    }

    public void setRichtungsVektor(Vektor richtungsVektor) {
        this.richtungsVektor = richtungsVektor.gibEinheitsVektor();
    }

    public float getQuellEntfernung() {
        return quellEntfernung;
    }

    public void setQuellEntfernung(float quellEntfernung) {
        this.quellEntfernung = quellEntfernung;
    }

    public boolean isAusDemUnendlichen() {
        return ausDemUnendlichen;
    }

    public void setAusDemUnendlichen(boolean ausDemUnendlichen) {
        this.ausDemUnendlichen = ausDemUnendlichen;
    }
}

