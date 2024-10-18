package model;

public class Trajet {
    private Intersection depart;
    private Intersection arrivee;
    private double distance;

    // Constructeur
    public Trajet(Intersection depart, Intersection arrivee, double distance) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
    }

}