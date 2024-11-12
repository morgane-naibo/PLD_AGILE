package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private HBox hboxUndoRedo;

    private View view;

    private Demande demande;

    private Plan plan;

    private Scale scale = new Scale(1.0, 1.0, 0, 0); // Zoom initial à 1 (100%)

    // Variables pour le drag de la carte
    private double initialMouseX;
    private double initialMouseY;
    private double initialTranslateX;
    private double initialTranslateY;

    // Variables pour définir les limites maximales de déplacement
    private static final double MIN_X = 0;
    private static final double MIN_Y = 0;
    private static final double MAX_X = 550; // Remplacez par la limite maximale souhaitée pour X
    private static final double MAX_Y = 600; // Remplacez par la limite maximale souhaitée pour Y


    private Etat etat;

    // Constructeur
    public Controller() {
        this.view = new View();
        view.setController(this);
        this.demande = new Demande();
        this.plan = new Plan();
        this.etat = new PlanNonCharge(this);
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
            
            // Ajout de la fonctionnalité de drag
            mapPane.setOnMousePressed(this::handleMousePressed);
            mapPane.setOnMouseDragged(this::handleMouseDragged);
        }
    }
    
    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public View getView() {
        return this.view;
    }

    public Button getBoutonPlus() {
        return boutonPlus;
    }

    public BorderPane getPane() {
        return pane;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Button getChargerFichierButton() {
        return chargerFichierButton;
    }

    public Button getSelectionnerPointButton() {
        return selectionnerPointButton;
    }

    public Button getChargerPlan() {
        return chargerPlan;
    }

    public VBox getDeliveryInfoVBox() {
        return deliveryInfoVBox;
    }

    public Button getChargerNouveauPlan() {
        return chargerNouveauPlan;
    }

    public Button getCalculerChemin() {
        return calculerChemin;
    }

    public Label getLabel() {
        return label;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public Etat getEtat() {
        return etat;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public Button getRedoButton() {
        return redoButton;
    }

    public HBox getHboxUndoRedo() {
        return hboxUndoRedo;
    }


// Gestionnaire d'événements pour le clic de souris
private void handleMousePressed(MouseEvent event) {
    initialMouseX = event.getSceneX();
    initialMouseY = event.getSceneY();
    initialTranslateX = mapPane.getTranslateX();
    initialTranslateY = mapPane.getTranslateY();
}

private void handleMouseDragged(MouseEvent event) {
    double deltaX = event.getSceneX() - initialMouseX;
    double deltaY = event.getSceneY() - initialMouseY;

    // Coordonnées potentielles après le glissement
    double newTranslateX = initialTranslateX + deltaX;
    double newTranslateY = initialTranslateY + deltaY;

    // Dimensions de mapPane et de son conteneur (ex. AnchorPane ou autre parent)
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


    // Click sur le bouton "charger un (nouveau) plan"
    @FXML
    public void handleLoadPlan() {
        etat.handleLoadPlan();
        System.out.println(etat);
    }

    @FXML
    public void handleButtonClick() {
        // Affiche ou masque les boutons
        view.toggleButtons(boutonPlus, chargerFichierButton, selectionnerPointButton, chargerNouveauPlan);
    }

    @FXML
    public void handleSelectButton() {
        etat.handleSelectButton();
        System.out.println(etat);

    }

    @FXML
    public void handleFileButton() {
        etat.handleFileButton();
        System.out.println(etat);
    }  

    @FXML
    public void calculerChemin() {
        etat.calculerChemin();
        undoButton.setVisible(true);
        redoButton.setVisible(true);
        System.out.println(etat);
    }

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
