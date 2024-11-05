package controller;

public abstract class Etat {
    
    // Méthodes génériques, présentes dans tous les états mais surchargées selon le contexte.
    public void entrerEtat() {
        // Par défaut : aucune action
    }

    public void quitterEtat() {
        // Par défaut : aucune action
    }

    public void handleActionA() {
        throw new UnsupportedOperationException("ActionA n'est pas disponible dans cet état.");
    }

    public void handleActionB() {
        throw new UnsupportedOperationException("ActionB n'est pas disponible dans cet état.");
    }

    public void handleActionC() {
        throw new UnsupportedOperationException("ActionC n'est pas disponible dans cet état.");
    }

    // Autres méthodes spécifiques à ajouter selon les besoins
}
