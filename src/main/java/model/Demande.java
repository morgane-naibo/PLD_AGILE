package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*; 
import java.util.stream.Collectors;
import java.lang.Exception;
import exceptions.*;
import tsp.RunTSP;

/**
 * Classe représentant une demande de livraison, incluant l'entrepôt, les points de livraison,
 * les matrices d'adjacence, les trajets, ainsi que d'autres informations liées à la gestion des livraisons.
 */
public class Demande {
    private Entrepot entrepot;  // Entrepôt associé à la demande
    private List<PointDeLivraison> listePointDeLivraison;  // Liste des points de livraison
    private List<List<Etape>> matriceAdjacence;  // Matrice des étapes d'adjacence pour chaque livreur
    private List<List<List<Etape>>> listeMatriceAdjacence;  // Liste des matrices d'adjacence pour tous les livreurs
    private Trajet tournee;  // Tournée associée à la demande
    private Plan plan;  // Plan contenant les informations géographiques
    private int nbLivreurs;  // Nombre de livreurs
    private ArrayList<ArrayList<Integer>> listesIndex;  // Liste des indices des points de livraison
    private List<Trajet> livraisons;  // Liste des trajets de livraison
    private List<RunTSP> listeRun;  // Liste des exécutions de TSP (problème du voyageur de commerce)

    /**
     * Constructeur de la classe Demande.
     * Initialise les propriétés de la demande avec des valeurs par défaut.
     */
    public Demande() {
        this.entrepot = new Entrepot();
        this.listePointDeLivraison = new ArrayList<PointDeLivraison>();
        this.matriceAdjacence = new ArrayList<List<Etape>>();
        this.tournee = new Trajet();
        this.nbLivreurs = 2;
        this.listeMatriceAdjacence = new ArrayList<>(this.nbLivreurs);
        this.listesIndex = new ArrayList<>();
        this.livraisons = new ArrayList<>();
        this.listeRun = new ArrayList<RunTSP>();
    }

    // -------------------------- Getters --------------------------

    /**
     * Retourne l'entrepôt associé à la demande.
     * @return l'entrepôt
     */
    public Entrepot getEntrepot() {
        return this.entrepot;
    }

    /**
     * Retourne la liste des points de livraison.
     * @return la liste des points de livraison
     */
    public List<PointDeLivraison> getListePointDeLivraison() {
        return this.listePointDeLivraison;
    }

    /**
     * Retourne la matrice d'adjacence des étapes.
     * @return la matrice d'adjacence
     */
    public List<List<Etape>> getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    /**
     * Retourne la liste des matrices d'adjacence pour tous les livreurs.
     * @return la liste des matrices d'adjacence
     */
    public List<List<List<Etape>>> getListeMatriceAdjacence() {
        return this.listeMatriceAdjacence;
    }

    /**
     * Retourne la tournée associée à la demande.
     * @return la tournée
     */
    public Trajet getTournee() {
        return this.tournee;
    }

    /**
     * Retourne le plan associé à la demande.
     * @return le plan
     */
    public Plan getPlan() {
        return this.plan;
    }

    /**
     * Retourne les indices des points de livraison dans la liste.
     * @return les indices des points de livraison
     */
    public ArrayList<ArrayList<Integer>> getListesIndex() {
        return this.listesIndex;
    }

    /**
     * Retourne la liste des trajets de livraison.
     * @return la liste des trajets
     */
    public List<Trajet> getLivraisons() {
        return this.livraisons;
    }

    /**
     * Retourne le nombre de livreurs dans la demande.
     * @return le nombre de livreurs
     */
    public int getNbLivreurs(){
        return this.nbLivreurs;
    }

    /**
     * Retourne la liste des exécutions du problème du voyageur de commerce (TSP).
     * @return la liste des exécutions de TSP
     */
    public List<RunTSP> getListeRunTSP(){
        return this.listeRun;
    }

    // -------------------------- Setters --------------------------

    /**
     * Définit un nouvel entrepôt pour la demande.
     * @param newEntrepot le nouvel entrepôt
     */
    public void setEntrepot(Entrepot newEntrepot) {
        this.entrepot = newEntrepot;
    }

    /**
     * Définit une nouvelle liste de points de livraison pour la demande.
     * @param newListePointDeLivraison la nouvelle liste de points de livraison
     */
    public void setListePointDeLivraison(List<PointDeLivraison> newListePointDeLivraison) {
        this.listePointDeLivraison = newListePointDeLivraison;
    }

    /**
     * Définit une nouvelle matrice d'adjacence pour la demande.
     * @param newMatriceAdjacence la nouvelle matrice d'adjacence
     */
    public void setMatriceAdjacence(List<List<Etape>> newMatriceAdjacence) {
        this.matriceAdjacence = newMatriceAdjacence;
    }

    /**
     * Définit une nouvelle tournée pour la demande.
     * @param newTournee la nouvelle tournée
     */
    public void setTournee(Trajet newTournee) {
        this.tournee = newTournee;
    }

