package de.hhu.alobe.ba2016.editor;


import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.aktionen.Aktion;
import de.hhu.alobe.ba2016.editor.aktionen.AktionsListe;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaften;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug;
import de.hhu.alobe.ba2016.editor.werkzeuge.Werkzeug_Auswahl;
import de.hhu.alobe.ba2016.jdom.Dateifunktionen;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Panel beinhaltet die gesamte graphische Anzeige der optischen Bank
 */
public class OptischeBank extends JPanel implements Speicherbar {

    //Name des Elements in XML
    private static final String XML_OPTISCHEBANK = "optische_bank";

    //Pfad zum Speicherort der Optischen Bank. null falls Optische Bank noch nicht gespeichert wurde.
    private File dateiPfad;

    //Zoomfaktor zum skalieren beim Zeichnen
    private double zoom;
    //Name des Zooms in XML
    private static final String XML_ZOOM = "zoom";

    //Größe der Optischen Bank, die durch Scrollen benutzbar ist
    private Point groesse;

    //Rahmen um die Optische Bank zum Scrollen des Bildausschnittes
    private JScrollPane zeichenRahmen;
    //Leinwand zum Zeichnen aller relevanten Elemente
    private Zeichenbrett zeichenBrett;

    //Optionsleiste zum Anzeigen von Reglern von Bauelementen
    private Eigenschaften eigenschaften;

    //Art der Berechnung der Strahlen (Hauptebene oder reale Brechung durch Snellius Formel)
    private int modus = MODUS_HAUPTEBENE;
    //Name des modus in XML
    private static final String XML_MODUS = "modus";

    /**
     * Wert zum Übergeben an Funktionen für die Berechnung durch reale Brechung und Reflektion)
     */
    public static final int MODUS_SNELLIUS = 1;

    /**
     * Wert zum Übergeben an Funktionen für die Berechnung durch Hauptebenen
     */
    public static final int MODUS_HAUPTEBENE = 2;

    //Gibt an, ob virtuelle Strahlen und Bilder geeichnet werden sollen
    private boolean virtuelleStrahlenAktiv = false;
    //Name von virtuelleStrahlenAktiv in XML
    public static final String XML_VIRTUELLE_STRAHLEN = "virtuelle_strahlen";

    //Liste aller Bauelemente, die dieser Optischen Bank hinzugefügt wurden
    private ArrayList<Bauelement> bauelemente;

    //Liste aller Lichtquellen, die dieser Optischen Bank hinzugefügt wurden
    private ArrayList<Lichtquelle> lichtquellen;

    //Liste aller Objekte, die Kollision mit Lichtstrahlen ausführen, die dieser Optischen Bank hinzugefügt wurden
    private ArrayList<KannKollision> kollisionsObjekte;

    //Aktionsliste zum Ausführen und rückgängig machen von Aktionen
    private AktionsListe aktionsListe;

    //Die optische Achse
    private OptischeAchse optischeAchse;

    //Aktuell ausgewaehltes Werkzeug zur Tastatur-/ Mausinteraktion
    Werkzeug aktuellesWerkzeug;

    /**
     * Initialisiert neue, leere Optische Bank ohne Voreinstellungen
     */
    public OptischeBank() {
        initialisiere(1);
    }

