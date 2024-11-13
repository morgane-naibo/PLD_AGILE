package model;

import java.util.List;
import java.util.ArrayList;

public class Trajet {
    protected List<Etape> listeEtapes;
   
    //constructeur
    public Trajet(List<Etape> listeEtapes) {
        this.listeEtapes=listeEtapes;
    }

    public Trajet() {
        this.listeEtapes = new ArrayList<Etape>() ;
    }

    //getters
    public List<Etape> getListeEtapes() {
        return this.listeEtapes;
    }

    //setters
    public void setListeEtapes(List<Etape> newListeEtapes) {
        this.listeEtapes = newListeEtapes;
    }

    //toString

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trajet :\n");

        for (Etape etape : listeEtapes) {
            sb.append(etape.toString());
        }

        return sb.toString();
    }

    //autres methodes
    public void ajouterEtape(Etape etape) {
        this.listeEtapes.add(etape);
    }

    public double calculerDureeTrajet(){
        double duree =0.0;
        for (Etape i : this.listeEtapes){
            duree+=i.getLongueur()*60/15000 + 5.0;
        }
        duree -=5.0;
        return duree;
    }

}