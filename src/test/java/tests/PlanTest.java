package tests;

import model.Plan;
import model.Intersection;
import model.Troncon;
import model.Etape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import util.XMLPlan;

import java.util.List;

public class PlanTest {
    
    private Plan plan;

    @BeforeEach
    public void setUp() {
        // Initialisation du plan avant chaque test
        plan = new Plan();
    }

    @Test
    public void testAjouterIntersection() {
        Intersection intersection = new Intersection(1, 45.754, 4.831);
        plan.ajouterIntersection(intersection);
        
        List<Intersection> intersections = plan.getListeIntersections();
        assertEquals(1, intersections.size());
        assertTrue(intersections.contains(intersection));
    }

    @Test
    public void testAjouterTroncon() {
        Intersection origine = new Intersection(1, 45.754, 4.831);
        Intersection destination = new Intersection(2, 45.755, 4.832);
        Troncon troncon = new Troncon(origine, destination, 100.0, "Rue A");

        plan.ajouterIntersection(origine);
        plan.ajouterIntersection(destination);
        plan.ajouterTroncon(troncon);

        List<Troncon> troncons = plan.getListeTroncons();
        assertEquals(1, troncons.size());
        assertTrue(troncons.contains(troncon));
    }

    @Test
    public void testChercherIntersectionParId() {
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        
        plan.ajouterIntersection(intersection1);
        plan.ajouterIntersection(intersection2);
        
        Intersection result = plan.chercherIntersectionParId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        
        Intersection resultNull = plan.chercherIntersectionParId(3);
        assertNull(resultNull);
    }

    @Test
    public void testChercherPlusCourtChemin() {
        // Créer des intersections pour tester l'algorithme
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        Intersection intersection3 = new Intersection(3, 45.756, 4.833);
        Intersection intersection4 = new Intersection(4, 45.757, 4.834); // Nouvelle intersection
    
        // Créer des tronçons entre les intersections
        Troncon troncon1 = new Troncon(intersection1, intersection2, 50.0, "Rue A");
        Troncon troncon2 = new Troncon(intersection2, intersection3, 30.0, "Rue B");
        Troncon troncon3 = new Troncon(intersection1, intersection3, 100.0, "Rue C");
        Troncon troncon4 = new Troncon(intersection3, intersection4, 40.0, "Rue D"); // Nouveau tronçon
        Troncon troncon5 = new Troncon(intersection2, intersection4, 60.0, "Rue E"); // Nouveau tronçon
    
        // Ajouter les intersections
        plan.ajouterIntersection(intersection1);
        plan.ajouterIntersection(intersection2);
        plan.ajouterIntersection(intersection3);
        plan.ajouterIntersection(intersection4); // Ajout de la nouvelle intersection
    
        // Ajouter les tronçons
        plan.ajouterTroncon(troncon1);
        plan.ajouterTroncon(troncon2);
        plan.ajouterTroncon(troncon3);
        plan.ajouterTroncon(troncon4); // Ajout du nouveau tronçon
        plan.ajouterTroncon(troncon5); // Ajout du nouveau tronçon
    
        // Associer les tronçons aux intersections correspondantes
        intersection1.ajouterTroncon(troncon1);
        intersection1.ajouterTroncon(troncon3);
        intersection2.ajouterTroncon(troncon1);
        intersection2.ajouterTroncon(troncon2);
        intersection2.ajouterTroncon(troncon5);
        intersection3.ajouterTroncon(troncon2);
        intersection3.ajouterTroncon(troncon3);
        intersection3.ajouterTroncon(troncon4);
        intersection4.ajouterTroncon(troncon4);
        intersection4.ajouterTroncon(troncon5);
    
        // Rechercher le plus court chemin entre intersection1 et intersection4
        Etape etape = plan.chercherPlusCourtChemin(intersection1, intersection4);
        
        assertNotNull(etape);
        assertEquals(intersection1, etape.getDepart());
        assertEquals(intersection4, etape.getArrivee());
        assertEquals(110.0, etape.getLongueur(), 0.01);  // 50 + 30 + 40 = 120, mais le plus court chemin devrait être via intersection2 à 80.0
    }
    

    @Test
    public void testChargerPlanDepuisXML() {
        
        // Spécifiez le chemin du fichier XML ici
        String xmlFilePath = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlan.xml"; // Remplacez par le chemin réel
        XMLPlan xmlPlanReader = new XMLPlan();
        
        // Charger le plan depuis le fichier XML
        Plan planCharge = xmlPlanReader.parse(xmlFilePath);
        
        assertNotNull(planCharge);
        assertTrue(planCharge.getListeIntersections().size() > 0);
        assertTrue(planCharge.getListeTroncons().size() > 0);
        
        // Vous pouvez ajouter d'autres assertions pour vérifier les détails spécifiques du plan chargé
    }
}
