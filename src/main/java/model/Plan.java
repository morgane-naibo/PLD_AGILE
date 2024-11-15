package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import exceptions.IDIntersectionException;

/**
 * Classe représentant un plan composé d'intersections et de tronçons de route.
 * Un plan permet de gérer un ensemble d'intersections et de tronçons, 
 * ainsi que d'effectuer des calculs sur les chemins entre les intersections.
 */
public class Plan {

    /** Liste des intersections dans le plan. */
    private List<Intersection> listeIntersections;
    
    /** Liste des tronçons dans le plan. */
    private List<Troncon> listeTroncons;

    /**
     * Constructeur de la classe Plan.
     * Initialise une nouvelle instance de Plan avec des listes vides pour les intersections et les tronçons.
     */
    public Plan() {
        this.listeIntersections = new ArrayList<Intersection>();
        this.listeTroncons = new ArrayList<Troncon>();
    }

    /**
     * Récupère la liste des intersections du plan.
     * 
     * @return La liste des intersections.
     */
    public List<Intersection> getListeIntersections() {
        return this.listeIntersections;
    }

    /**
     * Récupère la liste des tronçons du plan.
     * 
     * @return La liste des tronçons.
     */
    public List<Troncon> getListeTroncons() {
        return this.listeTroncons;
    }

    /**
     * Modifie la liste des intersections du plan.
     * 
     * @param newListeIntersections La nouvelle liste d'intersections.
     */
    public void setListeIntersections(List<Intersection> newListeIntersections) {
        this.listeIntersections = newListeIntersections;
    }

