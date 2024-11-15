package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe représentant une intersection dans le système.
 * Une intersection est définie par une latitude, une longitude, un identifiant unique, 
 * une liste de tronçons associés et un numéro optionnel.
 */
public class Intersection {

    /** Latitude de l'intersection. */
    protected double latitude;

    /** Longitude de l'intersection. */
    protected double longitude;

    /** Identifiant unique de l'intersection. */
    protected long id;

    /** Liste des tronçons connectés à cette intersection. */
    protected List<Troncon> listeTroncons;

    /** Numéro optionnel de l'intersection, utilisé pour des références spécifiques. */
    protected int numero;

    /**
     * Constructeur avec tous les paramètres principaux.
     * 
     * @param id l'identifiant unique de l'intersection.
     * @param latitude la latitude de l'intersection.
     * @param longitude la longitude de l'intersection.
     */
    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listeTroncons = new ArrayList<Troncon>();
    }

    /**
     * Constructeur par copie.
     * Crée une nouvelle intersection avec les mêmes attributs qu'une intersection existante.
     * 
     * @param i l'intersection à copier.
     */
    public Intersection(Intersection i) {
        this.id = i.id;
        this.latitude = i.latitude;
        this.longitude = i.longitude;
        this.listeTroncons = i.listeTroncons;
    }

    /**
     * Constructeur par défaut.
     * Initialise une intersection sans paramètres spécifiques.
     */
    public Intersection() {
    }

    /**
     * Récupère la latitude de l'intersection.
     * 
     * @return la latitude de l'intersection.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Récupère la longitude de l'intersection.
     * 
     * @return la longitude de l'intersection.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Récupère l'identifiant unique de l'intersection.
     * 
     * @return l'identifiant de l'intersection.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Récupère la liste des tronçons connectés à l'intersection.
     * 
     * @return une liste de tronçons connectés.
     */
    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    /**
     * Récupère le numéro de l'intersection.
     * 
     * @return le numéro de l'intersection.
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Modifie la latitude de l'intersection.
     * 
     * @param newLatitude la nouvelle latitude à attribuer.
     */
    public void setLatitude(double newLatitude) {
        this.latitude = newLatitude;
    }

    /**
     * Modifie la longitude de l'intersection.
     * 
     * @param newLongitude la nouvelle longitude à attribuer.
     */
    public void setLongitude(double newLongitude) {
        this.longitude = newLongitude;
    }

    /**
     * Modifie l'identifiant unique de l'intersection.
     * 
     * @param newId le nouvel identifiant unique.
     */
    public void setId(long newId) {
        this.id = newId;
    }

    /**
     * Modifie la liste des tronçons connectés à l'intersection.
     * 
     * @param newListeTroncons la nouvelle liste de tronçons.
     */
    public void setListeTroncons(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    /**
     * Modifie le numéro de l'intersection.
     * 
     * @param num le nouveau numéro à attribuer.
     */
    public void setNumero(int num) {
        this.numero = num;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'intersection.
     * Cette représentation inclut uniquement l'identifiant de l'intersection.
     * 
     * @return une chaîne de caractères représentant l'intersection.
     */
    @Override
    public String toString() {
        return "id " + id;
    }

    /**
     * Ajoute un tronçon à la liste des tronçons connectés à l'intersection.
     * 
     * @param troncon le tronçon à ajouter.
     */
    public void ajouterTroncon(Troncon troncon) {
        this.listeTroncons.add(troncon);
    }

    // Méthodes supplémentaires à implémenter selon les besoins spécifiques.

}
