package de.hhu.alobe.ba2016.physik.elemente.Licht;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.editor.eigenschaften.Eigenschaftenregler;
import de.hhu.alobe.ba2016.mathe.Strahl;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PunktLichtquelle extends Lichtquelle{

    public static double groesse = 20;

    public PunktLichtquelle(OptischeBank optischeBank, Vektor mittelpunkt, Color farbe) {
        super(optischeBank, mittelpunkt, farbe);
        setRahmen(generiereRahmen());
    }

    @Override
    public Strahlengang berechneNeuenStrahl(Vektor strahlPunkt) {
        Vektor richtungsPunkt = strahlPunkt.kopiere();
        Vektor richtungsVektor = Vektor.subtrahiere(richtungsPunkt, mittelPunkt);
        return new Strahlengang(new Strahl(mittelPunkt, richtungsVektor));
    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
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
    public void paintComponent(Graphics2D g) {
        g.setColor(farbe);
        Arc2D zeichenKreis = new Arc2D.Double(mittelPunkt.getX() - groesse / 2, mittelPunkt.getY() - groesse / 2, groesse, groesse, 0, 360, Arc2D.OPEN);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(zeichenKreis);
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.draw(new Line2D.Double(mittelPunkt.getX(), optischeBank.getOptischeAchse().getHoehe(), mittelPunkt.getX(), mittelPunkt.getY()));
        super.paintComponent(g);
    }

    @Override
    public void waehleAus() {
        super.waehleAus();

        ArrayList<Eigenschaftenregler> regler = new ArrayList<>();

        JComboBox farben_box = new JComboBox(Farbe.farbenpalette.keySet().toArray());
        farben_box.setSelectedItem(Farbe.gibFarbenName(getFarbe()));
        farben_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFarbe(Farbe.farbenpalette.get(farben_box.getSelectedItem()));
                optischeBank.aktualisieren();
            }
        });
        regler.add(new Eigenschaftenregler("Farbe", farben_box));

        JCheckBox anAus = new JCheckBox("Lampe aktiv", isAktiv());
        anAus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(anAus.isSelected()) {
                    setAktiv(true);
                } else {
                    setAktiv(false);
                }
                optischeBank.aktualisieren();
            }
        });
        regler.add(new Eigenschaftenregler("", anAus));

        optischeBank.getEigenschaften().setOptionen("Punktlichtquelle", regler);

    }

}
