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
        controller.getChargerFichierButton().setDisable(false);
        controller.getChargerFichierButton().setStyle("-fx-background-color: #3498db;");
    }

    @Override
    public void handleSelectButton() {
        controller.setEtat(new DemandeChargee(controller));
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getDeliveryInfoVBox());
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
        controller.getCalculerChemin().setVisible(true);
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