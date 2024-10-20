package model;

import java.util.List;
import java.util.ArrayList;

public class Trajet {
    private List<Etape> listeEtapes;
   
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

    //autres methodes
    public void ajouterEtape(Etape etape) {
        this.listeEtapes.add(etape);
    }

}