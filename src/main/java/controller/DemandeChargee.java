package controller;

import java.io.File;

import javafx.stage.FileChooser;
import model.Demande;
import model.Plan;
import model.Trajet;
import tsp.RunTSP;
import util.XMLDemande;
import util.XMLPlan;

public class DemandeChargee extends Etat {
    public DemandeChargee(Controller controller) {
        super(controller);
    }

    @Override
    public void calculerChemin() {
        controller.getDemande().setPlan(controller.getPlan());
        controller.getDemande().initialiserMatriceAdjacence();
        controller.getDemande().creerClusters();
        RunTSP run = new RunTSP();
        for (int i=0; i<controller.getDemande().getNbLivreurs(); i++) {
            Trajet trajet = run.calculerTSP(controller.getDemande().getListeMatriceAdjacence().get(i));
            view.calculerChemin(controller.getMapPane(), controller.getDeliveryInfoVBox(), trajet);
        }
        controller.setEtat(new TourneeAffichee(controller));
    }

    @Override
    public void handleSelectButton() {
        controller.getMessageLabel().setVisible(true);
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getDeliveryInfoVBox());
        controller.setEtat(new DemandeChargee(controller));
    }

    @Override
    public void handleFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadDemande(file.getPath());
            controller.setEtat(new DemandeChargee(controller));
        }
    }

    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        Demande demandeFile = xmlDemande.parse(filePath);
        if (demandeFile != null) {
            controller.getView().demande.setPlan(controller.getPlan());
            controller.getDemande().setPlan(controller.getPlan());
            view.displayDemande(demandeFile, controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getMessageLabel());
        }
        controller.setDemande(controller.getView().demande);
    }

    @Override
    public void handleLoadPlan() {
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
            view.displayPlan(controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel(), controller.getMessageLabel()); // Afficher le plan dans mapPane
            view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin());
            controller.getBoutonPlus().setVisible(true);
        }
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
