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

    @Override
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

    }
    
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
            loadPlan(file.getPath());
            Plan plan = xmlPlan.parse(file.getPath());
            controller.setPlan(plan);
            PlanCharge planCharge = new PlanCharge(controller);
            controller.setEtat(planCharge);
        }
    }

    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
        if (plan != null) {
            view.setPlan(plan);
            view.displayPlan(controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel(), controller.getMessageLabel(), controller.getCalculerChemin()); // Afficher le plan dans mapPane
            view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin());
            controller.getBoutonPlus().setVisible(true);
            controller.getCalculerChemin().setVisible(false);
        }
    }
    
    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}