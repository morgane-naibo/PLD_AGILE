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
    public String getHeureDepart() {
        return this.heureDepart;
    }

    //setters
    public void setHeureDepart(String newHeureDepart){
        this.heureDepart = newHeureDepart;
    }

    //toString
    @Override
    public String toString() {
        return super.toString();
    }

    //autres methodes

}

