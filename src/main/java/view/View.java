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
import javafx.scene.shape.Polygon;
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

import javafx.scene.paint.Color;
import java.util.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javafx.scene.layout.BorderPane;
import model.Trajet;
import model.Tournee;
import controller.*;

import exceptions.IDIntersectionException;

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
    private boolean tourneeCalculee = false;

    private Circle entrepotCircle;
    private Label labelEntrepot;

    public Demande demande;

    private Tournee tournee;

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)

    private Stack<Intersection> intersectionsSupprimees = new Stack<>();

    private Stack<Label> labelsSupprimes = new Stack<>();

    private Stack<Intersection> intersectionsAjoutees = new Stack<>();

    private Stack<Label> labelsAjoutes = new Stack<>();

    private Controller controller;

    public Commande derniereCommande;

    private Map<Integer, Color> livreurCouleurs = new HashMap<>();

    private Map<Integer, Trajet> trajets = new HashMap<>();

    public Stack<Commande> commandes = new Stack<>();

    Stack<Commande> commandesAnnulees = new Stack<>(); 

    private int livreur = 0;

    public boolean isEntrepotExiste() {
        return entrepotExiste;
    }

    public boolean isSelectionModeEnabled() {
        return selectionModeEnabled;
    }

    public boolean isTourneeCalculee() {
        return tourneeCalculee;
    }

    public void setTourneeCalculee(boolean tourneeCalculee) {
        this.tourneeCalculee = tourneeCalculee;
    }

    public Tournee getTournee() {
        return tournee;
    }

    public void setTournee(Tournee tournee) {
        this.tournee = tournee;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public Stack<Intersection> getIntersectionsSupprimees() {
        return this.intersectionsSupprimees;
    }

    public Stack<Label> getLabelsSupprimes() {
        return this.labelsSupprimes;
    }

    public Stack<Intersection> getIntersectionsAjoutees() {
        return this.intersectionsAjoutees;
    }

    public Stack<Label> getLabelsAjoutes() {
        return this.labelsAjoutes;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Stack<Commande> getCommandes() {
        return this.commandes;
    }

    public Stack<Commande> getCommandesAnnulees() {
        return this.commandesAnnulees;
    }

    public void setCommandes(Stack<Commande> commandes) {
        this.commandes = commandes;
    }

    public Commande getDerniereCommande(Commande derniereCommande) {
        return commandes.peek();
    }

    public void setDerniereCommande(Commande derniereCommande) {
        this.derniereCommande = derniereCommande;
    }

    public int getLivreur() {
        return livreur;
    }

    public HashMap<Integer, Trajet> getTrajets() {
        return (HashMap<Integer, Trajet>) trajets;
    }

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
        intersectionsAjoutees.clear();
        labelsAjoutes.clear();
        intersectionsSupprimees.clear();
        labelsSupprimes.clear();
        this.plan = plan;
        entrepotExiste = false;
        demande = new Demande();
        latMin = plan.trouverLatitudeMin();
        latMax = plan.trouverLatitudeMax();
        longMin = plan.trouverLongitudeMin();
        longMax = plan.trouverLongitudeMax();
    }

    public void displayPlan(Pane pane, VBox deliveryInfoVBox, Label label, Label messageLabel, Button calculerChemin) {
        pane.getChildren().clear();
        deliveryInfoVBox.getChildren().clear();
        tourneeCalculee = false;


        for (Troncon troncon : plan.getListeTroncons()) {
            double startX = longitudeToX(troncon.getOrigine().getLongitude());
            double startY = latitudeToY(troncon.getOrigine().getLatitude());
            double endX = longitudeToX(troncon.getDestination().getLongitude());
            double endY = latitudeToY(troncon.getDestination().getLatitude());

            Line line = new Line(startX, startY, endX, endY);
            line.setStrokeWidth(2);
            line.setStroke(Color.GRAY);
            line.setOnMouseClicked(event -> handleLineClick(event, pane, deliveryInfoVBox, messageLabel, calculerChemin));
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


    public void handleLineClick(MouseEvent event, Pane pane, VBox deliveryInfoVBox, Label label, Button calculerChemin) {
        if (!selectionModeEnabled) {
            return;
        }

        if (tourneeCalculee) {
            calculerChemin.setVisible(false);
        }
        else calculerChemin.setVisible(true);

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

        } else {
            try {
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
       
            if (tourneeCalculee) {
                intersectionsAjoutees.push(inter);
                labelsAjoutes.push(pdLabel);
                AjouterPointDeLivraisonCommande ajouterPointDeLivraisonCommande = new AjouterPointDeLivraisonCommande(this, pane, deliveryInfoVBox, inter, pdLabel);
                commandes.push(ajouterPointDeLivraisonCommande);
                derniereCommande = ajouterPointDeLivraisonCommande;
                //System.out.println(commandes);
                //System.out.println(ajouterPointDeLivraisonCommande.getIntersection());
                reafficherTournee(pane, deliveryInfoVBox, livreur);
            }
        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }
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
        
        try {
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
        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }

        List<Long> existingPdlIds = this.demande.getListePointDeLivraison().stream().map(PointDeLivraison::getId).collect(Collectors.toList());

        for (PointDeLivraison pdl : demandeFile.getListePointDeLivraison()) {
            if (!existingPdlIds.contains(pdl.getId())) {
                try {
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
                } catch (IDIntersectionException e) {
                    e.printStackTrace();
                }
            }
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
        deleteButton.setOnAction (e ->  { supprimerPointDeLivraison(inter, pane, deliveryInfoVBox, pdlLabel, true, -1);
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

    public void supprimerPointDeLivraison(Intersection inter, Pane pane, VBox deliveryInfoVBox, Label label, boolean addCommand, int livreur) {
        // Remove the delivery point from the request
        this.demande.supprimerIntersection(inter);

    
        // Only add the command to the stack if the addCommand flag is true
        if (addCommand) {
            intersectionsSupprimees.push(inter);
            labelsSupprimes.push(label);
            SupprimerPointDeLivraisonCommande supprimerPointDeLivraisonCommande = new SupprimerPointDeLivraisonCommande(this, pane, deliveryInfoVBox, inter, label);
            commandes.push(supprimerPointDeLivraisonCommande);
            derniereCommande = supprimerPointDeLivraisonCommande;
        }
    
        if (inter.getId() == entrepot.getId()) {
            if (!tourneeCalculee) {
                entrepotExiste = false;
                entrepotCircle = null;
                deliveryInfoVBox.getChildren().remove(labelEntrepot);
                pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
            }
        } else {
            // Remove the delivery point from the plan
            pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
    
            // Remove the label of the delivery point
            deliveryInfoVBox.getChildren().remove(label);
    
            if (tourneeCalculee) {
                List<Etape> etapes = this.trajets.get(livreur).getListeEtapes();
                System.out.println("etapes : "  + etapes);
                List<PointDeLivraison> pointsRestants = demande.getListePointDeLivraison();

                for (int i = 0; i < etapes.size(); i++) {
                    Etape etape = etapes.get(i);
                    System.out.println("etape : " + etape);
                    Intersection intersection = etape.getArrivee();
                    PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), new Livraison(0, intersection.getId(), 5.0, 5.0));
                    //pointsRestants.add(pdl);
                }
                List<Etape> nouvellesEtapes = new ArrayList<>();
    
                // Set the origin intersection to the warehouse
                Intersection origine;
                try {
                    origine = plan.chercherIntersectionParId(entrepot.getId());
                } catch (IDIntersectionException e) {
                    e.printStackTrace();
                    return;
                }
    
                // For each remaining point, recalculate the optimal path from the origin and update the origin at each iteration
                for (PointDeLivraison pdl : pointsRestants) {
                    Intersection destination;
                    try {
                        destination = plan.chercherIntersectionParId(pdl.getId());
                    } catch (IDIntersectionException e) {
                        e.printStackTrace();
                        continue;
                    }
                    if (destination != null) {
                        Etape etape = plan.chercherPlusCourtChemin(origine, destination);
                        nouvellesEtapes.add(etape);
                        origine = destination; // Update the origin for the next step
                    }
                }
    
                // If the warehouse exists, add the final step for the return to the warehouse
                if (entrepotExiste) {
                    try {
                        Etape retourEntrepot = plan.chercherPlusCourtChemin(origine, plan.chercherIntersectionParId(entrepot.getId()));
                        nouvellesEtapes.add(retourEntrepot);
                    } catch (IDIntersectionException e) {
                        e.printStackTrace();
                    }
                }
    
                // Update the tour with the new steps and display it
                tournee.setListeEtapes(nouvellesEtapes);
                afficherTourneeSurCarte(nouvellesEtapes, pane, livreur);
            }
        }
    }

    public void reafficherPointDeLivraison(Intersection intersection, Pane pane, VBox deliveryInfoVBox, Label label, int livreur) {
        // Recréer le cercle représentant le point de livraison
        double x = longitudeToX(intersection.getLongitude());
        double y = latitudeToY(intersection.getLatitude());
        Circle circle = new Circle(x, y, 5, Color.RED);
        pane.getChildren().add(circle);

        // Réajouter le label au VBox
        label.setStyle("-fx-background-color: #f5f5f5;");
        deliveryInfoVBox.getChildren().add(label);

        PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), new Livraison(0, intersection.getId(), 5.0, 5.0));
        this.demande.ajouterPointDeLivraison(pdl);

        // Réassocier le clic sur le cercle à la suppression du point de livraison  
        circle.setOnMouseClicked(event -> handleCircleClick(intersection, pane, deliveryInfoVBox, label));
        
        reafficherTournee(pane, deliveryInfoVBox, livreur);
    }

    public void reafficherTournee(Pane pane, VBox deliveryInfoVBox, int livreur) {
        List<PointDeLivraison> pointsRestants = demande.getListePointDeLivraison();
        List<Etape> nouvellesEtapes = new ArrayList<>();

        // Définir l'intersection d'origine comme étant celle de l'entrepôt
        Intersection origine;
        try {
             origine = plan.chercherIntersectionParId(entrepot.getId());
        } catch (IDIntersectionException e) {
             e.printStackTrace();
             return;
        }

        // Pour chaque point restant, recalculer le chemin optimal depuis l'origine et mettre à jour l'origine à chaque itération
        for (PointDeLivraison pdl2 : pointsRestants) {
            try {
                Intersection destination = plan.chercherIntersectionParId(pdl2.getId());
                if (destination != null) {
                    Etape etape = plan.chercherPlusCourtChemin(origine, destination);
                    nouvellesEtapes.add(etape);
                    origine = destination; // Met à jour l'origine pour la prochaine étape
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }

        // Si l'entrepôt existe, ajouter l'étape finale pour le retour à l'entrepôt
        if (entrepotExiste) {
            try {
                Intersection destination = plan.chercherIntersectionParId(entrepot.getId());
                if (destination != null) {
                    Etape retourEntrepot = plan.chercherPlusCourtChemin(origine, destination);
                    nouvellesEtapes.add(retourEntrepot);
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }

                // 3. Mettre à jour la tournée avec les nouvelles étapes et l'afficher
                tournee.setListeEtapes(nouvellesEtapes);
                afficherTourneeSurCarte(nouvellesEtapes, pane, livreur);
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

    public void calculerChemin(Pane pane, VBox deliveryInfoVBox, Trajet trajet, int livreur, Label messageLabel) {
        messageLabel.setText("Selectionnez une tournée pour la modifier.");
        
        System.out.println("Calculer chemin");
        tourneeCalculee = true;
        this.tournee = new Tournee(trajet.getListeEtapes(), null);
        this.demande.setPlan(this.plan);
        this.livreur = livreur;

        Color color;
        if (!livreurCouleurs.containsKey(livreur)) {
            double hue = (livreur * 60) % 360; // Espacement de 60 degrés entre chaque couleur
            color = Color.hsb(hue, 0.7, 0.8);
            livreurCouleurs.put(livreur, color);
        } else {
            color = livreurCouleurs.get(livreur);
        }

        if(!trajets.containsKey(livreur)) {
            trajets.put(livreur, trajet);
        }
        else {
            trajets.replace(livreur, trajet);
        }

        System.out.println("Trajets: " + trajets);

        Label vide = new Label(" ");
        deliveryInfoVBox.getChildren().add(vide);
        Label livreurLabel = new Label("Livreur " + (livreur + 1) + ": " + trajet.calculerDureeTrajet() + " minutes");
        livreurLabel.setStyle("-fx-background-color: " + toHexString(color) + ";");
        livreurLabel.setOnMouseClicked(event -> {
            afficherTourneeSurCarte(trajet.getListeEtapes(), pane, livreur);
        });
        deliveryInfoVBox.getChildren().add(livreurLabel);

        // Afficher le chemin
        for (Etape etape : trajet.getListeEtapes()) {
            double temps = etape.getLongueur()*60/15000;
            Label labelEtape = new Label("Etape : " + temps + " minutes");
            deliveryInfoVBox.getChildren().add(labelEtape);
            for (Troncon troncon : etape.getListeTroncons()) {
                double startX = longitudeToX(troncon.getOrigine().getLongitude());
                double startY = latitudeToY(troncon.getOrigine().getLatitude());
                double endX = longitudeToX(troncon.getDestination().getLongitude());
                double endY = latitudeToY(troncon.getDestination().getLatitude());

                Line line = new Line(startX, startY, endX, endY);
                line.setOnMouseClicked(event -> afficherTourneeSurCarte(trajet.getListeEtapes(), pane, livreur));
                line.setStrokeWidth(5);
                line.setStroke(color);
                pane.getChildren().add(line);

                // Calcul de l'angle de rotation
                double angle = Math.atan2(endY - startY, endX - startX) * (180 / Math.PI);

                // Création de la flèche
                Polygon arrowHead = new Polygon(0, 0, -8, 3, -8, -3); // Forme triangulaire
                arrowHead.setFill(Color.GRAY);

                // Positionnement de la flèche au milieu du segment
                double arrowX = (startX + endX) / 2;
                double arrowY = (startY + endY) / 2;


                arrowHead.setLayoutX(arrowX+5);
                arrowHead.setLayoutY(arrowY );

                // Rotation de la flèche pour qu'elle pointe dans la direction du tronçon
                arrowHead.setRotate(angle);

                pane.getChildren().add(arrowHead);

                Label label = new Label("Troncon: " + troncon.getNomRue());
                boolean labelExists = deliveryInfoVBox.getChildren().stream()
                    .filter(node -> node instanceof Label)
                    .map(node -> (Label) node)
                    .anyMatch(existingLabel -> existingLabel.getText().equals(label.getText()));
                if (!labelExists) {
                    deliveryInfoVBox.getChildren().add(label);
                }
            }


            try {
                Intersection pdl = plan.chercherIntersectionParId(etape.getArrivee().getId());
                Label pdLabel = new Label("Point de Livraison:");
                for (Troncon troncon : pdl.getListeTroncons()) {
                    pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                }
                deliveryInfoVBox.getChildren().add(pdLabel);
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
        
    }


public void afficherTourneeLivreur(List<Etape> etapes, Pane pane, int livreur) {
    // Supprime les anciens tracés de la tournée du plan
    this.livreur = livreur;

    pane.getChildren().removeIf(node -> node instanceof Line && ((Line) node).getStroke() != livreurCouleurs.get(livreur) && ((Line) node).getStroke() != Color.GRAY);

    afficherTourneeSurCarte(etapes, pane, livreur);
}


public void afficherTourneeSurCarte(List<Etape> etapes, Pane pane, int livreur) {
    // Supprime les anciens tracés de la tournée du plan
    this.livreur = livreur;

    pane.getChildren().removeIf(node -> node instanceof Line && ((Line) node).getStroke() == livreurCouleurs.get(livreur));
    pane.getChildren().removeIf(node -> node instanceof Polygon && ((Polygon) node).getFill() == Color.GRAY);

    // Ajoute les nouvelles lignes de la tournée
    for (Etape etape : etapes) {
        for (Troncon troncon : etape.getListeTroncons()) {
            double startX = longitudeToX(troncon.getOrigine().getLongitude());
            double startY = latitudeToY(troncon.getOrigine().getLatitude());
            double endX = longitudeToX(troncon.getDestination().getLongitude());
            double endY = latitudeToY(troncon.getDestination().getLatitude());

            Line line = new Line(startX, startY, endX, endY);
            line.setOnMouseClicked(event -> afficherTourneeSurCarte(etapes, pane, livreur));
            line.setStrokeWidth(5);
            line.setStroke(livreurCouleurs.get(livreur));
            pane.getChildren().add(line);

            // Calcul de l'angle de rotation
            double angle = Math.atan2(endY - startY, endX - startX) * (180 / Math.PI);

            // Création de la flèche
            Polygon arrowHead = new Polygon(0, 0, -8, 3, -8, -3); // Forme triangulaire
            arrowHead.setFill(Color.GRAY);

            // Positionnement de la flèche au milieu du segment
            double arrowX = (startX + endX) / 2;
            double arrowY = (startY + endY) / 2;


            arrowHead.setLayoutX(arrowX+5);
            arrowHead.setLayoutY(arrowY );

            // Rotation de la flèche pour qu'elle pointe dans la direction du tronçon
            arrowHead.setRotate(angle);

            pane.getChildren().add(arrowHead);
        }
    }
}

public String toHexString(Color color) {
    int red = (int) (color.getRed() * 255);
    int green = (int) (color.getGreen() * 255);
    int blue = (int) (color.getBlue() * 255);
    return String.format("#%02X%02X%02X", red, green, blue);
}


}