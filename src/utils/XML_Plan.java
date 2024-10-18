import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Intersection;
import model.Plan;

public class XML_Plan extends XML_Reader {

    @Override
    public void parse(String filePath) {
        // Charger le document XML en utilisant la méthode de la classe mère
        Document doc = loadDocument(filePath);
        if (doc == null) {
            System.out.println("Erreur lors du chargement du fichier XML de plan de ville.");
            return;
        }

        //Plan plan = new Plan();

        // Lecture des noeuds (intersections)
        NodeList noeudListe = doc.getElementsByTagName("noeud");
        for (int i = 0; i < noeudListe.getLength(); i++) {
            Element noeudElement = (Element) noeudListe.item(i);
            String id = noeudElement.getAttribute("id");
            String latitude = noeudElement.getAttribute("latitude");
            String longitude = noeudElement.getAttribute("longitude");

            System.out.println("Noeud ID: " + id + ", Latitude: " + latitude + ", Longitude: " + longitude);

            /*
            Intersection inter = new Intersection(id,latitude,longitude);

            plan.ajouterIntersection(inter);
            */
        }

        /*
        // Lecture des tronçons (segments de route)
        NodeList tronconList = doc.getElementsByTagName("troncon");
        for (int i = 0; i < tronconList.getLength(); i++) {
            Element tronconElement = (Element) tronconList.item(i);
            String idOrigine = tronconElement.getAttribute("origine");
            String idDestination = tronconElement.getAttribute("destination");
            String longueur = tronconElement.getAttribute("longueur");
            String nomRue = tronconElement.getAttribute("nomRue");

            System.out.println("Tronçon: " + idOrigine + " -> " + idDestination + ", Longueur: " + longueur + "m, Rue: " + nomRue);

            Intersection interOrigine = plan.chercherIntersectionById(idOrigine);
            Intersection interDestination = plan.chercherIntersectionById(idDestination);
            Troncon troncon = new Troncon(interOrigine,interDestination,longueur,nomRue);
            
            plan.ajouterTroncon(troncon);
            interOrigine.ajouterTroncon(troncon);
        }   
        */
    }
}
