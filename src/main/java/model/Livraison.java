package main.java.model;

public class Livraison {
    private double dureeLivraison;
    private double dureeEnlevement;
    private long adresseEnlevement;
    private long adresseLivraison;
    private Livreur livreur;

    //constructeur
    public Livraison(long adresseEnlevement, long adresseLivraison, double dureeEnlevement, double dureeLivraison) {
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
    }

    //getters
    public double getTempsLivraison() {
        return this.dureeLivraison;
    }

    public double getTempsEnlevement() {
        return this.dureeEnlevement;
    }

    public long getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public long getAdresseEnelevement() {
        return this.adresseEnlevement;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    //setters
    public void setTempsLivraison(double newTempsLivraison){
        this.dureeLivraison = newTempsLivraison;
    }

    public void setLivreur(Livreur newLivreur) {
        this.livreur = newLivreur;
    }

    //toString
    @Override
    public String toString() {
        return "Livraison [Temps: " + dureeLivraison + " min, Livreur: " + livreur + "]";
    }

    //autres methodes

}