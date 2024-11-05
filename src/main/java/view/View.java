package view;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Popup;
import javafx.scene.input.MouseEvent;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import model.Demande;
import model.Entrepot;

public class View {

    private double paneWidth = 550.0;
    private double paneHeight = 550.0;
    private double latMin;
    private double latMax;
    private double longMin;
    private double longMax;

    private Plan plan;
    private Demande demande;
    private Entrepot entrepot;

    private boolean button_visible = false;
    private boolean selectionModeEnabled = false;
    private boolean entrepotExiste = false;
    private boolean popupOuverte = false;

    private Circle entrepotCircle;
    private Label labelEntrepot;

    public void fileChooser() {
        // Implémentez la logique pour charger un fichier
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Fichier chargé avec succès !");
        alert.showAndWait(); // Affiche la fenêtre et attend la fermeture
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
        latMin = plan.trouverLatitudeMin();
        latMax = plan.trouverLatitudeMax();
        longMin = plan.trouverLongitudeMin();
        longMax = plan.trouverLongitudeMax();
    }

    public void displayPlan(AnchorPane pane, VBox deliveryInfoVBox, Label label) {
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
            line.setOnMouseClicked(event -> handleLineClick(event, pane, deliveryInfoVBox, label));
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

    public void displayButtons(AnchorPane pane, VBox deliveryInfoVBox, Button... buttons) {
        for (Button button : buttons) {
            pane.getChildren().add(button);
        }
        pane.getChildren().add(deliveryInfoVBox);
    }

    public void toggleSelectionMode(Pane pane, Button selectionnerPointButton, Button chargerFichierButton, Button chargerNouveauPlan, VBox deliveryInfoVBox) {
        selectionModeEnabled = !selectionModeEnabled;
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        addMessagePane(pane);
    }

    public void handleLineClick(MouseEvent event, AnchorPane pane, VBox deliveryInfoVBox, Label label) {
        // Implementer la logique pour gérer le clic sur une ligne
        if (!selectionModeEnabled) {
            return; // Ignore le clic si le mode de sélection n'est pas activé
        }
    
        double x=event.getX();
        double y=event.getY();
    
        // Convertit les coordonnées X et Y en latitude et longitude
        double lat = yToLatitude(y);
        double lon = xToLongitude(x);
    
        // Cherche l'intersection la plus proche des coordonnées cliquées
        Intersection intersection = plan.chercherIntersectionLaPlusProche(lat, lon);
    
        // Si l'entrepôt n'existe pas, on le crée en premier
        if (!entrepotExiste) {
            // Crée l'entrepôt
            entrepot = new Entrepot(intersection.getId(), "8");
    
            // Marquer l'entrepôt comme défini
            entrepotExiste = true;
    
            // Afficher l'entrepôt sur le plan
            entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 5, Color.BLUE);
            pane.getChildren().add(entrepotCircle);
            entrepotCircle.setOnMouseClicked(event2 -> handleLabelClick(intersection, pane, deliveryInfoVBox));
    
            // Afficher l'entrepôt dans la vue textuelle
            labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(labelEntrepot);
            labelEntrepot.setOnMouseClicked(event2 -> handleLabelClick(intersection, pane, deliveryInfoVBox));
    
            System.out.println("Entrepôt sélectionné à (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
            } else {
                // Sinon on crée un point de livraison
                Livraison livraison = new Livraison(0, intersection.getId(), 5.0, 5.0);
                PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), livraison);
                if (demande == null) {
                    // Crée une nouvelle demande si elle n'existe pas
                    demande = new Demande();
                }
                // Ajoute le point de livraison à la demande
                demande.ajouterPointDeLivraison(pdl);
                deliveryInfoVBox.setVisible(true);
    
                // Affiche le point de livraison sur le plan
                Intersection inter = plan.chercherIntersectionParId(pdl.getId());
    
                double startX = longitudeToX(inter.getLongitude());
                double startY = latitudeToY(inter.getLatitude());
    
                // Crée un cerlce pour représenter le point de livraison
                Circle newPdl= new Circle(startX, startY, 5, Color.RED);
                newPdl.setOnMouseClicked(event2 -> handleLabelClick(inter, pane, deliveryInfoVBox));
    
                System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");
    
                Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");
    
                deliveryInfoVBox.getChildren().add(pdLabel);
                pane.getChildren().add(newPdl);
    
                // Ajoute un événement de clic sur le label
                pdLabel.setOnMouseClicked(event2 -> handleLabelClick(inter, pane, deliveryInfoVBox));
            }
    }

    public void displayDemande(Demande demande, AnchorPane pane, VBox deliveryInfoVBox) {
        // Implémentez la logique pour afficher la demande sur l'interface graphique
        // Supprime l'entrepôt si il existe déjà (sélectionné à la main)
        if (entrepotExiste) {
            pane.getChildren().remove(entrepotCircle);
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
        }


        button_visible = false;


        entrepotExiste = true;

        // Affiche l'entrepôt
        entrepot = demande.getEntrepot();
        Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
        double lat = latitudeToY(intersection.getLatitude());
        double lon = longitudeToX(intersection.getLongitude());

        entrepotCircle = new Circle(lon, lat, 5, Color.BLUE);
        pane.getChildren().add(entrepotCircle);
        entrepotCircle.setOnMouseClicked(event -> handleLabelClick(intersection, pane, deliveryInfoVBox));

        System.out.println("Entrepot Point: (" + lon + ", " + lat + ")");

        labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");

        deliveryInfoVBox.getChildren().add(labelEntrepot);
        labelEntrepot.setOnMouseClicked(event -> handleLabelClick(intersection, pane, deliveryInfoVBox));


        // Affiche les points de livraison
        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            Intersection inter = plan.chercherIntersectionParId(pdl.getId());

            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());
    
            // Crée un cercle pour représenter le point de livraison
            Circle newPdl= new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event -> handleLabelClick(inter, pane, deliveryInfoVBox));

            System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

            Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(pdLabel);

            // Ajoute un événement de clic sur le label
            pdLabel.setOnMouseClicked(event -> handleLabelClick(inter, pane, deliveryInfoVBox));

            pane.getChildren().add(newPdl);
        }
    }

    public void handleLabelClick(Intersection inter, AnchorPane pane, VBox deliveryInfoVBox) {
        // Implémentez la logique pour gérer le clic sur un label
        double startX = longitudeToX(inter.getLongitude());
        double startY = latitudeToY(inter.getLatitude());
    
        // Met en surbrillance le point de livraison sélectionné
        if (!popupOuverte) {
            if (inter.getId() == entrepot.getId()) {
                entrepotCircle = new Circle(startX, startY, 8, Color.BLUE);
                entrepotCircle.setStrokeWidth(5);
                entrepotCircle.setStroke(Color.LIGHTBLUE);
                pane.getChildren().add(entrepotCircle);
                popupDelete(startX, startY, inter, entrepotCircle, pane, deliveryInfoVBox);
            }
            else {
                Circle newPdl= new Circle(startX, startY, 8, Color.RED);
                newPdl.setStrokeWidth(5);
                newPdl.setStroke(Color.CORAL);
                pane.getChildren().add(newPdl);
                popupDelete(startX, startY, inter, newPdl, pane, deliveryInfoVBox);
            }
        } else {
            return;
        }
    }


     public void popupDelete(Double x, Double y, Intersection inter, Circle newPdl, AnchorPane pane, VBox deliveryInfoVBox) {

        Popup popup = new Popup();

        // Créer le contenu de la pop-up
        Label label = new Label("Voulez-vous supprimer ce point de livraison ?");
        label.setStyle("-fx-background-color: white; -fx-padding: 10;");

        // Bouton pour fermer la pop-up
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction (e ->  { supprimerPointDeLivraison(inter, pane, deliveryInfoVBox);
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
            newPdl.setOnMouseClicked(event -> handleLabelClick(inter, pane, deliveryInfoVBox));
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


    public void addMessagePane(Pane pane) {
        pane.getChildren().clear();
        Label message;
        if (entrepotExiste) {
            message = new Label("Cliquez sur une intersection pour ajouter un point de livraison");
        }
        else {
            message = new Label("Cliquez sur une intersection pour ajouter l'entrepôt, puis les points de livraison");
        }
        message.setStyle("fx-font-size: 26px;");
        pane.getChildren().add(message);
    }

    private double longitudeToX(double longitude) {
        return ((longitude - longMin) / (longMax - longMin)) * (paneWidth);
    }

    private double latitudeToY(double latitude) {
        return 50+(paneHeight - ((latitude - (latMin)) / (latMax - latMin)) * (paneHeight));
    }

        // Convertit les coordonnées Y en latitude
        private double yToLatitude(double y) {
            return 100+ ((paneHeight - y) / paneHeight) * (latMax - latMin) + latMin;    
        }
    
        // Convertit les coordonnées X en longitude
        private double xToLongitude(double x) {
            return (x / paneWidth) * (longMax - longMin) + longMin;
        }

        public void supprimerPointDeLivraison(Intersection inter, AnchorPane pane, VBox deliveryInfoVBox) {
            // Suppression de la ligne de la vue
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
                deliveryInfoVBox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().contains(inter.getLongitude() + ", " + inter.getLatitude()));
            }
        }
        
}