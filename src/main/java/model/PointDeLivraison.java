package model;


public class PointDeLivraison extends Intersection{
    private Livraison livraison;

    // Constructeur
    // Constructeur
    public PointDeLivraison(Long id, Livraison livraison) {
        this.setId(id);
        this.livraison=livraison;
        
    }

    public PointDeLivraison(Intersection inter) {
        this.id=inter.getId();
        this.latitude=inter.getLatitude();
        this.longitude=inter.getLongitude();
        this.listeTroncons = inter.getListeTroncons();
        this.numero = inter.getNumero();
        this.livraison=null;
        
    }

    public PointDeLivraison(Long id) {
        this.setId(id);
        
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
        //return "Point de Livraison: " + super.toString() + ", Livraison: " + livraison;
        return super.toString();
    }

    //autres methodes

}