    /**
     * Définit un nouveau plan pour la demande.
     * @param newPlan le nouveau plan
     */
    public void setPlan(Plan newPlan) {
        this.plan = newPlan;
    }

    /**
     * Définit le nombre de livreurs dans la demande.
     * @param newNbLivreur le nouveau nombre de livreurs
     */
    public void setNbLivreur(int newNbLivreur){
        this.nbLivreurs = newNbLivreur;
    }

    // -------------------------- Autres Méthodes --------------------------

    /**
     * Ajoute un point de livraison à la demande.
     * @param point le point de livraison à ajouter
     */
    public void ajouterPointDeLivraison(PointDeLivraison point) {
        if (point != null) {
            this.listePointDeLivraison.add(point);
        } else {
            System.out.println("Le point de livraison est nul et ne peut pas être ajouté.");
        }
    }

    /**
     * Retourne une représentation textuelle de la demande.
     * @return la chaîne de caractères représentant la demande
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Demande:\n");
        sb.append("Entrepôt: ").append(entrepot).append("\n");
        sb.append("Points de livraison:\n");

        for (PointDeLivraison point : listePointDeLivraison) {
            sb.append(point).append("\n");
        }

        sb.append("Matrice d'adjacence (Etapes):\n");
        for (List<Etape> listeEtapes : matriceAdjacence) {
            sb.append("[ ");
            for (Etape etape : listeEtapes) {
                sb.append(etape).append(" ");
            }
            sb.append("]\n");
        }

        sb.append("Tournée: ").append(tournee).append("\n");
        return sb.toString();
    }

    /**
     * Convertit une matrice d'adjacence en une chaîne de caractères pour l'affichage.
     * @param matrix la matrice d'adjacence à convertir
     * @return la chaîne de caractères représentant la matrice d'adjacence
     */
    public String matrixToString(List<List<Etape>> matrix) {
        StringBuilder sb = new StringBuilder();
    
        for (List<Etape> row : matrix) {
            for (Etape etape : row) {
                if (etape != null) {
                    sb.append(String.format("[O:%s, D:%s, L:%.2f]", etape.getDepart(), etape.getArrivee(), etape.getLongueur()));
                } else {
                    sb.append("[null]");  // Utiliser [null] pour indiquer les positions vides d'Etape
                }
                sb.append("\t");  // Séparer les entrées avec une tabulation pour la lisibilité
            }
            sb.append("\n");  // Nouvelle ligne après chaque ligne de la matrice
        }
        
        return sb.toString();
    }

        /**
     * Initialise les informations géographiques (latitude, longitude, numéro) pour chaque point de livraison
     * et pour l'entrepôt, en utilisant les données de l'intersection correspondante dans le plan.
     * 
     * Cette méthode parcourt tous les points de livraison de la demande, récupère l'intersection correspondante
     * en utilisant l'ID de chaque point, et met à jour ses informations géographiques. De même, elle récupère
     * les informations de l'entrepôt et les met à jour.
     * 
     * Si une erreur survient lors de l'accès aux intersections ou lors de la mise à jour des informations,
     * l'exception est capturée et son message est affiché.
     * 
     * @throws Exception si une erreur se produit lors de la recherche des intersections ou de la mise à jour des informations.
     */
    public void initialiserListePointdeLivraisons(){
        int n = listePointDeLivraison.size();
        // Ajouter latitude, longitude, et numéro aux points de livraison
        try{
            for (int i = 0; i < n; i++){
                Intersection inter = plan.chercherIntersectionParId(listePointDeLivraison.get(i).getId());
                listePointDeLivraison.get(i).setLatitude(inter.getLatitude());
                listePointDeLivraison.get(i).setLongitude(inter.getLongitude());
                listePointDeLivraison.get(i).setNumero(inter.getNumero());
            }
            Intersection inter = plan.chercherIntersectionParId(this.entrepot.getId());
            this.entrepot.setLatitude(inter.getLatitude());
            this.entrepot.setLongitude(inter.getLongitude());
            this.entrepot.setNumero(inter.getNumero());
        } catch (Exception e){
            System.out.println("Erreur : "+ e.getMessage());
            e.printStackTrace();
        }
    }


