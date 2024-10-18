package model;

public class Etape {
    private String description;
    private String heure;
    private PointDeLivraison point;

    // Constructeur
    public Etape(String description, String heure, PointDeLivraison point) {
        this.description = description;
        this.heure = heure;
        this.point = point;
    }

}