    /**
     * Initialisiere neue Optische Bank über Ein jdom Element, welches die notwendigen Attribute enthalten muss.
     *
     * @param xmlElement jdom Element zum Initialisieren
     * @param dateiPfad  Ort, von dem die Optische Bank geladen wurde
     * @throws Exception Fehler der zurückgegeben wird, falls Laden nicht erfolgreich durchgeführt werden konnte
     */
    public OptischeBank(Element xmlElement, File dateiPfad) throws Exception {
        this.dateiPfad = dateiPfad;
        setName(Dateifunktionen.getDateiNamen(dateiPfad));
        initialisiere(xmlElement.getAttribute(XML_ZOOM).getDoubleValue());
        modus = xmlElement.getAttribute(XML_MODUS).getIntValue();
        virtuelleStrahlenAktiv = xmlElement.getAttribute(XML_VIRTUELLE_STRAHLEN).getBooleanValue();
        Iterator<?> bauelemente = xmlElement.getChildren().iterator();
        while (bauelemente.hasNext()) {
            Element nextBau = (Element) bauelemente.next();
            String name = nextBau.getName();
            if (name.equals(Blende.XML_BLENDE)) bauelementHinzufuegen(new Blende(this, nextBau));
            if (name.equals(Schirm.XML_SCHIRM)) bauelementHinzufuegen(new Schirm(this, nextBau));
            if (name.equals(Auge.XML_AUGE)) bauelementHinzufuegen(new Auge(this, nextBau));
            if (name.equals(Linse.XML_LINSE)) bauelementHinzufuegen(new Linse(this, nextBau));
            if (name.equals(ParallelLichtquelle.XML_PARALLELLICHT))
                bauelementHinzufuegen(new ParallelLichtquelle(this, nextBau));
            if (name.equals(PunktLichtquelle.XML_PUNKTLICHT))
                bauelementHinzufuegen(new PunktLichtquelle(this, nextBau));
            if (name.equals(Hohlspiegel.XML_HOHLSPIEGEL)) bauelementHinzufuegen(new Hohlspiegel(this, nextBau));
            if (name.equals(Spiegel.XML_SPIEGEL)) bauelementHinzufuegen(new Spiegel(this, nextBau));
        }
    }

    //Initialisiert die Optische Bank
    private void initialisiere(double zoom) {
        setLayout(new BorderLayout());
        setOpaque(true);

        this.zoom = zoom;

        zeichenBrett = new Zeichenbrett(this);

        zeichenRahmen = new JScrollPane(zeichenBrett);
        groesse = new Point(2000, 600);
        zeichenBrett.setPreferredSize(new Dimension(groesse.x, groesse.y));
        this.add(zeichenRahmen, BorderLayout.CENTER);

        optischeAchse = new OptischeAchse(250);
        zeichenBrett.neuesZeichenObjekt(optischeAchse);

        eigenschaften = new Eigenschaften();
        this.add(eigenschaften, BorderLayout.SOUTH);

        bauelemente = new ArrayList<>();
        lichtquellen = new ArrayList<>();
        kollisionsObjekte = new ArrayList<>();

        aktionsListe = new AktionsListe();

        werkzeugWechseln(new Werkzeug_Auswahl(this));

    }

