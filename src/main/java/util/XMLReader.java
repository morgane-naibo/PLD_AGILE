package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import exceptions.IDIntersectionException;

import java.io.File;


public abstract class XMLReader {

    // Méthode commune pour charger un document XML
    protected Document loadDocument(String filePath) {
        Document doc = null;
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(xmlFile);
            
            // Normaliser le document (recommandé pour une lecture cohérente)
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du document XML : " + e.getMessage());
            e.printStackTrace();
        }

        return doc;
    }

    // Méthode abstraite que les classes dérivées doivent implémenter
    public abstract <T> T parse(String filePath) throws NumberFormatException, IDIntersectionException;
}
