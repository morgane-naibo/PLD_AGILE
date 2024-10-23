package main.java.controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import main.java.util.XMLPlan;
import main.java.model.Intersection;
import main.java.model.Livraison;
import main.java.model.Plan;
import main.java.model.PointDeLivraison;
import main.java.model.Troncon;
import main.java.util.XMLDemande;
import main.java.model.Demande;
import main.java.model.Entrepot;
import main.java.model.Livraison;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

    private double paneWidth = 550;
    private double paneHeight = 600;

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

    private Circle entrepotCircle;
    private Label labelEntrepot;

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
        plan = xmlPlan.parse(filePath);
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
        entrepotExiste = false;
        chargerPlan.setVisible(false);

        pane.getChildren().clear();
        deliveryInfoVBox.getChildren().clear();

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

            line.setOnMouseClicked(event -> handleLineClick(event));
    
            // Ajoute la ligne à l'AnchorPane
            pane.getChildren().add(line);
        }

        deliveryInfoVBox.setVisible(true);
        deliveryInfoVBox.getChildren().add(label);
        pane.getChildren().add(boutonPlus);
        boutonPlus.setVisible(true);
        pane.getChildren().add(chargerFichierButton);
        pane.getChildren().add(selectionnerPointButton);
        pane.getChildren().add(chargerNouveauPlan);
        pane.getChildren().add(deliveryInfoVBox);
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
            chargerNouveauPlan.setVisible(true);
            button_visible = true;
            selectionModeEnabled = false;
        }
        else {
            boutonPlus.setText("+");
            chargerFichierButton.setVisible(false);
            selectionnerPointButton.setVisible(false);
            chargerNouveauPlan.setVisible(false);
            button_visible = false;
            selectionModeEnabled = false;
        }
       
    }

    @FXML
    public void handleSelectButton() {
        selectionModeEnabled = !selectionModeEnabled;
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        showInfoPopup();
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
        demande = xmlDemande.parse(filePath);
        if (demande != null) {
            System.out.println("Demande chargée avec succès !");
            displayDemande(demande);
        }
    }

    private void displayDemande(Demande demande) {
        if (entrepotExiste) {
            pane.getChildren().remove(entrepotCircle);
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
        }

        boutonPlus.setVisible(true);
        chargerFichierButton.setVisible(true);
        selectionnerPointButton.setVisible(true);
        chargerNouveauPlan.setVisible(true);
        deliveryInfoVBox.setVisible(true);

        entrepotExiste = true;

        entrepot = demande.getEntrepot();
        Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
        double lat = latitudeToY(intersection.getLatitude());
        double lon = longitudeToX(intersection.getLongitude());

        entrepotCircle = new Circle(lon, lat, 5, Color.BLUE);
        pane.getChildren().add(entrepotCircle);

        System.out.println("Entrepot Point: (" + lon + ", " + lat + ")");

        labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");

        deliveryInfoVBox.getChildren().add(labelEntrepot);

        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            // Coordonnées de l'intersection d'origine
            Intersection inter = plan.chercherIntersectionParId(pdl.getId());

            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());
    
            // Crée une ligne pour représenter le tronçon
            Circle newPdl= new Circle(startX, startY, 5, Color.RED);

            System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

            deliveryInfoVBox.getChildren().add(new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")"));
            pane.getChildren().add(newPdl);
        }
    }

    @FXML
    public void handleLineClick(MouseEvent event) {
        if (!selectionModeEnabled) {
            return; // Ignore le clic si le mode de sélection n'est pas activé
        }

        double x=event.getX();
        double y=event.getY();

        double lat = yToLatitude(y);
        double lon = xToLongitude(x);

        Intersection intersection = plan.chercherIntersectionLaPlusProche(lat, lon);

        if (!entrepotExiste) {
            // Crée l'entrepôt
            entrepot = new Entrepot(intersection.getId(), "8");
    
            // Marquer l'entrepôt comme défini
            entrepotExiste = true;
    
            // Afficher l'entrepôt sur le plan
            entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 5, Color.BLUE);
            pane.getChildren().add(entrepotCircle);
    
            // Ajouter des informations dans l'interface utilisateur
            labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(labelEntrepot);
    
            System.out.println("Entrepôt sélectionné à (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
        } else {
            Livraison livraison = new Livraison(0, intersection.getId(), 5.0, 5.0);
            PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), livraison);
            if (demande == null) {
                demande = new Demande();
            }
            demande.ajouterPointDeLivraison(pdl);
            deliveryInfoVBox.setVisible(true);

            Intersection inter = plan.chercherIntersectionParId(pdl.getId());

            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());
    
            // Crée une ligne pour représenter le tronçon
            Circle newPdl= new Circle(startX, startY, 5, Color.RED);

            System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

            deliveryInfoVBox.getChildren().add(new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")"));
            pane.getChildren().add(newPdl);
        
        }
    }

    private double yToLatitude(double y) {
        return (y / paneHeight) * (latMax - latMin) + latMin;
    }

    private double xToLongitude(double x) {
        return (x / paneWidth) * (longMax - longMin) + longMin;
    }


    public void showInfoPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez en premier lieu sélectionner l'entrepôt, puis les points de livraison.");
        alert.showAndWait(); // Affiche la fenêtre et attend la fermeture
    }
}