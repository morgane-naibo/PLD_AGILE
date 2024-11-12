package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import model.Intersection;
import model.Plan;
import model.Troncon;

public class XMLPlan extends XMLReader {

    @Override
    public Plan parse(String filePath) {
        Plan plan = new Plan();

        try {
            // Charger le document XML
            Document doc = loadDocument(filePath);
            if (doc == null) {
                System.out.println("Erreur lors du chargement du fichier XML de plan de ville.");
                return null;
            }

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
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de format pour une intersection, vérifiez les attributs id, latitude ou longitude.");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Erreur inattendue lors de la lecture d'une intersection : " + e.getMessage());
                    e.printStackTrace();
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

                    //System.out.println("Tronçon: " + idOrigine + " -> " + idDestination + ", Longueur: " + longueur + "m, Rue: " + nomRue);

                    Intersection interOrigine = plan.chercherIntersectionParId(idOrigine);
                    Intersection interDestination = plan.chercherIntersectionParId(idDestination);

                    if (interOrigine != null && interDestination != null) {
                        Troncon troncon = new Troncon(interOrigine, interDestination, longueur, nomRue);
                        plan.ajouterTroncon(troncon);
                        interOrigine.ajouterTroncon(troncon);
                    } else {
                        System.out.println("Erreur : Intersection(s) non trouvée(s) pour le tronçon " + idOrigine + " -> " + idDestination);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Erreur de format pour un tronçon, vérifiez les attributs origine, destination ou longueur.");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Erreur inattendue lors de la lecture d'un tronçon : " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur générale lors de l'analyse du fichier XML de plan : " + e.getMessage());
            e.printStackTrace();
        }

        return plan;
    }
}
