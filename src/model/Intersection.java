package model;

import java.util.List;
import java.util.ArrayList;

public class Intersection {
    private double latitude;
    private double longitude;
    private long id;
    private List<Troncon> listeTroncons;

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

    //toString
    @Override
    public String toString() {
        return "Intersection [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

    //autres methodes


}

