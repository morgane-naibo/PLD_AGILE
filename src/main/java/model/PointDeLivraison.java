package main.java.model;


public class PointDeLivraison extends Intersection{
    private Livraison livraison;

    // Constructeur
    // Constructeur
    public PointDeLivraison(Long id, Livraison livraison) {
        this.setId(id);;
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

    //toString
    @Override
    public String toString() {
        return "Point de Livraison: " + super.toString() + ", Livraison: " + livraison;
    }

    //autres methodes

}
