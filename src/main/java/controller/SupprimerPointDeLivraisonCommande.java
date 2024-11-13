package controller;

import model.Intersection;

import java.util.Stack;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import view.View;

public class SupprimerPointDeLivraisonCommande extends Commande {

    private final View view;
    private final Pane pane;
    private final VBox deliveryInfoVBox;
    private Stack<Intersection> intersectionsSupprimees;
    private Stack<Label> labelsSupprimes;
    private Label messageLabel;

    public SupprimerPointDeLivraisonCommande(View view, Pane pane, VBox deliveryInfoVBox, Intersection intersection, Label label, Label messageLabel) {
        super(intersection, label);
        this.view = view;
        this.pane = pane;
        this.deliveryInfoVBox = deliveryInfoVBox;
        this.messageLabel = messageLabel;
    }

    @Override
    public void redoCommande(Intersection intersection, Label label) {
        //intersectionsSupprimees.push(this.getIntersection());
        //labelsSupprimes.push(this.getLabel());
        view.getIntersectionsSupprimees().push(this.getIntersection());
        view.getLabelsSupprimes().push(this.getLabel());
        view.supprimerPointDeLivraison(intersection, pane, deliveryInfoVBox, label, false, view.getLivreurSelectionne());
    }

    @Override
    public void undoCommande() {
        if (!view.getIntersectionsSupprimees().isEmpty() && !view.getLabelsSupprimes().isEmpty()) {
            // Retirer le dernier point de livraison supprimé des piles pour le réafficher
            Intersection intersectionARestaurer = view.getIntersectionsSupprimees().peek();
            view.getIntersectionsSupprimees().remove(intersectionARestaurer);
            Label labelARestaurer = view.getLabelsSupprimes().peek();
            view.getLabelsSupprimes().remove(labelARestaurer);
            view.reafficherPointDeLivraison(intersectionARestaurer, pane, deliveryInfoVBox, labelARestaurer, view.getLivreurSelectionne());
        }
        //intersectionsSupprimees.pop();
        //labelsSupprimes.pop();
    }
    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
