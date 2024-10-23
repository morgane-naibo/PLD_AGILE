package main.java.model;

public class Troncon {
    private Intersection origine;
    private Intersection destination;
    private double longueur;
    private String nomRue;

    // Constructeur
    public Troncon(Intersection origine, Intersection destination, double longueur, String rue) {
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.nomRue = rue;
    }

    //getters
    public Intersection getOrigine(){
        return this.origine;
    }

    public Intersection getDestination(){
        return this.destination;
    }

    public double getLongueur(){
        return this.longueur;
    }

    public String getNomRue(){
        return this.nomRue;
    }

    //setters
    public void setOrigine(Intersection newOrigine){
        this.origine = newOrigine;
    }

    public void setDestination(Intersection newDestination){
        this.destination = newDestination;
    }

    public void setLongueur(double newLongueur){
        this.longueur = newLongueur;
    }

    public void setNomRue(String newNomRue){
        this.nomRue = newNomRue;
    }

    //toString
    @Override
    public String toString() {
        return "Troncon [Origine: " + origine + ", Destination: " + destination + 
            ", Longueur: " + longueur + " m, Rue: " + nomRue + "]";
    }

    //autres methodes


}