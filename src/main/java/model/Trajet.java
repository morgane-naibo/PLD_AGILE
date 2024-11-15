package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe représentant un trajet constitué d'une liste d'étapes.
 * Un trajet peut être composé de plusieurs étapes qui sont parcourues dans un ordre précis.
 */
public class Trajet {

    /** Liste des étapes qui composent ce trajet. */
    protected List<Etape> listeEtapes;

    /**
     * Constructeur du trajet avec une liste d'étapes spécifiée.
     * 
     * @param listeEtapes la liste des étapes qui forment le trajet.
     */
    public Trajet(List<Etape> listeEtapes) {
        this.listeEtapes = listeEtapes;
    }

    /**
     * Constructeur d'un trajet sans étapes initiales.
     * Crée un trajet avec une liste d'étapes vide.
     */
    public Trajet() {
        this.listeEtapes = new ArrayList<Etape>();  // Initialise la liste des étapes comme vide
    }

    /**
     * Récupère la liste des étapes du trajet.
     * 
     * @return la liste des étapes.
     */
    public List<Etape> getListeEtapes() {
        return this.listeEtapes;
    }

    /**
     * Modifie la liste des étapes du trajet.
     * 
     * @param newListeEtapes la nouvelle liste des étapes à assigner.
     */
    public void setListeEtapes(List<Etape> newListeEtapes) {
        this.listeEtapes = newListeEtapes;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du trajet.
     * Cette méthode génère une chaîne qui décrit toutes les étapes du trajet.
     * 
     * @return une chaîne de caractères représentant le trajet et ses étapes.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trajet :\n");

        // Parcours chaque étape et ajoute sa représentation sous forme de chaîne
        for (Etape etape : listeEtapes) {
            sb.append(etape.toString());  // Appel à la méthode toString de l'étape
        }

        return sb.toString();
    }

    /**
     * Ajoute une étape à la fin de la liste des étapes du trajet.
     * 
     * @param etape l'étape à ajouter au trajet.
     */
    public void ajouterEtape(Etape etape) {
        this.listeEtapes.add(etape);
    }

    /**
     * Calcule la durée totale du trajet en fonction des étapes.
     * La durée est estimée en fonction de la longueur des étapes et d'une vitesse moyenne de 15 km/h.
     * Une pause de 5 minutes est ajoutée après chaque étape, sauf pour la dernière.
     * 
     * @return la durée totale du trajet en minutes.
     */
    public double calculerDureeTrajet() {
        double duree = 0.0;

        // Calcul de la durée de chaque étape (en minutes)
        for (Etape i : this.listeEtapes) {
            // La durée d'une étape est calculée en fonction de sa longueur et d'une vitesse de 15 km/h
            duree += i.getLongueur() * 60 / 15000 + 5.0;  // 15000 m/h = 15 km/h
        }

        // On retire les 5 minutes de la dernière étape (pas de pause après la dernière)
        duree -= 5.0;

        return duree;
    }

}

