package model;

import java.util.List;

/**
 * Classe représentant une tournée de livraison.
 * Une tournée est un trajet effectué par un livreur, avec une liste d'étapes à parcourir.
 */
public class Tournee extends Trajet {

    /** Le livreur qui effectue cette tournée. */
    private Livreur livreur;

    /**
     * Constructeur de la tournée.
     * 
     * @param listeEtapes la liste des étapes constituant la tournée.
     * @param livreur le livreur qui effectue cette tournée.
     */
    public Tournee(List<Etape> listeEtapes, Livreur livreur) {
        super(listeEtapes);  // Appel au constructeur de la classe parente Trajet
        this.livreur = livreur;
    }

    /**
     * Modifie la liste des étapes de la tournée.
     * 
     * @param newListeEtapes la nouvelle liste d'étapes à assigner à cette tournée.
     */
    public void setListeEtapes(List<Etape> newListeEtapes) {
        this.listeEtapes = newListeEtapes;
    }

    /**
     * Récupère le livreur assigné à cette tournée.
     * 
     * @return le livreur assigné à la tournée.
     */
    public Livreur getLivreur() {
        return this.livreur;
    }

    /**
     * Modifie le livreur assigné à cette tournée.
     * 
     * @param newLivreur le nouveau livreur à associer à cette tournée.
     */
    public void setLivreur(Livreur newLivreur) {
        this.livreur = newLivreur;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la tournée.
     * Cette représentation inclut les identifiants des étapes du trajet.
     * 
     * @return une chaîne de caractères représentant la tournée.
     */
    @Override
    public String toString() {
        String msg;
        msg = "Tournée : ";
        // Parcours de la liste des étapes pour afficher les points de départ
        for (int i = 0; i < this.listeEtapes.size(); i++) {
            msg += this.listeEtapes.get(i).getDepart().getId();  // Affiche l'ID du départ
            msg += "  -> ";
        }
        // Affiche l'ID du dernier point d'arrivée
        msg += this.listeEtapes.get(this.listeEtapes.size() - 1).getArrivee().getId();  
        return msg;
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.
}
