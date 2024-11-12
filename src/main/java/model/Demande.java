package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*; 
import java.util.stream.Collectors;
import java.lang.Exception;
import exceptions.*;
import tsp.RunTSP;

public class Demande {
    private Entrepot entrepot;
    private List<PointDeLivraison> listePointDeLivraison;
    private List<List<Etape>> matriceAdjacence;
    private List<List<List<Etape>>> listeMatriceAdjacence;
    private Trajet tournee;
    private Plan plan;
    private int nbLivreurs;
    private ArrayList<ArrayList<Integer>> listesIndex;

    //constructeur
    public Demande() {
        this.entrepot = new Entrepot() ;
        this.listePointDeLivraison = new ArrayList<PointDeLivraison>();
        this.matriceAdjacence = new ArrayList<List<Etape>>();
        this.tournee = new Trajet() ;
        this.nbLivreurs = 2;
        this.listeMatriceAdjacence = new ArrayList<>(this.nbLivreurs);
        this.listesIndex = new ArrayList<>();
    }

    //getters
    public Entrepot getEntrepot() {
        return this.entrepot;
    }

    public int getNbLivreurs() {
        return this.nbLivreurs;
    }

    public List<PointDeLivraison> getListePointDeLivraison() {
        return this.listePointDeLivraison;
    }

    public List<List<Etape>> getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    public List<List<List<Etape>>> getListeMatriceAdjacence() {
        return this.listeMatriceAdjacence;
    }

