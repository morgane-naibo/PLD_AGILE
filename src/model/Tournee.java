package model;


public class Tournee {
    private Livreur livreur;
    private List<Etape> etapes;

    // Constructeur
    public Tournee(Livreur livreur, List<Etape> etapes) {
        this.livreur = livreur;
        this.etapes = etapes;
    }

}

