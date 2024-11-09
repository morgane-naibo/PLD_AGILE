package controller;

import java.util.Stack;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Intersection;
import view.View;

public class AjouterPointDeLivraisonCommande extends Commande {
    private final View view;
    private final Pane pane;
    private final VBox deliveryInfoVBox;
    private Stack<Intersection> intersectionsAjoutees = new Stack<>();
    private Stack<Label> labelsAjoutes = new Stack<>();


    public AjouterPointDeLivraisonCommande(View view, Pane pane, VBox deliveryInfoVBox) {
        this.view = view;
        this.pane = pane;
        this.deliveryInfoVBox = deliveryInfoVBox;
    }

    @Override
    public void redoCommande(Intersection intersection, Label label) {
        intersectionsAjoutees.push(intersection);
        labelsAjoutes.push(label);
        view.reafficherPointDeLivraison(intersection, pane, deliveryInfoVBox, label);
    }

    @Override
    public void undoCommande() {
        if (!view.getIntersectionsAjoutees().isEmpty() && !view.getLabelsAjoutes().isEmpty()) {
            Intersection intersectionASupprimer = view.getIntersectionsAjoutees().pop();
            Label labelARestaurer = view.getLabelsAjoutes().pop();
            view.supprimerPointDeLivraison(intersectionASupprimer, pane, deliveryInfoVBox, labelARestaurer);
        }
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
