package view;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Light.Point;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import model.Demande;
import model.Entrepot;
import model.Etape;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.layout.BorderPane;
import model.Trajet;
import model.Tournee;

public class View {

    private double paneWidth = 550.0;
    private double paneHeight = 550.0;
    private double latMin;
    private double latMax;
    private double longMin;
    private double longMax;

    private Plan plan;
    private Entrepot entrepot;

    private boolean button_visible = false;
    private boolean selectionModeEnabled = false;
    private boolean entrepotExiste = false;
    private boolean popupOuverte = false;

    private Circle entrepotCircle;
    private Label labelEntrepot;

    public Demande demande;

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)


    public void fileChooser() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Fichier chargé avec succès !");
        alert.showAndWait();
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
        entrepotExiste = false;
        demande = new Demande();
        latMin = plan.trouverLatitudeMin();
        latMax = plan.trouverLatitudeMax();
        longMin = plan.trouverLongitudeMin();
        longMax = plan.trouverLongitudeMax();
    }

    public void displayPlan(Pane pane, VBox deliveryInfoVBox, Label label, Label messageLabel) {
        pane.getChildren().clear();
        deliveryInfoVBox.getChildren().clear();

        for (Troncon troncon : plan.getListeTroncons()) {
            double startX = longitudeToX(troncon.getOrigine().getLongitude());
            double startY = latitudeToY(troncon.getOrigine().getLatitude());
            double endX = longitudeToX(troncon.getDestination().getLongitude());
            double endY = latitudeToY(troncon.getDestination().getLatitude());

            Line line = new Line(startX, startY, endX, endY);
            line.setStrokeWidth(2);
            line.setStroke(Color.GRAY);
            line.setOnMouseClicked(event -> handleLineClick(event, pane, deliveryInfoVBox, messageLabel));
            pane.getChildren().add(line);
        }

        deliveryInfoVBox.setVisible(true);
        deliveryInfoVBox.getChildren().add(label);
    }

    public void toggleButtons(Button boutonPlus, Button... buttons) {
        if (!button_visible) {
            boutonPlus.setText("x");
            boutonPlus.setStyle("-fx-background-color:GRAY");
            for (Button button : buttons) {
                button.setVisible(true);
            }
            button_visible = true;
        } else {
            boutonPlus.setText("+");
            boutonPlus.setStyle("-fx-background-color:#3498db");
            for (Button button : buttons) {
                button.setVisible(false);
            }
            button_visible = false;
            selectionModeEnabled = false;
        }
    }

    public void displayButtons(BorderPane pane, VBox deliveryInfoVBox, Button... buttons) {
        for (Button button : buttons) {
            if (button != null && !pane.getChildren().contains(button)) {
                pane.getChildren().add(button);
            }
        }
        if (deliveryInfoVBox != null && !pane.getChildren().contains(deliveryInfoVBox)) {
            pane.getChildren().add(deliveryInfoVBox);
        }
    }

    public void toggleSelectionMode(Label label, Button selectionnerPointButton, Button chargerFichierButton, Button chargerNouveauPlan, VBox deliveryInfoVBox) {
        selectionModeEnabled = !selectionModeEnabled;
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        if (!selectionModeEnabled) {
            label.setText("Cliquer sur un point existant pour le supprimer.");
        } else if (entrepotExiste) {
            label.setText("Veuillez selectionner les points de livraison.");
        } else {
            label.setText("Veuillez selectionner en premier un entrepôt, puis les points de livraison.");
        }
    }


    public void handleLineClick(MouseEvent event, Pane pane, VBox deliveryInfoVBox, Label label) {
        if (!selectionModeEnabled) {
            return;
        }

        label.setText(null);
        label.setText("Cliquer sur la carte pour sélectionner un nouveau point de livraison, ou sur un point existant pour le supprimer.");

        double x = event.getX();
        double y = event.getY();
        double lat = yToLatitude(y);
        double lon = xToLongitude(x);

        Intersection intersection = plan.chercherIntersectionLaPlusProche(lat, lon);
        List<Troncon> listeTroncons = intersection.getListeTroncons();

        if (!entrepotExiste) {
            entrepot = new Entrepot(intersection.getId(), "8");
            entrepotExiste = true;

            entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 5, Color.BLUE);
            pane.getChildren().add(entrepotCircle);

            labelEntrepot = new Label("Entrepôt:");

            for (Troncon troncon : listeTroncons) {
                labelEntrepot.setText(labelEntrepot.getText() + troncon.getNomRue() + ", ");
            }

            this.demande.setEntrepot(entrepot);

            deliveryInfoVBox.getChildren().add(labelEntrepot);

            entrepotCircle.setOnMouseClicked(event2 -> handleCircleClick(intersection, pane, deliveryInfoVBox, labelEntrepot));
            labelEntrepot.setOnMouseClicked(event2 -> handleCircleClick(intersection, pane, deliveryInfoVBox, labelEntrepot));

            System.out.println("Entrepôt sélectionné à (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
        } else {
            Livraison livraison = new Livraison(0, intersection.getId(), 5.0, 5.0);
            PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), livraison);

            this.demande.ajouterPointDeLivraison(pdl);
            deliveryInfoVBox.setVisible(true);

            Intersection inter = plan.chercherIntersectionParId(pdl.getId());
            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());

            Label pdLabel = new Label("Point de Livraison:");
            for (Troncon troncon : inter.getListeTroncons()) {
                pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ",");
            }
            deliveryInfoVBox.getChildren().add(pdLabel);

            Circle newPdl = new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));

            pane.getChildren().add(newPdl);

            pdLabel.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));
       
        }

        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            System.out.println("PDL: " + pdl.getId());
        }
    }

    public void displayDemande(Demande demandeFile, Pane pane, VBox deliveryInfoVBox, Label label) {
        if (entrepotExiste) {
            pane.getChildren().remove(entrepotCircle);
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
        }

        label.setVisible(true);
        label.setText("Selectionnez un point de livraison pour le supprimer.");

        entrepotExiste = true;

        entrepot = demandeFile.getEntrepot();
        this.demande.setEntrepot(entrepot);
        
        Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
        double lat = latitudeToY(intersection.getLatitude());
        double lon = longitudeToX(intersection.getLongitude());

        entrepotCircle = new Circle(lon, lat, 5, Color.BLUE);
        pane.getChildren().add(entrepotCircle);

        List<Troncon> listeTroncons = intersection.getListeTroncons();
        labelEntrepot = new Label("Entrepôt:");

        for (Troncon troncon : listeTroncons) {
            labelEntrepot.setText(labelEntrepot.getText() + troncon.getNomRue() + ", ");
        }

        entrepotCircle.setOnMouseClicked(event -> handleCircleClick(intersection, pane, deliveryInfoVBox, labelEntrepot));
        deliveryInfoVBox.getChildren().add(labelEntrepot);
        labelEntrepot.setOnMouseClicked(event -> handleCircleClick(intersection, pane, deliveryInfoVBox, labelEntrepot));

        List<Long> existingPdlIds = this.demande.getListePointDeLivraison().stream().map(PointDeLivraison::getId).collect(Collectors.toList());
        System.out.println("Existing PDLs: " + existingPdlIds);


        for (PointDeLivraison pdl : demandeFile.getListePointDeLivraison()) {
            if (!existingPdlIds.contains(pdl.getId())) {
                Intersection inter = plan.chercherIntersectionParId(pdl.getId());
                double startX = longitudeToX(inter.getLongitude());
                double startY = latitudeToY(inter.getLatitude());           

                Label pdLabel = new Label("Point de Livraison:");
                for (Troncon troncon : inter.getListeTroncons()) {
                    pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                }
                deliveryInfoVBox.getChildren().add(pdLabel);

                this.demande.ajouterPointDeLivraison(pdl);

                pdLabel.setOnMouseClicked(event -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));
                Circle newPdl = new Circle(startX, startY, 5, Color.RED);
                newPdl.setOnMouseClicked(event -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));
                pane.getChildren().add(newPdl);
            }
        }

        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            System.out.println("PDL: " + pdl.getId());
        }
    }


    public void handleCircleClick(Intersection inter, Pane pane, VBox deliveryInfoVBox, Label label) {
        double startX = longitudeToX(inter.getLongitude());
        double startY = latitudeToY(inter.getLatitude());

        if (!popupOuverte) {
            Circle newPdl = new Circle(startX, startY, 8, inter.getId() == entrepot.getId() ? Color.BLUE : Color.RED);
            newPdl.setStrokeWidth(5);
            newPdl.setStroke(inter.getId() == entrepot.getId() ? Color.LIGHTBLUE : Color.CORAL);
            pane.getChildren().add(newPdl);
            popupDelete(startX, startY, inter, newPdl, pane, deliveryInfoVBox, newPdl, label);
        }
        label.setStyle("-fx-background-color: lightblue;");
    }


    public void addMessagePane(Pane pane) {
        pane.getChildren().clear();
        Label message = new Label("Sélectionnez l'entrepôt et les points de livraison.");
        HBox box = new HBox();
        box.setStyle("-fx-background-color:lightblue;");
        box.getChildren().add(message);
        pane.getChildren().add(box);
    }

    public void popupDelete(double x, double y, Intersection inter, Circle circle, Pane pane, VBox deliveryInfoVBox, Circle newPdl, Label pdlLabel) {
        Popup popup = new Popup();

        // Créer le contenu de la pop-up
        Label label = new Label("Voulez-vous supprimer ce point de livraison ?");
        label.setStyle("-fx-background-color: white; -fx-padding: 10;");

        // Bouton pour fermer la pop-up
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction (e ->  { supprimerPointDeLivraison(inter, pane, deliveryInfoVBox, pdlLabel);
            popup.hide();
            popupOuverte = false;
        });

        Button closeButton = new Button("Annuler");
        closeButton.setOnAction(e -> {
            popup.hide();
            pane.getChildren().remove(newPdl);
            newPdl.setStrokeWidth(0);
            newPdl.setRadius(5);
            pane.getChildren().add(newPdl);
            newPdl.setOnMouseClicked(event -> handleCircleClick(inter, pane, deliveryInfoVBox, pdlLabel));
            pdlLabel.setStyle("-fx-background-color: #f5f5f5;");
            popupOuverte = false;
        });

        // Utiliser un HBox pour aligner les boutons côte à côte
        HBox buttonBox = new HBox(30); // 10 est l'espacement entre les boutons
        buttonBox.getChildren().addAll(closeButton, deleteButton);

        // Ajouter les composants dans un VBox
        VBox popupContent = new VBox(0);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10;");
        popupContent.getChildren().addAll(label, buttonBox);

        // Ajouter le VBox à la pop-up
        popup.getContent().add(popupContent);

        popup.setHeight(100);
        popup.setWidth(200);

        if (x > paneWidth / 2) {
            popup.setX(x - 60);
        }
        else popup.setX(x + 260);
        popup.setY(y);

        popupOuverte = true;

        // Afficher la pop-up en relation avec la fenêtre principale
        popup.show(pane.getScene().getWindow());
    }

    public void supprimerPointDeLivraison(Intersection inter, Pane pane, VBox deliveryInfoVBox, Label label) {
        // Supprime le point de livraison de la demande
        this.demande.supprimerIntersection(inter);

        if (inter.getId() == entrepot.getId()) {
            entrepotExiste = false;
            entrepotCircle = null;
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
            pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
        }
        else {
            // Supprime le point de livraison du plan
            pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));

            // Supprime le label du point de livraison
            deliveryInfoVBox.getChildren().remove(label);
        }
        
    }

    private double latitudeToY(double latitude) {
        return paneHeight * (latMax - latitude) / (latMax - latMin);
    }

    private double longitudeToX(double longitude) {
        return paneWidth * (longitude - longMin) / (longMax - longMin);
    }

    private double xToLongitude(double x) {
        return x / paneWidth * (longMax - longMin) + longMin;
    }

    private double yToLatitude(double y) {
        return latMax - (y / paneHeight * (latMax - latMin));
    }

   

    public void calculerChemin(Pane pane, VBox deliveryInfoVBox, Trajet trajet) {
        this.demande.setPlan(this.plan);
        this.demande.initialiserMatriceAdjacence();
        this.demande.creerClusters();

        for (PointDeLivraison pdl : this.demande.getListePointDeLivraison()) {
            System.out.println("PDL: " + pdl.getId());
        }

        // Afficher le chemin
        for (Etape etape : trajet.getListeEtapes()) {
            for (Troncon troncon : etape.getListeTroncons()) {
                double startX = longitudeToX(troncon.getOrigine().getLongitude());
                double startY = latitudeToY(troncon.getOrigine().getLatitude());
                double endX = longitudeToX(troncon.getDestination().getLongitude());
                double endY = latitudeToY(troncon.getDestination().getLatitude());

                Line line = new Line(startX, startY, endX, endY);
                line.setStrokeWidth(5);
                line.setStroke(Color.DODGERBLUE);
                pane.getChildren().add(line);
            }
        }
        
    }
}