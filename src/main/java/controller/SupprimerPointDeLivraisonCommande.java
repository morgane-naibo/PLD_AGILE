package controller;

import model.Intersection;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import view.View;

/**
 * La classe SupprimerPointDeLivraisonCommande représente une commande pour supprimer un point de livraison.
 * Elle hérite de la classe Commande et permet de gérer l'annulation et la réexécution de la commande.
 */
public class SupprimerPointDeLivraisonCommande extends Commande {

    /**
     * Vue associée à la commande.
     */
    private final View view;

    /**
     * Pane associé à la commande.
     */
    private final Pane pane;

    /**
     * VBox contenant les informations de livraison.
     */
    private final VBox deliveryInfoVBox;

    /**
     * Label pour afficher les messages.
     */
    private Label messageLabel;

    /**
     * Constructeur de la classe SupprimerPointDeLivraisonCommande.
     *
     * @param view La vue associée à la commande.
     * @param pane Le pane associé à la commande.
     * @param deliveryInfoVBox La VBox contenant les informations de livraison.
     * @param intersection L'intersection à supprimer.
     * @param label Le label associé à l'intersection.
     * @param messageLabel Le label pour afficher les messages.
     */
    public SupprimerPointDeLivraisonCommande(View view, Pane pane, VBox deliveryInfoVBox, Intersection intersection, Label label, Label messageLabel) {
        super(intersection, label);
        this.view = view;
        this.pane = pane;
        this.deliveryInfoVBox = deliveryInfoVBox;
        this.messageLabel = messageLabel;
    }

    /**
     * Réexécute la commande pour supprimer un point de livraison.
     *
     * @param intersection L'intersection à supprimer.
     * @param label Le label associé à l'intersection.
     */
    @Override
    public void redoCommande(Intersection intersection, Label label) {
        view.getIntersectionsSupprimees().push(this.getIntersection());
        view.getLabelsSupprimes().push(this.getLabel());
        view.supprimerPointDeLivraison(intersection, pane, deliveryInfoVBox, label, false, view.getLivreurSelectionne());
    }

    /**
     * Annule la commande pour restaurer un point de livraison supprimé.
     */
    @Override
    public void undoCommande() {
        if (!view.getIntersectionsSupprimees().isEmpty() && !view.getLabelsSupprimes().isEmpty()) {
            Intersection intersectionARestaurer = view.getIntersectionsSupprimees().peek();
            view.getIntersectionsSupprimees().remove(intersectionARestaurer);
            Label labelARestaurer = view.getLabelsSupprimes().peek();
            view.getLabelsSupprimes().remove(labelARestaurer);
            view.reafficherPointDeLivraison(intersectionARestaurer, pane, deliveryInfoVBox, labelARestaurer, view.getLivreurSelectionne());
        }
    }
}
