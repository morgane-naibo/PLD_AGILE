package controller;

import java.io.File;

import javafx.stage.FileChooser;
import model.Demande;
import model.Plan;
import util.XMLDemande;
import util.XMLPlan;

public class PlanCharge extends Etat {
    public PlanCharge(Controller controller) {
        super(controller);
        controller.getStackPane().getChildren().clear();
        controller.getChargerFichierButton().setDisable(false);
        controller.getChargerFichierButton().setStyle("-fx-background-color: #3498db;");
    }

    @Override
    public void handleSelectButton() {
        controller.setEtat(new DemandeChargee(controller));
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getExport(), controller.getDeliveryInfoVBox());
        controller.getMessageLabel().setVisible(true);
    }

    @Override
    public void handleFileButton() {
        view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
            loadDemande(file.getPath());
        }
    }
    
    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        Demande demandeFile = xmlDemande.parse(filePath);
    
        // Vérifiez si la demande est valide et contient des points de livraison
        if (demandeFile != null && demandeFile.getListePointDeLivraison() != null && !demandeFile.getListePointDeLivraison().isEmpty()) {
            
            // Initialiser la demande dans le contrôleur si elle est null
            if (controller.getDemande() == null) {
                controller.setDemande(new Demande());
            }
    
            // Configurer la demande avec le plan et afficher
            controller.getDemande().setPlan(controller.getPlan());
            view.displayDemande(demandeFile, controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getMessageLabel());
            
            controller.setDemande(demandeFile); // Mettre à jour la demande dans le contrôleur
            controller.getCalculerChemin().setVisible(true);
            controller.setEtat(new DemandeChargee(controller)); // Passer à l'état "DemandeChargee"
        } else {
            // Si la demande est invalide, afficher un message et réinitialiser l'état
            controller.getMessageLabel().setText("Le fichier de demande n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            // Réinitialiser l'interface et revenir à l'état initial
            controller.setDemande(null);
            controller.setEtat(new PlanCharge(controller)); // Revenir à l'état initial
        }
    }

    @Override
    public void handleLoadPlan() {
        view.setDemande(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        fileChooser.setTitle("Ouvrir un plan");
        File file = fileChooser.showOpenDialog(null);
        //XMLPlan xmlPlan = new XMLPlan();
        if (file != null) {
            loadPlan(file.getPath()); // Tentative de chargement du plan
        }
    }
    
    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
    
        // Vérifie si le plan est chargé et contient des intersections
        if (plan != null && plan.getListeIntersections().size() > 0) {
            controller.setPlan(plan);
            view.setPlan(plan);
            view.demande.setNbLivreur(controller.getNbLivreur());
            view.displayPlan(controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel(), controller.getMessageLabel(), controller.getCalculerChemin());
            view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin(), controller.getUndoButton(), controller.getRedoButton());
            controller.getBoutonPlus().setVisible(true);
            controller.getCalculerChemin().setVisible(false);
            controller.getUndoButton().setVisible(false);
            controller.getRedoButton().setVisible(false);
            controller.setEtat(new PlanCharge(controller)); // Passer à l'état chargé
        } else {
            // Si le plan est invalide, afficher un message et réinitialiser l'état
            controller.getMessageLabel().setText("Le plan n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            // Réinitialiser l'interface et revenir à l'état initial
            controller.setPlan(null);
            controller.setEtat(new PlanNonCharge(controller)); // Revenir à l'état initial
        }
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
