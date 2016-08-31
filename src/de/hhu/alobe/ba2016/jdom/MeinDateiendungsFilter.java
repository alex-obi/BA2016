package de.hhu.alobe.ba2016.jdom;

import de.hhu.alobe.ba2016.Konstanten;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MeinDateiendungsFilter extends FileFilter {

    private String dateiEndung;

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
