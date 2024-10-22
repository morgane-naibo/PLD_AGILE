package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Plan {
    private List<Intersection> listeIntersections;
    private List<Troncon> listeTroncons;

    // Constructeur
    public Plan() {
        this.listeIntersections = new ArrayList<Intersection>();
        this.listeTroncons = new ArrayList<Troncon>();
    }

    //getters
    public List<Intersection> getListeIntersections() {
        return this.listeIntersections;
    }

    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    //setters
    public void setListeIntersections(List<Intersection> newListeIntersections) {
        this.listeIntersections = newListeIntersections ;
    }

    public void setListeTroncon(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    //toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plan:\n");
        sb.append("Intersections:\n");

        for (Intersection intersection : listeIntersections) {
            sb.append(intersection).append("\n");
        }

        sb.append("Tronçons:\n");
        for (Troncon troncon : listeTroncons) {
            sb.append(troncon).append("\n");
        }

        return sb.toString();
    }

    //autres methodes
    public void ajouterIntersection(Intersection intersection){
        intersection.setNumero(this.listeIntersections.size());
        this.listeIntersections.add(intersection);
    }

    public void ajouterIntersection(List<Intersection> listeIntersections) {
        for (Intersection i : listeIntersections) {
            this.listeIntersections.add(i);
        }
    }

    public void ajouterTroncon(Troncon troncon) {
        this.listeTroncons.add(troncon);
    }

    public void ajouterTroncon(List<Troncon> listeTroncons) {
        for (Troncon t : listeTroncons) {
            this.listeTroncons.add(t);
        }
    }

    public Intersection chercherIntersectionParId(long id){
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getId() == id) {
                return iteratorIntersection;
            }
        }
        return null;
    }


    public Etape chercherPlusCourtChemin(Intersection origine, Intersection destination){
        Intersection actuel = new Intersection();
        actuel = origine;
        double[] distance = new double[this.listeIntersections.size()];
        for (int i=0 ; i<this.listeIntersections.size(); i++){
            distance[i] = 100000000;
        }
        distance[origine.getNumero()] = 0;
        Troncon[] predecesseurs = new Troncon[this.listeIntersections.size()];
        predecesseurs[origine.getNumero()] = null;

        List<Integer> sommetsGris = new ArrayList<Integer>();
        sommetsGris.add(origine.getNumero());

        while (sommetsGris.size() != 0){
            // Trouver le sommet gris tel que la distance soit minimale
            double dist_min = 10000000;
            int posMin = 0;
            for (int i=0 ; i<this.listeIntersections.size(); i++){
                if (distance[i]<dist_min){
                    posMin = i;
                    dist_min = distance[i];
                }
            }
            actuel = this.listeIntersections.get(posMin);
            //pour les successeurs de ce sommet
            for (Troncon iteratorTroncon : actuel.getListeTroncons()){
                if (iteratorTroncon.getLongueur()+distance[actuel.getNumero()] < distance[iteratorTroncon.getDestination().getNumero()]){
                    distance[iteratorTroncon.getDestination().getNumero()]= iteratorTroncon.getLongueur()+distance[actuel.getNumero()];
                    predecesseurs[iteratorTroncon.getDestination().getNumero()] = iteratorTroncon;
                    if (! sommetsGris.contains(iteratorTroncon.getDestination().getNumero())){
                        sommetsGris.add(iteratorTroncon.getDestination().getNumero());
                    }
                }
            }
            // enlever actuel des gris
            sommetsGris.remove(Integer.valueOf(actuel.getNumero()));

        }
        List<Troncon> troncons = new ArrayList<Troncon>();
        Troncon tronconActuel = predecesseurs[destination.getNumero()];
        troncons.add(tronconActuel);
        while (predecesseurs[tronconActuel.getOrigine().getNumero()] != null){
            tronconActuel = predecesseurs[tronconActuel.getOrigine().getNumero()];
            troncons.add(tronconActuel);
        }
        Collections.reverse(troncons);
        Etape plusCourtChemin = new Etape(troncons,origine,destination);
        return plusCourtChemin;
    }

    public double trouverLatitudeMin(){
        double latitudeMin = 100;
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getLatitude() < latitudeMin) {
                latitudeMin = iteratorIntersection.getLatitude();
            }
        }
        return latitudeMin;
    }

    public double trouverLongitudeMin(){
        double longitudeMin = 100;
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getLongitude() < longitudeMin) {
                longitudeMin = iteratorIntersection.getLongitude();
            }
        }
        return longitudeMin;
    }

    public double trouverLatitudeMax(){
        double latitudeMax = -100;
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getLatitude() > latitudeMax) {
                latitudeMax = iteratorIntersection.getLatitude();
            }
        }
        return latitudeMax;
    }

    public double trouverLongitudeMax(){
        double longitudeMax = -100;
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getLongitude() > longitudeMax) {
                longitudeMax = iteratorIntersection.getLongitude();
            }
        }
        return longitudeMax;
    }


    public Intersection chercherIntersectionLaPlusProche(double latitude, double longitude) {
        if (listeIntersections.isEmpty()) {
            return null;
        }
    
        Intersection intersectionLaPlusProche = null;
        double distanceMin = Double.MAX_VALUE;
    
        // boucle pour trouver l'intersection la plus proche
        for (Intersection intersection : listeIntersections) {
            double distance = calculerDistance(latitude, longitude, intersection.getLatitude(), intersection.getLongitude());
            
            if (distance < distanceMin) {
                distanceMin = distance;
                intersectionLaPlusProche = intersection;
            }
        }
    
        return intersectionLaPlusProche;
    }
    
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }
    

}