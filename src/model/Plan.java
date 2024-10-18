package model;

import java.util.ArrayList;
import java.util.List;
import model.Intersection;
import model.Troncon;

public class Plan {
    private List<Intersection> intersections;
    private List<Troncon> troncons;

    // Constructeur
    public Plan() {
        this.intersections = new ArrayList<Intersection>;
        this.troncons = new ArrayList<Troncons>;
    }

    public void ajouterIntersection(Intersection intersection){
        this.intersections.add(intersection);
    }

    public Intersection chercherIntersectionParId(Long id){
        Intersection inter;
        for (int i = 0; i<intersections.size();i++){
            if (intersections[i].getId()==id){
                inter = intersections[i];
            }
        }
        return inter;
    }

}
