package de.hhu.alobe.ba2016.physik.flaechen;


import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

/**
 * Grenzflaeche zwischen zwei Medien mit unterschiedlichen Brechzahlen und Eigenschaften wie Reflektion und Absorbtion von Strahlen.
 * Stellt Methoden zur Verfuegung um Kollisionen mit Strahlen zu verarbeiten.
 */
public abstract class Grenzflaeche extends Flaeche {

    //Brechzahl aussen (Richtung, in die Normalenvektor zeigt)
    private double n1;

    //Brechzahl innen
    private double n2;

    /**
     * Initialisiert die Grenzflaeche mit einem bestimmten Modus
     *
     * @param modus Berechnungsmodus, wie eintreffenden Strahlen veraendert werden.
     */
    Grenzflaeche(int modus) {
        this.n1 = 1;
        this.n2 = 1;
        this.modus = modus;
    }

    /**
     * Initialisiert die Grenzflaeche mit einem bestimmten Modus und einer aeusseren und einer inneren Brechzahl
     *
     * @param modus Berechnungsmodus, wie eintreffenden Strahlen veraendert werden.
     * @param n1    Aeussere Brechzahl (Auf der Seite, auf die der Normalenvektor zeigt)
     * @param n2    Innere Brechzahl
     */
    Grenzflaeche(int modus, double n1, double n2) {
        this.n1 = n1;
        this.n2 = n2;
        this.modus = modus;
    }

    /**
     * Gibt den Tangentialvektor zu einem bestimmten Punkt auf der Flaeche.
     *
     * @param position Ein Punkt auf der Flaeche.
     * @return Normierter Tangentialvektor an dem uebergebenen Punkt.
     */
    public Vektor gibTangentialVektor(Vektor position) {
        Vektor normalenVektor = gibNormalenVektor(position);
        return new Vektor(normalenVektor.getY(), -normalenVektor.getX());
    }

    /**
     * Gibt den Normalenvektor zu einem bestimmten Punkt auf der Flaeche.
     * Der Normalenvektor muss auf Laenge 1 normiert sein.
     *
     * @param position Ein Punkt auf der Flaeche.
     * @return Normierter Normalenvektor an dem uebergebenen Punkt.
     */
    public abstract Vektor gibNormalenVektor(Vektor position);

    /**
     * Berechnet die Richtung, in die ein einfallender Strahl reflektiert wird.
     *
     * @param einfallVektor Richtung des einfallenden Strahls.
     * @param position      Punkt, an dem der Strahl auf die Flaeche trifft.
     * @return Richtung des reflektierten Strahls
     */
    private Vektor gibReflektiertenVektor(Vektor einfallVektor, Vektor position) {
        //Berechne Normal- und Tangentialvektor an position
        Vektor normalVektor = gibNormalenVektor(position);
        Vektor tangentialVektor = gibTangentialVektor(position);

        //Berechne Normal- und Tangentialanteil des Einfallsvektor
        double normalAnteil = Vektor.skalarprodukt(einfallVektor, normalVektor);
        double tangentialAnteil = Vektor.skalarprodukt(einfallVektor, tangentialVektor);

        //Berechne relektierten Vektor aus dem Tangentialanteil und dem gespiegelten Normalanteil und gebe diesen zurueck
        Vektor normalReflexion = Vektor.multipliziere(normalVektor, -normalAnteil);
        Vektor tangentialReflexion = Vektor.multipliziere(tangentialVektor, tangentialAnteil);
        return Vektor.addiere(normalReflexion, tangentialReflexion);
    }

    /**
     * Berechnet die Richtung, in die ein einfallender Strahl gebrochen wird.
     *
     * @param einfallVektor Richtung des einfallenden Strahls.
     * @param position      Punkt, an dem der Strahl auf die Flaeche trifft.
     * @return Richtung des gebrochenen Strahls. null bei Totalreflexion.
     */
    private Vektor gibGebrochenenVektor(Vektor einfallVektor, Vektor position) {
        //Berechne Normal- und Tangentialvektor an position
        Vektor normalVektor = gibNormalenVektor(position);
        Vektor tangentialVektor = gibTangentialVektor(position);

        //Berechne Normal- und Tangentialanteil des Einfallsvektor
        double normalAnteil = Vektor.skalarprodukt(einfallVektor, normalVektor);
        double tangentialAnteil = Vektor.skalarprodukt(einfallVektor, tangentialVektor);

        double einfallsWinkel = Math.acos(Math.abs(Vektor.skalarprodukt(einfallVektor, normalVektor)));
        double ausfallsWinkel;

        //Normal- und Tangentialanteil des gebrochenen Vektors als Vektoren
        Vektor normalBrechung;
        Vektor tangentialBrechung;

        //Fallunterscheidung fuer 4 moegliche Richtungen bei gegebenem Einfallswinkel
        if (normalAnteil < 0) {
            ausfallsWinkel = Math.asin((n1 * Math.sin(einfallsWinkel) / n2));
            if (Double.isNaN(ausfallsWinkel)) return null; //Totalreflexion (ausfallsWinkel nicht berechenbar)
            normalBrechung = Vektor.multipliziere(normalVektor, -Math.cos(ausfallsWinkel));
        } else if (normalAnteil > 0) {
            ausfallsWinkel = Math.asin((n2 * Math.sin(einfallsWinkel) / n1));
            if (Double.isNaN(ausfallsWinkel)) return null; //Totalreflexion (ausfallsWinkel nicht berechenbar)
            normalBrechung = Vektor.multipliziere(normalVektor, Math.cos(ausfallsWinkel));
        } else {
            return null; //Nicht klar in welche Richtung gebrochen werden soll, da Einfallsvektor parallel zu Tangentialvektor ist
        }

        if (tangentialAnteil >= 0) {
            tangentialBrechung = Vektor.multipliziere(tangentialVektor, Math.sin(ausfallsWinkel));
        } else {
            tangentialBrechung = Vektor.multipliziere(tangentialVektor, -Math.sin(ausfallsWinkel));
        }

        return Vektor.addiere(normalBrechung, tangentialBrechung);
    }

    /**
     * Manipuliert den eintreffenden Strahlengang abhaengig vom Berechnungsmodus dieser Flaeche.
     *
     * @param cStrGng  Zu manipulierender Strahlengang.
     * @param position Schnittpunkt mit der Grenzflaeche.
     */
    @Override
    public void kollisionDurchfuehren(Strahlengang cStrGng, Vektor position) {
        if (modus == MODUS_ABSORB) {
            //Strahlengang wird hier beendet
            cStrGng.strahlengangBeenden(position);
        }
        if (modus == MODUS_REFLEKT) {
            //An den Strahlengang wird ein neuer, reflektierter Strahl angehaengt
            Vektor reflexion = gibReflektiertenVektor(cStrGng.getAktuellerStrahl().getRichtungsVektor(), position);
            Strahl reflexionsStrahl = new Strahl(position, reflexion);
            cStrGng.neuenStrahlAnhaengen(reflexionsStrahl);
        }
        if (modus == MODUS_BRECHUNG) {
            //
            Vektor brechung = gibGebrochenenVektor(cStrGng.getAktuellerStrahl().getRichtungsVektor(), position);
            if (brechung != null) {
                Strahl gebrochenerStrahl = new Strahl(position, brechung);
                cStrGng.neuenStrahlAnhaengen(gebrochenerStrahl);
            } else {
                //Totalreflexion. Kein gebrochener Strahl. Der Strahlengang wird hier beendet
                cStrGng.strahlengangBeenden(position);
            }
        }
    }

}
