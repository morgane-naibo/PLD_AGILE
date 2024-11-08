package controller;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Trajet;
import model.Troncon;
import tsp.RunTSP;
import util.XMLPlan;
import util.XMLDemande;
import model.Demande;
import model.Entrepot;
import view.View;


public class Controller {

    @FXML
    private Button boutonPlus;

    @FXML
    private BorderPane pane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button chargerFichierButton;

    @FXML
    private Button selectionnerPointButton;

    @FXML
    private Button chargerPlan;

    @FXML
    private VBox deliveryInfoVBox;

    @FXML
    private Button chargerNouveauPlan;

    @FXML
    private Button calculerChemin;

    @FXML
    private Label label;

    @FXML 
    private Label messageLabel;

    @FXML
    private Pane mapPane;


    private View view;

    private Demande demande;

    private Plan plan;

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)

    // Variables pour le drag de la carte
    private double initialMouseX;
    private double initialMouseY;
    private double initialTranslateX;
    private double initialTranslateY;

    // Variables pour définir les limites maximales de déplacement
    private static final double MIN_X = 0;
    private static final double MIN_Y = 0;
    private static final double MAX_X = 550; // Remplacez par la limite maximale souhaitée pour X
    private static final double MAX_Y = 600; // Remplacez par la limite maximale souhaitée pour Y

    private Etat etat;

    // Constructeur
    public Controller() {
        this.view = new View();
        this.demande = new Demande();
        this.plan = new Plan();
        this.etat = new PlanNonCharge(this);
    }

    // Méthode d'initialisation appelée après le chargement du FXML
    
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) deliveryInfoVBox.getScene().getWindow();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                deliveryInfoVBox.setPrefWidth(newVal.doubleValue() * 0.4);
            });

            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                deliveryInfoVBox.setPrefHeight(newVal.doubleValue());
            });
        });
        // Appliquer la transformation de zoom uniquement sur mapPane
        if (mapPane != null) {
            mapPane.getTransforms().add(scale);
            mapPane.setOnScroll(event -> handleZoom(event.getDeltaY()));
            
            // Ajout de la fonctionnalité de drag
            mapPane.setOnMousePressed(this::handleMousePressed);
            mapPane.setOnMouseDragged(this::handleMouseDragged);
        }
    }
    
    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public View getView() {
        return this.view;
    }

    public Button getBoutonPlus() {
        return boutonPlus;
    }

    public BorderPane getPane() {
        return pane;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Button getChargerFichierButton() {
        return chargerFichierButton;
    }

    public Button getSelectionnerPointButton() {
        return selectionnerPointButton;
    }

    public Button getChargerPlan() {
        return chargerPlan;
    }

    public VBox getDeliveryInfoVBox() {
        return deliveryInfoVBox;
    }

    public Button getChargerNouveauPlan() {
        return chargerNouveauPlan;
    }

    public Button getCalculerChemin() {
        return calculerChemin;
    }

    public Label getLabel() {
        return label;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public Etat getEtat() {
        return etat;
    }


// Gestionnaire d'événements pour le clic de souris
private void handleMousePressed(MouseEvent event) {
    initialMouseX = event.getSceneX();
    initialMouseY = event.getSceneY();
    initialTranslateX = mapPane.getTranslateX();
    initialTranslateY = mapPane.getTranslateY();

    // Ajustement initial pour rester dans les limites
    if (initialTranslateX < MIN_X) {
        mapPane.setTranslateX(MIN_X);
    } 
    else if (initialTranslateX > MAX_X) {
        mapPane.setTranslateX(MAX_X);
    }

    if (initialTranslateY < MIN_Y) {
        mapPane.setTranslateY(MIN_Y);
    } 
    else if (initialTranslateY > MAX_Y) {
        mapPane.setTranslateY(MAX_Y);
    }
}

private void handleMouseDragged(MouseEvent event) {
    double deltaX = event.getSceneX() - initialMouseX;
    double deltaY = event.getSceneY() - initialMouseY;

    // Coordonnées potentielles après le glissement
    double newTranslateX = initialTranslateX + deltaX;
    double newTranslateY = initialTranslateY + deltaY;

    // Dimensions de mapPane et de son conteneur (ex. AnchorPane ou autre parent)
    double paneWidth = mapPane.getBoundsInParent().getWidth();
    double paneHeight = mapPane.getBoundsInParent().getHeight();
    double containerWidth = ((Pane) mapPane.getParent()).getWidth();
    double containerHeight = ((Pane) mapPane.getParent()).getHeight();

    // Limiter le déplacement pour que la carte reste visible
    if (newTranslateX > 0) {
        newTranslateX = 0;
    } else if (newTranslateX < containerWidth - paneWidth) {
        newTranslateX = containerWidth - paneWidth;
    }

    if (newTranslateY > 0) {
        newTranslateY = 0;
    } else if (newTranslateY < containerHeight - paneHeight) {
        newTranslateY = containerHeight - paneHeight;
    }

    // Appliquer les nouvelles coordonnées
    mapPane.setTranslateX(newTranslateX);
    mapPane.setTranslateY(newTranslateY);
}


    // Click sur le bouton "charger un (nouveau) plan"
    @FXML
    public void handleLoadPlan() {
        etat.handleLoadPlan();
        System.out.println(etat);
    }

    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
    }

    @FXML
    public void handleSelectButton() {
        etat.handleSelectButton();
        System.out.println(etat);

    }

    @FXML
    public void handleFileButton() {
        etat.handleFileButton();
        System.out.println(etat);

    }  

    @FXML
    public void calculerChemin() {
        etat.calculerChemin();
        System.out.println(etat);

    }


    private void handleZoom(double deltaY) {
        double zoomFactor = 1.05;
        double minZoom = 1.0;
        double maxZoom = 3.0;
    
        // Sauvegarder les échelles actuelles
        double oldScale = scale.getX();
    
        // Calculer le centre visuel actuel du conteneur
        double containerWidth = ((Pane) mapPane.getParent()).getWidth();
        double containerHeight = ((Pane) mapPane.getParent()).getHeight();
        double centerX = containerWidth / 2 - mapPane.getTranslateX();
        double centerY = containerHeight / 2 - mapPane.getTranslateY();
    
        // Calculer le ratio de zoom
        double newScale;
        if (deltaY > 0) { // Zoom avant
            newScale = Math.min(oldScale * zoomFactor, maxZoom);
        } else { // Zoom arrière
            newScale = Math.max(oldScale / zoomFactor, minZoom);
        }
    
        // Appliquer le nouveau scale
        scale.setX(newScale);
        scale.setY(newScale);
    
        // Calculer la différence de scale
        double scaleRatio = newScale / oldScale;
    
        // Ajuster la translation pour recentrer la carte après le zoom
        double newTranslateX = centerX - centerX * scaleRatio;
        double newTranslateY = centerY - centerY * scaleRatio;
    
        mapPane.setTranslateX(mapPane.getTranslateX() + newTranslateX);
        mapPane.setTranslateY(mapPane.getTranslateY() + newTranslateY);
    
        // Limiter la translation pour ne pas sortir des bords du conteneur
        double mapWidth = mapPane.getBoundsInLocal().getWidth() * newScale;
        double mapHeight = mapPane.getBoundsInLocal().getHeight() * newScale;
    
        // Limiter la position de mapPane pour qu'il reste dans les limites du conteneur
        if (mapPane.getTranslateX() > 0) {
            mapPane.setTranslateX(0);
        } else if (mapPane.getTranslateX() < containerWidth - mapWidth) {
            mapPane.setTranslateX(containerWidth - mapWidth);
        }
    
        if (mapPane.getTranslateY() > 0) {
            mapPane.setTranslateY(0);
        } else if (mapPane.getTranslateY() < containerHeight - mapHeight) {
            mapPane.setTranslateY(containerHeight - mapHeight);
        }
    }
 
}
