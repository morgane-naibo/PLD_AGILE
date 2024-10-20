package model;

import java.util.List;

public class Etape {
    private List<Troncon> listeTroncons;
    private Intersection depart;
    private Intersection arrivee;
    private double longueur;

    //constructeur
    public Etape(List<Troncon> listeTroncons, Intersection depart, Intersection arrivee) {
        this.listeTroncons = listeTroncons;
        this.depart = depart;
        this.arrivee = arrivee;
        this.longueur = 0 ;
        for(Troncon t : listeTroncons){
            this.longueur += t.getLongueur();
        }

    }

    //getters
    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    public Intersection getDepart() {
        return this.depart;
    }

    public Intersection getArrivee() {
        return this.arrivee;
    }

    public double getLongueur() {
        return this.longueur;
    }

    //setters
    public void setListeTroncons(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    public void setDepart(Intersection newDepart) {
        this.depart = newDepart;
    }

    public void setArrivee(Intersection newArrivee) {
        this.arrivee = newArrivee;
    }

    public void setLongueur(double newLongueur) {
        this.longueur = newLongueur;
    }

}
