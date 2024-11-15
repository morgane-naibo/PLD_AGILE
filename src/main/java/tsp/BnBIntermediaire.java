package tsp;

import java.util.Collection;

/**
 * Représente un état intermédiaire utilisé dans l'algorithme Branch and Bound (BnB) 
 * pour résoudre un problème de type TSP (Traveling Salesman Problem).
 */
public class BnBIntermediaire {
    /** Le sommet actuel où se trouve le voyageur. */
    private int currentVertex;
    
    /** Les sommets non encore visités. */
    private Collection<Integer> unvisited;
    
    /** Les sommets déjà visités. */
    private Collection<Integer> visited;
    
    /** Le coût actuel du chemin parcouru. */
    private double currentCost;

    /**
     * Constructeur de la classe BnBIntermediaire.
     *
     * @param currentVertex le sommet actuel où se trouve le voyageur
     * @param unvisited les sommets non encore visités
     * @param visited les sommets déjà visités
     * @param currentCost le coût actuel du chemin parcouru
     */
    BnBIntermediaire(int currentVertex, Collection<Integer> unvisited, Collection<Integer> visited, double currentCost) {
        this.currentVertex = currentVertex;
        this.unvisited = unvisited;
        this.currentCost = currentCost;
        this.visited = visited;
    }

    /**
     * Retourne le sommet actuel.
     *
     * @return le sommet actuel
     */
    public int getCurrentVertex() {
        return this.currentVertex;
    }

    /**
     * Retourne la liste des sommets non visités.
     *
     * @return une collection des sommets non visités
     */
    public Collection<Integer> getUnvisited() {
        return this.unvisited;
    }

    /**
     * Retourne la liste des sommets visités.
     *
     * @return une collection des sommets visités
     */
    public Collection<Integer> getVisited() {
        return this.visited;
    }

    /**
     * Retourne le coût actuel du chemin parcouru.
     *
     * @return le coût actuel
     */
    public double getCurrentCost() {
        return this.currentCost;
    }

    /**
     * Met à jour le sommet actuel.
     *
     * @param newCurrentVertex le nouveau sommet actuel
     */
    public void setCurrentVertex(int newCurrentVertex) {
        this.currentVertex = newCurrentVertex;
    }

    /**
     * Met à jour le coût actuel du chemin parcouru.
     *
     * @param newCurrentCost le nouveau coût actuel
     */
    public void setCurrentVertex(double newCurrentCost) {
        this.currentCost = newCurrentCost;
    }

    /**
     * Met à jour la liste des sommets visités.
     *
     * @param newVisited la nouvelle collection de sommets visités
     */
    public void setVisited(Collection<Integer> newVisited) {
        this.visited = newVisited;
    }

    /**
     * Met à jour la liste des sommets non visités.
     *
     * @param newUnvisited la nouvelle collection de sommets non visités
     */
    public void setUnvisited(Collection<Integer> newUnvisited) {
        this.unvisited = newUnvisited;
    }
}
