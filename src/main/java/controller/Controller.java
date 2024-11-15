package controller;

import java.util.Stack;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Intersection;
import model.Plan;
import model.Demande;
import view.View;
import javafx.scene.layout.StackPane;

/**
 * La classe Controller gère les interactions entre la vue et le modèle.
 */
public class Controller {

    /**
     * Bouton pour afficher les autres boutons.
     */
    @FXML
    private Button boutonPlus;

    /**
     * Pane principal de la vue.
     */
    @FXML
    private BorderPane pane;

    /**
     * Pane ancré pour la disposition des éléments.
     */
    @FXML
    private AnchorPane anchorPane;

    /**
     * Bouton pour charger un fichier.
     */
    @FXML
    private Button chargerFichierButton;

    /**
     * Bouton pour sélectionner un point.
     */
    @FXML
    private Button selectionnerPointButton;

    /**
     * Bouton pour charger un plan.
     */
    @FXML
    private Button chargerPlan;

    /**
     * VBox pour afficher les informations de livraison.
     */
    @FXML
    private VBox deliveryInfoVBox;

    /**
     * Bouton pour charger un nouveau plan.
     */
    @FXML
    private Button chargerNouveauPlan;

    /**
     * Bouton pour calculer le chemin.
     */
    @FXML
    private Button calculerChemin;

    /**
     * Bouton pour exporter en XML.
     */
    @FXML
    private Button exportXML;

    /**
     * Label pour afficher des informations.
     */
    @FXML
    private Label label;

    /**
     * Label pour afficher des messages.
     */
    @FXML
    private Label messageLabel;

    /**
     * Pane pour afficher la carte.
     */
    @FXML
    private Pane mapPane;

    /**
     * Bouton pour annuler la dernière action.
     */
    @FXML
    private Button undoButton;

    /**
     * Bouton pour rétablir la dernière action annulée.
     */
    @FXML
    private Button redoButton;

    /**
     * HBox pour contenir les boutons d'annulation et de rétablissement.
     */
    @FXML
    private HBox hboxUndoRedo;

    /**
     * Label pour afficher le titre.
     */
    @FXML
    private Label title;

    /**
     * ComboBox pour sélectionner le nombre de livreurs.
     */
    @FXML
    private ComboBox<Integer> nbLivreurs;

    /**
     * StackPane pour empiler les éléments.
     */
    @FXML
    private StackPane stackPane;

    /**
     * Vue associée au contrôleur.
     */
    private View view;

    /**
     * Demande associée au contrôleur.
     */
    private Demande demande;

    /**
     * Plan associé au contrôleur.
     */
    private Plan plan;

    /**
     * Transformation de zoom initialisée à 1 (100%).
     */
    private Scale scale = new Scale(1.0, 1.0, 0, 0);

    // Variables pour le drag de la carte
    private double initialMouseX;
    private double initialMouseY;
    private double initialTranslateX;
    private double initialTranslateY;

    // Variables pour le drag de `deliveryInfoVBox`
    private double mouseInitialY;

    /**
     * Nombre de livreurs sélectionné.
     */
    private int nbLivreur;

    /**
     * État actuel du contrôleur.
     */
    private Etat etat;

