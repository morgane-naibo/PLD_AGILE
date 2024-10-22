package model;

public class Livreur {
    private Long id;

    // Constructeur
    public Livreur(Long id) {
        this.id=id;
    }

    //getter
    public long getId() {
        return this.id;
    }

    //setter
    public void setId(long newId) {
        this.id = newId;
    }

    //toString
    @Override
    public String toString() {
        return "Livreur [ID: " + id + "]";
    }


    //autres methodes

}
