package model;

public class Livreur {
    private int id;

    // Constructeur
    public Livreur(int id) {
        this.id=id;
    }

    //getter
    public int getId() {
        return this.id;
    }

    //setter
    public void setId(int newId) {
        this.id = newId;
    }

    //toString
    @Override
    public String toString() {
        return "Livreur [ID: " + id + "]";
    }


    //autres methodes

}
