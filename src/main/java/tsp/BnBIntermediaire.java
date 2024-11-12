package tsp;

import java.util.Collection;

public class BnBIntermediaire {
    private int currentVertex ;
    private Collection<Integer> unvisited;
    private Collection<Integer> visited;
    private double currentCost;

    BnBIntermediaire(int currentVertex, Collection<Integer> unvisited, Collection<Integer> visited, double currentCost) {
        this.currentVertex = currentVertex;
        this.unvisited = unvisited;
        this.currentCost = currentCost;
        this.visited = visited;
    }

    public int getCurrentVertex(){
        return this.currentVertex;
    }

    public Collection<Integer> getUnvisited(){
        return this.unvisited;
    }

    public Collection<Integer> getVisited(){
        return this.visited;
    }

    public double getCurrentCost(){
        return this.currentCost;
    }

    public void setCurrentVertex(int newCurrentVertex){
        this.currentVertex= newCurrentVertex;
    }

    public void setCurrentVertex(double newCurrentCost){
        this.currentCost = newCurrentCost;
    }

    public void setVisited(Collection<Integer> newVisited){
        this.visited= newVisited;
    }

    public void setUnvisited(Collection<Integer> newUnvisited){
        this.unvisited= newUnvisited;
    }
}
