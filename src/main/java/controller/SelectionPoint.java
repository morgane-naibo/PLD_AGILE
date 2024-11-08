package controller;

public class SelectionPoint extends Etat {
    public SelectionPoint(Controller controller) {
        super(controller);
    }

    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
