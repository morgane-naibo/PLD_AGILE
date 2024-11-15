package controller;

import java.io.File;

import javafx.stage.FileChooser;
import model.Demande;
import model.Plan;
import util.XMLDemande;
import util.XMLPlan;

/**
 * La classe PlanCharge représente l'état où un plan est chargé dans l'application.
 * Elle hérite de la classe Etat et gère les interactions de l'utilisateur dans cet état.
 */
public class PlanCharge extends Etat {

    /**
     * Constructeur de la classe PlanCharge.
     * Initialise l'état avec le contrôleur et configure l'interface utilisateur.
     *
     * @param controller Le contrôleur de l'application.
     */
    public PlanCharge(Controller controller) {
        super(controller);
        controller.getStackPane().getChildren().clear();
        controller.getChargerFichierButton().setDisable(false);
        controller.getChargerFichierButton().setStyle("-fx-background-color: #3498db;");
    }

    /**
     * Gère l'événement de sélection du bouton.
     * Passe à l'état "DemandeChargee" et configure l'interface utilisateur en mode sélection.
     */
    @Override
    public void handleSelectButton() {
        controller.setEtat(new DemandeChargee(controller));
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getExport(), controller.getDeliveryInfoVBox());
        controller.getMessageLabel().setVisible(true);
    }

    /**
     * Gère l'événement de clic sur le bouton de chargement de fichier.
     * Affiche une boîte de dialogue pour sélectionner un fichier et tente de charger une demande.
     */
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
    
    /**
     * Charge une demande à partir d'un fichier XML.
     * Met à jour l'état et l'interface utilisateur en fonction de la validité de la demande.
     *
     * @param filePath Le chemin du fichier XML de la demande.
     */
    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        Demande demandeFile = xmlDemande.parse(filePath);
    
        if (demandeFile != null && demandeFile.getListePointDeLivraison() != null && !demandeFile.getListePointDeLivraison().isEmpty()) {
            if (controller.getDemande() == null) {
                controller.setDemande(new Demande());
            }
    
            controller.getDemande().setPlan(controller.getPlan());
            view.displayDemande(demandeFile, controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getMessageLabel());
            
            controller.setDemande(demandeFile);
            controller.getCalculerChemin().setVisible(true);
            controller.setEtat(new DemandeChargee(controller));
            view.toggleButtons(controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan());
        } else {
            controller.getMessageLabel().setText("Le fichier de demande n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            controller.setDemande(null);
            controller.setEtat(new PlanCharge(controller));
        }
    }

    /**
     * Gère l'événement de chargement d'un plan.
     * Affiche une boîte de dialogue pour sélectionner un fichier et tente de charger un plan.
     */
    @Override
    public void handleLoadPlan() {
        view.setDemande(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        fileChooser.setTitle("Ouvrir un plan");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadPlan(file.getPath());
        }
    }
    
    /**
     * Charge un plan à partir d'un fichier XML.
     * Met à jour l'état et l'interface utilisateur en fonction de la validité du plan.
     *
     * @param filePath Le chemin du fichier XML du plan.
     */
    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
    
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
            controller.setEtat(new PlanCharge(controller));
            controller.getExport().setVisible(false);
            view.toggleButtons(controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan());

        } else {
            controller.getMessageLabel().setText("Le plan n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            controller.setPlan(null);
            controller.setEtat(new PlanNonCharge(controller));
        }
    }
}
