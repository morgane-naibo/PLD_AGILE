package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import exceptions.IDIntersectionException;

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
        sb.append("\n");

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

    public Intersection chercherIntersectionParId(long id) throws IDIntersectionException{
        boolean existence=false;
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getId() == id) {
                existence=true;
                return iteratorIntersection;
            }
        }
        if (!existence){
            throw new IDIntersectionException("Cette intersection n'esxiste pas.");
        }
        return null;
    }

    public Etape chercherPlusCourtChemin(Intersection origine, Intersection destination){
        //initialisation
        Intersection actuel = origine;
        double[] distance = new double[this.listeIntersections.size()];
        for (int i=0 ; i<this.listeIntersections.size(); i++){
            distance[i] = Double.MAX_VALUE;
        }
        distance[origine.getNumero()] = 0;
        Troncon[] predecesseurs = new Troncon[this.listeIntersections.size()];
        predecesseurs[origine.getNumero()] = null;
        List<Integer> sommetsGris = new ArrayList<Integer>();
        sommetsGris.add(origine.getNumero());
        List<Integer> sommetsNoirs = new ArrayList<Integer>();

        //tant qu'il y a encore des sommets (intersections) à visiter
        while (sommetsGris.size() != 0){
            double dist_min = Double.MAX_VALUE;
            int posMin = 0;

            //chercher le sommet gris (intersection) minimisant la ditance
            for (int i=0 ; i<this.listeIntersections.size(); i++){
                if (distance[i]<dist_min && sommetsGris.contains(i)){
                    posMin = i;
                    dist_min = distance[i];
                }
            }
            actuel = this.listeIntersections.get(posMin);

            //pour les successeurs de ce sommet, relâcher les arcs (troncons)
            for (Troncon iteratorTroncon : actuel.getListeTroncons()){
                if (! sommetsNoirs.contains(iteratorTroncon.getDestination().getNumero())){
                    if (iteratorTroncon.getLongueur()+distance[actuel.getNumero()] < distance[iteratorTroncon.getDestination().getNumero()]){
                        distance[iteratorTroncon.getDestination().getNumero()]= iteratorTroncon.getLongueur()+distance[actuel.getNumero()];
                        predecesseurs[iteratorTroncon.getDestination().getNumero()] = iteratorTroncon;
                        if (! sommetsGris.contains(iteratorTroncon.getDestination().getNumero())){
                            sommetsGris.add(iteratorTroncon.getDestination().getNumero());
                        }
                    }
                }
            }

            // enlever actuel des gris
            sommetsGris.remove(Integer.valueOf(actuel.getNumero()));
            sommetsNoirs.add(actuel.getNumero());

        }

        //remettre les troncons dans l'ordre et créer l'étape à retourner
        List<Troncon> troncons = new ArrayList<Troncon>();
        if (predecesseurs[destination.getNumero()] == null){
            return null;
        }
        Troncon tronconActuel = predecesseurs[destination.getNumero()];
        
        //System.out.println( predecesseurs[destination.getNumero()].toString());
        troncons.add(tronconActuel);
        while (predecesseurs[tronconActuel.getOrigine().getNumero()] != null){
            tronconActuel = predecesseurs[tronconActuel.getOrigine().getNumero()];
            troncons.add(tronconActuel);
        }
        Collections.reverse(troncons);
        double longueur = distance[destination.getNumero()];
        Etape plusCourtChemin = new Etape(troncons,origine,destination, longueur);
        //System.out.println(plusCourtChemin.toString());
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
        //distance cartésienne
        //return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));

        //avec la formule de Harsevine pour calculer des distances sur Terre
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double rayonTerreMetre = 6371000.0 ;
        double dLat = lat2Rad-lat1Rad;
        double dLong = lon2Rad - lon1Rad;

        return 2 * rayonTerreMetre * Math.asin(
            Math.sqrt(
                (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLong / 2) * Math.sin(dLong / 2)
                )
                )
            );
    }

}