package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion;
import de.hhu.alobe.ba2016.editor.aktionen.AktionsListe;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.grafik.OptischeAchse;
import de.hhu.alobe.ba2016.physik.elemente.Auge.Auge;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.Licht.ParrallelLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.Licht.PunktLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Blende;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import de.hhu.alobe.ba2016.physik.elemente.spiegel.Hohlspiegel;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.elemente.*;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel beinhaltet die gesamte graphische Anzeige der optischen Bank
 */
public class OptischeBank extends JPanel {

    private double zoom;

    private Vektor groesse;

    private JScrollPane zeichenRahmen;
    private Zeichenbrett zeichenBrett;

    private Werkzeuge werkzeuge;

    private Eigenschaften eigenschaften;

    //Offset zum verschieben des Bildausschnittes
    private Vektor positionOffset;

    public static final int MODUS_SNELLIUS = 1;
    public static final int MODUS_HAUPTEBENE = 2;

    private int modus = MODUS_HAUPTEBENE;

    private ArrayList<Bauelement> bauelemente;

    public double getZoom() {
        return zoom;
    }

    private ArrayList<Lichtquelle> lichtquellen;
    private ArrayList<KannKollision> kollisionsObjekte;

    private AktionsListe aktionsListe;

    private OptischeAchse optischeAchse;

    //Aktuell ausgewaehltes Werkzeug zur Tastatur-/ Mausinteraktion

    Werkzeug aktuellesWerkzeug;

    public OptischeBank() {

        super(new BorderLayout());
        setOpaque(true);

        zoom = 1;

        zeichenBrett =  new Zeichenbrett(this);

        zeichenRahmen = new JScrollPane(zeichenBrett);
        groesse = new VektorInt(2000, 600);
        zeichenBrett.setPreferredSize(new Dimension(groesse.getXint(), groesse.getYint()));
        this.add(zeichenRahmen, BorderLayout.CENTER);

        optischeAchse = new OptischeAchse(250);
        zeichenBrett.neuesZeichenObjekt(optischeAchse);

        werkzeuge = new Werkzeuge(this);
        this.add(werkzeuge, BorderLayout.NORTH);

        eigenschaften = new Eigenschaften(this);
        this.add(eigenschaften, BorderLayout.SOUTH);

        positionOffset = new VektorInt(0, 0);

        bauelemente =  new ArrayList<>();
        lichtquellen =  new ArrayList<>();
        kollisionsObjekte = new ArrayList<>();

        aktionsListe = new AktionsListe();

        werkzeugWechseln(new Werkzeug_Auswahl(this));

        //########### Erzeuge Testszenario

        bauelementHinzufuegen(new PunktLichtquelle(this, new VektorInt(50, 250), Color.BLACK));
        bauelementHinzufuegen(new ParrallelLichtquelle(this, new VektorInt(50, 250), Color.BLACK, 250, 0));
        bauelementHinzufuegen(new Hohlspiegel(this, new VektorInt(500, 450), -300, 150));
        /*bauelementHinzufuegen(new Hohlspiegel(this, new VektorInt(700, 500), 300, 200));
        bauelementHinzufuegen(new Hohlspiegel(this, new VektorInt(300, 500), 100, 200));
        bauelementHinzufuegen(new Hohlspiegel(this, new VektorInt(900, 500), 0, 200));*/
        bauelementHinzufuegen(new Schirm(this, new VektorInt(650, 450), 150));
        bauelementHinzufuegen(new Linse(this, new VektorFloat(50, 450), 1.8, 200, 10, 50, -60));
        bauelementHinzufuegen(new Linse(this, new VektorFloat(350, 450), 2.5, 150, 10, 300, 250));
        bauelementHinzufuegen(new Linse(this, new VektorFloat(200, 450), 1.8, 150, 10, -300, -250));
        bauelementHinzufuegen(new Blende(this, new VektorFloat(800, 450), 150, 50));
        bauelementHinzufuegen(new Auge(this, new VektorFloat(700, 250)));

    }

    public void werkzeugWechseln(Werkzeug neuesWerkzeug) {
        if(aktuellesWerkzeug != null) {
            aktuellesWerkzeug.auswahlAufheben();
            zeichenBrett.removeMouseListener(aktuellesWerkzeug);
            zeichenBrett.removeMouseMotionListener(aktuellesWerkzeug);
            zeichenBrett.removeMouseWheelListener(aktuellesWerkzeug);
        }
        aktuellesWerkzeug = neuesWerkzeug;
        zeichenBrett.addMouseListener(neuesWerkzeug);
        zeichenBrett.addMouseMotionListener(neuesWerkzeug);
        zeichenBrett.addMouseWheelListener(neuesWerkzeug);
        neuesWerkzeug.auswaehlen();
    }

