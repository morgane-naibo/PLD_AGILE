package model;

public class Troncon {
    private Intersection intersectionDepart;
    private Intersection intersectionArrivee;
    private double longueur;
    private String nomRue;

    // Constructeur
    public Troncon(Intersection intersectionDepart, Intersection intersectionArrivee, double longueur, String nomRue) {
        this.intersectionDepart = intersectionDepart;
        this.intersectionArrivee = intersectionArrivee;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

}