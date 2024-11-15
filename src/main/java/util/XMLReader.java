package util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import exceptions.IDIntersectionException;

import java.io.File;

/**
 * Classe abstraite fournissant des méthodes utilitaires pour lire et analyser des fichiers XML.
 * Les classes dérivées doivent implémenter la méthode abstraite {@link #parse(String)} pour définir
 * une logique spécifique d'analyse.
 */
public abstract class XMLReader {

    /**
     * Charge et analyse un document XML à partir du chemin de fichier donné.
     * Cette méthode est commune et permet de charger un document XML en mémoire sous forme d'objet {@link Document}.
     * 
     * @param filePath Le chemin absolu ou relatif du fichier XML à charger.
     * @return Un objet {@link Document} représentant le contenu du fichier XML.
     *         Retourne {@code null} en cas d'erreur de chargement.
     */
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

    /**
     * Méthode abstraite que les classes dérivées doivent implémenter pour analyser un fichier XML.
     *
     * @param filePath Le chemin du fichier XML à analyser.
     * @param <T> Le type de l'objet résultant de l'analyse du fichier XML.
     * @return Un objet de type {@code T} représentant les données analysées.
     * @throws NumberFormatException Si un attribut numérique dans le fichier XML est mal formaté ou manquant.
     * @throws IDIntersectionException Si une intersection requise dans le fichier XML est introuvable ou incorrecte.
     */
    public abstract <T> T parse(String filePath) throws NumberFormatException, IDIntersectionException;
}