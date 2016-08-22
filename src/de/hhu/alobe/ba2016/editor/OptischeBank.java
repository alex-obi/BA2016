package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion;
import de.hhu.alobe.ba2016.editor.aktionen.AktionsListe;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.grafik.OptischeAchse;
import de.hhu.alobe.ba2016.physik.elemente.Auge.Auge;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Farbe;
import de.hhu.alobe.ba2016.physik.elemente.Licht.Lichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.Licht.ParallelLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.Licht.PunktLichtquelle;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Blende;
import de.hhu.alobe.ba2016.physik.elemente.absorbtion.Schirm;
import de.hhu.alobe.ba2016.physik.elemente.glasskoerper.Linse;
import de.hhu.alobe.ba2016.physik.elemente.spiegel.Hohlspiegel;
import de.hhu.alobe.ba2016.physik.elemente.spiegel.Spiegel;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import de.hhu.alobe.ba2016.physik.elemente.*;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.jdom.Speicherbar;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Panel beinhaltet die gesamte graphische Anzeige der optischen Bank
 */
public class OptischeBank extends JPanel implements Speicherbar {

    public static final String XML_OPTISCHEBANK = "optische_bank";

    private String name;
    public static final String XML_NAME = "name";

    private double zoom;

    private Point groesse;

    private JScrollPane zeichenRahmen;
    private Zeichenbrett zeichenBrett;

    private Eigenschaften eigenschaften;

    //Offset zum verschieben des Bildausschnittes
    private Point positionOffset;

    private int modus = MODUS_HAUPTEBENE;
    public static final int MODUS_SNELLIUS = 1;
    public static final int MODUS_HAUPTEBENE = 2;

    private boolean virtuelleStrahlenAktiv = false;

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

    public OptischeBank(String name) {
        this.name = name;
        initialisiere();

        //########### Erzeuge Testszenario

        //bauelementHinzufuegen(new Linse(this, new Vektor(50, 450), 1.336, 90, 0, 60, 0));

        bauelementHinzufuegen(new PunktLichtquelle(this, new Vektor(50, 150), new Farbe(Color.BLACK)));
        /*bauelementHinzufuegen(new Blende(this, new Vektor(800, 450), 150, 50));
        bauelementHinzufuegen(new ParallelLichtquelle(this, new Vektor(50, 250), Color.BLACK, 250, 0));
        bauelementHinzufuegen(new Hohlspiegel(this, new Vektor(500, 450), -300, 150));
        bauelementHinzufuegen(new Hohlspiegel(this, new Vektor(700, 500), 300, 200));
        bauelementHinzufuegen(new Hohlspiegel(this, new Vektor(300, 500), 100, 200));
        bauelementHinzufuegen(new Hohlspiegel(this, new Vektor(900, 500), 0, 200));
        bauelementHinzufuegen(new Schirm(this, new Vektor(650, 450), 150));
        bauelementHinzufuegen(new Linse(this, new Vektor(50, 450), 1.8, 200, 10, 50, -60));
        bauelementHinzufuegen(new Linse(this, new Vektor(350, 450), 2.5, 150, 10, 300, 250));
        bauelementHinzufuegen(new Linse(this, new Vektor(200, 450), 1.8, 150, 10, -300, -250));
        bauelementHinzufuegen(new Blende(this, new Vektor(800, 450), 150, 50));
        bauelementHinzufuegen(new Auge(this, new Vektor(700, 250)));*/
    }

