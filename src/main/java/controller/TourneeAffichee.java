package controller;

import java.io.File;

import javafx.stage.FileChooser;
import model.Demande;
import model.Plan;
import model.Trajet;
import tsp.RunTSP;
import util.XMLDemande;
import util.XMLPlan;

public class TourneeAffichee extends Etat {
    public TourneeAffichee(Controller controller) {
        super(controller);
        controller.getChargerFichierButton().setStyle("-fx-background-color: grey;");
        controller.getChargerFichierButton().setDisable(true);
    }

    /*@Override
    public void calculerChemin() {
        controller.getCalculerChemin().setVisible(false);
        controller.getDemande().setPlan(controller.getPlan());
        controller.getDemande().initialiserMatriceAdjacence();
        controller.getDemande().creerClusters();
        RunTSP run = new RunTSP();
        for (int i=0; i<controller.getDemande().getNbLivreurs(); i++) {
            Trajet trajet = run.calculerTSP(controller.getDemande().getListeMatriceAdjacence().get(i));
            view.calculerChemin(controller.getMapPane(), controller.getDeliveryInfoVBox(), trajet, i, controller.getMessageLabel());
        }
        controller.setEtat(new TourneeAffichee(controller));
        controller.getCalculerChemin().setVisible(false);

    }*/
    
    @Override
    public void handleSelectButton() {
        controller.getMessageLabel().setVisible(true);
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getDeliveryInfoVBox());
        if (view.isTourneeCalculee()) {
            controller.setEtat(new TourneeAffichee(controller));
            controller.getCalculerChemin().setVisible(false);
        }
        else controller.setEtat(new DemandeChargee(controller));
    }


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
