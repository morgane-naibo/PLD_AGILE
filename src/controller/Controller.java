package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import util.XMLPlan;
import model.Intersection;
import model.Plan;
import model.Troncon;


public class Controller {

    @FXML
    private Button boutonPlus;

    @FXML
    private Line myLine;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button chargerFichierButton;

    @FXML
    private Button selectionnerPointButton;

    @FXML
    private Button chargerPlan;

    private double paneWidth = 800;
    private double paneHeight = 600;

    private double latMin;
    private double latMax;
    private double longMin;
    private double longMax;

    private boolean button_visible = false;

    @FXML
    public void handleLoadPlan() {
        // Ouverture d'un sélecteur de fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\PC\\source\\repos\\PLD-Agile\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Charge et parse le plan depuis le fichier sélectionné
            loadPlan(file.getPath());
        }
    }

    private void loadPlan(String filePath) {
        XMLPlan xmlPlan = new XMLPlan();
        Plan plan = xmlPlan.parse(filePath);
        if (plan != null) {
            latMin = plan.trouverLatitudeMin();
            latMax = plan.trouverLatitudeMax();
            longMin = plan.trouverLongitudeMin();
            longMax = plan.trouverLongitudeMax();
            System.out.println("Plan chargé avec succès !");
            displayPlan(plan);
        }
    }

    private void displayPlan(Plan plan) {
        pane.getChildren().clear(); // Efface le contenu actuel du Pane
        

        // Ajoute les tronçons au Pane
        for (Troncon troncon : plan.getListeTroncons()) {
            // Coordonnées de l'intersection d'origine
            double startX = longitudeToX(troncon.getOrigine().getLongitude());
            double startY = latitudeToY(troncon.getOrigine().getLatitude());
    
            // Coordonnées de l'intersection de destination
            double endX = longitudeToX(troncon.getDestination().getLongitude());
            double endY = latitudeToY(troncon.getDestination().getLatitude());
    
            // Crée une ligne pour représenter le tronçon
            Line line = new Line(startX, startY, endX, endY);
            line.setStrokeWidth(2); // Largeur de la ligne
            line.setStroke(Color.GRAY); // Couleur grise pour les tronçons
    
            // Ajoute la ligne à l'AnchorPane
            pane.getChildren().add(line);
        }
        pane.getChildren().add(boutonPlus);
        pane.getChildren().add(chargerFichierButton);
        pane.getChildren().add(selectionnerPointButton);
    }
    
    // Méthode pour convertir la longitude en position X dans le Pane
    private double longitudeToX(double longitude) {
        // Exemple de conversion de longitude en position X
        return ((longitude - longMin) / (longMax - longMin)) * (paneWidth) ;    }
    
    // Méthode pour convertir la latitude en position Y dans le Pane
    private double latitudeToY(double latitude) {
        // Exemple de conversion de latitude en position Y
        return ((latitude - (latMin)) / (latMax - latMin)) * (paneHeight);    }


    @FXML
    public void handleButtonClick() {
        if (!button_visible) {
            boutonPlus.setText("x");
            chargerFichierButton.setVisible(true);
            selectionnerPointButton.setVisible(true);
            button_visible = true;
        }
        else {
            boutonPlus.setText("+");
            chargerFichierButton.setVisible(false);
            selectionnerPointButton.setVisible(false);
            button_visible = false;
        }
       
    }

    @FXML
    public void handleSelectButton() {
        myLine.setVisible(true);
    }

    @FXML
    public void handleFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\PC\\source\\repos\\PLD-Agile\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
    }

    @FXML
    public void handleLineClick(MouseEvent event) {
        double x=event.getX();
        double y=event.getY();

        // Create a new Circle at the clicked position
        Circle newPoint = new Circle(x, y, 5, Color.RED);
            
        // Add the new Circle to the Pane
        pane.getChildren().add(newPoint);

        System.out.println("Point added at (" + x + ", " + y + ")");
        
        System.out.println("Line clicked!");
    }

}