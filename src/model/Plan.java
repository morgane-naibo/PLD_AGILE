package model;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    private List<Intersection> listeIntersections;
    private List<Troncon> listeTroncons;

    // Constructeur
    public Plan() {
        this.listeIntersections = new ArrayList<Intersection>();
        this.listeTroncons = new ArrayList<Troncon>();
    }

    //getters
    public List<Intersection> getListeIntersections() {
        return this.listeIntersections;
    }

    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    //setters
    public void setListeIntersections(List<Intersection> newListeIntersections) {
        this.listeIntersections = newListeIntersections ;
    }

    public void setListeTroncon(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    //autres methodes
    public void ajouterIntersection(Intersection intersection){
        this.listeIntersections.add(intersection);
    }

    public void ajouterTroncon(Troncon troncon) {
        this.listeTroncons.add(troncon);
    }

    public Intersection chercherIntersectionParId(long id){
        Intersection inter = new Intersection();
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getId() == id) {
                inter = iteratorIntersection;
                break;
            }
        }
        return inter;
    }

    public List<Troncon> chercherPlusCourtChemin(Intersection intersection1, Intersection intersection2) {
        List<Troncon> plusCourtChemin = new ArrayList<Troncon>();

        return plusCourtChemin;
    }

}
