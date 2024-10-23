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
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import util.XMLPlan;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import util.XMLDemande;
import model.Demande;
import model.Entrepot;
import javafx.scene.Scene;
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

    // Taille du Pane
    private double paneWidth = 550.0;
    private double paneHeight = 600.0;

    // Coordonnées minimales et maximales du plan (pour la conversion)
    private double latMin;
    private double latMax;
    private double longMin;
    private double longMax;

    // Variables pour stocker le plan, la demande et l'entrepôt
    private Plan plan;
    private Demande demande;
    private Entrepot entrepot;

    // Variables pour gérer l'affichage des boutons et des informations
    private boolean button_visible = false;
    private boolean selectionModeEnabled = false;
    private boolean entrepotExiste = false;

    // Variables pour afficher l'entrepôt
    private Circle entrepotCircle;
    private Label labelEntrepot;


    // Click sur le bouton "charger un (nouveau) plan"
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

    // Charge et parse le plan depuis le fichier sélectionné
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

    // Affiche le plan sur l'interface graphique
    private void displayPlan(Plan plan) {
        // Réinitialise l'interface graphique
        entrepotExiste = false;
        pane.getChildren().clear();
        deliveryInfoVBox.getChildren().clear();

        // Ajoute les tronçons au Pane
        for (Troncon troncon : plan.getListeTroncons()) {

            // Coordonnées de l'intersection d'origine converties en position X et Y
            double startX = longitudeToX(troncon.getOrigine().getLongitude());
            double startY = latitudeToY(troncon.getOrigine().getLatitude());
    
            // Coordonnées de l'intersection de destination converties en position X et Y
            double endX = longitudeToX(troncon.getDestination().getLongitude());
            double endY = latitudeToY(troncon.getDestination().getLatitude());
    
            // Crée une ligne pour représenter le tronçon
            Line line = new Line(startX, startY, endX, endY);
            line.setStrokeWidth(2); // Largeur de la ligne
            line.setStroke(Color.GRAY); // Couleur grise pour les tronçons

            // Ajoute un événement de clic sur la ligne
            line.setOnMouseClicked(event -> handleLineClick(event));
    
            // Ajoute la ligne à l'AnchorPane
            pane.getChildren().add(line);
        }

        // Affiche les boutons et les informations
        deliveryInfoVBox.setVisible(true);
        deliveryInfoVBox.getChildren().add(label);
        pane.getChildren().add(boutonPlus);
        boutonPlus.setVisible(true);
        pane.getChildren().add(chargerFichierButton);
        pane.getChildren().add(selectionnerPointButton);
        pane.getChildren().add(chargerNouveauPlan);
        pane.getChildren().add(deliveryInfoVBox);
    }
    

    // Click sur le bouton "+"
    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        if (!button_visible) {
            boutonPlus.setText("x");
            chargerFichierButton.setVisible(true);
            selectionnerPointButton.setVisible(true);
            chargerNouveauPlan.setVisible(true);
            button_visible = true;
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

    // Click sur le bouton "Sélectionner un point"
    @FXML
    public void handleSelectButton() {
        // Active ou désactive le mode de sélection
        selectionModeEnabled = !selectionModeEnabled;
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        // Affiche une popup d'information
        showInfoPopup();
    }



// Click sur une ligne du plan
@FXML
public void handleLineClick(MouseEvent event) {
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

        // Afficher l'entrepôt dans la vue textuelle
        labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
        deliveryInfoVBox.getChildren().add(labelEntrepot);

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
        newPdl.setOnMouseClicked(event2 -> handleLabelClick(inter));

        System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

        Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");

        deliveryInfoVBox.getChildren().add(pdLabel);
        pane.getChildren().add(newPdl);

        // Ajoute un événement de clic sur le label
        pdLabel.setOnMouseClicked(event2 -> handleLabelClick(inter));
    }
}




    // Click sur le bouton "Charger un fichier de demandes"
    @FXML
    public void handleFileButton() {
        // Ouvre un sélecteur de fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\PC\\source\\repos\\PLD-Agile\\PLD_AGILE\\resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadDemande(file.getPath());
        }
    }

    // Charge et parse la demande depuis le fichier sélectionné
    private void loadDemande(String filePath) {
        XMLDemande xmlDemande = new XMLDemande();
        demande = xmlDemande.parse(filePath);
        if (demande != null) {
            System.out.println("Demande chargée avec succès !");
            displayDemande(demande);
        }
    }

    // Affiche la demande sur l'interface graphique et textuelle
    private void displayDemande(Demande demande) {

        // Supprime l'entrepôt si il existe déjà (sélectionné à la main)
        if (entrepotExiste) {
            pane.getChildren().remove(entrepotCircle);
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
        }

        // Réinitialise l'interface graphique
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        deliveryInfoVBox.setVisible(true);
        boutonPlus.setText("+");
        button_visible = false;


        entrepotExiste = true;

        // Affiche l'entrepôt
        entrepot = demande.getEntrepot();
        Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
        double lat = latitudeToY(intersection.getLatitude());
        double lon = longitudeToX(intersection.getLongitude());

        entrepotCircle = new Circle(lon, lat, 5, Color.BLUE);
        pane.getChildren().add(entrepotCircle);

        System.out.println("Entrepot Point: (" + lon + ", " + lat + ")");

        labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");

        deliveryInfoVBox.getChildren().add(labelEntrepot);


        // Affiche les points de livraison
        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            Intersection inter = plan.chercherIntersectionParId(pdl.getId());

            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());
    
            // Crée un cercle pour représenter le point de livraison
            Circle newPdl= new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event -> handleLabelClick(inter));

            System.out.println("Point de Livraison: (" + startX + ", " + startY + ")");

            Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(pdLabel);

            // Ajoute un événement de clic sur le label
            pdLabel.setOnMouseClicked(event -> handleLabelClick(inter));

            pane.getChildren().add(newPdl);
        }
    }


    // Click sur un point de livraison textuel
    public void handleLabelClick(Intersection inter) {
        double startX = longitudeToX(inter.getLongitude());
        double startY = latitudeToY(inter.getLatitude());
    
        // Met en surbrillance le point de livraison sélectionné
        Circle newPdl= new Circle(startX, startY, 8, Color.RED);
        newPdl.setStrokeWidth(5);
        newPdl.setStroke(Color.CORAL);
        pane.getChildren().add(newPdl);

        popupDelete(startX, startY, inter, newPdl);

    }

    public void popupDelete(Double x, Double y, Intersection inter, Circle newPdl) {
        Popup popup = new Popup();

        // Créer le contenu de la pop-up
        Label label = new Label("Voulez-vous supprimer ce point de livraison ?");
        label.setStyle("-fx-background-color: white; -fx-padding: 10;");

        // Bouton pour fermer la pop-up
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction (e ->  { supprimerPointDeLivraison(inter);
            popup.hide();
        });

        Button closeButton = new Button("Annuler");
        closeButton.setOnAction(e -> {
            popup.hide();
            pane.getChildren().remove(newPdl);
            newPdl.setStrokeWidth(0);
            newPdl.setRadius(5);
            pane.getChildren().add(newPdl);
        });

        // Ajouter les composants dans un VBox
        VBox popupContent = new VBox(10);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10;");
        popupContent.getChildren().addAll(label, closeButton, deleteButton);

        // Ajouter le VBox à la pop-up
        popup.getContent().add(popupContent);

        popup.setHeight(100);
        popup.setWidth(200);

        if (x > paneWidth / 2) {
            popup.setX(x - 60);
        }
        else popup.setX(x + 260);
        popup.setY(y);

        // Afficher la pop-up en relation avec la fenêtre principale
        popup.show(pane.getScene().getWindow());
    }


    // Supprime un point de livraison
    public void supprimerPointDeLivraison(Intersection inter) {
        // Supprime le point de livraison de la demande
        //demande.supprimerPointDeLivraison(inter.getId());

        // Supprime le point de livraison du plan
        pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));

        // Supprime le label du point de livraison
        deliveryInfoVBox.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().contains(inter.getLongitude() + ", " + inter.getLatitude()));
    }

    // Méthode pour convertir la longitude en position X dans le Pane
    private double longitudeToX(double longitude) {
        return ((longitude - longMin) / (longMax - longMin)) * (paneWidth) ;    }
    
    // Méthode pour convertir la latitude en position Y dans le Pane
    private double latitudeToY(double latitude) {
        return ((latitude - (latMin)) / (latMax - latMin)) * (paneHeight);    }


    // Convertit les coordonnées Y en latitude
    private double yToLatitude(double y) {
        return (y / paneHeight) * (latMax - latMin) + latMin;
    }

    // Convertit les coordonnées X en longitude
    private double xToLongitude(double x) {
        return (x / paneWidth) * (longMax - longMin) + longMin;
    }


    // Fenêtre d'information lors de la sélection des points de livraison
    public void showInfoPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        if (!entrepotExiste)
            alert.setContentText("Veuillez en premier lieu sélectionner l'entrepôt, puis les points de livraison.");
        else
        alert.setContentText("Veuillez sélectionner les points de livraison.");
        alert.showAndWait(); // Affiche la fenêtre et attend la fermeture
    }

}