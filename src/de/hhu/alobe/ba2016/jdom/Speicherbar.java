package de.hhu.alobe.ba2016.jdom;

import org.jdom2.Element;

/**
 * Interface um Methoden zur Verfügung zu stellen, damit Elemente ihre Werte als jdom2.Element auszugeben.
 */
public interface Speicherbar {

    /**
     * jdom2.Element, dass die Attribute dieser Klasse speichert. Die Klasse muss auch über dieses Element initialisiert werden können.
     *
     * @return Element mit Attributen der implementierenden Klasse.
     */
    Element getXmlElement();

    /**
     * Name des Elements im XML-Dokument. Namen sollten nicht doppelt vergeben werden.
     *
     * @return Name des Elements in XML.
     */
    String getXmlElementTyp();
}
