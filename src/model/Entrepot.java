package model;


public class Entrepot extends Intersection{
   
    private String heureDepart;
    // Constructeur
    public Entrepot(long id, String heure) {
     this.setId(id);
     this.heureDepart = heure;
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

