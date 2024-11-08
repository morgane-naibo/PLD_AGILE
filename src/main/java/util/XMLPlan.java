package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Intersection;
import model.Plan;
import model.Troncon;
import exceptions.XMLParsingException;

public class XMLPlan extends XMLReader {
@Override
public Plan parse(String filePath) {
    Plan plan = new Plan();
    try {
        // Charger le document XML
        Document doc = loadDocument(filePath);

        // Lecture des noeuds (intersections)
        NodeList noeudListe = doc.getElementsByTagName("noeud");
        for (int i = 0; i < noeudListe.getLength(); i++) {
            try {
                Element noeudElement = (Element) noeudListe.item(i);
                Long id = Long.parseLong(noeudElement.getAttribute("id"));
                Double latitude = Double.parseDouble(noeudElement.getAttribute("latitude"));
                Double longitude = Double.parseDouble(noeudElement.getAttribute("longitude"));

                Intersection inter = new Intersection(id, latitude, longitude);
                plan.ajouterIntersection(inter);
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture d'une intersection : " + e.getMessage());
            }
        }

        // Lecture des tronçons (segments de route)
        NodeList tronconList = doc.getElementsByTagName("troncon");
        for (int i = 0; i < tronconList.getLength(); i++) {
            try {
                Element tronconElement = (Element) tronconList.item(i);
                long idOrigine = Long.parseLong(tronconElement.getAttribute("origine"));
                long idDestination = Long.parseLong(tronconElement.getAttribute("destination"));
                double longueur = Double.parseDouble(tronconElement.getAttribute("longueur"));
                String nomRue = tronconElement.getAttribute("nomRue");

                Intersection interOrigine = plan.chercherIntersectionParId(idOrigine);
                Intersection interDestination = plan.chercherIntersectionParId(idDestination);

                if (interOrigine != null && interDestination != null) {
                    Troncon troncon = new Troncon(interOrigine, interDestination, longueur, nomRue);
                    plan.ajouterTroncon(troncon);
                    interOrigine.ajouterTroncon(troncon);
                } else {
                    System.err.println("Intersection(s) non trouvée(s) pour le tronçon " + idOrigine + " -> " + idDestination);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture d'un tronçon : " + e.getMessage());
            }
        }
    } catch (Exception e) {
        System.err.println("Erreur lors du chargement du document XML : " + e.getMessage());
    }
    return plan;
}
}
