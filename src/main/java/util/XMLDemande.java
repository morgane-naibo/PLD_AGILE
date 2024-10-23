package main.java.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import main.java.model.Demande;
import main.java.model.Entrepot;
import main.java.model.Livraison;
import main.java.model.PointDeLivraison;

public class XMLDemande extends XMLReader {

    @Override
    public Demande parse(String filePath) {
        // Charger le document XML en utilisant la méthode de la classe mère
        Document doc = loadDocument(filePath);
        if (doc == null) {
            System.out.println("Erreur lors du chargement du fichier XML des demandes de livraisons.");
            return null;
        }

        // Créer l'objet DemandeLivraison pour stocker les données
        Demande demande = new Demande();

        // Lire l'élément entrepôt
        NodeList entrepotList = doc.getElementsByTagName("entrepot");
        if (entrepotList.getLength() > 0) {
            Element entrepotElement = (Element) entrepotList.item(0);
            long adresseEntrepot = Long.parseLong(entrepotElement.getAttribute("adresse"));
            String heureDepart = entrepotElement.getAttribute("heureDepart");

            // Créer un objet Entrepot et l'ajouter à la demande
            Entrepot entrepot = new Entrepot(adresseEntrepot, heureDepart);
            demande.setEntrepot(entrepot);
        }
        
        // Lire les éléments livraison
        NodeList livraisonList = doc.getElementsByTagName("livraison");
        for (int i = 0; i < livraisonList.getLength(); i++) {
            Element livraisonElement = (Element) livraisonList.item(i);
            long adresseEnlevement = Long.parseLong(livraisonElement.getAttribute("adresseEnlevement"));
            long adresseLivraison = Long.parseLong(livraisonElement.getAttribute("adresseLivraison"));
            double dureeEnlevement = Double.parseDouble(livraisonElement.getAttribute("dureeEnlevement"));
            double dureeLivraison = Double.parseDouble(livraisonElement.getAttribute("dureeLivraison"));

            // Créer un objet Livraison et l'ajouter à la demande
            Livraison livraison = new Livraison(adresseEnlevement, adresseLivraison, dureeEnlevement, dureeLivraison);
            PointDeLivraison point = new PointDeLivraison(adresseLivraison, livraison);
            demande.ajouterPointDeLivraison(point);
        }
        
        return demande;
    }
}