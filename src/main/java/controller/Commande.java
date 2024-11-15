package controller;

import javafx.scene.control.Label;
import model.Intersection;

/**
 * La classe abstraite Commande représente une commande générique avec une intersection et un label.
 * Elle fournit des méthodes pour annuler et refaire une commande, qui peuvent être surchargées par les sous-classes.
 */
public abstract class Commande {

    private Intersection intersection;
    private Label label;

    public Commande() {
    }

    public Commande(Intersection intersection, Label label) {
        this.intersection = intersection;
        this.label = label;
    }

    public Intersection getIntersection() {
        return this.intersection;
    }

    public Label getLabel() {
        return this.label;
    }
    
    // Méthodes génériques, présentes dans tous les états mais surchargées selon le contexte.
    public void undoCommande() {
        // Par défaut : aucune action
    }

    public void redoCommande(Intersection intersection, Label label) {
        // Par défaut : aucune action
    }
}
