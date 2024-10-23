package main.java.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import main.java.model.Intersection;
import main.java.model.Plan;
import main.java.model.Troncon;

public class XMLPlan extends XMLReader {

    @Override
    public Plan parse(String filePath) {
        // Charger le document XML en utilisant la méthode de la classe mère
        Document doc = loadDocument(filePath);
        if (doc == null) {
            System.out.println("Erreur lors du chargement du fichier XML de plan de ville.");
            return null;
        }

        Plan plan = new Plan();

        // Lecture des noeuds (intersections)
        NodeList noeudListe = doc.getElementsByTagName("noeud");
        for (int i = 0; i < noeudListe.getLength(); i++) {
            Element noeudElement = (Element) noeudListe.item(i);
            Long id = Long.parseLong(noeudElement.getAttribute("id"));
            Double latitude = Double.parseDouble(noeudElement.getAttribute("latitude"));
            Double longitude = Double.parseDouble(noeudElement.getAttribute("longitude"));

            Intersection inter = new Intersection(id,latitude,longitude);

            plan.ajouterIntersection(inter);
            
        }
        
        // Lecture des tronçons (segments de route)
        NodeList tronconList = doc.getElementsByTagName("troncon");
        for (int i = 0; i < tronconList.getLength(); i++) {
            Element tronconElement = (Element) tronconList.item(i);
            long idOrigine = Long.parseLong(tronconElement.getAttribute("origine"));
            long idDestination = Long.parseLong(tronconElement.getAttribute("destination"));
            double longueur = Double.parseDouble(tronconElement.getAttribute("longueur"));
            String nomRue = tronconElement.getAttribute("nomRue");

            System.out.println("Tronçon: " + idOrigine + " -> " + idDestination + ", Longueur: " + longueur + "m, Rue: " + nomRue);

            Intersection interOrigine = plan.chercherIntersectionParId(idOrigine);
            Intersection interDestination = plan.chercherIntersectionParId(idDestination);
            Troncon troncon = new Troncon(interOrigine,interDestination,longueur,nomRue);
            
            plan.ajouterTroncon(troncon);
            interOrigine.ajouterTroncon(troncon);
        }
        return plan;   
        
    }
}
