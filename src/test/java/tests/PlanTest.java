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
        // Créer des intersections et tronçons pour tester l'algorithme
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        Intersection intersection3 = new Intersection(3, 45.756, 4.833);
        
        Troncon troncon1 = new Troncon(intersection1, intersection2, 50.0, "Rue A");
        Troncon troncon2 = new Troncon(intersection2, intersection3, 30.0, "Rue B");
        Troncon troncon3 = new Troncon(intersection1, intersection3, 100.0, "Rue C");
        
        plan.ajouterIntersection(intersection1);
        plan.ajouterIntersection(intersection2);
        plan.ajouterIntersection(intersection3);
        
        plan.ajouterTroncon(troncon1);
        plan.ajouterTroncon(troncon2);
        plan.ajouterTroncon(troncon3);

        // Rechercher le plus court chemin entre intersection1 et intersection3
        Etape etape = plan.chercherPlusCourtChemin(intersection1, intersection3);
        
        assertNotNull(etape);
        assertEquals(intersection1, etape.getDepart());
        assertEquals(intersection3, etape.getArrivee());
        assertEquals(80.0, etape.getLongueur(), 0.01);  // 50 + 30 = 80
    }

    @Test
    public void testChargerPlanDepuisXML() {
        String xmlFilePath = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";

        XMLPlan xmlPlanReader = new XMLPlan();
        
        // Charger le plan depuis le fichier XML
        Plan planCharge = xmlPlanReader.parse(xmlFilePath);
        
        assertNotNull(planCharge);
        assertTrue(planCharge.getListeIntersections().size() > 0);
        assertTrue(planCharge.getListeTroncons().size() > 0);
    }
    
}
