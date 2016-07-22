package de.hhu.alobe.ba2016.physik.elemente.Auge;

import de.hhu.alobe.ba2016.editor.OptischeBank;
import de.hhu.alobe.ba2016.grafik.OptischeAchse;
import de.hhu.alobe.ba2016.mathe.Vektor;
import de.hhu.alobe.ba2016.mathe.VektorFloat;
import de.hhu.alobe.ba2016.mathe.VektorInt;
import de.hhu.alobe.ba2016.physik.elemente.Bauelement;
import de.hhu.alobe.ba2016.physik.elemente.Rahmen;
import de.hhu.alobe.ba2016.physik.strahlen.KannKollision;
import de.hhu.alobe.ba2016.physik.strahlen.StrahlenKollision;
import de.hhu.alobe.ba2016.physik.strahlen.Strahlengang;

import java.awt.*;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;

public class Auge extends Bauelement implements KannKollision {

    private Hornhaut hornhaut;

    private Augenlinse augenlinse;

    private Netzhaut netzhaut;

    public Auge(OptischeBank optischeBank, Vektor mittelpunkt) {
        super(optischeBank, mittelpunkt, Bauelement.TYP_AUGE);
        hornhaut = new Hornhaut(this, Vektor.addiere(mittelpunkt, new VektorFloat(-40, 0)), 60);
        augenlinse = new Augenlinse(this, mittelpunkt.kopiere(), 1.4, 120, 150);
        netzhaut = new Netzhaut(this, Vektor.addiere(mittelpunkt, new VektorFloat(200, 0)), 120);

        Rahmen rahmen = new Rahmen(mittelPunkt);
        rahmen.rahmenErweitern(new VektorInt(-45, 80));
        rahmen.rahmenErweitern(new VektorInt(+205 , 80));
        rahmen.rahmenErweitern(new VektorInt(+205, -80));
        rahmen.rahmenErweitern(new VektorInt(-45, -80));
        setRahmen(rahmen);

    }

    @Override
    public void verschiebeUm(Vektor verschiebung) {
        mittelPunkt.addiere(verschiebung);
        augenlinse.verschiebeUm(verschiebung);
        hornhaut.verschiebeUm(verschiebung);
        netzhaut.verschiebeUm(verschiebung);
    }

    @Override
    public void waehleAus() {

    }

    @Override
    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        hornhaut.paintComponent(g);
        augenlinse.paintComponent(g);
        netzhaut.paintComponent(g);
    }

    @Override
    public StrahlenKollision kollisionUeberpruefen(Strahlengang cStrGng) {
        ArrayList<StrahlenKollision> kollisionen = new ArrayList();
        kollisionen.add(hornhaut.kollisionUeberpruefen(cStrGng));
        kollisionen.add(augenlinse.kollisionUeberpruefen(cStrGng));
        kollisionen.add(netzhaut.kollisionUeberpruefen(cStrGng));
        return StrahlenKollision.getErsteKollision(kollisionen);
    }
}
