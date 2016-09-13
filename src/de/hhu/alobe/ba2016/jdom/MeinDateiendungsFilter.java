package de.hhu.alobe.ba2016.jdom;


import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Filter um Dateien mit entsprechender Endung zu filtern.
 */
public class MeinDateiendungsFilter extends FileFilter {

    //Dateiendung, nach der gefiltert werden soll.
    private String dateiEndung;

    /**
     * Initialisiert neuen Filter mit übergebener Dateiendung.
     *
     * @param dateiEndung Dateiendung, nach der gefiltert werden soll.
     */
    public MeinDateiendungsFilter(String dateiEndung) {
        this.dateiEndung = dateiEndung;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        String endung = Dateifunktionen.getDateiEndung(pathname);
        if (endung != null) {
            return (endung.equals(dateiEndung));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
