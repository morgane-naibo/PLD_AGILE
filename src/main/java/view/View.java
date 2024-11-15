package view;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;
import javafx.scene.input.MouseEvent;
import model.Intersection;
import model.Livraison;
import model.Plan;
import model.PointDeLivraison;
import model.Troncon;
import model.Demande;
import model.Entrepot;
import model.Etape;

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

/**
 * Classe View représentant la vue graphique du système.
 * Gère l'affichage et les interactions avec les éléments graphiques
 * tels que le plan, les livraisons, et les tournées.
 */
public class View {

    /** Largeur du panneau de visualisation. */
    private double paneWidth = 550.0;

    /** Hauteur du panneau de visualisation. */
    private double paneHeight = 550.0;

    /** Latitude minimale utilisée pour la mise à l'échelle du plan. */
    private double latMin;

    /** Latitude maximale utilisée pour la mise à l'échelle du plan. */
    private double latMax;

    /** Longitude minimale utilisée pour la mise à l'échelle du plan. */
    private double longMin;

    /** Longitude maximale utilisée pour la mise à l'échelle du plan. */
    private double longMax;

    /** Plan contenant les intersections et tronçons. */
    private Plan plan;

    /** Entrepôt associé à la demande. */
    private Entrepot entrepot;

    /** Indique si les boutons de l'interface sont visibles. */
    private boolean button_visible = false;

    /** Indique si le mode de sélection est activé. */
    private boolean selectionModeEnabled = false;

    /** Indique si l'entrepôt est défini. */
    private boolean entrepotExiste = false;

    /** Indique si une fenêtre contextuelle est ouverte. */
    private boolean popupOuverte = false;

    /** Indique si une tournée a été calculée. */
    private boolean tourneeCalculee = false;

    /** Représentation graphique de l'entrepôt sous forme de cercle. */
    private Circle entrepotCircle;

    /** Étiquette associée à l'entrepôt pour afficher des informations. */
    private Label labelEntrepot;

    /** Demande contenant les informations sur les livraisons. */
    public Demande demande;

    /** Pile des intersections supprimées pour permettre un retour en arrière. */
    private Stack<Intersection> intersectionsSupprimees = new Stack<>();

    /** Pile des étiquettes supprimées pour permettre un retour en arrière. */
    private Stack<Label> labelsSupprimes = new Stack<>();

    /** Pile des intersections ajoutées pour permettre un retour en arrière. */
    private Stack<Intersection> intersectionsAjoutees = new Stack<>();

    /** Pile des étiquettes ajoutées pour permettre un retour en arrière. */
    private Stack<Label> labelsAjoutes = new Stack<>();

    /** Contrôleur principal de l'application. */
    private Controller controller;

    /** Dernière commande exécutée. */
    public Commande derniereCommande;

    /** Couleurs associées à chaque livreur pour la visualisation. */
    private Map<Integer, Color> livreurCouleurs = new HashMap<>();

    /** Pile des commandes exécutées pour permettre un retour en arrière. */
    public Stack<Commande> commandes = new Stack<>();

    /** Pile des commandes annulées pour permettre une réexécution. */
    Stack<Commande> commandesAnnulees = new Stack<>();

    /** Liste des livreurs associés aux tournées. */
    private List<Livreur> livreurs = new ArrayList<>();

    /** Livreur actuellement sélectionné. */
    private Livreur livreurSelectionne = null;

    /** Liste des tournées calculées. */
    private List<Tournee> tournees = new ArrayList<>();


    /**
     * Retourne la liste des tournées calculées.
     *
     * @return Liste des tournées {@link Tournee}.
     */
    public List<Tournee> getTournees() {
        return tournees;
    }

    /**
     * Retourne la demande actuelle associée à la vue.
     *
     * @return La demande {@link Demande}.
     */
    public Demande getDemande() {
        return demande;
    }

    /**
     * Indique si un entrepôt est défini.
     *
     * @return {@code true} si l'entrepôt existe, sinon {@code false}.
     */
    public boolean isEntrepotExiste() {
        return entrepotExiste;
    }

    /**
     * Indique si le mode de sélection est activé.
     *
     * @return {@code true} si le mode de sélection est activé, sinon {@code false}.
     */
    public boolean isSelectionModeEnabled() {
        return selectionModeEnabled;
    }

