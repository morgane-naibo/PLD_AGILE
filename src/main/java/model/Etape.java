package model;

import java.util.List;
import java.util.ArrayList;

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

    public Etape(List<Troncon> listeTroncons, Intersection depart, Intersection arrivee, double longueur) {
        this.listeTroncons = listeTroncons;
        this.depart = depart;
        this.arrivee = arrivee;
        this.longueur = longueur;

    }

    public Etape() {
        this.listeTroncons = new ArrayList<Troncon>() ;
        this.depart = new Intersection() ;
        this.arrivee = new Intersection() ;
        this.longueur = 0 ;
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

    //toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Etape:\n");
        sb.append("Départ: ").append(depart).append("\n");
        sb.append("Arrivée: ").append(arrivee).append("\n");
        sb.append("Longueur totale: ").append(longueur).append(" m\n");
        sb.append("Tronçons:\n");

        for (Troncon troncon : listeTroncons) {
            sb.append(troncon).append("\n");
        }

        return sb.toString();
    }

    //autres methodes


}
