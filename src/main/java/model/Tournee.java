package model;

import java.util.List;

public class Tournee extends Trajet{
    private Livreur livreur;
    
    // Constructeur
    public Tournee(List<Etape> listeEtapes, Livreur livreur) {
        super(listeEtapes);
        this.livreur = livreur;
    }

    public void setListeEtapes(List<Etape> newListeEtapes) {
        this.listeEtapes = newListeEtapes;
    }

    //getters
    public Livreur getLivreur() {
        return this.livreur;
    }

    //setters
    public void setLivreur(Livreur newLivreur) {
        this.livreur = newLivreur;
    }

    //toString
    @Override
    public String toString() {
        return "Tournée: [Livreur: " + livreur + ", Étapes: " + super.toString() + "]";
    }


    //autres methodes
    

}

