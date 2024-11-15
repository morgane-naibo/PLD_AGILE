package tests;

import model.Plan;
import model.Intersection;
import model.Troncon;
import model.Etape;
import model.Demande;
import model.PointDeLivraison;
import model.Livreur;
import model.Tournee;
import model.Trajet;
import util.XMLPlan;
import exceptions.IDIntersectionException;
import exceptions.XMLParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class PlanTest {
    
    private Plan plan;
    private Demande demande;

    @BeforeEach
    public void setUp() {
        demande = new Demande();
        this.plan = new Plan();  // Utilisation de 'this.plan' pour associer l'instance de Plan à la variable d'instance
        demande.setPlan(this.plan); // Assurez-vous d'associer le plan à la demande avant les tests
    }
    
    

   

    @Test
    public void testChercherIntersectionParId() throws IDIntersectionException {
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        
        plan.ajouterIntersection(intersection1);
        plan.ajouterIntersection(intersection2);
        
        Intersection result = plan.chercherIntersectionParId(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        
        assertThrows(IDIntersectionException.class, () -> {
            plan.chercherIntersectionParId(3);
        });
    }

    @Test
    public void testChercherPlusCourtChemin() {
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        Intersection intersection3 = new Intersection(3, 45.756, 4.833);
        Intersection intersection4 = new Intersection(4, 45.757, 4.834);

        Troncon troncon1 = new Troncon(intersection1, intersection2, 50.0, "Rue A");
        Troncon troncon2 = new Troncon(intersection2, intersection3, 30.0, "Rue B");
        Troncon troncon3 = new Troncon(intersection1, intersection3, 100.0, "Rue C");
        Troncon troncon4 = new Troncon(intersection3, intersection4, 40.0, "Rue D");
        Troncon troncon5 = new Troncon(intersection2, intersection4, 60.0, "Rue E");

        plan.ajouterIntersection(intersection1);
        plan.ajouterIntersection(intersection2);
        plan.ajouterIntersection(intersection3);
        plan.ajouterIntersection(intersection4);

        plan.ajouterTroncon(troncon1);
        plan.ajouterTroncon(troncon2);
        plan.ajouterTroncon(troncon3);
        plan.ajouterTroncon(troncon4);
        plan.ajouterTroncon(troncon5);

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

        Etape etape = plan.chercherPlusCourtChemin(intersection1, intersection4);
        
        assertNotNull(etape);
        assertEquals(intersection1, etape.getDepart());
        assertEquals(intersection4, etape.getArrivee());
        assertEquals(110.0, etape.getLongueur(), 0.01);
    }


}