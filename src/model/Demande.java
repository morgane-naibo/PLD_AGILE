package model;

import java.util.List;
import java.util.ArrayList;

public class Demande {
    private Entrepot entrepot;
    private List<PointDeLivraison> listePointDeLivraison;
    private List<List<Etape>> matriceAdjacence;
    private Trajet tournee;

    //constructeur
    public Demande() {
        this.entrepot = new Entrepot() ;
        this.listePointDeLivraison = new ArrayList<PointDeLivraison>();
        this.matriceAdjacence = new ArrayList<List<Etape>>();
        this.tournee = new Trajet() ;
    }

    //getters
    public Entrepot getEntrepot() {
        return this.entrepot;
    }

    public List<PointDeLivraison> getListePointDeLivraison() {
        return this.listePointDeLivraison;
    }

    public List<List<Etape>> getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    public Trajet getTournee() {
        return this.tournee;
    }

    //setters
    public void setEntrepot(Entrepot newEntrepot) {
        this.entrepot = newEntrepot;
    }

    public void setListePointDeLivraison(List<PointDeLivraison> newListePointDeLivraison) {
        this.listePointDeLivraison = newListePointDeLivraison;
    }

    public void setMatriceAdjacence(List<List<Etape>> newMatriceAdjacence) {
        this.matriceAdjacence = newMatriceAdjacence;
    }

    public void setTournee(Trajet newTournee) {
        this.tournee = newTournee;
    }

    //autres methodes
    

}
