package controller;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
import javafx.scene.transform.Scale;
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
import javafx.scene.layout.BorderPane;

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

    @FXML
    private Group mapGroup;

    private View view;

    private Demande demande;

    private Plan plan;

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)

    // Constructeur
    public Controller() {
        this.view = new View();
        this.demande = new Demande();
        this.plan = new Plan();
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
        }
    }

    // Click sur le bouton "charger un (nouveau) plan"
    @FXML
    public void handleLoadPlan() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadPlan(file.getPath());
        }
    }

    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        plan = xmlPlan.parse(filePath);
        if (plan != null) {
            view.setPlan(plan);
            view.displayPlan(mapPane, deliveryInfoVBox, label, messageLabel); // Afficher le plan dans mapPane
            view.displayButtons(pane, deliveryInfoVBox, boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
            boutonPlus.setVisible(true);
        }
    }

    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
    }

    @FXML
    public void handleSelectButton() {
        messageLabel.setVisible(true);
        view.toggleSelectionMode(messageLabel, selectionnerPointButton, chargerFichierButton, chargerNouveauPlan, deliveryInfoVBox);
    }

    @FXML
    public void handleFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadDemande(file.getPath());
        }
    }

    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        Demande demandeFile = xmlDemande.parse(filePath);
        if (demandeFile != null) {
            view.demande.setPlan(plan);
            this.demande.setPlan(plan);
            view.displayDemande(demandeFile, mapPane, deliveryInfoVBox, messageLabel);
        }
        this.demande = view.demande;
    }

    

    /*public void handleLabelClick(Intersection inter) {
        view.handleLabelClick(inter, pane, deliveryInfoVBox);
    }*/


    private void handleZoom(double deltaY) {
        double zoomFactor = 1.05;
        double minZoom = 1.0;
        double maxZoom = 3.0;
    
        if (deltaY > 0) { // Zoom avant
            if (scale.getX() < maxZoom) {
                scale.setX(scale.getX() * zoomFactor);
                scale.setY(scale.getY() * zoomFactor);
            }
        } else { // Zoom arrière
            if (scale.getX() > minZoom) {
                scale.setX(scale.getX() / zoomFactor);
                scale.setY(scale.getY() / zoomFactor);
            }
        }
    }

    @FXML
    public void calculerChemin() {
        demande.setPlan(plan);
        demande.initialiserMatriceAdjacence();
        demande.creerClusters();
        RunTSP run = new RunTSP();
        for (int i=0; i<demande.getNbLivreurs(); i++) {
            Trajet trajet = run.calculerTSP(demande.getListeMatriceAdjacence().get(i));
            view.calculerChemin(mapPane, deliveryInfoVBox, trajet);
        }
    }

}