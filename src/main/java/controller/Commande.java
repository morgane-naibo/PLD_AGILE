package controller;

public abstract class Commande {
    
    // Méthodes génériques, présentes dans tous les états mais surchargées selon le contexte.
    public void doCommande() {
        // Par défaut : aucune action
    }

    public void undoCommande() {
        // Par défaut : aucune action
    }
}
