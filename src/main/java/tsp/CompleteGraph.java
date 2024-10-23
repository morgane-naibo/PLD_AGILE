package main.java.tsp;

import main.java.model.Plan;
import main.java.model.Etape;
import main.java.model.Intersection;
import java.util.List;

public class CompleteGraph implements Graph {
	int nbVertices;
	Etape[][] cost;
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(int nbVertices, Plan plan, List<Intersection> listeIntersections){
		this.nbVertices = nbVertices;
		cost = new Etape[nbVertices][nbVertices];
		for (int i=0; i<nbVertices; i++){
		    for (int j=0; j<nbVertices; j++){
		        if (i == j) cost[i][j] = null;
		        else {
		           cost[i][j] = plan.chercherPlusCourtChemin(listeIntersections.get(i), listeIntersections.get(j));
		        }
		    }
		}
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public int getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		// return (cost[i][j]);
		return 1;
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

}
