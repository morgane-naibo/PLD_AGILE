package model;

/**
 * Classe représentant un entrepôt dans le système.
 * Un entrepôt est une intersection spécifique qui a une heure de départ associée.
 * Elle hérite de la classe {@link Intersection}.
 */
public class Entrepot extends Intersection {

    /** Heure de départ associée à l'entrepôt. */
    private String heureDepart;

    /**
     * Constructeur avec paramètres.
     * 
     * @param id l'identifiant unique de l'entrepôt.
     * @param heure l'heure de départ associée à l'entrepôt.
     */
    public Entrepot(long id, String heure) {
        this.setId(id);
        this.heureDepart = heure;
    }

    /**
     * Constructeur par défaut.
     * Initialise un entrepôt sans paramètres spécifiques.
     */
    public Entrepot() {
        super();
    }

    /**
     * Récupère l'heure de départ de l'entrepôt.
     * 
     * @return l'heure de départ sous forme de chaîne de caractères.
     */
    public String getHeureDepart() {
        return this.heureDepart;
    }

    /**
     * Modifie l'heure de départ de l'entrepôt.
     * 
     * @param newHeureDepart la nouvelle heure de départ à attribuer.
     */
    public void setHeureDepart(String newHeureDepart) {
        this.heureDepart = newHeureDepart;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'entrepôt.
     * Cette méthode utilise la méthode {@code toString()} de la classe parente {@link Intersection}.
     * 
     * @return une chaîne de caractères représentant l'entrepôt.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.

}
