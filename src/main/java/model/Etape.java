package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe représentant une étape entre deux intersections dans le système.
 * Une étape est composée d'une liste de tronçons, d'un point de départ, d'un point d'arrivée et d'une longueur totale.
 */
public class Etape {

    /** Liste des tronçons constituant l'étape. */
    private List<Troncon> listeTroncons;

    /** Intersection de départ de l'étape. */
    private Intersection depart;

    /** Intersection d'arrivée de l'étape. */
    private Intersection arrivee;

    /** Longueur totale de l'étape, en mètres. */
    private double longueur;

    /**
     * Constructeur avec liste de tronçons, départ et arrivée.
     * La longueur totale de l'étape est calculée automatiquement en additionnant les longueurs des tronçons.
     * 
     * @param listeTroncons la liste des tronçons de l'étape.
     * @param depart l'intersection de départ de l'étape.
     * @param arrivee l'intersection d'arrivée de l'étape.
     */
    public Etape(List<Troncon> listeTroncons, Intersection depart, Intersection arrivee) {
        this.listeTroncons = listeTroncons;
        this.depart = depart;
        this.arrivee = arrivee;
        this.longueur = 0;
        for (Troncon t : listeTroncons) {
            this.longueur += t.getLongueur();
        }
    }

    /**
     * Constructeur avec tous les paramètres définis, y compris la longueur.
     * 
     * @param listeTroncons la liste des tronçons de l'étape.
     * @param depart l'intersection de départ de l'étape.
     * @param arrivee l'intersection d'arrivée de l'étape.
     * @param longueur la longueur totale de l'étape.
     */
    public Etape(List<Troncon> listeTroncons, Intersection depart, Intersection arrivee, double longueur) {
        this.listeTroncons = listeTroncons;
        this.depart = depart;
        this.arrivee = arrivee;
        this.longueur = longueur;
    }

    /**
     * Constructeur par défaut.
     * Initialise une étape vide avec une liste de tronçons vide, une longueur de 0,
     * et des intersections par défaut.
     */
    public Etape() {
        this.listeTroncons = new ArrayList<Troncon>();
        this.depart = new Intersection();
        this.arrivee = new Intersection();
        this.longueur = 0;
    }

    /**
     * Récupère la liste des tronçons de l'étape.
     * 
     * @return une liste de tronçons.
     */
    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    /**
     * Récupère l'intersection de départ.
     * 
     * @return l'intersection de départ.
     */
    public Intersection getDepart() {
        return this.depart;
    }

    /**
     * Récupère l'intersection d'arrivée.
     * 
     * @return l'intersection d'arrivée.
     */
    public Intersection getArrivee() {
        return this.arrivee;
    }

    /**
     * Récupère la longueur totale de l'étape.
     * 
     * @return la longueur en mètres.
     */
    public double getLongueur() {
        return this.longueur;
    }

    /**
     * Modifie la liste des tronçons de l'étape.
     * 
     * @param newListeTroncons la nouvelle liste de tronçons.
     */
    public void setListeTroncons(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    /**
     * Modifie l'intersection de départ de l'étape.
     * 
     * @param newDepart la nouvelle intersection de départ.
     */
    public void setDepart(Intersection newDepart) {
        this.depart = newDepart;
    }

    /**
     * Modifie l'intersection d'arrivée de l'étape.
     * 
     * @param newArrivee la nouvelle intersection d'arrivée.
     */
    public void setArrivee(Intersection newArrivee) {
        this.arrivee = newArrivee;
    }

    /**
     * Modifie la longueur totale de l'étape.
     * 
     * @param newLongueur la nouvelle longueur totale en mètres.
     */
    public void setLongueur(double newLongueur) {
        this.longueur = newLongueur;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'étape.
     * Cette représentation inclut les informations sur les intersections de départ et d'arrivée,
     * la longueur de l'étape et les tronçons associés.
     * 
     * @return une chaîne de caractères décrivant l'étape.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Etape:\n");
        sb.append("D: ").append(depart.getId());
        sb.append(" A: ").append(arrivee.getId());
        sb.append(" L: ").append(longueur).append(" m\n");
        sb.append("Tronçons: ");

        for (Troncon troncon : listeTroncons) {
            sb.append(troncon.getOrigine().getId()).append(" -> ");
        }
        sb.append(listeTroncons.get(listeTroncons.size() - 1).getDestination().getId()).append("\r\n");

        return sb.toString();
    }

    // Méthodes supplémentaires à implémenter selon les besoins.
}
