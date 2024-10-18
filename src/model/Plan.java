package model;

public class Plan {
    private List<Intersection> intersections;
    private List<Troncon> troncons;

    // Constructeur
    public Plan(List<Intersection> intersections, List<Troncon> troncons) {
        this.intersections = intersections;
        this.troncons = troncons;
    }

}
