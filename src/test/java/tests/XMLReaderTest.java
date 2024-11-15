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
        String cheminInvalide = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/inexistant.xml";
        
        // Vérifie que le parse lance une RuntimeException quand le fichier est invalide
        assertThrows(RuntimeException.class, () -> {
            xmlPlanReader.parse(cheminInvalide);
        }, "Une RuntimeException devrait être lancée si le fichier XML est invalide.");
    }
    
    /**
     * Test pour vérifier que les erreurs de format dans le fichier XML des intersections sont bien gérées.
     */
    @Test
    public void testErreurFormatIntersection() {
        String cheminFichierAvecErreurs = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlanAvecErreurs.xml";

        // Charger le plan et vérifier qu'une exception de type NumberFormatException est levée
        assertThrows(NumberFormatException.class, () -> {
            xmlPlanReader.parse(cheminFichierAvecErreurs);
        }, "Une NumberFormatException devrait être lancée si les attributs du fichier XML ne sont pas au bon format.");
    }


    /**
     * Test pour vérifier que le chargement d'un fichier XML valide pour le plan fonctionne correctement.
    */
    @Test
    public void testChargerPlanDepuisXMLValide() {
        String cheminValide = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlan.xml";
        
        Plan plan = xmlPlanReader.parse(cheminValide);
        assertNotNull(plan, "Le plan ne devrait pas être null avec un fichier valide.");
    }



    @Test
    public void testLongueurTronconNegative() {
        XMLPlan xmlPlanReader = new XMLPlan();
        String cheminFichierAvecLongueurNegative = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlanLongueurNegative.xml";
        
        // Vérifie qu'une IllegalArgumentException est levée si la longueur d'un tronçon est négative
        assertThrows(IllegalArgumentException.class, () -> {
            xmlPlanReader.parse(cheminFichierAvecLongueurNegative);
        }, "Une IllegalArgumentException devrait être lancée si la longueur du tronçon est négative.");
    }
    
    @Test
    public void testLatitudeInvalide() {
        String cheminFichierAvecLatitudeInvalide = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlanLatitudeInvalide.xml";
        // Teste si une NumberFormatException est levée si la latitude n'est pas au bon format
        assertThrows(NumberFormatException.class, () -> {
            xmlPlanReader.parse(cheminFichierAvecLatitudeInvalide);
        }, "Une NumberFormatException devrait être lancée si la latitude est invalide.");
    }

    @Test
    public void testLongitudeInvalide() {
        String cheminFichierAvecLongitudeInvalide = "/Users/morganenaibo/4IF/AGILE/PLD_AGILE/resources/fichiersXMLPickupDelivery/fichiersXMLPickupDelivery/testPlanLongitudeInvalide.xml";
        // Teste si une NumberFormatException est levée si la longitude n'est pas au bon format
        assertThrows(NumberFormatException.class, () -> {
            xmlPlanReader.parse(cheminFichierAvecLongitudeInvalide);
        }, "Une NumberFormatException devrait être lancée si la longitude est invalide.");
    }



}