        /**
     * Initialise la matrice d'adjacence représentant les chemins entre l'entrepôt et les points de livraison.
     * 
     * Cette méthode crée une matrice carrée de taille (n + 1) x (n + 1), où n est le nombre de points de livraison.
     * La matrice d'adjacence est utilisée pour stocker les chemins les plus courts entre chaque paire d'intersections
     * (entrepôt et points de livraison) dans le plan. 
     * 
     * La matrice est initialisée de manière à ce que chaque cellule (i, j) contienne un objet {@link Etape} 
     * représentant le chemin le plus court entre l'intersection i et l'intersection j. Si i est égal à j, la valeur
     * de la matrice est définie sur null, car il n'y a pas de chemin à calculer pour une intersection vers elle-même.
     * 
     * L'entrepôt est ajouté comme première intersection dans la liste, suivi des points de livraison.
     * Ensuite, pour chaque paire d'intersections distinctes, la méthode cherche le chemin le plus court dans le plan
     * en utilisant la méthode {@link Plan#chercherPlusCourtChemin(Intersection, Intersection)}.
     * 
     * @throws NullPointerException si le plan ou les points de livraison ne sont pas correctement initialisés.
     */
    public void initialiserMatriceAdjacence() {
        int n = listePointDeLivraison.size();
        
        // Initialiser la matrice carrée de taille (n + 1) x (n + 1)
        matriceAdjacence = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            matriceAdjacence.add(new ArrayList<>(Collections.nCopies(n + 1, null)));
        }

        // Ajouter l'entrepôt à la liste des intersections (si nécessaire)
        List<Intersection> listeIntersections = new ArrayList<>(n + 1);
        listeIntersections.add(entrepot); // index 0 pour l'entrepôt
        listeIntersections.addAll(listePointDeLivraison); // index 1 à n pour les points de livraison

