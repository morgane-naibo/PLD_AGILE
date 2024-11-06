package tsp;

import model.Etape;


import java.util.List;

public class RunTSP {
	public RunTSP() {

	}

	public void calculerTSP(List<List<Etape>> matrice) {
		TSP tsp = new TSP1();
		Graph g = new CompleteGraph(matrice);
		long startTime = System.currentTimeMillis();
		tsp.searchSolution(20000, g);
		System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
				+(System.currentTimeMillis() - startTime)+"ms : ");
		for (int i=0; i<g.getNbVertices(); i++)
			System.out.print(tsp.getSolution(i)+" ");
		System.out.println("0");
	}

}
