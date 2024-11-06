package tsp;

import model.Plan;
import model.Demande;
import model.Etape;
import model.Intersection;
import java.util.List;

public class CompleteGraph implements Graph {
	int nbVertices;
	double[][] cost;
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param nbVertices
	 */
	public CompleteGraph(List<List<Etape>> matrice){
		this.nbVertices = matrice.size();
		cost = new double[nbVertices][nbVertices];
		for (int i=0; i<nbVertices; i++){
		    for (int j=0; j<nbVertices; j++){
		        if (i == j) cost[i][j] = 0;
		        else {
		           cost[i][j] = matrice.get(i).get(j).getLongueur();
		        }
		    }
		}
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

}
