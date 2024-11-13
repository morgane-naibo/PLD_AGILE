package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Intersection;
import model.Plan;
import model.Troncon;
import exceptions.IDIntersectionException;

public class XMLPlan extends XMLReader {

    @Override
    public Plan parse(String filePath) {
        Plan plan = new Plan();

        try {
            Document doc = loadDocument(filePath);
            if (doc == null) {
                throw new RuntimeException("Erreur lors du chargement du fichier XML de plan de ville.");
            }

            // Lecture des noeuds (intersections)
            NodeList noeudListe = doc.getElementsByTagName("noeud");
            for (int i = 0; i < noeudListe.getLength(); i++) {
                Element noeudElement = (Element) noeudListe.item(i);
                if (!noeudElement.hasAttribute("id") || !noeudElement.hasAttribute("latitude") || !noeudElement.hasAttribute("longitude")) {
                    throw new NumberFormatException("Un attribut est manquant pour une intersection.");
                }

                long id = Long.parseLong(noeudElement.getAttribute("id"));

                // Validation de la latitude
                double latitude = Double.parseDouble(noeudElement.getAttribute("latitude"));
                if (latitude < -90 || latitude > 90) {
                    throw new NumberFormatException("La latitude doit être comprise entre -90 et 90.");
                }

                // Validation de la longitude
                double longitude = Double.parseDouble(noeudElement.getAttribute("longitude"));
                if (longitude < -180 || longitude > 180) {
                    throw new NumberFormatException("La longitude doit être comprise entre -180 et 180.");
                }

                Intersection inter = new Intersection(id, latitude, longitude);
                plan.ajouterIntersection(inter);
            }

            // Lecture des tronçons (segments de route)
            NodeList tronconList = doc.getElementsByTagName("troncon");
            for (int i = 0; i < tronconList.getLength(); i++) {
                Element tronconElement = (Element) tronconList.item(i);
                if (!tronconElement.hasAttribute("origine") || !tronconElement.hasAttribute("destination") || !tronconElement.hasAttribute("longueur")) {
                    throw new NumberFormatException("Un attribut est manquant pour un tronçon.");
                }

                long idOrigine = Long.parseLong(tronconElement.getAttribute("origine"));
                long idDestination = Long.parseLong(tronconElement.getAttribute("destination"));
                double longueur = Double.parseDouble(tronconElement.getAttribute("longueur"));
                String nomRue = tronconElement.getAttribute("nomRue");

                // Vérification si la longueur est négative
                if (longueur < 0) {
                    throw new IllegalArgumentException("La longueur du tronçon ne peut pas être négative.");
                }

                // Vérification si le nom de rue est manquant ou invalide
             /*   if (nomRue == null || nomRue.trim().isEmpty()) {
                    throw new IllegalArgumentException("Le nom de rue ne peut pas être vide.");
                }*/ 

                Intersection interOrigine = plan.chercherIntersectionParId(idOrigine);
                Intersection interDestination = plan.chercherIntersectionParId(idDestination);

                if (interOrigine != null && interDestination != null) {
                    Troncon troncon = new Troncon(interOrigine, interDestination, longueur, nomRue);
                    plan.ajouterTroncon(troncon);
                    interOrigine.ajouterTroncon(troncon);
                } else {
                    throw new IDIntersectionException("Intersection(s) non trouvée(s) pour le tronçon " + idOrigine + " -> " + idDestination);
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Erreur de format : " + e.getMessage());
            throw e;  // Ne pas encapsuler la NumberFormatException, laissez-la se propager pour permettre au test de la vérifier
        } catch (Exception e) {
            System.out.println("Erreur générale lors de l'analyse du fichier XML de plan : " + e.getMessage());
            throw new RuntimeException(e);  // Encapsuler les autres exceptions dans une RuntimeException
        }

        return plan;
    }
}
