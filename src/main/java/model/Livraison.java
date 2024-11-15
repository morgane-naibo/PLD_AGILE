package model;

/**
 * Classe représentant une livraison dans le système.
 * Une livraison inclut des informations sur la durée de l'enlèvement et de la livraison, 
 * les adresses associées, ainsi que le livreur assigné.
 */
public class Livraison {

    /** Durée de la livraison en minutes. */
    private double dureeLivraison;

    /** Durée de l'enlèvement en minutes. */
    private double dureeEnlevement;

    /** Adresse de l'enlèvement, représentée par son identifiant unique. */
    private long adresseEnlevement;

    /** Adresse de la livraison, représentée par son identifiant unique. */
    private long adresseLivraison;

    /** Livreur assigné à la livraison. */
    private Livreur livreur;

    /**
     * Constructeur avec les paramètres principaux.
     * 
     * @param adresseEnlevement l'identifiant de l'adresse où le colis est enlevé.
     * @param adresseLivraison l'identifiant de l'adresse où le colis est livré.
     * @param dureeEnlevement la durée de l'enlèvement en minutes.
     * @param dureeLivraison la durée de la livraison en minutes.
     */
    public Livraison(long adresseEnlevement, long adresseLivraison, double dureeEnlevement, double dureeLivraison) {
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
    }

    /**
     * Récupère la durée de la livraison.
     * 
     * @return la durée de la livraison en minutes.
     */
    public double getTempsLivraison() {
        return this.dureeLivraison;
    }

    /**
     * Récupère la durée de l'enlèvement.
     * 
     * @return la durée de l'enlèvement en minutes.
     */
    public double getTempsEnlevement() {
        return this.dureeEnlevement;
    }

    /**
     * Récupère l'adresse de livraison.
     * 
     * @return l'identifiant de l'adresse de livraison.
     */
    public long getAdresseLivraison() {
        return this.adresseLivraison;
    }

    /**
     * Récupère l'adresse d'enlèvement.
     * 
     * @return l'identifiant de l'adresse d'enlèvement.
     */
    public long getAdresseEnelevement() {
        return this.adresseEnlevement;
    }

    /**
     * Récupère le livreur assigné à cette livraison.
     * 
     * @return le livreur assigné.
     */
    public Livreur getLivreur() {
        return this.livreur;
    }

    /**
     * Modifie la durée de la livraison.
     * 
     * @param newTempsLivraison la nouvelle durée de la livraison en minutes.
     */
    public void setTempsLivraison(double newTempsLivraison) {
        this.dureeLivraison = newTempsLivraison;
    }

    /**
     * Modifie le livreur assigné à cette livraison.
     * 
     * @param newLivreur le nouveau livreur assigné.
     */
    public void setLivreur(Livreur newLivreur) {
        this.livreur = newLivreur;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la livraison.
     * Cette représentation inclut la durée de la livraison et le livreur assigné.
     * 
     * @return une chaîne de caractères décrivant la livraison.
     */
    @Override
    public String toString() {
        return "Livraison [Temps: " + dureeLivraison + " min, Livreur: " + livreur + "]";
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.

}