    public OptischeBank(Element xmlElement) throws Exception {
        this.name = xmlElement.getAttributeValue(XML_NAME);
        initialisiere();
        Iterator<?> bauelemente = xmlElement.getChildren().iterator();
        while(bauelemente.hasNext()) {
            Element nextBau = (Element)bauelemente.next();
            String name = nextBau.getName();
            if(name.equals(Blende.XML_BLENDE)) bauelementHinzufuegen(new Blende(this, nextBau));
            if(name.equals(Schirm.XML_SCHIRM)) bauelementHinzufuegen(new Schirm(this, nextBau));
            if(name.equals(Auge.XML_AUGE)) bauelementHinzufuegen(new Auge(this, nextBau));
            if(name.equals(Linse.XML_LINSE)) bauelementHinzufuegen(new Linse(this, nextBau));
            if(name.equals(ParallelLichtquelle.XML_PARALLELLICHT)) bauelementHinzufuegen(new ParallelLichtquelle(this, nextBau));
            if(name.equals(PunktLichtquelle.XML_PUNKTLICHT)) bauelementHinzufuegen(new PunktLichtquelle(this, nextBau));
            if(name.equals(Hohlspiegel.XML_HOHLSPIEGEL)) bauelementHinzufuegen(new Hohlspiegel(this, nextBau));
            if(name.equals(Spiegel.XML_SPIEGEL)) bauelementHinzufuegen(new Spiegel(this, nextBau));
        }
    }

    private void initialisiere() {
        setLayout(new BorderLayout());
        setOpaque(true);

        zoom = 1;

        zeichenBrett =  new Zeichenbrett(this);

        zeichenRahmen = new JScrollPane(zeichenBrett);
        groesse = new Point(2000, 600);
        zeichenBrett.setPreferredSize(new Dimension(groesse.x, groesse.y));
        this.add(zeichenRahmen, BorderLayout.CENTER);

        optischeAchse = new OptischeAchse(250);
        zeichenBrett.neuesZeichenObjekt(optischeAchse);

        eigenschaften = new Eigenschaften(this);
        this.add(eigenschaften, BorderLayout.SOUTH);

        positionOffset = new Point(0, 0);

        bauelemente =  new ArrayList<>();
        lichtquellen =  new ArrayList<>();
        kollisionsObjekte = new ArrayList<>();

        aktionsListe = new AktionsListe();

        werkzeugWechseln(new Werkzeug_Auswahl(this));

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
                for (Strahlengang cStrahl : cLicht.getStrahlengaenge()) {
                    einzelStrahlNeuBerechnen(cStrahl);
                }
            }
        }
    }

    public void einzelStrahlNeuBerechnen(Strahlengang strahlengang) {
        strahlengang.resetteStrahlengang();
        for(int i = 0; i < Konstanten.MAX_STRAHLLAENGE; i++) {
            StrahlenKollision ersteKoll = null;
            //Finde die erste Kollision unter allen Kollisionsobjekten:
            for (KannKollision cElement : kollisionsObjekte) {
                StrahlenKollision nKoll = cElement.kollisionUeberpruefen(strahlengang);
                if (nKoll != null) {
                    if (ersteKoll == null || nKoll.getDistanz() < ersteKoll.getDistanz()) {
                        ersteKoll = nKoll;
                    }
                }
            }
            if (ersteKoll != null) {
                ersteKoll.kollisionDurchfuehren();
            } else {
                break;
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

        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getX() * zoom), (int)(groesse.getY() * zoom)));
        zeichenBrett.revalidate();
    }

    public void zoomStufeRaus() {
        zoom /= (1 + Konstanten.ZOOM_STUFE);

        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getX() * zoom), (int)(groesse.getY() * zoom)));
        zeichenBrett.revalidate();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        zeichenBrett.setPreferredSize(new Dimension((int)(groesse.getX() * zoom), (int)(groesse.getY() * zoom)));
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

    public Point getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Point positionOffset) {
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

    public boolean isVirtuelleStrahlenAktiv() {
        return virtuelleStrahlenAktiv;
    }

    public void setVirtuelleStrahlenAktiv(boolean virtuelleStrahlenAktiv) {
        this.virtuelleStrahlenAktiv = virtuelleStrahlenAktiv;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = new Element(getXmlElementTyp()).setAttribute(XML_NAME, name);
        for(Bauelement cBau : bauelemente) {
            xmlElement.addContent(cBau.getXmlElement());
        }
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_OPTISCHEBANK;
    }
}
