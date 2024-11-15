package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import model.Demande;
import model.Entrepot;
import model.Livraison;
import model.PointDeLivraison;

/**
 * Classe utilitaire pour analyser les fichiers XML contenant des demandes de livraison.
 * Cette classe hérite de {@link XMLReader} pour fournir des méthodes spécifiques au format des demandes.
 */
public class XMLDemande extends XMLReader {

    /**
     * Analyse un fichier XML et extrait les informations pour créer un objet {@link Demande}.
     *
     * @param filePath Chemin vers le fichier XML à analyser.
     * @return Un objet {@link Demande} contenant les informations extraites du fichier.
     * @throws RuntimeException si une erreur survient lors du chargement ou de l'analyse du fichier.
     */
    @Override
    public Demande parse(String filePath) {
        Demande demande = new Demande();

        try {
            // Charger le document XML en utilisant la méthode de la classe mère
            Document doc = loadDocument(filePath);
            
            if (doc == null) {
                throw new RuntimeException("Erreur lors du chargement du fichier XML des demandes de livraisons.");
            }

            // Lire l'élément entrepôt
            NodeList entrepotList = doc.getElementsByTagName("entrepot");
            if (entrepotList.getLength() > 0) {
                try {
                    Element entrepotElement = (Element) entrepotList.item(0);
                    if (!entrepotElement.hasAttribute("adresse") || !entrepotElement.hasAttribute("heureDepart")) {
                        throw new NumberFormatException("Un attribut est manquant pour l'entrepôt.");
                    }

                    long adresseEntrepot = Long.parseLong(entrepotElement.getAttribute("adresse"));
                    String heureDepart = entrepotElement.getAttribute("heureDepart");

                    // Créer un objet Entrepot et l'ajouter à la demande
                    Entrepot entrepot = new Entrepot(adresseEntrepot, heureDepart);
                    demande.setEntrepot(entrepot);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur lors de la lecture de l'entrepôt : " + e.getMessage());
                    throw e;  // Re-lancer l'exception pour permettre au test de la vérifier
                }
            }

            // Lire les éléments livraison
            NodeList livraisonList = doc.getElementsByTagName("livraison");
            for (int i = 0; i < livraisonList.getLength(); i++) {
                try {
                    Element livraisonElement = (Element) livraisonList.item(i);
                    if (!livraisonElement.hasAttribute("adresseEnlevement") || !livraisonElement.hasAttribute("adresseLivraison") ||
                        !livraisonElement.hasAttribute("dureeEnlevement") || !livraisonElement.hasAttribute("dureeLivraison")) {
                        throw new NumberFormatException("Un attribut est manquant pour une livraison.");
                    }

                    long adresseEnlevement = Long.parseLong(livraisonElement.getAttribute("adresseEnlevement"));
                    long adresseLivraison = Long.parseLong(livraisonElement.getAttribute("adresseLivraison"));
                    double dureeEnlevement = Double.parseDouble(livraisonElement.getAttribute("dureeEnlevement"));
                    double dureeLivraison = Double.parseDouble(livraisonElement.getAttribute("dureeLivraison"));

                    // Créer un objet Livraison et l'ajouter à la demande
                    Livraison livraison = new Livraison(adresseEnlevement, adresseLivraison, dureeEnlevement, dureeLivraison);
                    PointDeLivraison point = new PointDeLivraison(adresseLivraison, livraison);
                    demande.ajouterPointDeLivraison(point);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur lors de la lecture d'un élément livraison : " + e.getMessage());
                    throw e;  // Re-lancer l'exception pour permettre au test de la vérifier
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de l'analyse du fichier XML des demandes : " + e.getMessage());
            throw e;  // Re-lancer l'exception pour permettre au test de la vérifier
        }
        
        return demande;
    }
}
