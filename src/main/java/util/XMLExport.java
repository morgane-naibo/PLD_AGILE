package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Demande;
import model.PointDeLivraison;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

/**
 * Classe utilitaire pour exporter une demande de livraisons en un fichier XML.
 * Permet de générer un fichier XML à partir des données d'un objet {@link Demande}.
 */
public class XMLExport {

    /**
     * Exporte une demande de livraisons dans un fichier XML au chemin spécifié.
     *
     * @param demande  L'objet {@link Demande} contenant les informations à exporter.
     * @param filePath Le chemin du fichier dans lequel écrire le contenu XML.
     * @throws Exception si une erreur survient lors de la création ou de l'écriture du fichier XML.
     */
    public void exportDemande(Demande demande, String filePath) {
        try {
            // Initialisation du document XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            // Racine <demandeDeLivraisons>
            Element rootElement = doc.createElement("demandeDeLivraisons");
            doc.appendChild(rootElement);
            
            // Entrepôt (hardcodé pour l'instant)
            Element entrepotElement = doc.createElement("entrepot");
            entrepotElement.setAttribute("adresse", String.valueOf(demande.getEntrepot().getId())); // Adresse hardcodée
            entrepotElement.setAttribute("heureDepart", "8:0:0"); // Heure de départ hardcodée
            rootElement.appendChild(entrepotElement);
            
            // Ajout des points de livraison
            List<PointDeLivraison> pointsDeLivraison = demande.getListePointDeLivraison();
            for (PointDeLivraison point : pointsDeLivraison) {
                Element livraisonElement = doc.createElement("livraison");
                livraisonElement.setAttribute("adresseLivraison", String.valueOf(point.getId()));
                livraisonElement.setAttribute("adresseEnlevement", "0");
                livraisonElement.setAttribute("dureeEnlevement", "0");
                livraisonElement.setAttribute("dureeLivraison", "0");
                
                rootElement.appendChild(livraisonElement);
            }
            
            // Création du fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            System.out.println(source);
            StreamResult result = new StreamResult(new File(filePath));
            
            transformer.transform(source, result);
            
            System.out.println("Fichier XML généré avec succès à l'emplacement : " + filePath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
