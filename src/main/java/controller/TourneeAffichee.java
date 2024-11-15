package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.stage.FileChooser;
import model.Demande;
import model.Plan;
import model.Trajet;
import tsp.RunTSP;
import util.XMLDemande;
import util.XMLExport;
import util.XMLPlan;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TourneeAffichee extends Etat {
    public TourneeAffichee(Controller controller) {
        super(controller);
        controller.getChargerFichierButton().setStyle("-fx-background-color: grey;");
        controller.getChargerFichierButton().setDisable(true);
    }

    /*@Override
    public void calculerChemin() {
        controller.getCalculerChemin().setVisible(false);
        controller.getDemande().setPlan(controller.getPlan());
        controller.getDemande().initialiserMatriceAdjacence();
        controller.getDemande().creerClusters();
        RunTSP run = new RunTSP();
        for (int i=0; i<controller.getDemande().getNbLivreurs(); i++) {
            Trajet trajet = run.calculerTSP(controller.getDemande().getListeMatriceAdjacence().get(i));
            view.calculerChemin(controller.getMapPane(), controller.getDeliveryInfoVBox(), trajet, i, controller.getMessageLabel());
        }
        controller.setEtat(new TourneeAffichee(controller));
        controller.getCalculerChemin().setVisible(false);

    }*/
    
    @Override
    public void handleSelectButton() {
        controller.getMessageLabel().setVisible(true);
        controller.getView().toggleSelectionMode(controller.getMessageLabel(), controller.getSelectionnerPointButton(), controller.getChargerFichierButton(), controller.getChargerNouveauPlan(), controller.getExport(), controller.getDeliveryInfoVBox());
        if (view.isTourneeCalculee()) {
            controller.setEtat(new TourneeAffichee(controller));
            controller.getCalculerChemin().setVisible(false);
        }
        else controller.setEtat(new DemandeChargee(controller));
    }


    @Override
    public void handleLoadPlan() {
        view.setDemande(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        fileChooser.setTitle("Ouvrir un plan");
        File file = fileChooser.showOpenDialog(null);
        XMLPlan xmlPlan = new XMLPlan();
    
        if (file != null) {
            loadPlan(file.getPath()); // Tentative de chargement du plan
        }
    }

    @Override
    public void handleExportXML(){
        XMLExport export = new XMLExport();
        export.exportDemande(view.getDemande(), "docs\\export.xml");
        VBox deliveryInfoVBox = controller.getDeliveryInfoVBox();
        if (deliveryInfoVBox != null && !deliveryInfoVBox.getChildren().isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("docs\\tournees.txt"))) {
                // Parcourt chaque enfant de la VBox
                for (Node node : deliveryInfoVBox.getChildren()) {
                    // Vérifie si le noeud est un Label pour accéder à son texte
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        writer.write(label.getText() + "\n");
                    } else {
                        writer.write(node.getClass().getSimpleName() + "\n");
                    }
                }
                System.out.println("Contenu de deliveryInfoVBox écrit dans tournees.txt.");
            } catch (IOException e) {
                System.err.println("Erreur lors de l'écriture dans le fichier tournees.txt : " + e.getMessage());
            }
        } else {
            System.out.println("deliveryInfoVBox est vide ou non initialisée.");
        }
        /*
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("docs\\tournees.txt"))) {
            VBox deliveryInfoVBox = controller.getDeliveryInfoVBox();
            if (deliveryInfoVBox != null && !deliveryInfoVBox.getChildren().isEmpty()) {
                System.out.println("Contenu de deliveryInfoVBox :");
                
                // Parcours de chaque enfant de la VBox
                for (Node node : deliveryInfoVBox.getChildren()) {
                    // Vérifiez si le noeud est un Label pour accéder à son texte
                    if (node instanceof Label) {
                        Label label = (Label) node;
                        System.out.println("Label - Texte : " + label.getText());
                    } else {
                        System.out.println("Node type inconnu : " + node.getClass().getSimpleName());
                    }
                }
            } else {
                System.out.println("deliveryInfoVBox est vide ou non initialisée.");
            }

            for (Trajet trajet : view.getTournees()) {
                writer.write(trajet.toString());
                writer.write("\n"); // ajouter une nouvelle ligne entre chaque trajet pour la lisibilité
            }
            System.out.println("Exportation réussie vers le fichier : " + "docs\\tournees.txt");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exportation des trajets : " + e.getMessage());
        }*/
    }
    
    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
    
        // Vérifie si le plan est chargé et contient des intersections
        if (plan != null && plan.getListeIntersections().size() > 0) {
            controller.setPlan(plan);
            view.setPlan(plan);
            view.demande.setNbLivreur(controller.getNbLivreur());
            view.displayPlan(controller.getMapPane(), controller.getDeliveryInfoVBox(), controller.getLabel(), controller.getMessageLabel(), controller.getCalculerChemin());
            view.displayButtons(controller.getPane(), controller.getDeliveryInfoVBox(), controller.getBoutonPlus(), controller.getChargerFichierButton(), controller.getSelectionnerPointButton(), controller.getChargerNouveauPlan(), controller.getCalculerChemin(), controller.getUndoButton(), controller.getRedoButton());
            controller.getBoutonPlus().setVisible(true);
            controller.getCalculerChemin().setVisible(false);
            controller.getUndoButton().setVisible(false);
            controller.getRedoButton().setVisible(false);
            controller.setEtat(new PlanCharge(controller)); // Passer à l'état chargé
            //controller.getExport().setVisible(false);
        } else {
            // Si le plan est invalide, afficher un message et réinitialiser l'état
            controller.getMessageLabel().setText("Le plan n'a pas pu être chargé. Veuillez réessayer.");
            controller.getMessageLabel().setVisible(true);
    
            // Réinitialiser l'interface et revenir à l'état initial
            controller.setPlan(null);
            controller.setEtat(new PlanNonCharge(controller)); // Revenir à l'état initial
        }
    }
    
    // Pas de surcharge de handleActionB ou handleActionC car elles ne sont pas disponibles dans cet état
}
