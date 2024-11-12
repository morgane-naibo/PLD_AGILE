package tsp;

import model.Etape;
import model.Trajet;

import java.util.ArrayList;
import java.util.List;

public class RunTSP {
	private BnBIntermediaire intermediaire;
	private int timeLimit ;

	public RunTSP() {

	}

	public Trajet calculerTSP(List<List<Etape>> matrice) {
		TSP tsp = new TSP1();
		List<Etape> solution = new ArrayList<>();
		Graph g = new CompleteGraph(matrice);
		this.timeLimit = 1 ;
		long startTime = System.currentTimeMillis();
		this.intermediaire = tsp.searchSolution(20000, g);
		long runTime = System.currentTimeMillis() - startTime;
		if (runTime < 20000){
			this.timeLimit = 0 ;
		}

		/*
		System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
				+(System.currentTimeMillis() - startTime)+"ms : ");
		for (int i=0; i<g.getNbVertices(); i++)
			System.out.print(tsp.getSolution(i)+" ");
		System.out.println("0");
		*/

		for (int i =0; i< matrice.size()-1;i++){
			solution.add(matrice.get(tsp.getSolution(i)).get(tsp.getSolution(i+1)));
		}
		solution.add(matrice.get(tsp.getSolution(matrice.size()-1)).get(tsp.getSolution(0)));
		Trajet meilleurTrajet = new Trajet(solution);
		return meilleurTrajet;
	}

	public Trajet calculerTSPApresTimeLimit(List<List<Etape>> matrice) {
		TSP tsp = new TSP1();
		List<Etape> solution = new ArrayList<>();
		Graph g = new CompleteGraph(matrice);
		this.timeLimit = 1 ;
		long startTime = System.currentTimeMillis();
		this.intermediaire = tsp.searchSolutionFromPrevious(20000, g, this.intermediaire);
		long runTime = System.currentTimeMillis() - startTime;
		if (runTime < 20000){
			this.timeLimit = 0 ;
		}

		/*/
		System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
				+(System.currentTimeMillis() - startTime)+"ms : ");
		for (int i=0; i<g.getNbVertices(); i++)
			System.out.print(tsp.getSolution(i)+" ");
		System.out.println("0");
		*/

		for (int i =0; i< matrice.size()-1;i++){
			solution.add(matrice.get(tsp.getSolution(i)).get(tsp.getSolution(i+1)));
		}
		solution.add(matrice.get(tsp.getSolution(matrice.size()-1)).get(tsp.getSolution(0)));
		Trajet meilleurTrajet = new Trajet(solution);
		return meilleurTrajet;
	}

	public int getTimeLimit(){
		return this.timeLimit;
	}

}
