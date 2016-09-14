package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Punktförmige Lichtquelle.
 */
public class PunktLichtquelle extends Lichtquelle {

    /**
     * Name des Bauelements
     */
    public static final String NAME = "Punktlichtquelle";

    /**
     * Name der Punktlichtquelle im XML-Dokument.
     */
    public static final String XML_PUNKTLICHT = "punkt_licht";

    //Durchmesser der grafischen Darstellung
    public static double groesse = 20;

    //Eigenschaftenregler zur Manipulation der Werte der Punktlichtquelle durch den Benutzer:
    private JComboBox farben_box;
    private JCheckBox anAus;

    /**
     * Initialisiere Punktlichtquelle mit einer Farbe.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param mittelpunkt  Mittelpunkt der Punktlichtquelle.
     * @param farbe        Farbe der Punktlichtquelle.
     */
    public PunktLichtquelle(OptischeBank optischeBank, Vektor mittelpunkt, Farbe farbe) {
        super(optischeBank, mittelpunkt, farbe);
        initialisiere();
    }

    /**
     * Initialisiere Punktlichtquelle mit einem jdom2.Element.
     *
     * @param optischeBank Referenz auf Optische Bank.
     * @param xmlElement   jdom2.Element, das die benötigten Attribute enthält.
     * @throws Exception Exception, die geworfen wird, wenn bei der Initialisierung etwas schief läuft.
     */
    public PunktLichtquelle(OptischeBank optischeBank, Element xmlElement) throws Exception {
        super(optischeBank, xmlElement);
        initialisiere();
    }

    //Initialisiere die Werte der Punktlichtquelle
    private void initialisiere() {
        formatAktualisieren();

        anAus = new JCheckBox("Lampe aktiv", isAktiv());
        anAus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (anAus.isSelected()) {
                    setAktiv(true);
                } else {
                    setAktiv(false);
                }
                optischeBank.aktualisieren();
            }
        });

        farben_box = new JComboBox(Farbe.farbenpalette.keySet().toArray());
        farben_box.setSelectedItem(Farbe.gibFarbenName(getFarbe()));
        farben_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFarbe(Farbe.farbenpalette.get(farben_box.getSelectedItem()));
                optischeBank.aktualisieren();
            }
        });
    }

    //Aktualisiere das Format der Punktlichtquelle
    private void formatAktualisieren() {
        setRahmen(generiereRahmen());
    }

    @Override
    public Strahlengang berechneNeuenStrahl(Vektor strahlPunkt) {
        Vektor richtungsPunkt = strahlPunkt.kopiere();
        Vektor richtungsVektor = Vektor.subtrahiere(richtungsPunkt, mittelPunkt);
        return new Strahlengang(new Strahl(mittelPunkt.kopiere(), richtungsVektor));
    }

    @Override
    public Rahmen generiereRahmen() {
        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new Point2D.Double(-groesse / 2, -groesse / 2));
        rahmen.rahmenErweitern(new Point2D.Double(groesse / 2, -groesse / 2));
        rahmen.rahmenErweitern(new Point2D.Double(groesse / 2, groesse / 2));
        rahmen.rahmenErweitern(new Point2D.Double(-groesse / 2, groesse / 2));
        return rahmen;
    }

    @Override
    public Eigenschaftenregler[] gibEigenschaftenregler() {
        Eigenschaftenregler[] komponenten = new Eigenschaftenregler[2];
        komponenten[0] = new Eigenschaftenregler("", anAus);
        komponenten[1] = new Eigenschaftenregler("Farbe", farben_box);
        return komponenten;
    }

    @Override
    public String gibBauelementNamen() {
        return NAME;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setColor(Color.BLACK);
        Arc2D zeichenKreis = new Arc2D.Double(mittelPunkt.getX() - groesse / 2, mittelPunkt.getY() - groesse / 2, groesse, groesse, 0, 360, Arc2D.OPEN);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(zeichenKreis);
        g.setColor(farbe);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(new Line2D.Double(mittelPunkt.getX(), optischeBank.getOptischeAchse().getHoehe(), mittelPunkt.getX(), mittelPunkt.getY()));
        super.paintComponent(g);
    }

    @Override
    public Element getXmlElement() {
        Element xmlElement = super.getXmlElement();
        return xmlElement;
    }

    @Override
    public String getXmlElementTyp() {
        return XML_PUNKTLICHT;
    }

}
