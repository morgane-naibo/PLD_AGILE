package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import util.XMLPlan;
import util.XMLDemande;
import model.Demande;
import model.Entrepot;
import view.View;

public class Controller {

    @FXML
    private Button boutonPlus;

    @FXML
    private AnchorPane pane;

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
    private Label label;

    @FXML 
    private Pane messagePane;

    private View view;

    // Constructeur
    public Controller() {
        this.view = new View();
    }

    // Click sur le bouton "charger un (nouveau) plan"
    @FXML
    public void handleLoadPlan() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\PC\\source\\repos\\PLD-Agile\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadPlan(file.getPath());
        }
    }

    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
        if (plan != null) {
            view.setPlan(plan);
            view.displayPlan(pane, deliveryInfoVBox, label);
            view.displayButtons(pane, deliveryInfoVBox, boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
            pane.getChildren().add(messagePane);
            boutonPlus.setVisible(true);
            messagePane.setVisible(true);
        }
    }

    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
    }

    @FXML
    public void handleSelectButton() {
        view.toggleSelectionMode(messagePane, selectionnerPointButton, chargerFichierButton, chargerNouveauPlan, deliveryInfoVBox);
    }

    @FXML
    public void handleLineClick(MouseEvent event) {
        view.handleLineClick(event, pane, deliveryInfoVBox, label);
    }

    @FXML
    public void handleFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\PC\\source\\repos\\PLD-Agile\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadDemande(file.getPath());
        }
    }

    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        Demande demande = xmlDemande.parse(filePath);
        if (demande != null) {
            view.displayDemande(demande, pane, deliveryInfoVBox);
        }
    }

    public void handleLabelClick(Intersection inter) {
        view.handleLabelClick(inter, pane, deliveryInfoVBox);
    }

}