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

    //autres methodes
    public void ajouterEtape(Etape etape) {
        this.listeEtapes.add(etape);
    }

    public double calculerDureeTrajet(){
        double duree =0.0;
        for (Etape i : this.listeEtapes){
            duree+=i.getLongueur()*60/15000 + 5.0;
        }
        return duree;
    }

}