package model;

public class Etape {
    private List<Troncon> listeTroncons;
    private Intersection arrivee;
    private Intersection depart;

    public Etape(List<Troncon> listeTroncons, Intersection arrivee, Intersection depart) {
        this.listeTroncons = listeTroncons;
        this.arrivee = arrivee;
         this.depart = depart;
    }

}
