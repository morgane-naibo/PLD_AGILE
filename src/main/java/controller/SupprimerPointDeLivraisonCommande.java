package controller;

import model.Intersection;
import model.PointDeLivraison;

import java.util.Stack;

public class SupprimerPointDeLivraisonCommande extends Commande {

    private Intersection intersection;
    private controller.Controller controller;
    private Stack pile;

    public SupprimerPointDeLivraisonCommande(Intersection intersection, controller.Controller controller, Stack pile) {
        this.intersection = intersection;
        this.controller = controller;
        this.pile = pile;
    }

    @Override
    public void doCommande() {
        // Effectuer la suppression du point de livraison
        controller.getView().supprimerPointDeLivraison(intersection, controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel());
        System.out.println("Point de livraison supprimé.");
        pile.push(this);
    }

    @Override
    public void undoCommande() {
        System.out.println("Action A exécutée dans l'état d'accueil.");
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
