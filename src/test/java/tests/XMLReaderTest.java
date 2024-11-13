/**
 * Classe de test pour valider le fonctionnement des classes XMLPlan et XMLDemande.
 * Elle contient des tests pour vérifier que les fichiers XML sont correctement analysés
 * et que les erreurs de format sont bien gérées.
 */
package tests;

import model.Demande;
import model.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.IDIntersectionException;
import util.XMLDemande;
import util.XMLPlan;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XMLReaderTest {

    private XMLPlan xmlPlanReader;
    private XMLDemande xmlDemandeReader;

    /**
     * Initialisation des objets avant chaque test.
     */
    @BeforeEach
    public void setUp() {
        xmlPlanReader = new XMLPlan();
        xmlDemandeReader = new XMLDemande();
    }

    /**
     * Test pour vérifier qu'une RuntimeException est lancée lors du chargement d'un fichier XML invalide pour le plan.
     */
    @Test
    public void testChargerPlanDepuisXMLFichierInvalide() {
        String cheminInvalide = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\fichierInexistant.xml";
        
        // Vérifie que le parse lance une RuntimeException quand le fichier est invalide
        assertThrows(RuntimeException.class, () -> {
            xmlPlanReader.parse(cheminInvalide);
        }, "Une RuntimeException devrait être lancée si le fichier XML est invalide.");
    }
    
    /**
     * Test pour vérifier qu'une RuntimeException est lancée lors du chargement d'un fichier XML invalide pour la demande.
     */
    @Test
    public void testChargerDemandeDepuisXMLFichierInvalide() {
        String cheminInvalide = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\fichierInexistant.xml";
    
        // Vérifie que le parse lance une RuntimeException quand le fichier est invalide
        assertThrows(RuntimeException.class, () -> {
            xmlDemandeReader.parse(cheminInvalide);
        }, "Une RuntimeException devrait être lancée si le fichier XML est invalide.");
    }
    
    /**
     * Test pour vérifier que les erreurs de format dans le fichier XML des intersections sont bien gérées.
     */
    @Test
    public void testErreurFormatIntersection() {
        String cheminFichierAvecErreurs = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlanAvecErreurs.xml";

        // Charger le plan et vérifier qu'une exception de type NumberFormatException est levée
        assertThrows(NumberFormatException.class, () -> {
            xmlPlanReader.parse(cheminFichierAvecErreurs);
        }, "Une NumberFormatException devrait être lancée si les attributs du fichier XML ne sont pas au bon format.");
    }

    /**
     * Test pour vérifier que les erreurs de format dans le fichier XML des livraisons sont bien gérées.
     */
    @Test
    public void testErreurFormatLivraison() {
        String cheminFichierAvecErreurs = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testDemandeAvecErreurs";

        // Charger la demande et vérifier qu'une exception de type NumberFormatException est levée
        assertThrows(NumberFormatException.class, () -> {
            xmlDemandeReader.parse(cheminFichierAvecErreurs);
        }, "Une NumberFormatException devrait être lancée si les attributs du fichier XML des livraisons ne sont pas au bon format.");
    }

    /**
     * Test pour vérifier que le chargement d'un fichier XML valide pour le plan fonctionne correctement.
     */
    @Test
    public void testChargerPlanDepuisXMLValide() {
        String cheminValide = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";
        
        Plan plan = xmlPlanReader.parse(cheminValide);
        assertNotNull(plan, "Le plan ne devrait pas être null avec un fichier valide.");
    }

    /**
     * Test pour vérifier que le chargement d'un fichier XML valide pour la demande fonctionne correctement.
     */
    @Test
    public void testChargerDemandeDepuisXMLValide() {
        String cheminValide = "C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testDemande.xml";
        
        Demande demande = xmlDemandeReader.parse(cheminValide);
        assertNotNull(demande, "La demande ne devrait pas être null avec un fichier valide.");
    }
}