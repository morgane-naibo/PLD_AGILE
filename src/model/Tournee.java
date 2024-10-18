package model;


public class Tournee extends Trajet{
    private Livreur livreur;
    

    // Constructeur
    public Tournee(Livreur livreur, List<Etape> listeEtapes) {
        this.livreur = livreur;
       super(listeEtapes);
    }

}

