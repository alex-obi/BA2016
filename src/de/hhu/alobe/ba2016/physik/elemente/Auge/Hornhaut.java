package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.flaechen.Flaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche;
import de.hhu.alobe.ba2016.physik.flaechen.Grenzflaeche_Sphaerisch;
import de.hhu.alobe.ba2016.physik.flaechen.Hauptebene;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;

/**
 * Hornhaut eines Auges.
 */
public class Hornhaut implements KannKollision, Zeichenbar {

    //Brechzahl des Kammerwassers hinter der Hornhaut
    private static final double BRECHZAHL_HORNHAUT = 1.376;

    //Refrenz auf das zugehörige Auge
    private Auge auge;

    //Fläche der Hornhaut
    private Grenzflaeche grenzflaeche;

    //Hauptebene der Hornhaut
    private Hauptebene hauptebene;

    //Position der Hornhaut
    private Vektor position;

    /**
     * Initialisiere neue Hornhaut mit einem Radius.
     *
     * @param auge     Referenz auf das zugehörige Auge.
     * @param position Position der Hornhaut.
     * @param radius   Radius der Hornhaut.
     */
    public Hornhaut(Auge auge, Vektor position, double radius) {
        this.auge = auge;
        this.position = position;
        double hoehenfaktor = 1.5;
        this.hauptebene = new Hauptebene(Hauptebene.MODUS_BRECHUNG, position, 2.660 * radius, 3.660 * radius, 1.3 * radius * hoehenfaktor);
        double alpha = Math.asin(hoehenfaktor / 2);
        this.grenzflaeche = new Grenzflaeche_Sphaerisch(Flaeche.MODUS_BRECHUNG, 1, BRECHZAHL_HORNHAUT, new Vektor(position.getX() + radius, position.getY()), radius, Math.PI - alpha, alpha * 2);
    }

    /**
     * Verschiebt die Position der Hornhaut um den übergebenen Vektor.
     *
     * @param verschiebung Vektor, um den die Hornhaut verschoben werden soll.
     */
    public void verschiebeUm(Vektor verschiebung) {
        position.addiere(verschiebung);
        grenzflaeche.verschiebeUm(verschiebung);
        hauptebene.verschiebeUm(verschiebung);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        switch (auge.getOptischeBank().getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                return grenzflaeche.gibKollision(cStrGng);
            case OptischeBank.MODUS_HAUPTEBENE:
                StrahlenKollision sK = grenzflaeche.gibKollision(cStrGng);
                if (sK != null) {
                    return new StrahlenKollision(sK.getDistanz(), cStrGng, hauptebene);
                }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        switch (auge.getOptischeBank().getModus()) {
            case OptischeBank.MODUS_SNELLIUS:
                g.setColor(new Color(0, 7, 244));
                grenzflaeche.paintComponent(g);
                break;
            case OptischeBank.MODUS_HAUPTEBENE:
                g.setColor(Color.GRAY);
                grenzflaeche.paintComponent(g);
                break;
        }
    }
}