    /**
     * Wechselt das Werkzeug der Optischen Bank. Ruft auswahlAufheben() des alten und auswaehlen() des neuen Werkzeugs auf.
     *
     * @param neuesWerkzeug Neues Werkzeug
     * @see Werkzeug
     */
    public void werkzeugWechseln(Werkzeug neuesWerkzeug) {
        if (aktuellesWerkzeug != null) {
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

    /**
     * Fügt ein neues Bauelement hinzu.
     *
     * @param bauelement Neues Bauelement
     */
    public void bauelementHinzufuegen(Bauelement bauelement) {
        //Fügt -je nach Typ- das Bauelement den Listen lichtquellen, kollisionsObjekte und dem zeichenBrett hinzu
        switch (bauelement.getTyp()) {
            case Bauelement.TYP_LAMPE:
                lichtquellen.add((Lichtquelle) bauelement);
                break;
            case Bauelement.TYP_LINSE:
                kollisionsObjekte.add((Linse) bauelement);
                break;
            case Bauelement.TYP_SPIEGEL:
                kollisionsObjekte.add((Hohlspiegel) bauelement);
                break;
            case Bauelement.TYP_SCHIRM:
                kollisionsObjekte.add((Schirm) bauelement);
                break;
            case Bauelement.TYP_BLENDE:
                kollisionsObjekte.add((Blende) bauelement);
                break;
            case Bauelement.TYP_AUGE:
                kollisionsObjekte.add((Auge) bauelement);
        }
        zeichenBrett.neuesZeichenObjekt(bauelement);
        bauelemente.add(bauelement);
    }

    /**
     * Löscht ein Bauelement aus der Optischen Bank.
     *
     * @param bauelement Zu Löschendes Bauelement
     */
    public void bauelementLoeschen(Bauelement bauelement) {
        zeichenBrett.zeichenObjektLoeschen(bauelement);
        kollisionsObjekte.remove(bauelement);
        bauelemente.remove(bauelement);
        lichtquellen.remove(bauelement);
    }

    /**
     * Führt eine neue Aktion in der Optischen Bank aus, fügt sie der Aktionsliste hinzu und aktualisiert die Optische Bank.
     *
     * @param aktion Aktion
     */
    public void neueAktionDurchfuehren(Aktion aktion) {
        aktionsListe.neueAktion(aktion);
        aktion.aktionDurchfuehren();
        aktualisieren();
    }

    /**
     * Überschreibt die letzte Aktion der Aktionsliste und führt die neue Aktion danach aus. Aktualisiert die Optische Bank
     *
     * @param aktion Aktion
     */
    public void letzteAktionUeberschreiben(Aktion aktion) {
        aktionsListe.letzteAktionUeberschreiben(aktion);
        aktion.aktionDurchfuehren();
        aktualisieren();
    }

    /**
     * Macht die zuletzt ausgeführte Aktion rückgängig und aktualisiert die Optische Bank
     */
    public void aktionRueckgaengig() {
        aktionsListe.undo();
        aktualisieren();
    }

    /**
     * Führt die zuletzt rückgängig gemachte Aktion erneut aus und aktualisiert die Optische Bank
     */
    public void aktionWiederholen() {
        aktionsListe.redo();
        aktualisieren();
    }

    /**
     * Berechnet die Strahlengänge von allen Lichtquellen neu
     */
    public void strahlenNeuBerechnen() {
        for (Lichtquelle cLicht : lichtquellen) {
            if (cLicht.isAktiv()) {
                for (Strahlengang cStrahl : cLicht.getStrahlengaenge()) {
                    einzelStrahlNeuBerechnen(cStrahl);
                }
            }
        }
    }

    /**
     * Berechnet einen einzigen Strahlengang neu, indem nacheinander alle möglichen Kollisionen mit Elementen der Liste kollisionsObjekte durchgegangen werden.
     * Der Strahlengang wird solange durch die jeweils erste Kollision mit einem KollisionsElement manipuliert, bis er seine maximale Anzahl an Teilstücken
     * (MAX_STRAHLLAENGE) erreicht hat oder keine Kollision mehr findet.
     *
     * @param strahlengang Zu Berechnender Strahlengang
     */
    public void einzelStrahlNeuBerechnen(Strahlengang strahlengang) {
        strahlengang.resetteStrahlengang();
        for (int i = 0; i < Konstanten.MAX_STRAHLLAENGE; i++) {
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
                //Strahlengang ist auf eine Fläche getroffen
                ersteKoll.kollisionDurchfuehren();
            } else {
                //Strahlengang ist auf keine Fläche mehr getroffen
                break;
            }
        }
    }

    /**
     * Aktualisiert die Optische Bank in einem neuen Thread um durch Aufruf vom EDT diesen nicht zu blockieren.
     * Es werden alle Strahlen neu Berechnet.
     * Anschließend werden alle Elemente durch den Aufruf von repaint neu gezeichnet.
     */
    public void aktualisieren() {
        Thread aktThread = new Thread() {
            @Override
            public void run() {
                aktualisierenThreadSicher();
            }
        };
        aktThread.run();
    }

    /*
    Aktualisiert die Optische Bank, indem ein mit synchronized abgesicherter Bereich betreten wird,
    der verhindert, dass mehrere Threads gleichzeitig die Optische Bank aktualisieren.
    */
    private synchronized void aktualisierenThreadSicher() {
        strahlenNeuBerechnen();
        this.repaint();
        zeichenRahmen.repaint();
    }

    /**
     * Erhöht die Vergrößerung der Optischen Bank
     */
    public void zoomStufeRein() {
        zoom *= (1 + Konstanten.ZOOM_STUFE);

        zeichenBrett.setPreferredSize(new Dimension((int) (groesse.getX() * zoom), (int) (groesse.getY() * zoom)));
        zeichenBrett.revalidate();
    }

    /**
     * Verringert die Vergrößerung der Optischen Bank
     */
    public void zoomStufeRaus() {
        zoom /= (1 + Konstanten.ZOOM_STUFE);

        zeichenBrett.setPreferredSize(new Dimension((int) (groesse.getX() * zoom), (int) (groesse.getY() * zoom)));
        zeichenBrett.revalidate();
    }

    /**
     * Setzt die Vergrößerung der Optischen Bank auf einen bestimmten Wert
     *
     * @param zoom Wert der Vergrößerung
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        zeichenBrett.setPreferredSize(new Dimension((int) (groesse.getX() * zoom), (int) (groesse.getY() * zoom)));
        zeichenBrett.revalidate();
    }

    /**
     * @return Optische Achse
     */
    public OptischeAchse getOptischeAchse() {
        return optischeAchse;
    }

    /**
     * @return Zeichenbrett
     */
    public Zeichenbrett getZeichenBrett() {
        return zeichenBrett;
    }

    /**
     * @return Modus
     */
    public int getModus() {
        return modus;
    }

    /**
     * @param modus Modus
     */
    public void setModus(int modus) {
        this.modus = modus;
    }

    /**
     * @return Liste aller Lichtquellen
     */
    public ArrayList<Lichtquelle> getLichtquellen() {
        return lichtquellen;
    }

    /**
     * @return Liste aller Bauelemente
     */
    public ArrayList<Bauelement> getBauelemente() {
        return bauelemente;
    }

    /**
     * @return Eigenschaften-Leiste
     */
    public Eigenschaften getEigenschaften() {
        return eigenschaften;
    }

    /**
     * @return Gibt an, ob virtuelle Strahlen und Bilder aktiviert wurden
     */
    public boolean isVirtuelleStrahlenAktiv() {
        return virtuelleStrahlenAktiv;
    }

    /**
     * @param virtuelleStrahlenAktiv Sollen virtuelle Strahlen und Bilder gezeichnet werden
     */
    public void setVirtuelleStrahlenAktiv(boolean virtuelleStrahlenAktiv) {
        this.virtuelleStrahlenAktiv = virtuelleStrahlenAktiv;
    }

    /**
     * @return Gibt den Pfad zu der Datei, von der die Optische Bank geladen/ gespeichert wurde. null, wenn die Optische Bank noch nicht gespeichert wurde.
     */
    public File getDateiPfad() {
        return dateiPfad;
    }

    /**
     * @param dateiPfad Neuer Datei Pfad
     */
    public void setDateiPfad(File dateiPfad) {
        this.dateiPfad = dateiPfad;
    }

    /**
     * @return Zoomfaktor
     */
    public double getZoom() {
        return zoom;
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = new Element(getXmlElementTyp());
        xmlElement.setAttribute(XML_MODUS, String.valueOf(modus));
        xmlElement.setAttribute(XML_VIRTUELLE_STRAHLEN, String.valueOf(virtuelleStrahlenAktiv));
        xmlElement.setAttribute(XML_ZOOM, String.valueOf(zoom));
        for (Bauelement cBau : bauelemente) {
            xmlElement.addContent(cBau.getXmlElement());
        }
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_OPTISCHEBANK;
    }
}