    public void bauelementHinzufuegen(Bauelement bauelement) {
        switch(bauelement.getTyp()) {
            case Bauelement.TYP_LAMPE:
                lichtquellen.add((Lichtquelle)bauelement);
                break;
            case Bauelement.TYP_LINSE:
                kollisionsObjekte.add((Linse)bauelement);
                break;
            case Bauelement.TYP_SPIEGEL:
                kollisionsObjekte.add((Hohlspiegel)bauelement);
                break;
            case Bauelement.TYP_SCHIRM:
                kollisionsObjekte.add((Schirm)bauelement);
                break;
            case Bauelement.TYP_BLENDE:
                kollisionsObjekte.add((Blende)bauelement);
                break;
            case Bauelement.TYP_AUGE:
                kollisionsObjekte.add((Auge)bauelement);
        }
        zeichenBrett.neuesZeichenObjekt(bauelement);
        bauelemente.add(bauelement);
    }

    public void bauelementLoeschen(Bauelement bauelement) {
        zeichenBrett.zeichenObjektLoeschen(bauelement);
        kollisionsObjekte.remove(bauelement);
        bauelemente.remove(bauelement);
        lichtquellen.remove(bauelement);
    }

    public void neueAktionDurchfuehren(Aktion aktion) {
        aktionsListe.neueAktion(aktion);
        aktion.aktionDurchfuehren();
        aktualisieren();
    }

    public void letzteAktionUeberschreiben(Aktion aktion) {
        aktionsListe.letzteAktionUeberschreiben(aktion);
        aktion.aktionDurchfuehren();
        aktualisieren();
    }

    public void aktionRueckgaengig() {
        aktionsListe.undo();
        aktualisieren();
    }

    public void aktionWiederholen() {
        aktionsListe.redo();
        aktualisieren();
    }

    public void strahlenNeuBerechnen() {
        for(Lichtquelle cLicht : lichtquellen) {
            if(cLicht.isAktiv()) {
                ArrayList<Strahlengang> strahlen = cLicht.getStrahlengaenge();
                for (Strahlengang cStrahl : strahlen) {
                    cStrahl.resetteStrahlengang();
                }
                int i = 0;
                while (i < strahlen.size()) {
                    //Finde Erstkollision
                    StrahlenKollision ersteKoll = null;
                    for (KannKollision cElement : kollisionsObjekte) {
                        StrahlenKollision nKoll = cElement.kollisionUeberpruefen(strahlen.get(i));
                        if (nKoll != null) {
                            if (ersteKoll == null || nKoll.getDistanz() < ersteKoll.getDistanz()) {
                                ersteKoll = nKoll;
                            }
                        }
                    }

                    if (ersteKoll != null) {
                        ersteKoll.kollisionDurchfuehren();
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    public void aktualisieren() {
        Thread aktThread = new Thread() {
            @Override
            public void run() {
                aktualisierenThreadSicher();
            }
        };
        aktThread.run();
    }

    private void aktualisierenThreadSicher() {
        synchronized (this) {
            strahlenNeuBerechnen();
            this.repaint();
            zeichenRahmen.repaint();
        }
    }

    public void zoomStufeRein() {
        zoom *= (1 + Konstanten.ZOOM_STUFE);

        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getXfloat() * zoom), (int)(groesse.getYfloat() * zoom)));
        zeichenBrett.revalidate();
    }

    public void zoomStufeRaus() {
        zoom /= (1 + Konstanten.ZOOM_STUFE);

        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getXfloat() * zoom), (int)(groesse.getYfloat() * zoom)));
        zeichenBrett.revalidate();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getXfloat() * zoom), (int)(groesse.getYfloat() * zoom)));
        zeichenBrett.revalidate();
    }

    public OptischeAchse getOptischeAchse() {
        return optischeAchse;
    }

    public Zeichenbrett getZeichenBrett() {
        return zeichenBrett;
    }

    public int getModus() {
        return modus;
    }

    public void setModus(int modus) {
        this.modus = modus;
    }

    public Vektor getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Vektor positionOffset) {
        this.positionOffset = positionOffset;
    }

    public ArrayList<Lichtquelle> getLichtquellen() {
        return lichtquellen;
    }

    public ArrayList<Bauelement> getBauelemente() {
        return bauelemente;
    }

    public Eigenschaften getEigenschaften() {
        return eigenschaften;
    }
}
