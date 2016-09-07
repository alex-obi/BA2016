package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Stellt Methoden zur Verfügung um Kollisionen mit Strahlen zu verarbeiten.
 */
public abstract class Grenzflaeche extends Flaeche {

    //Brechzahl aussen (Richtung, in die Normalenvektor zeigt)
    private double n1;

    //Brechzahl innen
    private double n2;

    /**
     * Initialisiert die Grenzfläche mit einem bestimmten Modus
     * @param modus Berechnungsmodus, wie eintreffenden Strahlen verändert werden.
     */
    Grenzflaeche(int modus) {
        this.n1 = 1;
        this.n2 = 1;
        this.modus = modus;
    }

    /**
     * Initialisiert die Grenzfläche mit einem bestimmten Modus und einer äußeren und einer inneren Brechzahl
     * @param modus Berechnungsmodus, wie eintreffenden Strahlen verändert werden.
     * @param n1 Äußere Brechzahl (Auf der Seite, auf die der Normalenvektor zeigt)
     * @param n2 Innere Brechzahl
     */
    Grenzflaeche(int modus, double n1, double n2) {
        this.n1 = n1;
        this.n2 = n2;
        this.modus = modus;
    }

    /**
     * Gibt den Tangentialvektor zu einem bestimmten Punkt auf der Fläche.
     * @param position Ein Punkt auf der Fläche.
     * @return Tangentialvektor an dem übergebenen Punkt.
     */
    public Vektor gibTangentialVektor (Vektor position) {
        Vektor normalenVektor = gibNormalenVektor(position);
        return new Vektor(normalenVektor.getY(), -normalenVektor.getX());
    }

    /**
     * Gibt den Normalenvektor zu einem bestimmten Punkt auf der Fläche.
     * @param position Ein Punkt auf der Fläche.
     * @return Normalenvektor an dem übergebenen Punkt.
     */
    public abstract Vektor gibNormalenVektor (Vektor position);

    /**
     * Berechnet die Richtung, in die ein einfallender Strahl reflektiert wird.
     * @param einfallVektor Richtung des einfallenden Strahls.
     * @param position Punkt, an dem der Strahl auf die Fläche trifft.
     * @return Richtung des reflektierten Strahls
     */
    private Vektor gibReflektiertenVektor (Vektor einfallVektor, Vektor position) {
        //Berechne Normal- und Tangentialvektor an position
        Vektor normalVektor = gibNormalenVektor(position);
        Vektor tangentialVektor = gibTangentialVektor(position);

        //Berechne Normal- und Tangentialanteil des Einfallsvektor
        double normalAnteil = Vektor.skalarprodukt(einfallVektor, normalVektor);
        double tangentialAnteil = Vektor.skalarprodukt(einfallVektor, tangentialVektor);

        //Berechne relektierten Vektor aus dem Tangentialanteil und dem gespiegelten Normalanteil und gebe diesen zurück
        Vektor normalReflektion = Vektor.multipliziere(normalVektor, -normalAnteil);
        Vektor tangentialReflektion = Vektor.multipliziere(tangentialVektor, tangentialAnteil);
        return Vektor.addiere(normalReflektion, tangentialReflektion);
    }

    /**
     * Berechnet die Richtung, in die ein einfallender Strahl gebrochen wird.
     * @param einfallVektor Richtung des einfallenden Strahls.
     * @param position Punkt, an dem der Strahl auf die Fläche trifft.
     * @return Richtung des gebrochenen Strahls. null bei Totalreflektion.
     */
    private Vektor gibGebrochenenVektor (Vektor einfallVektor, Vektor position) {
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
     * Manipuliert den eintreffenden Strahlengang abhaengig vom Berechnungsmodus dieser Fläche.
     * @param cStrGng Zu manipulierender Strahlengang.
     * @param position Schnittpunkt mit der Grenzflaeche.
     */
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if (modus == MODUS_ABSORB) {
            cStrGng.strahlengangBeenden(position);
        }
        if (modus == MODUS_REFLEKT) {
            Vektor reflektion = gibReflektiertenVektor(cStrGng.getAktuellerStrahl().getRichtungsVektor(), position);
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

}
