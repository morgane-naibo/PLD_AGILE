package view;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import javafx.scene.layout.BorderPane;
import model.Trajet;
import model.Tournee;
import controller.*;
import model.Livreur;

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

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)

    private Stack<Intersection> intersectionsSupprimees = new Stack<>();

    private Stack<Label> labelsSupprimes = new Stack<>();

    private Stack<Intersection> intersectionsAjoutees = new Stack<>();

    private Stack<Label> labelsAjoutes = new Stack<>();

    private Controller controller;

    public Commande derniereCommande;

    private Map<Integer, Color> livreurCouleurs = new HashMap<>();

    public Stack<Commande> commandes = new Stack<>();

    Stack<Commande> commandesAnnulees = new Stack<>(); 

    private List<Livreur> livreurs = new ArrayList<>();

    private Livreur livreurSelectionne = null;

    private List<Tournee> tournees = new ArrayList<>();


    public Demande getDemande() {
        return demande;
    }

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

    public List<Livreur> getLivreurs() {
        return livreurs;
    }

    public Livreur getLivreurSelectionne() {
        return livreurSelectionne;
    }

    public View() {
        this.demande = new Demande();
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
        setDemande(controller.getDemande());
        System.out.println(controller.getDemande().getNbLivreurs());
        this.demande.setNbLivreurs(controller.getDemande().getNbLivreurs());
        intersectionsAjoutees.clear();
        labelsAjoutes.clear();
        intersectionsSupprimees.clear();
        labelsSupprimes.clear();
        this.plan = plan;
        entrepotExiste = false;
        latMin = plan.trouverLatitudeMin();
        latMax = plan.trouverLatitudeMax();
        longMin = plan.trouverLongitudeMin();
        longMax = plan.trouverLongitudeMax();
    }

    public void displayPlan(Pane pane, VBox deliveryInfoVBox, Label label, Label messageLabel, Button calculerChemin, Button... buttons) {
        pane.getChildren().clear();
        messageLabel.setText(null);
        deliveryInfoVBox.getChildren().clear();
        tourneeCalculee = false;
        livreurCouleurs.clear();

        for (Button button : buttons) {
            button.setVisible(false);
        }

        System.out.println(demande.getNbLivreurs());


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
        System.out.println(demande.getNbLivreurs());
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

            entrepotCircle = new Circle(longitudeToX(intersection.getLongitude()), latitudeToY(intersection.getLatitude()), 7, Color.BLUE);
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
            //Livraison livraison = new Livraison(0, intersection.getId(), 5.0, 5.0);
            PointDeLivraison pdl = new PointDeLivraison(intersection);

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
                AjouterPointDeLivraisonCommande ajouterPointDeLivraisonCommande = new AjouterPointDeLivraisonCommande(this, pane, deliveryInfoVBox, inter, pdLabel, controller.getMessageLabel());
                commandes.push(ajouterPointDeLivraisonCommande);
                derniereCommande = ajouterPointDeLivraisonCommande;
                //this.demande.ajouterPointDeLivraison(pdl);
                try {
                    Trajet trajet = this.demande.ajouterPDLaMatrice(livreurSelectionne.getId(), pdl);
                    Tournee tournee = new Tournee(trajet.getListeEtapes(), livreurSelectionne);
                    tournees.set((int)livreurSelectionne.getId(), tournee);
                    reafficherTournee(pane, deliveryInfoVBox, livreurSelectionne, label);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println(commandes);
                //System.out.println(ajouterPointDeLivraisonCommande.getIntersection());
                //reafficherTournee(pane, deliveryInfoVBox, livreurSelectionne);
            }
        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }
        }
    }

    public void displayDemande(Demande demandeFile, Pane pane, VBox deliveryInfoVBox, Label label) {
        label.setText(null);
        
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

            entrepotCircle = new Circle(lon, lat, 7, Color.BLUE);
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
        if (!popupOuverte && ((tourneeCalculee && livreurSelectionne != null) || !tourneeCalculee)) {
            Circle newPdl = new Circle(startX, startY, 8, inter.getId() == entrepot.getId() ? Color.BLUE : Color.RED);
            newPdl.setStrokeWidth(5);
            newPdl.setStroke(inter.getId() == entrepot.getId() ? Color.LIGHTBLUE : Color.CORAL);
            pane.getChildren().add(newPdl);
            popupDelete(startX, startY, inter, newPdl, pane, deliveryInfoVBox, newPdl, label);
        }
        label.setStyle("-fx-background-color: lightblue;");
        System.out.println("Intersection clicked: " + inter.getId());
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
        System.out.println("Popup delete :" + livreurSelectionne);
        if (livreurSelectionne != null || !tourneeCalculee) {
            Popup popup = new Popup();

            // Créer le contenu de la pop-up
            Label label = new Label("Voulez-vous supprimer ce point de livraison ?");
            label.setStyle("-fx-background-color: white; -fx-padding: 10;");

            // Bouton pour fermer la pop-up
            Button deleteButton = new Button("Supprimer");
            deleteButton.setOnAction(e -> {
                supprimerPointDeLivraison(inter, pane, deliveryInfoVBox, pdlLabel, true, livreurSelectionne);
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
    }

    public void supprimerPointDeLivraison(Intersection inter, Pane pane, VBox deliveryInfoVBox, Label label, boolean addCommand, Livreur livreur) {
        if (addCommand) {
            intersectionsSupprimees.push(inter);
            labelsSupprimes.push(label);
            SupprimerPointDeLivraisonCommande supprimerPointDeLivraisonCommande = new SupprimerPointDeLivraisonCommande(this, pane, deliveryInfoVBox, inter, label, controller.getMessageLabel());
            commandes.push(supprimerPointDeLivraisonCommande);
            derniereCommande = supprimerPointDeLivraisonCommande;
        }
        
        if (!tourneeCalculee || inter.getId() == entrepot.getId()) {
            this.demande.supprimerIntersection(inter);
            if (inter.getId() == entrepot.getId()) {
                entrepotExiste = false;
                entrepotCircle = null;
                deliveryInfoVBox.getChildren().remove(labelEntrepot);
                pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
            } else {
                pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
                deliveryInfoVBox.getChildren().remove(label);
                intersectionsSupprimees.push(inter);
                labelsSupprimes.push(label);
            }
        } else {
            PointDeLivraison pdl = new PointDeLivraison(inter);
            try {
                pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
                deliveryInfoVBox.getChildren().remove(label);
                Trajet nouveauTrajet = demande.recalculerTrajetApresSuppressionPDL((int) livreur.getId(), pdl);
                tournees.get((int) livreur.getId()).setListeEtapes(nouveauTrajet.getListeEtapes());
                reafficherTournee(pane, deliveryInfoVBox, livreur, label);
            } catch (IDIntersectionException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void reafficherPointDeLivraison(Intersection intersection, Pane pane, VBox deliveryInfoVBox, Label label, Livreur livreur) {
        // Recréer le cercle représentant le point de livraison
        double x = longitudeToX(intersection.getLongitude());
        double y = latitudeToY(intersection.getLatitude());
        Circle circle = new Circle(x, y, 5, Color.RED);
        pane.getChildren().add(circle);

        // Réajouter le label au VBox
        label.setStyle("-fx-background-color: #f5f5f5;");
        if (!deliveryInfoVBox.getChildren().contains(label)) {
            deliveryInfoVBox.getChildren().add(label);
        }
        circle.setOnMouseClicked(event -> handleCircleClick(intersection, pane, deliveryInfoVBox, label));

        PointDeLivraison pdl = new PointDeLivraison(intersection.getId(), new Livraison(0, intersection.getId(), 5.0, 5.0));
        this.demande.ajouterPointDeLivraison(pdl);

        try {
            Trajet trajet = this.demande.ajouterPDLaMatrice(livreurSelectionne.getId(), pdl);
            Tournee tournee = new Tournee(trajet.getListeEtapes(), livreurSelectionne);
            tournees.set((int)livreurSelectionne.getId(), tournee);
            reafficherTournee(pane, deliveryInfoVBox, livreurSelectionne, label);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Réassocier le clic sur le cercle à la suppression du point de livraison          
        //reafficherTournee(pane, deliveryInfoVBox, livreur);
    }

    public void reafficherTournee(Pane pane, VBox deliveryInfoVBox, Livreur livreur, Label messageLabel) {
        // Récupérer la tournée associée au livreur sélectionné

        Tournee tournee = tournees.get((int)livreur.getId());
        System.out.println(tournee.toString());

        // Obtenir la liste des points de livraison à partir des étapes de la tournée
        List<PointDeLivraison> pointsRestants = tournee.getListeEtapes().stream()
            .map(etape -> new PointDeLivraison(etape.getArrivee().getId(), new Livraison(0, etape.getArrivee().getId(), 5.0, 5.0)))
            .collect(Collectors.toList());


        System.out.println("pointsRestants : " + pointsRestants);
        
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
        for (PointDeLivraison pdl : pointsRestants) {
            try {
                Intersection destination = plan.chercherIntersectionParId(pdl.getId());
                if (destination != null) {
                    Etape etape = plan.chercherPlusCourtChemin(origine, destination);
                    if (etape != null) {
                        nouvellesEtapes.add(etape);
                        origine = destination; // Mise à jour de l'origine pour la prochaine étape
                    }
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
    
        // Ajouter l'étape finale pour le retour à l'entrepôt
        if (entrepotExiste) {
            try {
                Etape retourEntrepot = plan.chercherPlusCourtChemin(origine, plan.chercherIntersectionParId(entrepot.getId()));
                if (retourEntrepot != null) {
                    nouvellesEtapes.add(retourEntrepot);
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
    
        // Filtrer les étapes non nulles
        nouvellesEtapes = nouvellesEtapes.stream()
            .filter(etape -> etape != null)
            .collect(Collectors.toList());
    
        // Mettre à jour la tournée avec les nouvelles étapes
        tournees.get((int)livreurSelectionne.getId()).setListeEtapes(nouvellesEtapes);

        System.out.println("nouvellesEtapes : " + nouvellesEtapes);
    
        // Afficher la tournée mise à jour
        afficherTourneeSurCarte(nouvellesEtapes, pane, livreurSelectionne, messageLabel);
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

    public void calculerChemin(Pane pane, VBox deliveryInfoVBox, Trajet trajet, int livreurId, Label messageLabel) {
        
        messageLabel.setText("Selectionnez une tournée pour la modifier, en cliquant sur le texte surligné ou sur la tournée graphique.");
        
        Set<String> tronconsAffiches = new HashSet<>();

        Livreur livreur = new Livreur(livreurId);
        livreurs.add(livreur);

        //livreurSelectionne = livreur;

        tourneeCalculee = true;
        Tournee tournee = new Tournee(trajet.getListeEtapes(), livreur);
        tournees.add(tournee);
        this.demande.setPlan(this.plan);

        Color color;
        if (!livreurCouleurs.containsKey(livreur.getId())) {
            double hue = (livreur.getId() * 60) % 360; // Espacement de 60 degrés entre chaque couleur
            if (hue == 0) {
            hue += 30; // Éviter le rouge pur
            }
            color = Color.hsb(hue, 0.7, 0.8);
            livreurCouleurs.put(livreur.getId(), color);
        } else {
            color = livreurCouleurs.get(livreur.getId());
        }

        Label vide = new Label(" ");
        deliveryInfoVBox.getChildren().add(vide);
        Label livreurLabel = new Label("Livreur " + (livreur.getId() + 1) + ": " + String.format("%.2f", trajet.calculerDureeTrajet()) + " minutes");
        Color lightColor = color.deriveColor(0, 1, 1.3, 0.5); // Increase brightness by 30% and set opacity to 50%
        livreurLabel.setStyle("-fx-background-color: " + toHexString(lightColor) + ";");
        livreurLabel.setOnMouseClicked(event -> {
            afficherTourneeSurCarte(trajet.getListeEtapes(), pane, livreur, messageLabel);
        });
        deliveryInfoVBox.getChildren().add(livreurLabel);
        double heureDepartProchainTroncon = 8.0; // Heure de départ du premier tronçon

        // Afficher le chemin
        for (Etape etape : trajet.getListeEtapes()) {
            double temps = etape.getLongueur() * 60 / 15000;
            temps = Math.round(temps * 100.0) / 100.0;
            double heureFin = heureDepartProchainTroncon + temps / 60;
            int heures = (int) heureFin;
            int minutes = (int) ((heureFin - heures) * 60);
            Label labelEtape = new Label("Etape : " + String.format("%02d:%02d", heures, minutes));
            deliveryInfoVBox.getChildren().add(labelEtape);

            // Utiliser l'heure de fin comme heure de départ pour le prochain tronçon
            heureDepartProchainTroncon = heureFin;
            for (Troncon troncon : etape.getListeTroncons()) {
                double startX = longitudeToX(troncon.getOrigine().getLongitude());
                double startY = latitudeToY(troncon.getOrigine().getLatitude());
                double endX = longitudeToX(troncon.getDestination().getLongitude());
                double endY = latitudeToY(troncon.getDestination().getLatitude());

                Line line = new Line(startX, startY, endX, endY);
                line.setOnMouseClicked(event -> afficherTourneeSurCarte(trajet.getListeEtapes(), pane, livreur, messageLabel));
                line.setStrokeWidth(5);
                line.setStroke(color);
                pane.getChildren().add(line);

                // Calcul de l'angle de rotation
                double angle = Math.atan2(endY - startY, endX - startX) * (180 / Math.PI);

                // Création de la flèche
                Polygon arrowHead = new Polygon(0, 0, -8, 3, -8, -3); // Forme triangulaire
                arrowHead.setFill(Color.DARKGRAY);

                // Positionnement de la flèche au milieu du segment
                double arrowX = (startX + endX) / 2;
                double arrowY = (startY + endY) / 2;


                arrowHead.setLayoutX(arrowX+5);
                arrowHead.setLayoutY(arrowY );

                // Rotation de la flèche pour qu'elle pointe dans la direction du tronçon
                arrowHead.setRotate(angle);

                pane.getChildren().add(arrowHead);

                Label label = new Label("    Troncon: " + troncon.getNomRue());
                if (!tronconsAffiches.contains(troncon.getNomRue())) {
                    deliveryInfoVBox.getChildren().add(label);
                    tronconsAffiches.add(troncon.getNomRue());
                }
            }
            tronconsAffiches.clear();

            try {
                Intersection pdl = plan.chercherIntersectionParId(etape.getArrivee().getId());
                Label pdLabel = new Label("    Point de Livraison:");
                for (Troncon troncon : pdl.getListeTroncons()) {
                    pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                }
                deliveryInfoVBox.getChildren().add(pdLabel);
                deliveryInfoVBox.requestLayout();
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
        
    }



public void afficherTourneeSurCarte(List<Etape> etapes, Pane pane, Livreur livreur, Label messageLabel) {

    messageLabel.setText("Tournée du livreur " + (livreur.getId() + 1) + " sélectionnée");

    // Supprime les anciens tracés de la tournée du plan
    this.livreurSelectionne = livreur;

    System.out.println("Livreur selectionne: " + livreurSelectionne);

    pane.getChildren().removeIf(node -> node instanceof Line && ((Line) node).getStroke() == livreurCouleurs.get((int)livreur.getId()));
    pane.getChildren().removeIf(node -> node instanceof Polygon);

    // Ajoute les nouvelles lignes de la tournée
    for (Etape etape : etapes) {
        if (etape!=null) {
            for (Troncon troncon : etape.getListeTroncons()) {
                double startX = longitudeToX(troncon.getOrigine().getLongitude());
                double startY = latitudeToY(troncon.getOrigine().getLatitude());
                double endX = longitudeToX(troncon.getDestination().getLongitude());
                double endY = latitudeToY(troncon.getDestination().getLatitude());

                Line line = new Line(startX, startY, endX, endY);
                line.setOnMouseClicked(event -> afficherTourneeSurCarte(etapes, pane, livreur, messageLabel));
                line.setStrokeWidth(5);
                line.setStroke(livreurCouleurs.get(livreur.getId()));
                pane.getChildren().add(line);

                // Calcul de l'angle de rotation
                double angle = Math.atan2(endY - startY, endX - startX) * (180 / Math.PI);

                // Création de la flèche
                Polygon arrowHead = new Polygon(0, 0, -8, 3, -8, -3); // Forme triangulaire
                arrowHead.setFill(Color.DARKGRAY);

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
}

public String toHexString(Color color) {
    int red = (int) (color.getRed() * 255);
    int green = (int) (color.getGreen() * 255);
    int blue = (int) (color.getBlue() * 255);
    return String.format("#%02X%02X%02X", red, green, blue);
}


}