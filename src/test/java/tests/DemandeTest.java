package tests;

import model.Demande;
import model.Intersection;
import model.Plan;
import model.PointDeLivraison;
import model.Livraison;
import model.Troncon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DemandeTest {

    private Demande demande;

    @BeforeEach
    public void setUp() {
        demande = new Demande();
        // Initialisation du plan et affectation à la demande
        Plan plan = new Plan();
        demande.setPlan(plan);  // Assurez-vous que le plan est bien assigné avant d'utiliser demande.getPlan()
    }
    @Test
    public void testMoinsDePointsQueDeLivreurs() {
        // Configuration de Demande et ajout d'un plan (assurez-vous que demande.setPlan(plan) est fait avant ce test)
        Intersection intersection1 = new Intersection(1, 45.754, 4.831);
        Intersection intersection2 = new Intersection(2, 45.755, 4.832);
        Intersection intersection3 = new Intersection(3, 45.756, 4.833);
    
        Troncon troncon1 = new Troncon(intersection1, intersection2, 50.0, "Rue A");
        Troncon troncon2 = new Troncon(intersection2, intersection3, 60.0, "Rue B");
    
        demande.getPlan().ajouterIntersection(intersection1);
        demande.getPlan().ajouterIntersection(intersection2);
        demande.getPlan().ajouterIntersection(intersection3);
    
        demande.getPlan().ajouterTroncon(troncon1);
        demande.getPlan().ajouterTroncon(troncon2);
    
        intersection1.ajouterTroncon(troncon1);
        intersection2.ajouterTroncon(troncon1);
        intersection2.ajouterTroncon(troncon2);
        intersection3.ajouterTroncon(troncon2);
    
        Livraison livraison1 = new Livraison(12345L, 67890L, 15.0, 30.0); 
        PointDeLivraison point1 = new PointDeLivraison(1L, livraison1);
    
        // Ajout du point de livraison
        demande.ajouterPointDeLivraison(point1);
    
        // Définir 3 livreurs pour 1 point de livraison
        demande.setNbLivreurs(3);
    
        // Vérifier la création des clusters
        try {
            demande.initialiserMatriceAdjacence();
            demande.creerClusters(); // Vérifiez ici que les clusters sont bien créés
            assertTrue(demande.getListesIndex().size() > 1, "Les clusters doivent être correctement créés même avec plus de livreurs.");
        } catch (Exception e) {
            fail("Erreur lors de la gestion de l'attribution des livreurs : " + e.getMessage());
        }
    }
}
    