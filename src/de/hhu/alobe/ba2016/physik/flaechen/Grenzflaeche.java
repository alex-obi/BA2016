package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Implementierende Klassen stellen Methoden zur Verfügung um Kollisionen mit Strahlen zu verarbeiten
 */
public abstract class Grenzflaeche extends Flaeche {

    protected double n1; //Brechzahl aussen (Richtung, in die Normalenvektor zeigt)
    protected double n2; //Brechzahl innen

    public Grenzflaeche(int modus) {
        this.n1 = 1;
        this.n2 = 1;
        this.modus = modus;
    }

    public Grenzflaeche(int modus, double n1, double n2) {
        this.n1 = n1;
        this.n2 = n2;
        this.modus = modus;
    }

    public abstract Vektor gibTangentialVektor (Vektor position);

    public abstract Vektor gibNormalenVektor (Vektor position);

    public Vektor gibReflektiertenVektor (Vektor einfallVektor, Vektor position) {
        Vektor normalVektor = gibNormalenVektor(position);
        Vektor tangentialVektor = gibTangentialVektor(position);
        double normalAnteil = -Vektor.skalarprodukt(einfallVektor, normalVektor);
        double tangentialAnteil = Vektor.skalarprodukt(einfallVektor, tangentialVektor);
        Vektor normalAnteilVektor = Vektor.multipliziere(normalVektor, normalAnteil);
        Vektor tangentialAnteilVektor = Vektor.multipliziere(tangentialVektor, tangentialAnteil);
        Vektor ausfallsVektor = Vektor.addiere(normalAnteilVektor, tangentialAnteilVektor);
        return ausfallsVektor;
    }

    public Vektor gibGebrochenenVektor (Vektor einfallVektor, Vektor position) {
        Vektor normalVektor = gibNormalenVektor(position);
        Vektor tangentialVektor = gibTangentialVektor(position);
        double einfallsWinkel = Math.acos(Math.abs(Vektor.skalarprodukt(einfallVektor, normalVektor)));
        double ausfallsWinkel;
        Vektor normalAnteilAusfall;
        Vektor tangentialAnteilAusfall;
        if(Vektor.skalarprodukt(einfallVektor, normalVektor) < 0) { //Vektoren zeigen in unterschiedliche Richtungen
            ausfallsWinkel = Math.asin((n1 * Math.sin(einfallsWinkel) / n2));
            normalAnteilAusfall = Vektor.multipliziere(normalVektor, -Math.cos(ausfallsWinkel));
        } else {
            ausfallsWinkel = Math.asin((n2 * Math.sin(einfallsWinkel) / n1));
            normalAnteilAusfall = Vektor.multipliziere(normalVektor, Math.cos(ausfallsWinkel));
        }
        if(Double.isNaN(ausfallsWinkel)) return null; //Totalreflexion (ausfallsWinkel nicht berechenbar)
        if(Vektor.skalarprodukt(einfallVektor, tangentialVektor) >= 0) {
            tangentialAnteilAusfall = Vektor.multipliziere(tangentialVektor, Math.sin(ausfallsWinkel));
        } else {
            tangentialAnteilAusfall = Vektor.multipliziere(tangentialVektor, -Math.sin(ausfallsWinkel));
        }
        return Vektor.addiere(normalAnteilAusfall, tangentialAnteilAusfall);
    }

    /**
     * Manipuliert den eintreffenden Strahlengang abhaengig von den physikalischen Panel_Werkzeuge des Objekts.
     * @param cStrGng Zu manipulierender Strahlengang
     * @param position Schnittpunkt mit der Grenzflaeche
     */
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if (modus == MODUS_ABSORB) {
            cStrGng.strahlengangBeenden(position);
        }
        if (modus == MODUS_REFLEKT) {
            Vektor reflektion = gibReflektiertenVektor(cStrGng.getAktuellerStrahl().getRichtungsVektor(), position);
            Vektor vonBis = Vektor.subtrahiere(position, cStrGng.getAktuellerStrahl().getBasisVektor());
            Strahl reflektionsStrahl = new Strahl(position, reflektion);
            cStrGng.neuenStrahlAnhaengen(reflektionsStrahl);
        }
        if (modus == MODUS_BRECHUNG) {
            Vektor brechung = gibGebrochenenVektor(cStrGng.getAktuellerStrahl().getRichtungsVektor(), position);
            if(brechung != null) {
                Strahl gebrochenerStrahl = new Strahl(position, brechung);
                cStrGng.neuenStrahlAnhaengen(gebrochenerStrahl);
            } else {
                //Totalreflexion
                cStrGng.strahlengangBeenden(position);
            }
        }
    }

    public double getN1() {
        return n1;
    }

    public void setN1(double n1) {
        this.n1 = n1;
    }

    public double getN2() {
        return n2;
    }

    public void setN2(double n2) {
        this.n2 = n2;
    }

    public int getModus() {
        return modus;
    }

    public void setModus(int modus) {
        this.modus = modus;
    }
}
