package controller;

import view.View;
import javafx.scene.input.MouseEvent;

public abstract class Etat {

    public View view;
    protected Controller controller;

    public Etat(Controller controller) {
        this.controller = controller;
        this.view = controller.getView();
    }

    public Etat() {
    }
    
    
    // Définir des méthodes pour les actions principales que chaque état peut gérer
    public void handleLoadPlan() {
        System.out.println("Action non disponible dans cet état.");
    }

    public void handleSelectButton() {
        System.out.println("Action non disponible dans cet état.");
    }

    public void handleFileButton() {
        System.out.println("Action non disponible dans cet état.");
    }

    public void handleMousePressed(MouseEvent event) {
        System.out.println("Action non disponible dans cet état.");
    }

    public void handleMouseDragged(MouseEvent event) {
        System.out.println("Action non disponible dans cet état.");
    }

    public void calculerChemin() {
        System.out.println("Action non disponible dans cet état.");
    }

    public void selectionnerPoint() {
        System.out.println("Action non disponible dans cet état.");
    }


    // Autres méthodes spécifiques à ajouter selon les besoins
}
