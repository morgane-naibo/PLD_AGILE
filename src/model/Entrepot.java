package model;


public class Entrepot extends Intersection{

    // Constructeur
    public Entrepot(long id, double latitude, double longitude) {
     super(id, latitude, longitude);
    }

    public Entrepot() {
        super() ;
    }

    //getters

    //setters

    //toString
    @Override
    public String toString() {
        return "Entrepôt: " + super.toString();
    }

    //autres methodes

}

