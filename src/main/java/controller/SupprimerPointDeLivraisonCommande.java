package controller;

import model.Intersection;
import model.PointDeLivraison;
import model.Tournee;

import java.util.Stack;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import view.View;

import java.util.List;

public class SupprimerPointDeLivraisonCommande extends Commande {

    private final View view;
    private final Pane pane;
    private final VBox deliveryInfoVBox;


    public SupprimerPointDeLivraisonCommande(View view, Pane pane, VBox deliveryInfoVBox) {
        this.view = view;
        this.pane = pane;
        this.deliveryInfoVBox = deliveryInfoVBox;

    }

    public void redoCommande(Intersection intersection, Label label) {

        // Supprimer le point de livraison via View
        view.supprimerPointDeLivraison(intersection, pane, deliveryInfoVBox, label);
    }

    public void undoCommande() {
        if (!view.getIntersectionsSupprimees().isEmpty() && !view.getLabelsSupprimes().isEmpty()) {
            // Retirer le dernier point de livraison supprimé des piles pour le réafficher
            Intersection intersectionARestaurer = view.getIntersectionsSupprimees().pop();
            Label labelARestaurer = view.getLabelsSupprimes().pop();

            view.reafficherPointDeLivraison(intersectionARestaurer, pane, deliveryInfoVBox, labelARestaurer);
        }
    }
    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