    /**
     * Indique si une tournée a été calculée.
     *
     * @return {@code true} si une tournée a été calculée, sinon {@code false}.
     */
    public boolean isTourneeCalculee() {
        return tourneeCalculee;
    }

    /**
     * Définit si une tournée a été calculée.
     *
     * @param tourneeCalculee {@code true} si la tournée est calculée, sinon {@code false}.
     */
    public void setTourneeCalculee(boolean tourneeCalculee) {
        this.tourneeCalculee = tourneeCalculee;
    }

    /**
     * Retourne l'entrepôt actuel.
     *
     * @return L'entrepôt {@link Entrepot}.
     */
    public Entrepot getEntrepot() {
        return entrepot;
    }

    /**
     * Retourne la pile des intersections supprimées.
     *
     * @return Pile des intersections supprimées {@link Intersection}.
     */
    public Stack<Intersection> getIntersectionsSupprimees() {
        return this.intersectionsSupprimees;
    }

    /**
     * Retourne la pile des labels supprimés.
     *
     * @return Pile des labels supprimés {@link Label}.
     */
    public Stack<Label> getLabelsSupprimes() {
        return this.labelsSupprimes;
    }

    /**
     * Retourne la pile des intersections ajoutées.
     *
     * @return Pile des intersections ajoutées {@link Intersection}.
     */
    public Stack<Intersection> getIntersectionsAjoutees() {
        return this.intersectionsAjoutees;
    }

    /**
     * Retourne la pile des labels ajoutés.
     *
     * @return Pile des labels ajoutés {@link Label}.
     */
    public Stack<Label> getLabelsAjoutes() {
        return this.labelsAjoutes;
    }

    /**
     * Définit le contrôleur associé à la vue.
     *
     * @param controller Le contrôleur {@link Controller}.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Retourne la pile des commandes effectuées.
     *
     * @return Pile des commandes effectuées {@link Commande}.
     */
    public Stack<Commande> getCommandes() {
        return this.commandes;
    }

    /**
     * Retourne la pile des commandes annulées.
     *
     * @return Pile des commandes annulées {@link Commande}.
     */
    public Stack<Commande> getCommandesAnnulees() {
        return this.commandesAnnulees;
    }

    /**
     * Définit la pile des commandes effectuées.
     *
     * @param commandes La pile des commandes {@link Commande}.
     */
    public void setCommandes(Stack<Commande> commandes) {
        this.commandes = commandes;
    }

    /**
     * Retourne la dernière commande effectuée.
     *
     * @return La dernière commande {@link Commande}.
     */
    public Commande getDerniereCommande(Commande derniereCommande) {
        return commandes.peek();
    }

    /**
     * Définit la dernière commande effectuée.
     *
     * @param derniereCommande La dernière commande {@link Commande}.
     */
    public void setDerniereCommande(Commande derniereCommande) {
        this.derniereCommande = derniereCommande;
    }

    /**
     * Retourne la liste des livreurs.
     *
     * @return Liste des livreurs {@link Livreur}.
     */
    public List<Livreur> getLivreurs() {
        return livreurs;
    }

    /**
     * Retourne le livreur actuellement sélectionné.
     *
     * @return Le livreur sélectionné {@link Livreur}.
     */
    public Livreur getLivreurSelectionne() {
        return livreurSelectionne;
    }

    /**
     * Indique si les boutons sont actuellement visibles.
     *
     * @return {@code true} si les boutons sont visibles, sinon {@code false}.
     */
    public Boolean getButtonVisible() {
        return button_visible;
    }

    /**
     * Constructeur par défaut de la classe View.
     * Initialise une nouvelle demande.
     */
    public View() {
        this.demande = new Demande();
    }


