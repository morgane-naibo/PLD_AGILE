package model;

/**
 * Classe représentant un tronçon de route entre deux intersections.
 * Un tronçon a une origine, une destination, une longueur et un nom de rue.
 */
public class Troncon {

    /** Intersection de départ du tronçon. */
    private Intersection origine;
    
    /** Intersection d'arrivée du tronçon. */
    private Intersection destination;
    
    /** Longueur du tronçon en mètres. */
    private double longueur;
    
    /** Nom de la rue correspondant au tronçon. */
    private String nomRue;

    /**
     * Constructeur du tronçon.
     * Ce constructeur permet d'initialiser un tronçon avec l'origine, la destination,
     * la longueur et le nom de la rue associés.
     * 
     * @param origine L'intersection de départ du tronçon.
     * @param destination L'intersection d'arrivée du tronçon.
     * @param longueur La longueur du tronçon en mètres.
     * @param rue Le nom de la rue correspondant au tronçon.
     */
    public Troncon(Intersection origine, Intersection destination, double longueur, String rue) {
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.nomRue = rue;
    }

    /**
     * Récupère l'intersection de départ du tronçon.
     * 
     * @return L'intersection de départ du tronçon.
     */
    public Intersection getOrigine(){
        return this.origine;
    }

    /**
     * Récupère l'intersection d'arrivée du tronçon.
     * 
     * @return L'intersection d'arrivée du tronçon.
     */
    public Intersection getDestination(){
        return this.destination;
    }

    /**
     * Récupère la longueur du tronçon.
     * 
     * @return La longueur du tronçon en mètres.
     */
    public double getLongueur(){
        return this.longueur;
    }

    /**
     * Récupère le nom de la rue correspondant au tronçon.
     * 
     * @return Le nom de la rue du tronçon.
     */
    public String getNomRue(){
        return this.nomRue;
    }

    /**
     * Modifie l'intersection de départ du tronçon.
     * 
     * @param newOrigine La nouvelle intersection de départ.
     */
    public void setOrigine(Intersection newOrigine){
        this.origine = newOrigine;
    }

    /**
     * Modifie l'intersection d'arrivée du tronçon.
     * 
     * @param newDestination La nouvelle intersection d'arrivée.
     */
    public void setDestination(Intersection newDestination){
        this.destination = newDestination;
    }

    /**
     * Modifie la longueur du tronçon.
     * 
     * @param newLongueur La nouvelle longueur du tronçon en mètres.
     */
    public void setLongueur(double newLongueur){
        this.longueur = newLongueur;
    }

    /**
     * Modifie le nom de la rue correspondant au tronçon.
     * 
     * @param newNomRue Le nouveau nom de rue du tronçon.
     */
    public void setNomRue(String newNomRue){
        this.nomRue = newNomRue;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du tronçon.
     * Cette méthode permet d'afficher les informations complètes sur un tronçon,
     * incluant l'origine, la destination, la longueur et le nom de la rue.
     * 
     * @return Une chaîne de caractères représentant le tronçon.
     */
    @Override
    public String toString() {
        return "Troncon [Origine: " + origine + ", Destination: " + destination + 
               ", Longueur: " + longueur + " m, Rue: " + nomRue + "]";
    }

    // Autres méthodes à implémenter selon les besoins spécifiques.
}
