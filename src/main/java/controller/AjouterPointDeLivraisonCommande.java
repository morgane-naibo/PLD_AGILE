package controller;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Intersection;
import view.View;

/**
 * La classe AjouterPointDeLivraisonCommande représente une commande pour ajouter un point de livraison.
 * Elle hérite de la classe Commande.
 */
public class AjouterPointDeLivraisonCommande extends Commande {
    private final View view;
    private final Pane pane;
    private final VBox deliveryInfoVBox;
    private Label messageLabel;

    /**
     * Constructeur de la classe AjouterPointDeLivraisonCommande.
     *
     * @param view La vue associée à la commande.
     * @param pane Le panneau où le point de livraison sera ajouté.
     * @param deliveryInfoVBox Le conteneur VBox pour les informations de livraison.
     * @param intersection L'intersection où le point de livraison sera ajouté.
     * @param label Le label associé à l'intersection.
     * @param messageLabel Le label pour afficher les messages.
     */
    public AjouterPointDeLivraisonCommande(View view, Pane pane, VBox deliveryInfoVBox, Intersection intersection, Label label, Label messageLabel) {
        super(intersection, label);
        this.view = view;
        this.pane = pane;
        this.deliveryInfoVBox = deliveryInfoVBox;
        this.messageLabel = messageLabel;
    }

    /**
     * Réexécute la commande pour ajouter un point de livraison.
     *
     * @param intersection L'intersection où le point de livraison sera ajouté.
     * @param label Le label associé à l'intersection.
     */
    @Override
    public void redoCommande(Intersection intersection, Label label) {
        view.getIntersectionsAjoutees().push(this.getIntersection());
        view.getLabelsAjoutes().push(this.getLabel());
        view.reafficherPointDeLivraison(intersection, pane, deliveryInfoVBox, label, view.getLivreurSelectionne());
    }

    /**
     * Annule la commande pour supprimer le point de livraison ajouté.
     */
    @Override
    public void undoCommande() {
        if (!view.getIntersectionsAjoutees().isEmpty() && !view.getLabelsAjoutes().isEmpty()) {
            Intersection intersectionASupprimer = view.getIntersectionsAjoutees().peek();
            view.getIntersectionsAjoutees().remove(intersectionASupprimer);
            Label labelARestaurer = view.getLabelsAjoutes().peek();
            view.getLabelsAjoutes().remove(labelARestaurer);
            view.supprimerPointDeLivraison(intersectionASupprimer, pane, deliveryInfoVBox, labelARestaurer, false, view.getLivreurSelectionne());
        }
    }
}