    /**
     * Affiche une boîte de dialogue pour indiquer qu'un fichier a été chargé avec succès.
     */
    public void fileChooser() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Fichier chargé avec succès !");
        alert.showAndWait();
    }

    /**
     * Définit la demande actuelle.
     * 
     * @param demande La demande à définir.
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /**
     * Définit le plan et réinitialise les données associées.
     * 
     * @param plan Le plan à définir.
     */
    public void setPlan(Plan plan) {
        demande = new Demande();
        System.out.println(controller.getDemande().getNbLivreurs());
        this.demande.setNbLivreur(controller.getDemande().getNbLivreurs());
        this.demande.setPlan(plan);
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

    /**
     * Affiche le plan avec ses tronçons sur le panneau principal.
     * 
     * @param pane            Le panneau où afficher le plan.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label           Le label utilisé pour les messages.
     * @param messageLabel    Le label pour les messages d'état.
     * @param calculerChemin  Le bouton pour calculer le chemin.
     * @param buttons         Les autres boutons à afficher ou masquer.
     */
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

    /**
     * Active ou désactive l'affichage des boutons spécifiés.
     * 
     * @param boutonPlus Le bouton "+" qui contrôle l'affichage.
     * @param buttons    Les boutons à afficher ou masquer.
     */
    public void toggleButtons(Button boutonPlus, Button... buttons) {
        if (!button_visible) {
            if (!entrepotExiste) {
                controller.getSelectionnerPointButton().setText("Selectionner un entrepot sur la carte");
            } else {
                controller.getSelectionnerPointButton().setText("Ajouter un point de livraison sur la carte");
            }
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

    /**
     * Ajoute les boutons spécifiés au panneau principal et les rend visibles.
     * 
     * @param pane            Le panneau principal.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param buttons         Les boutons à ajouter.
     */
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

    /**
     * Active ou désactive le mode de sélection des points sur la carte.
     * 
     * @param label                  Le label utilisé pour les messages.
     * @param selectionnerPointButton Le bouton pour sélectionner un point.
     * @param chargerFichierButton   Le bouton pour charger un fichier.
     * @param chargerNouveauPlan     Le bouton pour charger un nouveau plan.
     * @param exportXML              Le bouton pour exporter en XML.
     * @param deliveryInfoVBox       La boîte contenant les informations sur les livraisons.
     */
    public void toggleSelectionMode(Label label, Button selectionnerPointButton, Button chargerFichierButton, Button chargerNouveauPlan, Button exportXML, VBox deliveryInfoVBox) {
        selectionModeEnabled = !selectionModeEnabled;
        chargerFichierButton.setVisible(false);
        selectionnerPointButton.setVisible(false);
        chargerNouveauPlan.setVisible(false);
        exportXML.setVisible(false);
        if (!selectionModeEnabled) {
            label.setText("Cliquer sur un point existant pour le supprimer.");
        } else if (entrepotExiste) {
            label.setText("Veuillez selectionner les points de livraison.");
        } else {
            label.setText("Veuillez selectionner en premier un entrepôt, puis les points de livraison.");
        }
    }

    /**
     * Gère le clic sur une ligne pour sélectionner un point sur la carte.
     * 
     * @param event            L'événement de clic.
     * @param pane             Le panneau où les éléments sont affichés.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label            Le label utilisé pour les messages.
     * @param calculerChemin   Le bouton pour calculer le chemin.
     */
    public void handleLineClick(MouseEvent event, Pane pane, VBox deliveryInfoVBox, Label label, Button calculerChemin) {
        if (!selectionModeEnabled) {
            return;
        }

        if (tourneeCalculee) {
            calculerChemin.setVisible(false);
        } else {
            calculerChemin.setVisible(true);
        }

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
                if (tourneeCalculee && livreurSelectionne == null) {
                    controller.getMessageLabel().setText("Veuillez sélectionner un livreur.");
                    return;
                }

                PointDeLivraison pdl = new PointDeLivraison(intersection);
                this.demande.ajouterPointDeLivraison(pdl);
                deliveryInfoVBox.setVisible(true);

                Intersection inter = plan.chercherIntersectionParId(pdl.getId());
                double startX = longitudeToX(inter.getLongitude());
                double startY = latitudeToY(inter.getLatitude());

                Label pdLabel = new Label("Point de Livraison:");
                for (Troncon troncon : inter.getListeTroncons()) {
                    pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                }
                deliveryInfoVBox.getChildren().add(pdLabel);

                Circle newPdl = new Circle(startX, startY, 5, Color.RED);
                newPdl.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));
                pane.getChildren().add(newPdl);
                pdLabel.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));

                if (tourneeCalculee && livreurSelectionne != null) {
                    intersectionsAjoutees.push(inter);
                    labelsAjoutes.push(pdLabel);
                    AjouterPointDeLivraisonCommande ajouterPointDeLivraisonCommande = new AjouterPointDeLivraisonCommande(this, pane, deliveryInfoVBox, inter, pdLabel, controller.getMessageLabel());
                    commandes.push(ajouterPointDeLivraisonCommande);
                    derniereCommande = ajouterPointDeLivraisonCommande;

                    try {
                        Trajet trajet = this.demande.ajouterPDLaMatrice(livreurSelectionne.getId(), pdl);
                        Tournee tournee = new Tournee(trajet.getListeEtapes(), livreurSelectionne);
                        tournees.set((int) livreurSelectionne.getId(), tournee);
                        reafficherTournee(pane, deliveryInfoVBox, livreurSelectionne, label);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Affiche la demande spécifiée sur le panneau principal.
     * 
     * @param demandeFile    La demande à afficher.
     * @param pane           Le panneau où afficher la demande.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label          Le label utilisé pour les messages.
     */
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

    /**
     * Gère le clic sur un cercle représentant une intersection.
     * 
     * @param inter           L'intersection associée au cercle.
     * @param pane            Le panneau où les éléments sont affichés.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label           Le label utilisé pour les messages.
     */
    public void handleCircleClick(Intersection inter, Pane pane, VBox deliveryInfoVBox, Label label) {
        double startX = longitudeToX(inter.getLongitude());
        double startY = latitudeToY(inter.getLatitude());
        long remainingPoints = 0;

        if (livreurSelectionne != null) {
            Tournee tournee = tournees.get((int) livreurSelectionne.getId());
            remainingPoints = tournee.getListeEtapes().stream()
                .map(Etape::getArrivee)
                .distinct()
                .count();
        }

        if (!popupOuverte && ((tourneeCalculee && livreurSelectionne != null && inter.getId() != entrepot.getId() && (remainingPoints - 1) != 1) || !tourneeCalculee)) {
            Circle newPdl = new Circle(startX, startY, 8, inter.getId() == entrepot.getId() ? Color.BLUE : Color.RED);
            newPdl.setStrokeWidth(5);
            newPdl.setStroke(inter.getId() == entrepot.getId() ? Color.LIGHTBLUE : Color.CORAL);
            pane.getChildren().add(newPdl);
            label.setStyle("-fx-background-color: lightblue;");
            popupDelete(startX, startY, inter, newPdl, pane, deliveryInfoVBox, newPdl, label);
        } else if (tourneeCalculee && livreurSelectionne != null && inter.getId() == entrepot.getId()) {
            controller.getMessageLabel().setText("Vous ne pouvez pas supprimer l'entrepôt.");
        } else if (remainingPoints - 1 == 1) {
            controller.getMessageLabel().setText("Vous ne pouvez pas supprimer le seul point de livraison.");
        }
        System.out.println("Intersection clicked: " + inter.getId());
    }

    /**
     * Ajoute un panneau contenant un message d'information.
     * 
     * @param pane Le panneau principal où le message sera ajouté.
     */
    public void addMessagePane(Pane pane) {
        pane.getChildren().clear();
        Label message = new Label("Sélectionnez l'entrepôt et les points de livraison.");
        HBox box = new HBox();
        box.setStyle("-fx-background-color:lightblue;");
        box.getChildren().add(message);
        pane.getChildren().add(box);
    }

    /**
     * Affiche une pop-up pour confirmer la suppression d'un point de livraison.
     * 
     * @param x                La position X où afficher la pop-up.
     * @param y                La position Y où afficher la pop-up.
     * @param inter            L'intersection associée au point de livraison.
     * @param circle           Le cercle représentant le point de livraison.
     * @param pane             Le panneau où les éléments sont affichés.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param newPdl           Le cercle représentant le point de livraison à supprimer.
     * @param pdlLabel         Le label associé au point de livraison.
     */
    public void popupDelete(double x, double y, Intersection inter, Circle circle, Pane pane, VBox deliveryInfoVBox, Circle newPdl, Label pdlLabel) {
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
            HBox buttonBox = new HBox(30);
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
            } else {
                popup.setX(x + 260);
            }
            popup.setY(y);

            popupOuverte = true;

            // Afficher la pop-up en relation avec la fenêtre principale
            popup.show(pane.getScene().getWindow());
        }
    }


    /**
     * Supprime un point de livraison de l'interface et met à jour la demande et la tournée si nécessaire.
     *
     * @param inter        L'intersection associée au point de livraison à supprimer.
     * @param pane         Le panneau où les éléments sont affichés.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label        Le label associé au point de livraison.
     * @param addCommand   Indique si cette suppression doit être enregistrée comme commande.
     * @param livreur      Le livreur associé à la tournée.
     */
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
            pane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getCenterX() == longitudeToX(inter.getLongitude()) && ((Circle) node).getCenterY() == latitudeToY(inter.getLatitude()));
            deliveryInfoVBox.getChildren().remove(label);
            try {
                Trajet nouveauTrajet = demande.supprimerPDL((int) livreur.getId(), pdl);
                tournees.get((int) livreur.getId()).setListeEtapes(nouveauTrajet.getListeEtapes());
                reafficherTournee(pane, deliveryInfoVBox, livreur, label);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Réaffiche un point de livraison supprimé précédemment, en le réintégrant à la tournée.
     *
     * @param intersection L'intersection associée au point de livraison.
     * @param pane         Le panneau où les éléments sont affichés.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param label        Le label associé au point de livraison.
     * @param livreur      Le livreur associé à la tournée.
     */
    public void reafficherPointDeLivraison(Intersection intersection, Pane pane, VBox deliveryInfoVBox, Label label, Livreur livreur) {
        try {
            if (tourneeCalculee && livreurSelectionne == null) {
                controller.getMessageLabel().setText("Veuillez sélectionner un livreur.");
                return;
            }

            PointDeLivraison pdl = new PointDeLivraison(intersection);
            this.demande.ajouterPointDeLivraison(pdl);
            deliveryInfoVBox.setVisible(true);

            Intersection inter = plan.chercherIntersectionParId(pdl.getId());
            double startX = longitudeToX(inter.getLongitude());
            double startY = latitudeToY(inter.getLatitude());

            Label pdLabel = new Label("Point de Livraison:");
            for (Troncon troncon : inter.getListeTroncons()) {
                pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
            }
            deliveryInfoVBox.getChildren().add(pdLabel);

            Circle newPdl = new Circle(startX, startY, 5, Color.RED);
            newPdl.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));
            pane.getChildren().add(newPdl);
            pdLabel.setOnMouseClicked(event2 -> handleCircleClick(inter, pane, deliveryInfoVBox, pdLabel));

            try {
                Trajet trajet = this.demande.ajouterPDLaMatrice(livreurSelectionne.getId(), pdl);
                Tournee tournee = new Tournee(trajet.getListeEtapes(), livreurSelectionne);
                tournees.set((int) livreurSelectionne.getId(), tournee);
                reafficherTournee(pane, deliveryInfoVBox, livreurSelectionne, label);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IDIntersectionException e) {
            e.printStackTrace();
            controller.getMessageLabel().setText("Erreur lors du calcul de la tournée.");
        }
    }

    /**
     * Réaffiche une tournée mise à jour avec de nouvelles étapes calculées.
     *
     * @param pane          Le panneau où la tournée est affichée.
     * @param deliveryInfoVBox La boîte contenant les informations sur les livraisons.
     * @param livreur       Le livreur associé à la tournée.
     * @param messageLabel  Le label utilisé pour afficher des messages.
     */
    public void reafficherTournee(Pane pane, VBox deliveryInfoVBox, Livreur livreur, Label messageLabel) {
        Tournee tournee = tournees.get((int)livreur.getId());

        List<PointDeLivraison> pointsRestants = tournee.getListeEtapes().stream()
            .map(etape -> new PointDeLivraison(etape.getArrivee().getId(), new Livraison(0, etape.getArrivee().getId(), 5.0, 5.0)))
            .collect(Collectors.toList());

        List<Etape> nouvellesEtapes = new ArrayList<>();

        Intersection origine;
        try {
            origine = plan.chercherIntersectionParId(entrepot.getId());
        } catch (IDIntersectionException e) {
            e.printStackTrace();
            return;
        }

        for (PointDeLivraison pdl : pointsRestants) {
            try {
                Intersection destination = plan.chercherIntersectionParId(pdl.getId());
                if (destination != null) {
                    Etape etape = plan.chercherPlusCourtChemin(origine, destination);
                    if (etape != null) {
                        nouvellesEtapes.add(etape);
                        origine = destination;
                    }
                }
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }

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

        nouvellesEtapes = nouvellesEtapes.stream()
            .filter(etape -> etape != null)
            .collect(Collectors.toList());

        tournees.get((int)livreurSelectionne.getId()).setListeEtapes(nouvellesEtapes);

        afficherTourneeSurCarte(nouvellesEtapes, pane, livreurSelectionne, messageLabel);
    }

    /**
     * Convertit une latitude en position Y pour le panneau.
     *
     * @param latitude La latitude à convertir.
     * @return La position Y correspondante.
     */
    private double latitudeToY(double latitude) {
        return paneHeight * (latMax - latitude) / (latMax - latMin);
    }

    /**
     * Convertit une longitude en position X pour le panneau.
     *
     * @param longitude La longitude à convertir.
     * @return La position X correspondante.
     */
    private double longitudeToX(double longitude) {
        return paneWidth * (longitude - longMin) / (longMax - longMin);
    }

    /**
     * Convertit une position X en longitude.
     *
     * @param x La position X à convertir.
     * @return La longitude correspondante.
     */
    private double xToLongitude(double x) {
        return x / paneWidth * (longMax - longMin) + longMin;
    }

    /**
     * Convertit une position Y en latitude.
     *
     * @param y La position Y à convertir.
     * @return La latitude correspondante.
     */
    private double yToLatitude(double y) {
        return latMax - (y / paneHeight * (latMax - latMin));
    }

    /**
     * Calcule le chemin d'une tournée pour un livreur et l'affiche graphiquement sur le panneau.
     *
     * @param pane             Le panneau où le chemin sera affiché.
     * @param deliveryInfoVBox La boîte contenant les informations des livraisons.
     * @param trajet           Le trajet correspondant à la tournée du livreur.
     * @param livreurId        L'identifiant du livreur associé à la tournée.
     * @param messageLabel     Le label pour afficher des messages informatifs.
     */
    public void calculerChemin(Pane pane, VBox deliveryInfoVBox, Trajet trajet, int livreurId, Label messageLabel) {
        messageLabel.setText("Selectionnez une tournée pour la modifier, en cliquant sur le texte surligné ou sur la tournée graphique.");
        
        Set<String> tronconsAffiches = new HashSet<>();

        Livreur livreur = new Livreur(livreurId);
        livreurs.add(livreur);

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
        Color lightColor = color.deriveColor(0, 1, 1.3, 0.5); // Augmente la luminosité de 30 % et règle l'opacité à 50 %
        livreurLabel.setStyle("-fx-background-color: " + toHexString(lightColor) + ";");
        livreurLabel.setOnMouseClicked(event -> {
            this.livreurSelectionne = livreur; // Met à jour le livreur sélectionné
            afficherTourneeSurCarte(trajet.getListeEtapes(), pane, livreur, messageLabel);
            System.out.println("Livreur selectionne: " + livreurSelectionne);
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


                arrowHead.setLayoutX(arrowX + 4);
                arrowHead.setLayoutY(arrowY);

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
                    pdLabel.setOnMouseClicked(event -> {
                        handleCircleClick(pdl, pane, deliveryInfoVBox, pdLabel);
                    });
                }
                deliveryInfoVBox.getChildren().add(pdLabel);
                deliveryInfoVBox.requestLayout();
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Affiche la tournée d'un livreur sélectionné sur la carte.
     *
     * @param etapes        La liste des étapes de la tournée à afficher.
     * @param pane          Le panneau où les lignes et flèches représentant la tournée seront dessinées.
     * @param livreur       Le livreur pour lequel la tournée est affichée.
     * @param messageLabel  Le label utilisé pour afficher les messages contextuels liés à la tournée.
     */
    public void afficherTourneeSurCarte(List<Etape> etapes, Pane pane, Livreur livreur, Label messageLabel) {

        messageLabel.setText("Tournée du livreur " + (livreur.getId() + 1) + " sélectionnée. Cliquez sur un point pour le supprimer, ou sur le bouton + pour ajouter un nouveau point.");

        Set<String> tronconsAffiches = new HashSet<>();

        // Supprime les anciens tracés de la tournée du plan
        this.livreurSelectionne = livreur;

        System.out.println("Livreur selectionne: " + livreurSelectionne);

        pane.getChildren().removeIf(node -> node instanceof Line && ((Line) node).getStroke() == livreurCouleurs.get((int) livreur.getId()));
        pane.getChildren().removeIf(node -> node instanceof Polygon);

        // Ajoute les nouvelles lignes de la tournée
        for (Etape etape : etapes) {
            if (etape != null) {
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

                    arrowHead.setLayoutX(arrowX + 4);
                    arrowHead.setLayoutY(arrowY);

                    // Rotation de la flèche pour qu'elle pointe dans la direction du tronçon
                    arrowHead.setRotate(angle);

                    pane.getChildren().add(arrowHead);
                }
            }
        }
        controller.getDeliveryInfoVBox().getChildren().clear();
        controller.getDeliveryInfoVBox().getChildren().add(new Label("Liste des Points de Livraison"));
        try {
            Intersection intersection = plan.chercherIntersectionParId(entrepot.getId());
            List<Troncon> listeTroncons = intersection.getListeTroncons();
            System.out.println(entrepot);
            labelEntrepot = new Label("Entrepôt:");
            for (Troncon troncon : listeTroncons) {
                labelEntrepot.setText(labelEntrepot.getText() + troncon.getNomRue() + ", ");
            }
            controller.getDeliveryInfoVBox().getChildren().add(labelEntrepot);
        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }
        for (PointDeLivraison pdl : demande.getListePointDeLivraison()) {
            try {
                Intersection inter = plan.chercherIntersectionParId(pdl.getId());
                Label pdLabel = new Label("Point de Livraison:");
                for (Troncon troncon : inter.getListeTroncons()) {
                    pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                    pdLabel.setOnMouseClicked(event -> {
                        handleCircleClick(inter, pane, controller.getDeliveryInfoVBox(), pdLabel);
                    });
                }
                controller.getDeliveryInfoVBox().getChildren().add(pdLabel);
            } catch (IDIntersectionException e) {
                e.printStackTrace();
            }
        }
        for (Tournee tournee : tournees) {
            Color color = livreurCouleurs.get(tournee.getLivreur().getId());
            Label vide = new Label(" ");
            controller.getDeliveryInfoVBox().getChildren().add(vide);
            Label livreurLabel = new Label("Livreur " + (tournee.getLivreur().getId() + 1) + ": " + String.format("%.2f", tournee.calculerDureeTrajet()) + " minutes");
            Color lightColor = color.deriveColor(0, 1, 1.3, 0.5); // Augmente la luminosité de 30 % et règle l'opacité à 50 %
            livreurLabel.setStyle("-fx-background-color: " + toHexString(lightColor) + ";");
            livreurLabel.setOnMouseClicked(event -> {
                this.livreurSelectionne = tournee.getLivreur(); // Met à jour le livreur sélectionné
                afficherTourneeSurCarte(tournee.getListeEtapes(), pane, livreurSelectionne, messageLabel);
            });
            controller.getDeliveryInfoVBox().getChildren().add(livreurLabel);
            double heureDepartProchainTroncon = 8.0;
            for (Etape etape : tournee.getListeEtapes()) {
                double temps = etape.getLongueur() * 60 / 15000;
                temps = Math.round(temps * 100.0) / 100.0;
                double heureFin = heureDepartProchainTroncon + temps / 60;
                int heures = (int) heureFin;
                int minutes = (int) ((heureFin - heures) * 60);
                Label labelEtape = new Label("Etape : " + String.format("%02d:%02d", heures, minutes));
                controller.getDeliveryInfoVBox().getChildren().add(labelEtape);
                heureDepartProchainTroncon = heureFin;
                for (Troncon troncon : etape.getListeTroncons()) {
                    Label label = new Label("    Troncon: " + troncon.getNomRue());
                    if (!tronconsAffiches.contains(troncon.getNomRue())) {
                        controller.getDeliveryInfoVBox().getChildren().add(label);
                        tronconsAffiches.add(troncon.getNomRue());
                    }
                }
                tronconsAffiches.clear();
                try {
                    Intersection pdl = plan.chercherIntersectionParId(etape.getArrivee().getId());
                    Label pdLabel = new Label("    Point de Livraison:");
                    for (Troncon troncon : pdl.getListeTroncons()) {
                        pdLabel.setText(pdLabel.getText() + troncon.getNomRue() + ", ");
                        pdLabel.setOnMouseClicked(event -> {
                            handleCircleClick(pdl, pane, controller.getDeliveryInfoVBox(), pdLabel);
                        });
                    }
                    controller.getDeliveryInfoVBox().getChildren().add(pdLabel);
                } catch (IDIntersectionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Convertit une couleur en une représentation hexadécimale sous forme de chaîne de caractères.
     *
     * @param color La couleur à convertir en format hexadécimal.
     * @return Une chaîne de caractères représentant la couleur au format hexadécimal (par exemple, "#RRGGBB").
     */
    public String toHexString(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

}