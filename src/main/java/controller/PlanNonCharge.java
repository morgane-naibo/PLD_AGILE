package controller;

import java.io.File;

import javafx.stage.FileChooser;
import util.XMLPlan;
import model.Plan;

/**
 * La classe PlanNonCharge représente l'état où aucun plan n'est chargé.
 * Elle hérite de la classe Etat et gère le chargement d'un plan.
 */
public class PlanNonCharge extends Etat {

    /**
     * Constructeur de la classe PlanNonCharge.
     *
     * @param controller Le contrôleur principal de l'application.
     */
    public PlanNonCharge(Controller controller) {
        super(controller);
    }

    /**
     * Gère l'action de chargement d'un plan.
     * Ouvre une boîte de dialogue pour sélectionner un fichier XML de plan.
     */
    @Override
    public void handleLoadPlan() {
        view.setDemande(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        fileChooser.setTitle("Ouvrir un plan");
        File file = fileChooser.showOpenDialog(null);
        XMLPlan xmlPlan = new XMLPlan();
    
        if (file != null) {
            loadPlan(file.getPath()); // Tentative de chargement du plan
        }
    }
    
    /**
     * Charge le plan à partir du chemin de fichier spécifié.
     * Met à jour l'état et l'interface utilisateur en fonction du succès ou de l'échec du chargement.
     *
     * @param filePath Le chemin du fichier XML du plan.
     */
    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
    
        // Vérifie si le plan est chargé et contient des intersections
        if (plan != null && plan.getListeIntersections().size() > 0 && controller.getNbLivreur() > 0) {
            controller.setPlan(plan);
            view.setPlan(plan);
            view.demande.setNbLivreur(controller.getNbLivreur());
            view.displayPlan(controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel(), controller.getMessageLabel(), controller.getCalculerChemin(), controller.getUndoButton(), controller.getRedoButton());
            view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin(), controller.getUndoButton(), controller.getRedoButton(), controller.getExport());
            controller.getBoutonPlus().setVisible(true);
            controller.getCalculerChemin().setVisible(false);
            controller.getUndoButton().setVisible(false);
            controller.getRedoButton().setVisible(false);
            controller.setEtat(new PlanCharge(controller)); // Passer à l'état chargé
            controller.getExport().setVisible(false);
        } else if (controller.getNbLivreur() == 0) {
            controller.getMessageLabel().setText("Veuillez sélectionner un nombre de livreurs avant de charger un plan.");
            controller.getMessageLabel().setVisible(true);
            controller.setEtat(new PlanNonCharge(controller)); // Revenir à l'état initial
        } else {
            // Si le plan est invalide, afficher un message et réinitialiser l'état
            controller.getMessageLabel().setText("Le plan n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            // Réinitialiser l'interface et revenir à l'état initial
            controller.setPlan(null);
            controller.setEtat(new PlanNonCharge(controller)); // Revenir à l'état initial
        }
    }

}
