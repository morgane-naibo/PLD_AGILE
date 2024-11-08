package controller;

import model.Trajet;
import tsp.RunTSP;

public class TourneeAffichee extends Etat {
    public TourneeAffichee(Controller controller) {
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
        controller.setEtat(new DemandeChargee(controller));
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getDeliveryInfoVBox());
    }
    
    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
