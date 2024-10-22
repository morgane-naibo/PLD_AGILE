package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.io.File;

public abstract class XMLReader {

    // Méthode commune pour charger un document XML
    protected Document loadDocument(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            // Normaliser le document (recommandé pour une lecture cohérente)
            doc.getDocumentElement().normalize();
            
            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode abstraite que les classes dérivées doivent implémenter
    public abstract <T> T parse(String filePath);
}
