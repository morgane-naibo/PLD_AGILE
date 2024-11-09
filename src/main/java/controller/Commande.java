package controller;

import javafx.scene.control.Label;
import model.Intersection;

public abstract class Commande {
    
    // Méthodes génériques, présentes dans tous les états mais surchargées selon le contexte.
    public void undoCommande() {
        // Par défaut : aucune action
    }

    public void redoCommande(Intersection intersection, Label label) {
        // Par défaut : aucune action
    }
}
