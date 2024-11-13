package model;

import java.util.List;

public class Tournee extends Trajet{
    private Livreur livreur;
    
    // Constructeur
    public Tournee(List<Etape> listeEtapes, Livreur livreur) {
        super(listeEtapes);
        this.livreur = livreur;
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
        String msg;
        msg = "Tournée : ";
        for(int i=0 ; i<this.listeEtapes.size() ; i++){
            msg += this.listeEtapes.get(i).getDepart().getId();
            msg+="  -> ";
        }
        msg += this.listeEtapes.getLast().getArrivee().getId();
        return msg;
    }


    //autres methodes
    

}

