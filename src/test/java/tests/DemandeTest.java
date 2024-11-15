package tests;

import model.Demande;
import model.Intersection;
import model.Plan;
import model.PointDeLivraison;
import model.Livraison;
import model.Troncon;
import util.XMLDemande;
import util.XMLPlan;
import model.Trajet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DemandeTest {

    private Demande demande;
    private PointDeLivraison pointDeLivraison;
    private Trajet trajet ;
    private Plan plan ;
    


    @BeforeEach
    public void setUp() {
     
        String mapPathPlan = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/moyenPlan.xml";
        String mapPathDemande = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/demandeMoyen5.xml";
        XMLPlan planReader = new XMLPlan();
        plan = planReader.parse(mapPathPlan);
        XMLDemande demandeReader = new XMLDemande();
        demande = new Demande();
        demande = demandeReader.parse(mapPathDemande);
        demande.setPlan(plan);

     

        // Créer un PointDeLivraison pour ajouter puis supprimer
     
     
        Intersection entrepot = new Intersection(123456789L, 45.754, 4.831); 
        plan.ajouterIntersection(entrepot); 

      
        pointDeLivraison = new PointDeLivraison(12345L, new Livraison(12345L, 67890L, 10.0, 20.0));
       
        Troncon troncon = new Troncon(entrepot,pointDeLivraison, 2L, "Azerrty");

        Troncon troncon1 = new Troncon(pointDeLivraison,entrepot, 2L, "Azerrty");
       
        plan.ajouterTroncon(troncon);
        plan.ajouterTroncon(troncon1);
        demande.setPlan(plan); 

        List<Troncon> listeTroncon1 = new ArrayList<>();
        listeTroncon1.add(troncon1);
        pointDeLivraison.setListeTroncons(listeTroncon1);

        List<Troncon> listeTroncon = new ArrayList<>();
        listeTroncon.add(troncon);
        entrepot.setListeTroncons(listeTroncon);

 
        demande.initialiserListePointdeLivraisons(); 
        demande.initialiserMatriceAdjacence();
       
    }

        @Test
        public void testSupprimerPointDeLivraison() {
          
            
            int initialSize = demande.getListePointDeLivraison().size();
          

            try {

                Intersection inter = plan.chercherIntersectionParId(2512682686L);
                pointDeLivraison = new PointDeLivraison(inter);

                System.out.println("\n\n\n\n\n\nLISTE ALEDDDDD" + pointDeLivraison);
                System.out.println("\n\n\n\n\n\nLISTE PDLLLLLLLLLLLLLLLLLL\n" + demande.getListePointDeLivraison() + "\n\n\n");
                demande.ajouterPDLaMatrice(0, pointDeLivraison); 
      
                
            /* assertTrue(demande.getListePointDeLivraison().contains(pointDeLivraison), "Le point de livraison doit être présent dans la liste avant la suppression.");*/
            trajet = demande.supprimerPDL(0, pointDeLivraison);
    
           /// assertFalse(demande.getListePointDeLivraison().contains(pointDeLivraison), "Le point de livraison devrait être supprimé de la liste.");
            System.out.println("demande.getListePointDeLivraison()");

            assertEquals(initialSize, demande.getListePointDeLivraison().size(), "La taille de la liste des points de livraison devrait être diminuée de 1 après la suppression.");

           /*  assertNotNull(demande.getMatriceAdjacence(), "La matrice d'adjacence ne doit pas être nulle.");
          assertEquals(demande.getMatriceAdjacence().size(), initialSize - 1, "La matrice d'adjacence devrait avoir une ligne de moins après la suppression.");*/
        
        }
        catch (Exception e) {
            fail("Erreur de suppression 😎 : " + e.getMessage());
        }
    }

        @Test
        public void testAjouterPointDeLivraison() throws Exception {
           
            int initialSize = (demande.getListePointDeLivraison().size());


            Intersection inter = plan.chercherIntersectionParId(2512682686L);
            pointDeLivraison = new PointDeLivraison(inter);
            
            System.out.println("\n\n\n\n\n\nLISTE ALEDDDDD" + pointDeLivraison);
            System.out.println("\n\n\n\n\n\nLISTE PDLLLLLLLLLLLLLLLLLL\n" + demande.getListePointDeLivraison() + "\n\n\n");
         
            demande.ajouterPDLaMatrice(0, pointDeLivraison);

            System.out.println("\n\n\n\n\n\nLISTE ALAISEEEEE\n" + demande.getListePointDeLivraison() + "\n\n\n");

            assertEquals(initialSize + 1, demande.getListePointDeLivraison().size(), "Le point de livraison devrait être ajouté à la liste.");
          ///  assertTrue(demande.getListePointDeLivraison().contains(pointDeLivraison), "Le point de livraison devrait être présent dans la liste.");
        
          ///  assertNotNull(demande.getMatriceAdjacence(), "La matrice d'adjacence ne doit pas être nulle.");
          ///  assertEquals(demande.getMatriceAdjacence().size(), initialSize + 1, "La matrice d'adjacence devrait avoir une ligne de plus après l'ajout.");
        }
        
        
        
        @Test
        public void testPlusPointLivraisonQueLivreur() {
            // Configuration de la demande et ajout du plan
            Demande demande = new Demande();
            Plan plan = new Plan();
            demande.setPlan(plan);
        
            // Créer des intersections (représentant l'entrepôt et les points de livraison)
            Intersection entrepot = new Intersection(0, 45.754, 4.831); 
            plan.ajouterIntersection(entrepot); // Ajouter l'entrepôt au plan
        
            Intersection point1 = new Intersection(1, 45.755, 4.832); 
            Intersection point2 = new Intersection(2, 45.756, 4.833); 
            Intersection point3 = new Intersection(3, 45.757, 4.834);
            
            plan.ajouterIntersection(point1);
            plan.ajouterIntersection(point2);
            plan.ajouterIntersection(point3);
        
            // Ajouter des points de livraison à la demande
            PointDeLivraison livraison1 = new PointDeLivraison(1L);
            PointDeLivraison livraison2 = new PointDeLivraison(2L);
            PointDeLivraison livraison3 = new PointDeLivraison(3L);
            
            demande.ajouterPointDeLivraison(livraison1);
            demande.ajouterPointDeLivraison(livraison2);
            demande.ajouterPointDeLivraison(livraison3);
        
            // Définir le nombre de livreurs (ici, 2 livreurs pour 3 points de livraison)
            demande.setNbLivreurs(2);
        
            // Vérifier la création des clusters
            try {
                demande.creerClusters();  // Créer les clusters en fonction des livreurs et points de livraison
                assertTrue(demande.getListesIndex().size() > 1, "Les clusters doivent être correctement créés même avec plus de livreurs.");
                assertTrue(demande.getListesIndex().get(0).size() > 0, "Le premier cluster doit contenir des points de livraison.");
                assertTrue(demande.getListesIndex().get(1).size() > 0, "Le second cluster doit contenir des points de livraison.");
            } catch (Exception e) {
                fail("Erreur lors de la gestion de l'attribution des livreurs : " + e.getMessage());
            }
        }
        
    }
        