    /**
     * Modifie la liste des tronçons du plan.
     * 
     * @param newListeTroncons La nouvelle liste de tronçons.
     */
    public void setListeTroncon(List<Troncon> newListeTroncons) {
        this.listeTroncons = newListeTroncons;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du plan.
     * Inclut les informations sur les intersections et les tronçons du plan.
     * 
     * @return La chaîne représentant le plan.
     */
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

    // Autres méthodes de gestion du plan

    /**
     * Ajoute une intersection au plan en lui assignant un numéro unique.
     * 
     * @param intersection L'intersection à ajouter.
     */
    public void ajouterIntersection(Intersection intersection){
        intersection.setNumero(this.listeIntersections.size());
        this.listeIntersections.add(intersection);
    }

    /**
     * Ajoute une liste d'intersections au plan.
     * 
     * @param listeIntersections La liste des intersections à ajouter.
     */
    public void ajouterIntersection(List<Intersection> listeIntersections) {
        for (Intersection i : listeIntersections) {
            this.listeIntersections.add(i);
        }
    }

    /**
     * Ajoute un tronçon au plan.
     * 
     * @param troncon Le tronçon à ajouter.
     */
    public void ajouterTroncon(Troncon troncon) {
        this.listeTroncons.add(troncon);
    }

    /**
     * Ajoute une liste de tronçons au plan.
     * 
     * @param listeTroncons La liste des tronçons à ajouter.
     */
    public void ajouterTroncon(List<Troncon> listeTroncons) {
        for (Troncon t : listeTroncons) {
            this.listeTroncons.add(t);
        }
    }

    /**
     * Cherche une intersection dans le plan par son identifiant.
     * 
     * @param id L'identifiant de l'intersection à rechercher.
     * @return L'intersection correspondante.
     * @throws IDIntersectionException Si l'intersection n'existe pas.
     */
    public Intersection chercherIntersectionParId(long id) throws IDIntersectionException{
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getId() == id) {
                return iteratorIntersection;
            }
        }
        throw new IDIntersectionException("Cette intersection n'existe pas.");
    }

    /**
     * Cherche le plus court chemin entre deux intersections en utilisant l'algorithme de Dijkstra.
     * 
     * @param origine L'intersection de départ.
     * @param destination L'intersection d'arrivée.
     * @return Une étape contenant le plus court chemin sous forme de tronçons entre les deux intersections.
     */
    public Etape chercherPlusCourtChemin(Intersection origine, Intersection destination) {
        // Initialisation
        Intersection actuel = origine;
        double[] distance = new double[this.listeIntersections.size()];
        for (int i = 0; i < this.listeIntersections.size(); i++) {
            distance[i] = Double.MAX_VALUE;
        }
        distance[origine.getNumero()] = 0;
        Troncon[] predecesseurs = new Troncon[this.listeIntersections.size()];
        predecesseurs[origine.getNumero()] = null;
        List<Integer> sommetsGris = new ArrayList<>();
        sommetsGris.add(origine.getNumero());
        List<Integer> sommetsNoirs = new ArrayList<>();

        // Algorithme de Dijkstra pour calculer le plus court chemin
        while (!sommetsGris.isEmpty()) {
            double distMin = Double.MAX_VALUE;
            int posMin = 0;

            // Cherche le sommet gris avec la distance minimale
            for (int i = 0; i < this.listeIntersections.size(); i++) {
                if (distance[i] < distMin && sommetsGris.contains(i)) {
                    posMin = i;
                    distMin = distance[i];
                }
            }
            actuel = this.listeIntersections.get(posMin);

            // Relâche les arcs pour les successeurs
            for (Troncon troncon : actuel.getListeTroncons()) {
                if (!sommetsNoirs.contains(troncon.getDestination().getNumero())) {
                    if (troncon.getLongueur() + distance[actuel.getNumero()] < distance[troncon.getDestination().getNumero()]) {
                        distance[troncon.getDestination().getNumero()] = troncon.getLongueur() + distance[actuel.getNumero()];
                        predecesseurs[troncon.getDestination().getNumero()] = troncon;
                        if (!sommetsGris.contains(troncon.getDestination().getNumero())) {
                            sommetsGris.add(troncon.getDestination().getNumero());
                        }
                    }
                }
            }

            // Retire le sommet actuel des sommets gris et ajoute-le aux sommets noirs
            sommetsGris.remove(Integer.valueOf(actuel.getNumero()));
            sommetsNoirs.add(actuel.getNumero());
        }

        // Reconstruit le chemin et crée l'étape à retourner
        List<Troncon> troncons = new ArrayList<>();
        if (predecesseurs[destination.getNumero()] == null) {
            return null;
        }
        Troncon tronconActuel = predecesseurs[destination.getNumero()];
        troncons.add(tronconActuel);
        while (predecesseurs[tronconActuel.getOrigine().getNumero()] != null) {
            tronconActuel = predecesseurs[tronconActuel.getOrigine().getNumero()];
            troncons.add(tronconActuel);
        }
        Collections.reverse(troncons);
        double longueur = distance[destination.getNumero()];
        Etape plusCourtChemin = new Etape(troncons, origine, destination, longueur);
        return plusCourtChemin;
    }

    // Méthodes pour trouver les coordonnées minimales et maximales

    /**
     * Trouve la latitude minimale parmi toutes les intersections du plan.
     * 
     * @return La latitude minimale.
     */
    public double trouverLatitudeMin() {
        double latitudeMin = 100;
        for (Intersection intersection : this.listeIntersections) {
            if (intersection.getLatitude() < latitudeMin) {
                latitudeMin = intersection.getLatitude();
            }
        }
        return latitudeMin;
    }

    /**
     * Trouve la longitude minimale parmi toutes les intersections du plan.
     * 
     * @return La longitude minimale.
     */
    public double trouverLongitudeMin() {
        double longitudeMin = 100;
        for (Intersection intersection : this.listeIntersections) {
            if (intersection.getLongitude() < longitudeMin) {
                longitudeMin = intersection.getLongitude();
            }
        }
        return longitudeMin;
    }

    /**
     * Trouve la latitude maximale parmi toutes les intersections du plan.
     * 
     * @return La latitude maximale.
     */
    public double trouverLatitudeMax() {
        double latitudeMax = -100;
        for (Intersection intersection : this.listeIntersections) {
            if (intersection.getLatitude() > latitudeMax) {
                latitudeMax = intersection.getLatitude();
            }
        }
        return latitudeMax;
    }

    /**
     * Trouve la longitude maximale parmi toutes les intersections du plan.
     * 
     * @return La longitude maximale.
     */
    public double trouverLongitudeMax() {
        double longitudeMax = -100;
        for (Intersection intersection : this.listeIntersections) {
            if (intersection.getLongitude() > longitudeMax) {
                longitudeMax = intersection.getLongitude();
            }
        }
        return longitudeMax;
    }

    /**
     * Cherche l'intersection la plus proche d'une position donnée par latitude et longitude.
     * 
     * @param latitude La latitude de la position.
     * @param longitude La longitude de la position.
     * @return L'intersection la plus proche.
     */
    public Intersection chercherIntersectionLaPlusProche(double latitude, double longitude) {
        if (listeIntersections.isEmpty()) {
            return null;
        }

        Intersection intersectionLaPlusProche = null;
        double distanceMin = Double.MAX_VALUE;

        // Recherche de l'intersection la plus proche
        for (Intersection intersection : listeIntersections) {
            double distance = calculerDistance(latitude, longitude, intersection.getLatitude(), intersection.getLongitude());
            
            if (distance < distanceMin) {
                distanceMin = distance;
                intersectionLaPlusProche = intersection;
            }
        }

        return intersectionLaPlusProche;
    }

    /**
     * Calcule la distance entre deux points géographiques (latitude, longitude)
     * en utilisant la formule de Haversine pour des coordonnées sur Terre.
     * 
     * @param lat1 La latitude du premier point.
     * @param lon1 La longitude du premier point.
     * @param lat2 La latitude du deuxième point.
     * @param lon2 La longitude du deuxième point.
     * @return La distance en mètres entre les deux points.
     */
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double rayonTerreMetre = 6371000.0; // Rayon de la Terre en mètres
        double dLat = lat2Rad - lat1Rad;
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
