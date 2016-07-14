package de.hhu.alobe.ba2016.grafik;

import de.hhu.alobe.ba2016.Konstanten;
import de.hhu.alobe.ba2016.grafik.Zeichenbar;

import java.awt.*;

public class OptischeAchse implements Zeichenbar {

    private int hoehe;

    public OptischeAchse(int hoehe) {
        this.hoehe = hoehe;
    }

    public int getHoehe() {
        return hoehe;
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.setStroke(new BasicStroke(Konstanten.LINIENDICKE));
        g.drawLine(0,
                hoehe,
                10000,
                hoehe);
    }
}
