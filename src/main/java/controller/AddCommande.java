package controller;

public class AddCommande extends Commande {
    @Override
    public void redoCommande() {
        System.out.println("Entrée dans l'état d'accueil.");
    }

    @Override
    public void undoCommande() {
        System.out.println("Action A exécutée dans l'état d'accueil.");
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
