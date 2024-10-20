package model;


public class PointDeLivraison extends Intersection{
    private Livraison livraison;

    // Constructeur
    public PointDeLivraison(Long id, double latitude, double longitude, Livraison livraison) {
        super(id, latitude, longitude);
        this.livraison=livraison;
        
    }

    //getters
    public Livraison getLivraison() {
        return this.livraison;
    }

    //setters
    public void setLivraison(Livraison newLivraison) {
        this.livraison = newLivraison;
    }

}
