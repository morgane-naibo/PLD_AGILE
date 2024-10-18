package model;

public class Troncon {
    private Intersection origine;
    private Intersection destination;
    private float distance;
    private String rue;

    // Constructeur
    public Troncon(Intersection origine, Intersection destination, float distance, String rue) {
        this.origine = origine;
        this.destination = destination;
        this.distance = distance;
        this.rue = rue;
    }

}