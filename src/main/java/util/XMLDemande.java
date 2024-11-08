package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import model.Demande;
import model.Entrepot;
import model.Livraison;
import model.PointDeLivraison;

public class XMLDemande extends XMLReader {

    @Override
    public Demande parse(String filePath) {
        Demande demande = new Demande();

        try {
            // Charger le document XML en utilisant la méthode de la classe mère
            Document doc = loadDocument(filePath);
            if (doc == null) {
                System.out.println("Erreur lors du chargement du fichier XML des demandes de livraisons.");
                return null;
            }

            // Lire l'élément entrepôt
            NodeList entrepotList = doc.getElementsByTagName("entrepot");
            if (entrepotList.getLength() > 0) {
                try {
                    Element entrepotElement = (Element) entrepotList.item(0);
                    long adresseEntrepot = Long.parseLong(entrepotElement.getAttribute("adresse"));
                    String heureDepart = entrepotElement.getAttribute("heureDepart");

                    // Créer un objet Entrepot et l'ajouter à la demande
                    Entrepot entrepot = new Entrepot(adresseEntrepot, heureDepart);
                    demande.setEntrepot(entrepot);
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture de l'entrepôt : " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Lire les éléments livraison
            NodeList livraisonList = doc.getElementsByTagName("livraison");
            for (int i = 0; i < livraisonList.getLength(); i++) {
                try {
                    Element livraisonElement = (Element) livraisonList.item(i);
                    long adresseEnlevement = Long.parseLong(livraisonElement.getAttribute("adresseEnlevement"));
                    long adresseLivraison = Long.parseLong(livraisonElement.getAttribute("adresseLivraison"));
                    double dureeEnlevement = Double.parseDouble(livraisonElement.getAttribute("dureeEnlevement"));
                    double dureeLivraison = Double.parseDouble(livraisonElement.getAttribute("dureeLivraison"));

                    // Créer un objet Livraison et l'ajouter à la demande
                    Livraison livraison = new Livraison(adresseEnlevement, adresseLivraison, dureeEnlevement, dureeLivraison);
                    PointDeLivraison point = new PointDeLivraison(adresseLivraison, livraison);
                    demande.ajouterPointDeLivraison(point);
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture d'un élément livraison : " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur générale lors de l'analyse du fichier XML des demandes : " + e.getMessage());
            e.printStackTrace();
        }
        
        return demande;
    }
}