        // Remplir la matrice d'adjacence
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (i != j) { // On ne calcule pas la distance vers soi-même
                    Intersection origine = listeIntersections.get(i);
                    Intersection destination = listeIntersections.get(j);
                    Etape chemin = plan.chercherPlusCourtChemin(origine, destination);
                    matriceAdjacence.get(i).set(j, chemin);
                } else {
                    matriceAdjacence.get(i).set(j, null);
                }
            }
        }
    }


        /**
     * Vérifie la matrice d'adjacence pour s'assurer qu'il n'y a pas d'impasses à sens unique.
     * 
     * Cette méthode parcourt la matrice d'adjacence et vérifie chaque paire de points (i, j) pour s'assurer
     * qu'il existe un chemin dans les deux sens entre les points. Si la matrice contient une cellule (i, j)
     * ou (j, i) avec la valeur `null`, cela signifie qu'il n'y a pas de chemin entre ces deux points dans 
     * un sens donné, ce qui est considéré comme une impasse. Dans ce cas, une exception {@link ImpasseErrorException}
     * est lancée.
     * 
     * @throws ImpasseErrorException si une impasse est détectée à sens unique, c'est-à-dire lorsque la cellule 
     *         (i, j) ou (j, i) est nulle dans la matrice d'adjacence.
     */
    public void verifierMatriceAdjacence() throws ImpasseErrorException {
        for (int i = 0; i < this.matriceAdjacence.size(); i++) {
            for (int j = i + 1; j < this.matriceAdjacence.size(); j++) {
                if (this.matriceAdjacence.get(i).get(j) == null) {
                    throw new ImpasseErrorException("Il y a une impasse à sens unique. On ne peut pas calculer d'itinéraire.");
                } else if (this.matriceAdjacence.get(j).get(i) == null) {
                    throw new ImpasseErrorException("Il y a une impasse à sens unique. On ne peut pas calculer d'itinéraire.");
                }
            }     
        }
    }


        /**
     * Crée des clusters de points de livraison pour attribuer les livraisons à chaque livreur.
     * 
     * Cette méthode répartit les points de livraison entre les livreurs en fonction de la matrice d'adjacence
     * qui contient les distances ou durées de parcours entre les différents points. L'objectif est de former des 
     * groupes de points de livraison, appelés "clusters", de manière à minimiser la distance parcourue par chaque 
     * livreur. Le processus est basé sur une approche gloutonne qui associe les points de livraison voisins les plus proches.
     * 
     * Si le nombre de livreurs est inférieur au nombre de points de livraison, la méthode tente de former des clusters
     * de manière optimale en choisissant les points les plus proches dans la matrice d'adjacence et en attribuant ces
     * points aux clusters existants. Les clusters sont créés jusqu'à ce que tous les points de livraison soient attribués.
     * Si le nombre de livreurs est supérieur ou égal au nombre de points de livraison, chaque livreur se voit attribuer
     * un point de livraison.
     * 
     * La méthode utilise la matrice d'adjacence pour déterminer les distances entre les points et pour organiser les
     * livraisons. Elle maintient également une liste des étapes visitées pour éviter de répéter les parcours déjà
     * pris en compte. Après la création des clusters, les indices des points de livraison sont réorganisés et chaque
     * cluster est assigné à un livreur.
     * 
     * @see Etape
     * @see Trajet
     * @see Plan
     * 
     * @throws ImpasseErrorException si un chemin est impossible à déterminer entre deux points dans la matrice d'adjacence.
     */
    public void creerClusters() {
        ArrayList<Integer> indexAAjouter = new ArrayList<Integer>();
        for (int i = 1; i < this.matriceAdjacence.size(); i++) {
            indexAAjouter.add(i);
        }
        ArrayList<Etape> etapesVisitees = new ArrayList<>();
        
        // Initialisation des clusters
        for (int init = 0; init < this.listePointDeLivraison.size(); init++) {
            this.listesIndex.add(new ArrayList<Integer>());
        }
        
        Etape enCours = null;
        Etape opposee = null;
        
        if (this.nbLivreurs < this.listePointDeLivraison.size()) {
            for (int i = 0; i < this.listePointDeLivraison.size() - this.nbLivreurs; i++) {
                double distanceMin = Double.MAX_VALUE;
                int indexDepart = -1;
                int indexArrivee = -1;
                
                // Recherche des points de livraison les plus proches dans la matrice d'adjacence
                for (int k = 1; k < this.listePointDeLivraison.size() + 1; k++) {
                    for (int j = 1; j < this.listePointDeLivraison.size() + 1; j++) {
                        if (this.matriceAdjacence.get(k).get(j) != null) {
                            if (etapesVisitees.contains(this.matriceAdjacence.get(k).get(j))) {
                                continue;
                            }
                            if (distanceMin > this.matriceAdjacence.get(k).get(j).getLongueur()) {
                                distanceMin = this.matriceAdjacence.get(k).get(j).getLongueur();
                                enCours = this.matriceAdjacence.get(k).get(j);
                                opposee = this.matriceAdjacence.get(j).get(k);
                                indexDepart = k;
                                indexArrivee = j;
                            }
                        }
                    }
                }
                
                // Attribution des points aux clusters
                for (int l = 0; l < this.listePointDeLivraison.size(); l++) {
                    if (this.listesIndex.get(l).contains(indexDepart)) {
                        boolean test = true;
                        for (int p = l + 1; p < this.listePointDeLivraison.size(); p++) {
                            if (this.listesIndex.get(p).contains(indexArrivee)) {
                                this.listesIndex.get(l).addAll(this.listesIndex.get(p));
                                this.listesIndex.get(p).removeAll(this.listesIndex.get(p));
                                test = false;
                                break;
                            }
                        }
                        if (test) {
                            this.listesIndex.get(l).add(indexArrivee);
                            indexAAjouter.remove(Integer.valueOf(indexArrivee));
                        }
                        break;
                    } else if (this.listesIndex.get(l).contains(indexArrivee)) {
                        boolean test = true;
                        for (int p = l; p < this.listePointDeLivraison.size(); p++) {
                            if (this.listesIndex.get(p).contains(indexDepart)) {
                                this.listesIndex.get(l).addAll(this.listesIndex.get(p));
                                this.listesIndex.get(p).clear();
                                test = false;
                                break;
                            }
                        }
                        if (test) {
                            this.listesIndex.get(l).add(indexDepart);
                            indexAAjouter.remove(Integer.valueOf(indexDepart));
                        }
                        break;
                    } else if (this.listesIndex.get(l).isEmpty()) {
                        this.listesIndex.get(l).add(indexDepart);
                        this.listesIndex.get(l).add(indexArrivee);
                        indexAAjouter.remove(Integer.valueOf(indexArrivee));
                        indexAAjouter.remove(Integer.valueOf(indexDepart));
                        break;
                    }
                }
                
                // Ajout des étapes visitées pour éviter les répétitions
                etapesVisitees.add(enCours);
                etapesVisitees.add(opposee);
                for (int l = 0; l < this.listesIndex.size(); l++) {
                    if (!listesIndex.get(l).isEmpty()) {
                        for (int k = 0; k < this.listesIndex.get(l).size(); k++) {
                            for (int t = 0; t < this.listesIndex.get(l).size(); t++) {
                                Etape test = this.matriceAdjacence.get(this.listesIndex.get(l).get(k)).get(this.listesIndex.get(l).get(t));
                                Etape test2 = this.matriceAdjacence.get(this.listesIndex.get(l).get(t)).get(this.listesIndex.get(l).get(k));
                                if (!(etapesVisitees.contains(test))) {
                                    etapesVisitees.add(test);
                                }
                                if (!(etapesVisitees.contains(test2))) {
                                    etapesVisitees.add(test2);
                                }
                            }
                        }
                    }
                }
            }
            // Traitement des points restants
            for (int k = 0; k < indexAAjouter.size(); k++) {
                ArrayList<Integer> cluster = new ArrayList<>();
                cluster.add(indexAAjouter.get(k));
                this.listesIndex.set(this.nbLivreurs - k - 1, cluster);
            }
        } else {
            // Cas où il y a moins de points de livraison que de livreurs
            for (int loop = 0; loop < this.listePointDeLivraison.size(); loop++) {
                this.listesIndex.get(loop).add(loop + 1);
            }
        }
    }



        /**
     * Crée des matrices d'adjacence pour chaque cluster de points de livraison.
     * 
     * Cette méthode génère une matrice d'adjacence spécifique pour chaque groupe de points de livraison (cluster),
     * où chaque cluster représente un sous-ensemble de points de livraison attribués à un livreur. La matrice d'adjacence
     * de chaque cluster est dérivée de la matrice d'adjacence globale, mais ne contient que les points de livraison
     * correspondant à un livreur particulier. L'objectif est de faciliter le calcul des itinéraires pour chaque livreur
     * en se concentrant sur un sous-ensemble réduit de points.
     * 
     * Si le nombre de livreurs est supérieur au nombre de points de livraison, la taille de la matrice sera limitée
     * au nombre de points de livraison. Chaque cluster est une sous-matrice représentant un livreur et ses points
     * de livraison assignés.
     * 
     * Le processus consiste à :
     * - Initialiser une matrice d'adjacence vide pour chaque livreur.
     * - Remplir cette matrice avec les informations de la matrice d'adjacence globale en fonction des points de
     *   livraison assignés à chaque livreur.
     * - Chaque matrice est une matrice carrée de taille `(nombre de points + 1)` pour inclure l'entrepôt.
     * 
     * Cette méthode modifie la liste `listeMatriceAdjacence`, qui contient toutes les matrices d'adjacence pour chaque
     * cluster de points de livraison.
     * 
     * @see Etape
     * @see Plan
     * 
     * @throws IndexOutOfBoundsException si les indices dans `listesIndex` sont invalides ou dépassent les limites de
     *         la matrice d'adjacence globale.
     */
    public void creerMatricesParClusters() {
        int size;

        if (this.nbLivreurs > this.listePointDeLivraison.size()) {
            size = this.listePointDeLivraison.size();
        } else {
            size = this.nbLivreurs;
        }

        for (int i = 0; i < size; i++) {
            // Initialiser la matrice d'adjacence pour chaque "livreur" sous forme de liste 2D d'objets Etape
            List<List<Etape>> matrice = new ArrayList<>();

            int clusterSize = this.listesIndex.get(i).size() + 1;

            // Créer les lignes de la matrice et initialiser chaque élément à null (type Etape)
            for (int j = 0; j < clusterSize; j++) {
                matrice.add(new ArrayList<>(Collections.nCopies(clusterSize, null)));
            }

            // Remplir la matrice avec les valeurs de la matrice d'adjacence d'origine
            matrice.get(0).set(0, this.matriceAdjacence.get(0).get(0));
            // Initialiser la première ligne et la première colonne de la matrice
            for (int j = 1; j < clusterSize; j++) {
                // Remplir la première ligne (0, j) depuis la matrice d'adjacence d'origine
                matrice.get(0).set(j, this.matriceAdjacence.get(0).get(this.listesIndex.get(i).get(j - 1)));

                // Remplir la première colonne (j, 0) depuis la matrice d'adjacence d'origine
                matrice.get(j).set(0, this.matriceAdjacence.get(this.listesIndex.get(i).get(j - 1)).get(0));
            }

            // Remplir les autres éléments de la matrice pour les indices (j, k)
            for (int j = 1; j < clusterSize; j++) {
                for (int k = 1; k < clusterSize; k++) {
                    matrice.get(j).set(k, this.matriceAdjacence.get(this.listesIndex.get(i).get(j - 1)).get(this.listesIndex.get(i).get(k - 1)));
                }
            }

            // Ajouter la matrice de ce cluster à la liste principale des matrices d'adjacence
            this.listeMatriceAdjacence.add(matrice);
        }
    }



        /**
     * Calcule le Problème du Voyageur de Commerce (TSP) pour chaque cluster de livreurs.
     * 
     * Cette méthode exécute une série d'opérations nécessaires pour résoudre le problème du TSP pour chaque livreur :
     * - Initialise la matrice d'adjacence globale.
     * - Vérifie que la matrice d'adjacence ne contient pas d'impasses.
     * - Crée des clusters de points de livraison.
     * - Crée des matrices d'adjacence spécifiques pour chaque cluster.
     * 
     * Ensuite, pour chaque cluster (chaque livreur), la méthode utilise un algorithme de TSP (via la classe `RunTSP`) pour calculer 
     * l'itinéraire optimal pour ce livreur. Les résultats (trajets) sont ensuite stockés dans la liste `livraisons`.
     * 
     * L'algorithme de TSP calculera l'itinéraire optimal en tenant compte des points de livraison du cluster et des distances
     * entre ces points, tout en respectant les contraintes de temps. Si un itinéraire est hors du temps imparti, l'utilisateur 
     * devra manuellement ajuster les trajets.
     * 
     * @return Une liste de trajets (`livraisons`) représentant les itinéraires optimaux calculés pour chaque livreur.
     * 
     * @throws Exception Si une erreur se produit lors de l'initialisation des matrices, de la vérification ou du calcul du TSP.
     */
    public List<Trajet> calculerTSP() {
        try {
            this.initialiserMatriceAdjacence();
            this.verifierMatriceAdjacence();
            this.creerClusters();
            this.creerMatricesParClusters();
           
            int size;
            if(this.nbLivreurs > this.listePointDeLivraison.size()){
                size = this.listePointDeLivraison.size();
            }
            else{
                size = this.nbLivreurs;
            }

            for (int i = 0; i < size; i++) {
                listeRun.add(new RunTSP());
                Trajet trajet = new Trajet();
                trajet = listeRun.get(i).calculerTSP(this.listeMatriceAdjacence.get(i));
                this.livraisons.add(trajet);
                // On a juste à afficher le temps des tournées et à signaler qu'une tournée est hors-temps,
                // c'est à l'utilisateur de modifier manuellement les tournées.
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        return livraisons;
    }


        /**
     * Crée une matrice d'adjacence spécifique pour un cluster donné de points de livraison.
     * 
     * Cette méthode génère une matrice d'adjacence pour un cluster de points de livraison assignés à un livreur particulier.
     * La matrice est dérivée de la matrice d'adjacence globale, mais ne contient que les points de livraison assignés à ce
     * livreur (le cluster).
     * 
     * La matrice générée est une matrice carrée où les indices représentent les points de livraison du cluster et
     * les valeurs représentent les distances ou les étapes entre ces points.
     * 
     * L'entrepôt est également inclus dans la matrice (index 0) et les valeurs de distance sont extraites de la matrice
     * d'adjacence globale.
     * 
     * Cette matrice est ensuite ajoutée à la liste `listeMatriceAdjacence` à la position correspondante au livreur.
     * 
     * @param nbLivreur L'indice du livreur pour lequel la matrice d'adjacence est générée. Cette valeur détermine quel
     *                  cluster de points de livraison sera utilisé pour créer la matrice.
     * 
     * @throws IndexOutOfBoundsException Si `nbLivreur` est en dehors des limites valides pour les clusters de points
     *         de livraison.
     */
    public void creerMatricesPourCluster(int nbLivreur) {
        int clusterSize = this.listesIndex.get(nbLivreur).size() + 1;

        // Créer les lignes pour la matrice et initialiser chaque élément avec null (type Etape)
        List<List<Etape>> matrice = new ArrayList<>(clusterSize);
        for (int j = 0; j < clusterSize; j++) {
            matrice.add(new ArrayList<>(Collections.nCopies(clusterSize, null)));
        }

        // Remplir la matrice avec les valeurs de la matrice d'adjacence d'origine
        matrice.get(0).set(0, this.matriceAdjacence.get(0).get(0));
        // Initialiser la première ligne et la première colonne de la matrice
        for (int j = 1; j < clusterSize; j++) {
            // Remplir la première ligne (0, j) depuis la matrice d'adjacence d'origine
            matrice.get(0).set(j, this.matriceAdjacence.get(0).get(this.listesIndex.get(nbLivreur).get(j - 1)));
            
            // Remplir la première colonne (j, 0) depuis la matrice d'adjacence d'origine
            matrice.get(j).set(0, this.matriceAdjacence.get(this.listesIndex.get(nbLivreur).get(j - 1)).get(0));
        }

        // Remplir les autres éléments de la matrice pour les indices (j, k)
        for (int j = 1; j < clusterSize; j++) {
            for (int k = 1; k < clusterSize; k++) {
                matrice.get(j).set(k, this.matriceAdjacence.get(this.listesIndex.get(nbLivreur).get(j - 1)).get(this.listesIndex.get(nbLivreur).get(k - 1)));
            }
        }

        // Mettre à jour la matrice dans la liste des matrices d'adjacence
        this.listeMatriceAdjacence.set(nbLivreur, matrice);
    }

        /**
     * Ajoute un nouveau point de livraison (PDL) à la matrice d'adjacence et met à jour les trajets des livreurs.
     * 
     * Cette méthode ajoute un nouveau point de livraison à la liste des points de livraison, puis met à jour la matrice
     * d'adjacence en fonction de ce nouveau point. Elle ajuste également les clusters associés aux livreurs et recalculer
     * leurs trajets respectifs.
     * 
     * Le nouveau point de livraison est inséré dans la matrice d'adjacence, avec les distances calculées entre ce PDL 
     * et les autres points (y compris l'entrepôt). La méthode met à jour les distances dans la matrice pour chaque combinaison
     * d'intersections en utilisant la méthode `chercherPlusCourtChemin` de la classe `Plan`.
     * 
     * Une fois la matrice d'adjacence mise à jour, les matrices de clusters sont recalculées, et un nouveau trajet est calculé
     * pour le livreur concerné. Le trajet mis à jour est retourné.
     * 
     * @param nbLivreur L'indice du livreur pour lequel le point de livraison est ajouté.
     * @param newPDL Le point de livraison à ajouter à la matrice d'adjacence.
     * @return Le trajet mis à jour pour le livreur.
     * @throws Exception Si une erreur se produit lors de l'ajout du point de livraison ou du recalcul du trajet.
     */
    public Trajet ajouterPDLaMatrice(int nbLivreur, PointDeLivraison newPDL) throws Exception {
        // Ajout du PDL à la liste des PDL
        this.ajouterPointDeLivraison(newPDL);
        // Préparation de l'index pour le nouveau PDL
        int indexNewPDL = this.listePointDeLivraison.size();
        // Mise à jour de la matrice d'adjacence
        this.matriceAdjacence.add(new ArrayList<>(Collections.nCopies(this.matriceAdjacence.size() + 1, null)));
        
        // Initialisation des valeurs de la matrice pour les nouveaux éléments
        this.matriceAdjacence.get(indexNewPDL).set(0, this.plan.chercherPlusCourtChemin(newPDL, this.entrepot));
        this.matriceAdjacence.get(0).add(null);
        this.matriceAdjacence.get(0).set(indexNewPDL, this.plan.chercherPlusCourtChemin(this.entrepot, newPDL));
        this.matriceAdjacence.get(indexNewPDL).set(this.matriceAdjacence.size() - 1, null);

        // Mise à jour des distances dans la matrice pour les autres PDL
        for (int i = 1; i < this.matriceAdjacence.size() - 1; i++) {
            this.matriceAdjacence.get(i).add(null);
            this.matriceAdjacence.get(i).set(indexNewPDL, this.plan.chercherPlusCourtChemin(this.listePointDeLivraison.get(i - 1), newPDL));
            this.matriceAdjacence.get(indexNewPDL).set(i, this.plan.chercherPlusCourtChemin(newPDL, this.listePointDeLivraison.get(i - 1)));
        }

        // Mise à jour des clusters
        this.listesIndex.get(nbLivreur).add(this.listePointDeLivraison.size());

        try {
            // Vérification de la matrice et mise à jour du trajet du livreur
            verifierMatriceAdjacence();
            creerMatricesPourCluster(nbLivreur);
            Trajet trajet = this.recalculerTrajetApresAjoutPDL(nbLivreur);
            this.livraisons.set(nbLivreur, trajet);
            return trajet;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


        /**
     * Recalcule le trajet d'un livreur après l'ajout d'un point de livraison (PDL).
     * 
     * Après l'ajout d'un nouveau point de livraison à la matrice d'adjacence, cette méthode ajuste le trajet du livreur
     * concerné en trouvant l'emplacement optimal pour le nouveau PDL dans le trajet existant.
     * 
     * Le trajet est ajusté en recherchant les distances les plus courtes entre le point de livraison nouvellement ajouté
     * et les étapes du trajet existant. Une fois la position optimale trouvée, les étapes du trajet sont mises à jour en
     * conséquence.
     * 
     * @param numLivreur L'indice du livreur dont le trajet est recalculé.
     * @return Le trajet mis à jour pour le livreur.
     * @throws IDIntersectionException Si un problème est rencontré lors de la recherche des intersections.
     */
    public Trajet recalculerTrajetApresAjoutPDL(int numLivreur) throws IDIntersectionException {
        Trajet trajet = this.livraisons.get(numLivreur);
        double distMin = Double.MAX_VALUE;
        PointDeLivraison newPDL = this.listePointDeLivraison.get(this.listePointDeLivraison.size() - 1);

        try {
            Intersection inter = this.plan.chercherIntersectionParId(newPDL.getId());
            int index = 0;
            Intersection depart = entrepot;
            Intersection arrivee = trajet.getListeEtapes().get(0).getArrivee();

            // Recherche de la meilleure position pour insérer le nouveau PDL dans le trajet
            for (int i = 0; i < trajet.getListeEtapes().size(); i++) {
                double dist3 = this.plan.chercherPlusCourtChemin(trajet.getListeEtapes().get(i).getDepart(), inter).getLongueur();
                double dist2 = this.plan.chercherPlusCourtChemin(inter, trajet.getListeEtapes().get(i).getArrivee()).getLongueur();
                double dist1 = trajet.getListeEtapes().get(i).getLongueur();
                double dist = dist3 + dist2 - dist1;
                if (dist < distMin) {
                    distMin = dist;
                    index = i;
                    depart = trajet.getListeEtapes().get(i).getDepart();
                    arrivee = trajet.getListeEtapes().get(i).getArrivee();
                }
            }
            // Mise à jour du trajet
            trajet.getListeEtapes().remove(index);
            trajet.getListeEtapes().add(index, this.plan.chercherPlusCourtChemin(depart, inter));
            trajet.getListeEtapes().add(index + 1, this.plan.chercherPlusCourtChemin(inter, arrivee));

        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }

        return trajet;
    }


    /**
     * Supprime une intersection de la demande.
     *
     * @param intersection L'intersection à supprimer.
     * @return La position de l'intersection supprimée dans la liste des points de livraison,
     *         ou -1 si l'intersection n'était pas présente ou si c'était l'entrepôt.
     */
    public int supprimerIntersection(Intersection intersection) {
        int position = -1;

        // Check if the intersection is the entrepot
        if (this.entrepot.getId() == intersection.getId()) {
            this.entrepot = null;
        } else {
            // Check if the intersection is a point de livraison
            for (int i = 0; i < this.listePointDeLivraison.size(); i++) {
                if (this.listePointDeLivraison.get(i).getId() == intersection.getId()) {
                    position = i;
                    break;
                }
            }

            if (position != -1) {
                this.listePointDeLivraison.remove(position);
                this.initialiserMatriceAdjacence();
            }
        }

        return position;
    }


         /**
     * Supprime un point de livraison (PDL) de la matrice d'adjacence et met à jour les trajets des livreurs.
     * 
     * Cette méthode supprime un point de livraison de la liste des PDL, de la matrice d'adjacence et des clusters de
     * livreurs. Elle met à jour les distances dans la matrice d'adjacence pour refléter la suppression du PDL et recalculer
     * les trajets des livreurs concernés.
     * 
     * La suppression du PDL entraîne également la mise à jour des indices dans les clusters de chaque livreur.
     * 
     * @param nbLivreur L'indice du livreur dont le trajet est mis à jour après la suppression du PDL.
     * @param oldPDL Le point de livraison à supprimer.
     * @return Le trajet mis à jour pour le livreur.
     * @throws Exception Si une erreur se produit lors de la suppression du PDL ou du recalcul du trajet.
     */
    public Trajet supprimerPDL(int nbLivreur, PointDeLivraison oldPDL) throws Exception {
        int indexOldPDL = -1;
        for (int i = 0; i < this.listePointDeLivraison.size(); i++) {
            if (this.listePointDeLivraison.get(i).getId() == oldPDL.getId()) {
                indexOldPDL = i;
                break;
            }
        }

        if (indexOldPDL != -1) {
            this.matriceAdjacence.remove(indexOldPDL);
            for (int i = 1; i < this.matriceAdjacence.size() - 1; i++) {
                this.matriceAdjacence.get(i).remove(indexOldPDL);
            }

            this.listesIndex.get(nbLivreur).remove(Integer.valueOf(indexOldPDL + 1));
            for (int j = 0; j < this.listesIndex.get(nbLivreur).size(); j++) {
                if (this.listesIndex.get(nbLivreur).get(j) >= indexOldPDL) {
                    int index = this.listesIndex.get(nbLivreur).get(j);
                    this.listesIndex.get(nbLivreur).set(j, index - 1);
                }
            }

            this.listePointDeLivraison.remove(indexOldPDL);
        } else {
            return null;
        }

        try {
            verifierMatriceAdjacence();
            creerMatricesPourCluster(nbLivreur);
            Trajet trajet = this.recalculerTrajetApresSuppressionPDL(nbLivreur, oldPDL);
            this.livraisons.set(nbLivreur, trajet);
            return trajet;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



        /**
     * Recalcule le trajet d'un livreur après la suppression d'un point de livraison (PDL).
     * 
     * Après la suppression d'un point de livraison de la matrice d'adjacence, cette méthode ajuste le trajet du livreur
     * concerné en supprimant l'étape associée au PDL supprimé et en recalculant les étapes restantes.
     * 
     * Le trajet est recalculé en trouvant les meilleures distances entre les intersections restantes dans le trajet,
     * après avoir supprimé le PDL.
     * 
     * @param numLivreur L'indice du livreur dont le trajet est recalculé.
     * @param pdl Le point de livraison supprimé.
     * @return Le trajet mis à jour pour le livreur.
     * @throws Exception Si une erreur se produit lors du recalcul du trajet.
     */
    public Trajet recalculerTrajetApresSuppressionPDL(int numLivreur, PointDeLivraison pdl) throws Exception {
        Trajet trajet = this.livraisons.get(numLivreur);

        try {
            Intersection inter = this.plan.chercherIntersectionParId(pdl.getId());
            int index = 0;

            // Recherche de l'étape associée au PDL supprimé et mise à jour du trajet
            for (int i = 0; i < trajet.getListeEtapes().size() - 1; i++) {
                if (trajet.getListeEtapes().get(i).getArrivee().getId() == inter.getId()) {
                    index = i;
                }
            }
            Intersection dep = trajet.getListeEtapes().get(index).getDepart();
            Intersection arr = trajet.getListeEtapes().get(index + 1).getArrivee();
            trajet.getListeEtapes().remove(index);
            trajet.getListeEtapes().add(index, this.plan.chercherPlusCourtChemin(dep, arr));
            trajet.getListeEtapes().remove(index + 1);

        } catch (IDIntersectionException e) {
            e.printStackTrace();
        }
        this.livraisons.set(numLivreur, trajet);
        return trajet;
    }

}
