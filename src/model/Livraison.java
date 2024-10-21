package model;

public class Livraison {
    private double tempsLivraison;
    private Livreur livreur;

    //constructeur
    public Livraison(float tempsLivraison, Livreur livreur) {
        this.tempsLivraison = tempsLivraison;
        this.livreur = livreur;
    }

    //getters
    public double getTempsLivraison() {
        return this.tempsLivraison;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    //setters
    public void setTempsLivraison(double newTempsLivraison){
        this.tempsLivraison = newTempsLivraison;
    }

    public void setLivreur(Livreur newLivreur) {
        this.livreur = newLivreur;
    }

    //toString
    @Override
    public String toString() {
        return "Livraison [Temps: " + tempsLivraison + " min, Livreur: " + livreur + "]";
    }

    //autres methodes

}