package tsp;

import model.Etape;
import model.Trajet;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de résoudre un problème TSP (Traveling Salesman Problem).
 * Elle utilise une approche basée sur l'algorithme Branch and Bound (BnB).
 */
public class RunTSP {

    /** L'état intermédiaire utilisé pour calculer le TSP. */
    private BnBIntermediaire intermediaire;

    /** La limite de temps pour les calculs en millisecondes. */
    private int timeLimit;

    /**
     * Constructeur par défaut de la classe RunTSP.
     */
    public RunTSP() {
    }

    /**
     * Calcule une solution au problème TSP à partir d'une matrice de distances entre les étapes.
     *
     * @param matrice une matrice représentant les distances entre les différentes étapes
     * @return un objet {@link Trajet} représentant le meilleur trajet trouvé
     */
    public Trajet calculerTSP(List<List<Etape>> matrice) {
        TSP tsp = new TSP1();
        List<Etape> solution = new ArrayList<>();
        Graph g = new CompleteGraph(matrice);
        this.timeLimit = 1;
        long startTime = System.currentTimeMillis();
        this.intermediaire = tsp.searchSolution(20000, g);
        long runTime = System.currentTimeMillis() - startTime;
        if (runTime < 20000) {
            this.timeLimit = 0;
        }

        for (int i = 0; i < matrice.size() - 1; i++) {
            solution.add(matrice.get(tsp.getSolution(i)).get(tsp.getSolution(i + 1)));
        }
        solution.add(matrice.get(tsp.getSolution(matrice.size() - 1)).get(tsp.getSolution(0)));
        Trajet meilleurTrajet = new Trajet(solution);
        return meilleurTrajet;
    }

    /**
     * Calcule une solution au problème TSP en partant de l'état intermédiaire précédent.
     *
     * @param matrice une matrice représentant les distances entre les différentes étapes
     * @return un objet {@link Trajet} représentant le meilleur trajet trouvé
     */
    public Trajet calculerTSPApresTimeLimit(List<List<Etape>> matrice) {
        TSP tsp = new TSP1();
        List<Etape> solution = new ArrayList<>();
        Graph g = new CompleteGraph(matrice);
        this.timeLimit = 1;
        long startTime = System.currentTimeMillis();
        this.intermediaire = tsp.searchSolutionFromPrevious(20000, g, this.intermediaire);
        long runTime = System.currentTimeMillis() - startTime;
        if (runTime < 20000) {
            this.timeLimit = 0;
        }

        for (int i = 0; i < matrice.size() - 1; i++) {
            solution.add(matrice.get(tsp.getSolution(i)).get(tsp.getSolution(i + 1)));
        }
        solution.add(matrice.get(tsp.getSolution(matrice.size() - 1)).get(tsp.getSolution(0)));
        Trajet meilleurTrajet = new Trajet(solution);
        return meilleurTrajet;
    }

    /**
     * Retourne la limite de temps pour les calculs en millisecondes.
     *
     * @return la limite de temps en millisecondes
     */
    public int getTimeLimit() {
        return this.timeLimit;
    }
}
