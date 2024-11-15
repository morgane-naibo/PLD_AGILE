package model;

/**
 * Classe représentant un point de livraison, qui hérite de la classe Intersection.
 * Un point de livraison est une intersection associée à une livraison spécifique.
 */
public class PointDeLivraison extends Intersection {

    /** Livraison associée à ce point de livraison. */
    private Livraison livraison;

    /**
     * Constructeur d'un point de livraison avec un identifiant et une livraison.
     * 
     * @param id l'identifiant du point de livraison.
     * @param livraison la livraison associée à ce point de livraison.
     */
    public PointDeLivraison(Long id, Livraison livraison) {
        this.setId(id);
        this.livraison = livraison;
    }

    /**
     * Constructeur d'un point de livraison à partir d'une intersection existante.
     * 
     * @param inter l'intersection à partir de laquelle le point de livraison est créé.
     *              Les informations de cette intersection sont copiées dans le point de livraison.
     */
    public PointDeLivraison(Intersection inter) {
        this.id = inter.getId();
        this.latitude = inter.getLatitude();
        this.longitude = inter.getLongitude();
        this.listeTroncons = inter.getListeTroncons();
        this.numero = inter.getNumero();
        this.livraison = null;
    }

    /**
     * Constructeur d'un point de livraison avec uniquement un identifiant.
     * 
     * @param id l'identifiant du point de livraison.
     */
    public PointDeLivraison(Long id) {
        this.setId(id);
    }

    /**
     * Récupère la livraison associée à ce point de livraison.
     * 
     * @return la livraison associée au point de livraison.
     */
    public Livraison getLivraison() {
        return this.livraison;
    }

    /**
     * Modifie la livraison associée à ce point de livraison.
     * 
     * @param newLivraison la nouvelle livraison à associer au point de livraison.
     */
    public void setLivraison(Livraison newLivraison) {
        this.livraison = newLivraison;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du point de livraison.
     * Cette méthode utilise la méthode `toString` de la classe parente `Intersection` pour la représentation.
     * 
     * @return une chaîne de caractères représentant le point de livraison.
     */
    @Override
    public String toString() {
        return super.toString();  // Utilise la méthode `toString` de l'Intersection pour afficher l'ID et autres informations
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.
}
