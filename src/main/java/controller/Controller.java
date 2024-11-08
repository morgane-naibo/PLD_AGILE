package controller;

import java.io.File;
import java.io.FileNotFoundException;

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
import util.XMLPlan;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import util.XMLDemande;
import model.Demande;
import model.Entrepot;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import exceptions.IDIntersectionException;
import java.lang.Exception;

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadPlan(file.getPath());
        }
    }

    private void loadPlan(String filePath) {
        try {
            XMLPlan xmlPlan = new XMLPlan();
            plan = xmlPlan.parse(filePath);
            if (plan != null) {
                latMin = plan.trouverLatitudeMin();
                latMax = plan.trouverLatitudeMax();
                longMin = plan.trouverLongitudeMin();
                longMax = plan.trouverLongitudeMax();
                System.out.println("Plan chargé avec succès !");
                displayPlan(plan);
            } else {
                throw new FileNotFoundException("Plan non trouvé ou fichier invalide.");
            }
        } catch (Exception e) {
            showErrorPopup("Erreur lors du chargement du plan", e.getMessage());
        }
    }

    private void displayPlan(Plan plan) {
        try {
            entrepotExiste = false;
            chargerPlan.setVisible(false);

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
                line.setOnMouseClicked(event -> handleLineClick(event));

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
        } catch (Exception e) {
            showErrorPopup("Erreur d'affichage du plan", e.getMessage());
        }
    }

    private double longitudeToX(double longitude) {
        return ((longitude - longMin) / (longMax - longMin)) * paneWidth;
    }

    private double latitudeToY(double latitude) {
        return ((latitude - latMin) / (latMax - latMin)) * paneHeight;
    }

    @FXML
    public void handleButtonClick() {
        if (!button_visible) {
            boutonPlus.setText("x");
            chargerFichierButton.setVisible(true);
            selectionnerPointButton.setVisible(true);
            chargerNouveauPlan.setVisible(true);
            button_visible = true;
            selectionModeEnabled = false;
        } else {
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
        fileChooser.setInitialDirectory(new File("C:\\Users\\mathi\\Documents\\INSA\\IF4\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadDemande(file.getPath());
        }
    }

    private void loadDemande(String filePath) {
        try {
            XMLDemande xmlDemande = new XMLDemande();
            demande = xmlDemande.parse(filePath);
            if (demande != null) {
                System.out.println("Demande chargée avec succès !");
                displayDemande(demande);
            } else {
                throw new FileNotFoundException("Demande non trouvée ou fichier invalide.");
            }
        } catch (Exception e) {
            showErrorPopup("Erreur lors du chargement de la demande", e.getMessage());
        }
    }

    private void displayDemande(Demande demande) throws IDIntersectionException {
        try {
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
                Intersection inter = plan.chercherIntersectionParId(pdl.getId());

                double startX = longitudeToX(inter.getLongitude());
                double startY = latitudeToY(inter.getLatitude());

                Circle newPdl = new Circle(startX, startY, 5, Color.RED);

                System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

                deliveryInfoVBox.getChildren().add(new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")"));
                pane.getChildren().add(newPdl);
            }
        } catch (IDIntersectionException e) {
            showErrorPopup("Erreur d'ID d'intersection", e.getMessage());
        } catch (Exception e) {
            showErrorPopup("Erreur lors de l'affichage de la demande", e.getMessage());
        }
    }

    @FXML
    public void handleLineClick(MouseEvent event) {
        try {
            if (!selectionModeEnabled) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            double lat = yToLatitude(y);
            double lon = xToLongitude(x);

            Intersection intersection = plan.chercherIntersectionLaPlusProche(lat, lon);
            if (intersection == null) {
                throw new IDIntersectionException("Aucune intersection trouvée pour ces coordonnées.");
            }

            if (!entrepotExiste) {
                entrepot = new Entrepot(intersection.getId(), "8");
                entrepotExiste = true;

                entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 5, Color.BLUE);
                pane.getChildren().add(entrepotCircle);

                labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
                deliveryInfoVBox.getChildren().add(labelEntrepot);
            }
        } catch (IDIntersectionException e) {
            showErrorPopup("Erreur d'intersection", e.getMessage());
        } catch (Exception e) {
            showErrorPopup("Erreur inconnue", e.getMessage());
        }
    }

    private double yToLatitude(double y) {
        return latMin + ((latMax - latMin) * (y / paneHeight));
    }
    
    private double xToLongitude(double x) {
        return longMin + ((longMax - longMin) * (x / paneWidth));
    }
    
    public void showErrorPopup(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mode Sélection");
        alert.setHeaderText(null);
        alert.setContentText("Mode de sélection activé, cliquez sur une ligne pour sélectionner un point.");
        alert.showAndWait();
    }
}