    /**
     * Constructeur de la classe Controller.
     */
    public Controller() {
        this.view = new View();
        view.setController(this);
        this.demande = new Demande();
        this.plan = new Plan();
        this.etat = new PlanNonCharge(this);
    }

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     */
    public void initialize() {
        // Code d'initialisation
        deliveryInfoVBox.setLayoutY(50);
        nbLivreurs.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Option sélectionnée : " + newValue);
                nbLivreur = newValue;
                demande.setNbLivreurs(nbLivreur);
            }
        });
        nbLivreurs.getItems().addAll(1, 2, 3, 4, 5);
        Platform.runLater(() -> {
            Stage stage = (Stage) deliveryInfoVBox.getScene().getWindow();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                deliveryInfoVBox.setPrefWidth(newVal.doubleValue() * 0.4);
            });
    
            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                deliveryInfoVBox.setPrefHeight(newVal.doubleValue());
            });
        });
        
       // Rendre `deliveryInfoVBox` déplaçable uniquement verticalement
        deliveryInfoVBox.setOnMousePressed(event -> {
            mouseInitialY = event.getSceneY();
        });

        deliveryInfoVBox.setOnMouseDragged(event -> {
            double offsetY = event.getSceneY() - mouseInitialY;
            deliveryInfoVBox.setLayoutY(deliveryInfoVBox.getLayoutY() + offsetY);
            mouseInitialY = event.getSceneY();
        });
        
        // Appliquer la transformation de zoom uniquement sur mapPane
        if (mapPane != null) {
            mapPane.getTransforms().add(scale);
            mapPane.setOnScroll(event -> handleZoom(event.getDeltaY()));
    
            // Ajout de la fonctionnalité de drag sur mapPane
            mapPane.setOnMousePressed(this::handleMousePressed);
            mapPane.setOnMouseDragged(this::handleMouseDragged);
        }
    }

    /**
     * Définit l'état du contrôleur.
     * 
     * @param etat L'état à définir.
     */
    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    /**
     * Définit la vue associée au contrôleur.
     * 
     * @param view La vue à définir.
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Définit le plan associé au contrôleur.
     * 
     * @param plan Le plan à définir.
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * Définit la demande associée au contrôleur.
     * 
     * @param demande La demande à définir.
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /**
     * Retourne le plan associé au contrôleur.
     * 
     * @return Le plan.
     */
    public Plan getPlan() {
        return this.plan;
    }

    /**
     * Retourne la demande associée au contrôleur.
     * 
     * @return La demande.
     */
    public Demande getDemande() {
        return this.demande;
    }

    /**
     * Retourne la vue associée au contrôleur.
     * 
     * @return La vue.
     */
    public View getView() {
        return this.view;
    }

    /**
     * Retourne le bouton pour ajouter un élément.
     * 
     * @return Le bouton.
     */
    public Button getBoutonPlus() {
        return boutonPlus;
    }

    /**
     * Retourne le pane principal de la vue.
     * 
     * @return Le pane.
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * Retourne le pane ancré pour la disposition des éléments.
     * 
     * @return Le pane.
     */
    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    /**
     * Retourne le bouton pour charger un fichier.
     * 
     * @return Le bouton.
     */
    public Button getChargerFichierButton() {
        return chargerFichierButton;
    }

    /**
     * Retourne le bouton pour sélectionner un point.
     * 
     * @return Le bouton.
     */
    public Button getSelectionnerPointButton() {
        return selectionnerPointButton;
    }

    /**
     * Retourne le bouton pour charger un plan.
     * 
     * @return Le bouton.
     */
    public Button getChargerPlan() {
        return chargerPlan;
    }

    /**
     * Retourne la VBox pour afficher les informations de livraison.
     * 
     * @return La VBox.
     */
    public VBox getDeliveryInfoVBox() {
        return deliveryInfoVBox;
    }

    /**
     * Retourne le bouton pour charger un nouveau plan.
     * 
     * @return Le bouton.
     */
    public Button getChargerNouveauPlan() {
        return chargerNouveauPlan;
    }

    /**
     * Retourne le bouton pour calculer le chemin.
     * 
     * @return Le bouton.
     */
    public Button getCalculerChemin() {
        return calculerChemin;
    }

    /**
     * Retourne le bouton pour exporter en XML.
     * 
     * @return Le bouton.
     */
    public Button getExport() {
        return exportXML;
    }

    /**
     * Retourne le label pour afficher des informations.
     * 
     * @return Le label.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Retourne le label pour afficher des messages.
     * 
     * @return Le label.
     */
    public Label getMessageLabel() {
        return messageLabel;
    }

    /**
     * Retourne le pane pour afficher la carte.
     * 
     * @return Le pane.
     */
    public Pane getMapPane() {
        return mapPane;
    }

    /**
     * Retourne l'état actuel du contrôleur.
     * 
     * @return L'état.
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Retourne le bouton pour annuler la dernière action.
     * 
     * @return Le bouton.
     */
    public Button getUndoButton() {
        return undoButton;
    }

    /**
     * Retourne le bouton pour rétablir la dernière action annulée.
     * 
     * @return Le bouton.
     */
    public Button getRedoButton() {
        return redoButton;
    }

    /**
     * Retourne la HBox pour contenir les boutons d'annulation et de rétablissement.
     * 
     * @return La HBox.
     */
    public HBox getHboxUndoRedo() {
        return hboxUndoRedo;
    }

    /**
     * Retourne le label pour afficher le titre.
     * 
     * @return Le label.
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Retourne le StackPane pour empiler les éléments.
     * 
     * @return Le StackPane.
     */
    public StackPane getStackPane() {
        return stackPane;
    }

    /**
     * Retourne le nombre de livreurs sélectionné.
     * 
     * @return Le nombre de livreurs.
     */
    public int getNbLivreur() {
        return nbLivreur;
    }

    /**
     * Gestionnaire d'événements pour le clic de souris.
     * 
     * @param event L'événement de clic de souris.
     */
    private void handleMousePressed(MouseEvent event) {
        initialMouseX = event.getSceneX();
        initialMouseY = event.getSceneY();
        initialTranslateX = mapPane.getTranslateX();
        initialTranslateY = mapPane.getTranslateY();
    }

    /**
     * Gestionnaire d'événements pour le glissement de la souris.
     * 
     * @param event L'événement de glissement de la souris.
     */
    private void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - initialMouseX;
        double deltaY = event.getSceneY() - initialMouseY;

        // Coordonnées potentielles après le glissement
        double newTranslateX = initialTranslateX + deltaX;
        double newTranslateY = initialTranslateY + deltaY;

        // Dimensions de mapPane et de son conteneur
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

    /**
     * Gestionnaire d'événements pour le clic sur le bouton "charger un (nouveau) plan".
     */
    @FXML
    public void handleLoadPlan() {
        etat.handleLoadPlan();
        System.out.println(etat);
    }

    /**
     * Gestionnaire d'événements pour le clic sur le bouton plus.
     */
    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        if (view.isTourneeCalculee()) {
            view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan, exportXML);
        } else {
            view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
        }
    }

    /**
     * Gestionnaire d'événements pour le clic sur le bouton de sélection.
     */
    @FXML
    public void handleSelectButton() {
        etat.handleSelectButton();
        System.out.println(etat);
    }

    /**
     * Gestionnaire d'événements pour le clic sur le bouton de fichier.
     */
    @FXML
    public void handleFileButton() {
        etat.handleFileButton();
        System.out.println(etat);
    }

    /**
     * Calculer le chemin.
     */
    @FXML
    public void calculerChemin() {
        etat.calculerChemin();
        System.out.println(etat);
    }

    /**
     * Gestionnaire d'événements pour l'exportation en XML.
     */
    @FXML
    public void handleExportXML() {
        etat.handleExportXML();
        System.out.println(etat);
    }

    /**
     * Annuler la dernière commande.
     */
    @FXML
    public void undo() {
        // Vérifiez si la pile des commandes n'est pas vide
        if (!view.commandes.isEmpty()) {
            Commande derniereCommande = view.commandes.peek(); // Obtenez la dernière commande
    
            // Afficher l'état de la pile pour le débogage
            System.out.println("Commandes avant undo : " + view.commandes);
    
            // Vérifiez que la commande est bien présente dans la pile
            if (derniereCommande != null) {
                System.out.println("Commande annulée : " + derniereCommande);  // Afficher la commande annulée
    
                System.out.println("Intersection : " + derniereCommande.getIntersection());

                // Annuler la commande
                derniereCommande.undoCommande(); 
    
                // Retirer la commande annulée de la pile
                view.commandes.remove(derniereCommande); 
    
                // Ajouter la commande annulée dans la pile des commandes annulées pour un éventuel redo
                // Vérifiez que la commande n'est pas déjà dans la pile des commandes annulées pour éviter les doublons
                if (!view.getCommandesAnnulees().contains(derniereCommande)) {
                    view.getCommandesAnnulees().push(derniereCommande);
                }
    
                // Mettre à jour la dernière commande si la pile de commandes n'est pas vide
                if (!view.commandes.isEmpty()) {
                    view.setDerniereCommande(view.commandes.peek());
                } else {
                    view.setDerniereCommande(null); // Pas de commande restante
                }
    
                // Afficher l'état des piles après l'annulation
                System.out.println("Commandes après undo : " + view.commandes);
                System.out.println("Commandes annulées après undo : " + view.getCommandesAnnulees());
            } else {
                System.out.println("Aucune commande à annuler.");
            }
        } else {
            System.out.println("Aucune commande à annuler.");
        }
    }

    /**
     * Rétablir la dernière commande annulée.
     */
    @FXML
    public void redo() {
        // Vérifiez s'il y a des commandes annulées pouvant être rejouées
        if (!view.getCommandesAnnulees().isEmpty()) {
            // Récupérez la dernière commande annulée
            Commande derniereCommandeAnnulee = view.getCommandesAnnulees().pop();

            System.out.println("Dernière commande annulée : " + derniereCommandeAnnulee);

            // Utilisez les attributs Intersection et Label de la commande
            Intersection intersection = derniereCommandeAnnulee.getIntersection(); // méthode à ajouter dans Commande
            Label label = derniereCommandeAnnulee.getLabel(); // méthode à ajouter dans Commande

            // Rejouez la commande avec les arguments requis
            derniereCommandeAnnulee.redoCommande(intersection, label);

            // Remettez la commande dans la pile des commandes effectuées
            view.getCommandes().push(derniereCommandeAnnulee);

            // Mettez à jour la dernière commande
            view.setDerniereCommande(derniereCommandeAnnulee);

            System.out.println("Commande rejouée : " + derniereCommandeAnnulee);
        } else {
            System.out.println("Aucune commande à rejouer.");
        }
    }

    /**
     * Gestionnaire d'événements pour le zoom.
     * 
     * @param deltaY La variation de la molette de la souris.
     */
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

