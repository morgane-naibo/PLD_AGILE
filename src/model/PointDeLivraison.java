package model;


public class PointDeLivraison extends Intersection{
    private Livraison livraison;

    // Constructeur
    public PointDeLivraison(Livraison livraison, Long id, float latitude, float longitude) {
        this.livraison=livraison;
        super(id, latitude, longitude);
    }

}