    public Trajet getTournee() {
        return this.tournee;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public ArrayList<ArrayList<Integer>> getListesIndex() {
        return this.listesIndex;
    }

    //setters
    public void setEntrepot(Entrepot newEntrepot) {
        this.entrepot = newEntrepot;
    }

    public void setNbLivreurs(int newNbLivreurs) {
        this.nbLivreurs = newNbLivreurs;
    }

    public void setListePointDeLivraison(List<PointDeLivraison> newListePointDeLivraison) {
        this.listePointDeLivraison = newListePointDeLivraison;
    }

    public void setMatriceAdjacence(List<List<Etape>> newMatriceAdjacence) {
        this.matriceAdjacence = newMatriceAdjacence;
    }

    public void setTournee(Trajet newTournee) {
        this.tournee = newTournee;
    }

    public void setPlan(Plan newPlan) {
        this.plan = newPlan;
    }

    // ajout point de livraison
    public void ajouterPointDeLivraison(PointDeLivraison point) {
        if (point != null) {
            this.listePointDeLivraison.add(point);
        } else {
            System.out.println("Le point de livraison est nul et ne peut pas être ajouté.");
        }
    }

    //toString
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

    public String matrixToString(List<List<Etape>> matrix) {
        StringBuilder sb = new StringBuilder();
    
        for (List<Etape> row : matrix) {
            for (Etape etape : row) {
                if (etape != null) {
                    sb.append(String.format("[O:%s, D:%s, L:%.2f]", etape.getDepart(), etape.getArrivee(), etape.getLongueur()));
                } else {
                    sb.append("[null]");  // Use [null] to indicate empty Etape positions
                }
                sb.append("\t");  // Separate entries with a tab for readability
            }
            sb.append("\n");  // Newline after each row
        }
        
        return sb.toString();
    }

    //autres methodes
    public void initialiserListePointdeLivraisons(){
        int n = listePointDeLivraison.size();
        //ajouter lat, long, num aux points de livraison 
        try{
            //ajouter lat, long, num aux points de livraison 
            for (int i=0;i<n;i++){
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

    public void verifierMatriceAdjacence() throws ImpasseErrorException {
        for (int i = 0; i<this.matriceAdjacence.size();i++){
            for (int j = i+1; j<this.matriceAdjacence.size();j++){
                if (this.matriceAdjacence.get(i).get(j) == null){
                    throw new ImpasseErrorException("Il y a une impasse à sens unique. On ne peut pas calculer d'itinéraire.");
                }
                else if (this.matriceAdjacence.get(j).get(i) == null){
                    throw new ImpasseErrorException("Il y a une impasse à sens unique. On ne peut pas calculer d'itinéraire.");
                }
            }     
        }
    }

    public void supprimerIntersection(Intersection intersection) {
        int position = -1;

        // Check if the intersection is the entrepot
        if (this.entrepot.getId() == intersection.getId()) {
            this.entrepot = new Entrepot();
            // Remove the first row and column in the adjacency matrix
            for (int i = 0; i < this.matriceAdjacence.size(); i++) {
                this.matriceAdjacence.get(i).remove(0);
            }
            this.matriceAdjacence.remove(0);
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
    }

    
    public void creerClusters(){
        ArrayList<Integer> indexAAjouter = new ArrayList();
        for (int i=1;i<this.matriceAdjacence.size();i++){
            indexAAjouter.add(i);
        }
        ArrayList<Etape> etapesVisitees = new ArrayList<>();
        
        for(int init = 0; init<this.listePointDeLivraison.size();init++){
            this.listesIndex.add(new ArrayList<Integer>());
            
        }
        Etape enCours = null;
        Etape opposee = null;
        if (this.nbLivreurs<this.listePointDeLivraison.size()){
            for (int i=0; i< this.listePointDeLivraison.size()-this.nbLivreurs;i++){
                double distanceMin = Double.MAX_VALUE;
                int indexDepart = -1;
                int indexArrivee = -1;
                //on commence à 1 car il y a l'entrepot
                for (int k =1; k<this.listePointDeLivraison.size()+1;k++){
                    for (int j =1; j<this.listePointDeLivraison.size()+1;j++){
                        if (this.matriceAdjacence.get(k).get(j) != null){
                            if (etapesVisitees.contains(this.matriceAdjacence.get(k).get(j))){
                                continue;
                            }
                            if (distanceMin>this.matriceAdjacence.get(k).get(j).getLongueur()){
                                distanceMin = this.matriceAdjacence.get(k).get(j).getLongueur();
                                enCours = this.matriceAdjacence.get(k).get(j);
                                opposee = this.matriceAdjacence.get(j).get(k);
                                indexDepart = k;
                                indexArrivee = j;
                            }
                        }
                        //prendre les distances nulles comme distances max (comment ajouter à enCours??)
                    }
                }
                
                for (int l=0;l<this.listePointDeLivraison.size();l++){
                    if (this.listesIndex.get(l).contains(indexDepart)){
                        boolean test= true;
                        for (int p = l+1 ; p<this.listePointDeLivraison.size();p++){
                            if (this.listesIndex.get(p).contains(indexArrivee)){
                                
                                this.listesIndex.get(l).addAll(this.listesIndex.get(p));
                                this.listesIndex.get(p).removeAll(this.listesIndex.get(p));
                                test = false;
                                break;
                            }
                        }
                        if (test){
                            this.listesIndex.get(l).add(indexArrivee);
                            indexAAjouter.remove(Integer.valueOf(indexArrivee));
                        }
                        break;
                    }
                    else if (this.listesIndex.get(l).contains(indexArrivee)){
                        boolean test= true;
                        for (int p = l ; p<this.listePointDeLivraison.size();p++){
                            if (this.listesIndex.get(p).contains(indexDepart)){
                                this.listesIndex.get(l).addAll(this.listesIndex.get(p));
                                this.listesIndex.get(p).clear();
                                test = false;
                                
                                break;
                            }
                        }
                        if (test){
                            this.listesIndex.get(l).add(indexDepart);
                            indexAAjouter.remove(Integer.valueOf(indexDepart));
                        }
                        break;
                    }
                    else if (this.listesIndex.get(l).isEmpty()){
                        this.listesIndex.get(l).add(indexDepart);
                        this.listesIndex.get(l).add(indexArrivee);
                        indexAAjouter.remove(Integer.valueOf(indexArrivee));
                        indexAAjouter.remove(Integer.valueOf(indexDepart));
                        
                        break;
                    }
                    
                }
                //On rajoute l'etape à la liste des visitées pour ne pas la prendre en compte plus tard
                etapesVisitees.add(enCours);
                etapesVisitees.add(opposee);


            }
            for (int k =0; k<indexAAjouter.size();k++){
                ArrayList<Integer> cluster = new ArrayList<>();
                cluster.add(indexAAjouter.get(k));
                this.listesIndex.set(this.nbLivreurs-k-1,cluster);
            }
        }
        //Cas ou il y a moins de points de livraison que de livreurs
        else{
            for (int loop = 0; loop < this.listePointDeLivraison.size();loop++){
                this.listesIndex.get(loop).add(loop+1);
            }
        }

    }


    public void creerMatricesParClusters() {
        for (int i = 0; i < this.nbLivreurs; i++) {
            // Initialize the adjacency matrix for each "livreur" as a 2D List of Etape objects
            List<List<Etape>> matrice = new ArrayList<>();
    
            int clusterSize = this.listesIndex.get(i).size() + 1;
    
            // Create rows for the matrix and initialize each element with null (Etape type)
            for (int j = 0; j < clusterSize; j++) {
                matrice.add(new ArrayList<>(Collections.nCopies(clusterSize, null)));
            }
    
            // Populate the matrix with values from the original adjacency matrix
            matrice.get(0).set(0, this.matriceAdjacence.get(0).get(0));
            // Initialize the first row and first column of the matrix
            for (int j = 1; j < clusterSize; j++) {
                // Set the first row (0, j) from the original adjacency matrix
                matrice.get(0).set(j, this.matriceAdjacence.get(0).get(this.listesIndex.get(i).get(j - 1)));
                
                // Set the first column (j, 0) from the original adjacency matrix
                matrice.get(j).set(0, this.matriceAdjacence.get(this.listesIndex.get(i).get(j - 1)).get(0));
            }
    
            for (int j = 1; j < clusterSize; j++) {
                
                for (int k = 1; k < clusterSize; k++) {
                    matrice.get(j).set(k, this.matriceAdjacence.get(this.listesIndex.get(i).get(j - 1)).get(this.listesIndex.get(i).get(k - 1)));
                }
            }
    
            // Add this cluster's matrix to the main list
            this.listeMatriceAdjacence.add(matrice);
        }
    
    }


    public List<Trajet> calculerTSP(){
        List<Trajet> livraisons = new ArrayList<>(); 
        try {
            this.initialiserMatriceAdjacence();
            this.verifierMatriceAdjacence();
            this.creerClusters();
            this.creerMatricesParClusters();
            RunTSP run = new RunTSP();
            for (int i = 0; i<nbLivreurs;i++){
                Trajet trajet = new Trajet();
                trajet = run.calculerTSP(this.listeMatriceAdjacence.get(i));
                livraisons.add(trajet);
                //on a juste à afficher le temps des tournées et à signaler qu'une tournée est hors-temps, 
                //c'est à l'utilisateur de modifier manuellement les tournées
                // while (duree>9*60){

                // }
            }

        } catch(Exception e){
            System.out.println("Erreur : "+ e.getMessage());
            e.printStackTrace();
        }

        return livraisons;
    }

    public void creerMatricesPourCluster(int nbLivreur) {
        int clusterSize = this.listesIndex.get(nbLivreur).size() + 1;
    
        // Create rows for the matrix and initialize each element with null (Etape type)
        List<List<Etape>> matrice = new ArrayList<>(clusterSize);
        for (int j = 0; j < clusterSize; j++) {
            matrice.add(new ArrayList<>(Collections.nCopies(clusterSize, null)));
        }

        // Populate the matrix with values from the original adjacency matrix
        matrice.get(0).set(0, this.matriceAdjacence.get(0).get(0));
        // Initialize the first row and first column of the matrix
        for (int j = 1; j < clusterSize; j++) {
            // Set the first row (0, j) from the original adjacency matrix
            matrice.get(0).set(j, this.matriceAdjacence.get(0).get(this.listesIndex.get(nbLivreur).get(j - 1)));
            
            // Set the first column (j, 0) from the original adjacency matrix
            matrice.get(j).set(0, this.matriceAdjacence.get(this.listesIndex.get(nbLivreur).get(j - 1)).get(0));
        }

        for (int j = 1; j < clusterSize; j++) {
            
            for (int k = 1; k < clusterSize; k++) {
                matrice.get(j).set(k, this.matriceAdjacence.get(this.listesIndex.get(nbLivreur).get(j - 1)).get(this.listesIndex.get(nbLivreur).get(k - 1)));
            }
        }

        this.listeMatriceAdjacence.set(nbLivreur, matrice);
    
    }

    public void ajouterPDLaMatrice(int nbLivreur){
        //avant d'appeler cette méthode il faut appeler la méthode ajouterPointDeLivraison pour que newPDL soit déjà pris en compte
        PointDeLivraison newPDL = this.listePointDeLivraison.get(this.listePointDeLivraison.size() - 1);
        this.matriceAdjacence.add(new ArrayList<>(Collections.nCopies(this.matriceAdjacence.size()+1, null)));
        this.matriceAdjacence.get(this.matriceAdjacence.size()-1).set(0,this.plan.chercherPlusCourtChemin(newPDL, this.entrepot));
        this.matriceAdjacence.get(0).add(null);
        this.matriceAdjacence.get(0).set(this.matriceAdjacence.size()-1,this.plan.chercherPlusCourtChemin(this.entrepot, newPDL));
        this.matriceAdjacence.get(this.matriceAdjacence.size()-1).set(this.matriceAdjacence.size()-1, null);

        for(int i=1 ; i<this.matriceAdjacence.size()-1 ; i++){
                this.matriceAdjacence.get(i).add(null);
                this.matriceAdjacence.get(i).set(this.listePointDeLivraison.size(), (this.plan.chercherPlusCourtChemin(this.listePointDeLivraison.get(i), newPDL)));
                this.matriceAdjacence.get(this.listePointDeLivraison.size()).set(i, this.plan.chercherPlusCourtChemin(newPDL, this.listePointDeLivraison.get(i)));
        }

        this.listesIndex.get(nbLivreur).add(this.listePointDeLivraison.size());
        creerMatricesPourCluster(nbLivreur);
    }

    

}