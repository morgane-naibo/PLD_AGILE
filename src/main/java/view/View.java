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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Fichier chargé avec succès !");
        alert.showAndWait();
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
        latMin = plan.trouverLatitudeMin();
        latMax = plan.trouverLatitudeMax();
        longMin = plan.trouverLongitudeMin();
        longMax = plan.trouverLongitudeMax();
    }

    public void displayPlan(Pane pane, VBox deliveryInfoVBox, Label label) {
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
        label.setVisible(true);
    }

    public void handleLineClick(MouseEvent event, Pane pane, VBox deliveryInfoVBox, Label label) {
        if (!selectionModeEnabled) {
            return;
        }

        double x = event.getX();
        double y = event.getY();
        double lat = yToLatitude(y);
        double lon = xToLongitude(x);

        Intersection intersection = plan.chercherIntersectionLaPlusProche(lat, lon);

        if (!entrepotExiste) {
            entrepot = new Entrepot(intersection.getId(), "8");
            entrepotExiste = true;

            entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 5, Color.BLUE);
            pane.getChildren().add(entrepotCircle);
            entrepotCircle.setOnMouseClicked(event2 -> handleLabelClick(intersection, pane, deliveryInfoVBox));

            labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(labelEntrepot);
            labelEntrepot.setOnMouseClicked(event2 -> handleLabelClick(intersection, pane, deliveryInfoVBox));

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

            Circle newPdl = new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event2 -> handleLabelClick(inter, pane, deliveryInfoVBox));

            Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(pdLabel);
            pane.getChildren().add(newPdl);

            pdLabel.setOnMouseClicked(event2 -> handleLabelClick(inter, pane, deliveryInfoVBox));
        }
    }

    public void displayDemande(Demande demande, Pane pane, VBox deliveryInfoVBox) {
        if (entrepotExiste) {
            pane.getChildren().remove(entrepotCircle);
            deliveryInfoVBox.getChildren().remove(labelEntrepot);
        }

        button_visible = false;
        entrepotExiste = true;

        entrepot = demande.getEntrepot();
        Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
        double lat = latitudeToY(intersection.getLatitude());
        double lon = longitudeToX(intersection.getLongitude());

        entrepotCircle = new Circle(lon, lat, 5, Color.BLUE);
        pane.getChildren().add(entrepotCircle);
        entrepotCircle.setOnMouseClicked(event -> handleLabelClick(intersection, pane, deliveryInfoVBox));

        labelEntrepot = new Label("Entrepôt: (" + intersection.getLongitude() + ", " + intersection.getLatitude() + ")");
        deliveryInfoVBox.getChildren().add(labelEntrepot);
        labelEntrepot.setOnMouseClicked(event -> handleLabelClick(intersection, pane, deliveryInfoVBox));

        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            Intersection inter = plan.chercherIntersectionParId(pdl.getId());
            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());

            Circle newPdl = new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event -> handleLabelClick(inter, pane, deliveryInfoVBox));

            Label pdLabel = new Label("Point de Livraison: (" + inter.getLongitude() + ", " + inter.getLatitude() + ")");
            deliveryInfoVBox.getChildren().add(pdLabel);

            pdLabel.setOnMouseClicked(event -> handleLabelClick(inter, pane, deliveryInfoVBox));
            pane.getChildren().add(newPdl);
        }
    }

    public void handleLabelClick(Intersection inter, Pane pane, VBox deliveryInfoVBox) {
        double startX = longitudeToX(inter.getLongitude());
        double startY = latitudeToY(inter.getLatitude());

        if (!popupOuverte) {
            Circle newPdl = new Circle(startX, startY, 8, inter.getId() == entrepot.getId() ? Color.BLUE : Color.RED);
            newPdl.setStrokeWidth(5);
            newPdl.setStroke(inter.getId() == entrepot.getId() ? Color.LIGHTBLUE : Color.CORAL);
            pane.getChildren().add(newPdl);
            popupDelete(startX, startY, inter, newPdl, pane, deliveryInfoVBox);
        }
    }

    public void addMessagePane(Pane pane) {
        pane.getChildren().clear();
        Label message = new Label("Sélectionnez l'entrepôt et les points de livraison.");
        HBox box = new HBox();
        box.setStyle("-fx-background-color:lightblue;");
        box.getChildren().add(message);
        pane.getChildren().add(box);
    }

    public void popupDelete(double x, double y, Intersection inter, Circle circle, Pane pane, VBox deliveryInfoVBox) {
        Popup popup = new Popup();
        Button button = new Button("Supprimer");
        button.setOnAction(event -> {
            demande.getListePointDeLivraison().removeIf(pdl -> pdl.getId() == inter.getId());
            deliveryInfoVBox.getChildren().removeIf(label -> ((Label) label).getText().contains("(" + inter.getLongitude() + ", " + inter.getLatitude() + ")"));
            pane.getChildren().remove(circle);
            popup.hide();
        });
        popup.getContent().add(button);
        popup.setAnchorX(x);
        popup.setAnchorY(y);
        popup.show(circle.getScene().getWindow());
        popupOuverte = true;
        popup.setOnHiding(event -> popupOuverte = false);
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
}