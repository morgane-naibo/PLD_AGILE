package model;

/**
 * Classe représentant un livreur dans le système.
 * Un livreur est défini par un identifiant unique.
 */
public class Livreur {

    /** Identifiant unique du livreur. */
    private int id;

    /**
     * Constructeur du livreur.
     * 
     * @param id l'identifiant unique du livreur.
     */
    public Livreur(int id) {
        this.id = id;
    }

    /**
     * Récupère l'identifiant du livreur.
     * 
     * @return l'identifiant du livreur.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Modifie l'identifiant du livreur.
     * 
     * @param newId le nouvel identifiant à attribuer.
     */
    public void setId(int newId) {
        this.id = newId;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du livreur.
     * Cette représentation inclut l'identifiant du livreur.
     * 
     * @return une chaîne de caractères décrivant le livreur.
     */
    @Override
    public String toString() {
        return "Livreur [ID: " + id + "]";
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.

}
