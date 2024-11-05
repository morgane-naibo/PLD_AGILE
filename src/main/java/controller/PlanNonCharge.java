package controller;

public class PlanNonCharge extends Etat {
    @Override
    public void entrerEtat() {
        System.out.println("Entrée dans l'état d'accueil.");
    }

    @Override
    public void handleActionA() {
        System.out.println("Action A exécutée dans l'état d'accueil.");
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
