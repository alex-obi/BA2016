package de.hhu.alobe.ba2016.editor.eigenschaften;

public interface ReglerEvent {

    void reglerWurdeVerschoben(double wert);

    double berechneReglerWert(double reglerProzent, double minimum, double maximum);

    double berechneReglerProzent(double wert, double minimum, double maximum);

    String berechnePhysikalischenWert(double zahl);
}
