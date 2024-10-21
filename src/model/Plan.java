package model;

import java.util.ArrayList;
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
        Intersection inter = new Intersection();
        for(Intersection iteratorIntersection : this.listeIntersections){
            if (iteratorIntersection.getId() == id) {
                inter = iteratorIntersection;
                break;
            }
        }
        return inter;
    }

    public Etape chercherPlusCourtChemin(Intersection intersection1, Intersection intersection2) {
        // Initialiser un nouvel objet Etape pour stocker le meilleur chemin
        Etape meilleurChemin = new Etape();
        double meilleurCout = Double.MAX_VALUE; // On commence avec un coût maximum
        List<Troncon> cheminActuel = new ArrayList<>(); // Chemin en cours d'exploration
        double coutActuel = 0; // Coût actuel du chemin
    
        // Vérification des intersections de départ et d'arrivée
        if (intersection1 == null || intersection2 == null) {
            return null; // Si l'une des intersections est nulle, pas de chemin possible
        }
    
        // Appel à la méthode branch and bound
        branchAndBound(intersection1, intersection2, cheminActuel, coutActuel, meilleurCout, meilleurChemin);
    
        // Si aucun chemin n'a été trouvé, retourner null
        if (meilleurChemin.getListeTroncons().isEmpty()) {
            System.out.println("Aucun chemin trouvé entre " + intersection1.getId() + " et " + intersection2.getId());
            return null;
        }
    
        return meilleurChemin;
    }
    
    private void branchAndBound(Intersection courant, Intersection destination, List<Troncon> cheminActuel, double coutActuel, double meilleurCout, Etape meilleurChemin) {
        // Cas de base : si on atteint la destination
        if (courant.equals(destination)) {
            if (coutActuel < meilleurCout) {
                meilleurCout = coutActuel; // Mise à jour du meilleur coût
                meilleurChemin.setListeTroncons(new ArrayList<>(cheminActuel)); // Copier le chemin actuel
                meilleurChemin.setDepart(cheminActuel.get(0).getOrigine());
                meilleurChemin.setArrivee(courant);
                meilleurChemin.setLongueur(coutActuel);
            }
            return;
        }
    
        // Parcours des tronçons à partir de l'intersection courante
        for (Troncon troncon : this.listeTroncons) {
            if (troncon.getOrigine().equals(courant)) {
                // Si ce tronçon mène à un nouveau sommet (pour éviter des cycles)
                if (!cheminActuel.contains(troncon)) {
                    cheminActuel.add(troncon);
                    double nouveauCout = coutActuel + troncon.getLongueur();
    
                    // Si le coût est inférieur au meilleur coût actuel, continuer l'exploration
                    if (nouveauCout < meilleurCout) {
                        branchAndBound(troncon.getDestination(), destination, cheminActuel, nouveauCout, meilleurCout, meilleurChemin);
                    }
    
                    // Backtracking : retirer le tronçon après exploration
                    cheminActuel.remove(cheminActuel.size() - 1);
                }
            }
        }
    }
    

}
