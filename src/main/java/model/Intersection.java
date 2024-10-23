package model;

import java.util.List;
import java.util.ArrayList;

public class Intersection {
    private double latitude;
    private double longitude;
    private long id;
    private List<Troncon> listeTroncons;
    protected int numero;

    // Constructeurs
    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listeTroncons = new ArrayList<Troncon>();
    }

    public Intersection() {
    }

    //getters
    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public long getId() {
        return this.id;
    }

    public List<Troncon> getListeTroncons(){
        return this.listeTroncons;
    }

    public int getNumero(){
        return this.numero;
    }

    //setters
    public void setLatitude(double newLatitude) {
        this.latitude = newLatitude;
    }

    public void setLongitude(double newLongitude) {
        this.longitude = newLongitude;
    }

    public void setId(long newId){
        this.id = newId;
    }

    public void setListeTroncons(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    public void setNumero(int num){
        this.numero = num;
    }

    //toString
    @Override
    public String toString() {
        return "Intersection [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + "]" ;
    }

    //autres methodes
    public void ajouterTroncon(Troncon troncon) {
        this.listeTroncons.add(troncon);
    }

}