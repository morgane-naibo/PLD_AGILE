package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*; 
import java.util.stream.Collectors;

public class Demande {
    private Entrepot entrepot;
    private List<PointDeLivraison> listePointDeLivraison;
    private List<List<Etape>> matriceAdjacence;
    private List<List<List<Etape>>> listeMatriceAdjacence;
    private Trajet tournee;
    private Plan plan;
    private int nbLivreurs;

    //constructeur
    public Demande() {
        this.entrepot = new Entrepot() ;
        this.listePointDeLivraison = new ArrayList<PointDeLivraison>();
        this.matriceAdjacence = new ArrayList<List<Etape>>();
        this.tournee = new Trajet() ;
        this.nbLivreurs = 2;
        this.listeMatriceAdjacence = new ArrayList<>(this.nbLivreurs);
    }

    //getters
    public Entrepot getEntrepot() {
        return this.entrepot;
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

    //setters
    public void setEntrepot(Entrepot newEntrepot) {
        this.entrepot = newEntrepot;
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
    public void initialiserMatriceAdjacence() {
        int n = listePointDeLivraison.size();
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

    public void supprimerPointDeLivraison(PointDeLivraison pdl){
        int position=-1;
        for(int i=0; i<this.listePointDeLivraison.size();i++){
            if (listePointDeLivraison.get(i)==pdl) {
                position = i;
            }
        }

        if (position!=-1){
            for(int i=0; i<this.listePointDeLivraison.size(); i++){
                this.matriceAdjacence.get(i).remove(position);
            }
    
            this.matriceAdjacence.remove(position);
            this.listePointDeLivraison.remove(position);
        }
    }
    public void creerClusters(){
        ArrayList<Etape> etapesVisitees = new ArrayList<>();
        ArrayList<ArrayList<Intersection>> listesClusters = new ArrayList<>();
        ArrayList<ArrayList<Integer>> listesIndex = new ArrayList<>();
        //ArrayList<Intersection>[] listesClusters = new ArrayList<Intersection>[this.listePointDeLivraison.size()]
        for(int init = 0; init<this.listePointDeLivraison.size();init++){
            listesClusters.add(new ArrayList<Intersection>());
            listesIndex.add(new ArrayList<Integer>());
            
        }
        Etape enCours = null;
        Etape opposee = null;
        if (this.nbLivreurs<this.listePointDeLivraison.size()){
            for (int i=0; i< this.listePointDeLivraison.size()-this.nbLivreurs;i++){
                double distanceMin = Double.MAX_VALUE;
                Intersection origine = null;
                Intersection destination = null;
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
                                origine = this.matriceAdjacence.get(k).get(j).getDepart();
                                destination = this.matriceAdjacence.get(k).get(j).getArrivee();
                                distanceMin = this.matriceAdjacence.get(k).get(j).getLongueur();
                                enCours = this.matriceAdjacence.get(k).get(j);
                                opposee = this.matriceAdjacence.get(j).get(k);
                                indexDepart = k;
                                indexArrivee = j;
                            }
                        }
                    }
                }
                System.out.println("\r\n\r\n\r\n");
                // System.out.println(enCours.toString());
                for (int l=0;l<this.listePointDeLivraison.size();l++){
                    if (listesClusters.get(l).contains(origine)){
                        boolean test= true;
                        for (int p = l+1 ; p<this.listePointDeLivraison.size();p++){
                            if (listesClusters.get(p).contains(destination)){
                                listesClusters.get(l).addAll(listesClusters.get(p));
                                listesClusters.get(p).removeAll(listesClusters.get(p));
                                
                                listesIndex.get(l).addAll(listesIndex.get(p));
                                listesIndex.get(p).removeAll(listesIndex.get(p));
                                test = false;
                                break;
                            }
                        }
                        if (test){
                            listesClusters.get(l).add(destination);
                            listesIndex.get(l).add(indexArrivee);
                        }
                        break;
                    }
                    else if (listesClusters.get(l).contains(destination)){
                        boolean test= true;
                        for (int p = l ; p<this.listePointDeLivraison.size();p++){
                            if (listesClusters.get(p).contains(origine)){
                                listesClusters.get(l).addAll(listesClusters.get(p));
                                listesClusters.get(p).clear();
                                listesIndex.get(l).addAll(listesIndex.get(p));
                                listesIndex.get(p).clear();
                                test = false;
                                
                                break;
                            }
                        }
                        if (test){
                            listesClusters.get(l).add(origine);
                            listesIndex.get(l).add(indexDepart);
                           
                        }
                        break;
                    }
                    else if (listesClusters.get(l).isEmpty()){
                        System.out.println("iteration l="+l);
                        System.out.println("origine "+origine);
                        System.out.println("destination " +destination);
                        System.out.println("");
                        listesClusters.get(l).add(origine);
                        listesClusters.get(l).add(destination);
                        listesIndex.get(l).add(indexDepart);
                        listesIndex.get(l).add(indexArrivee);
                        
                        break;
                    }
                    
                }
                //On rajoute l'etape à la liste des visitées pour ne pas la prendre en compte plus tard
                etapesVisitees.add(enCours);
                etapesVisitees.add(opposee);


            }
        }
        //Cas ou il y a moins de points de livraison que de livreurs
        else{
            for (int loop = 0; loop < this.listePointDeLivraison.size();loop++){
                listesClusters.get(loop).add(this.listePointDeLivraison.get(loop));
                listesIndex.get(loop).add(loop+1);
            }
        }

        //System.out.println(listesClusters.toString());
        creerMatricesParClusters(listesIndex);
        System.out.println(matrixToString(this.listeMatriceAdjacence.get(0)));
    }


    public void creerMatricesParClusters(ArrayList<ArrayList<Integer>> listesIndex) {
        for (int i = 0; i < this.nbLivreurs; i++) {
            // Initialize the adjacency matrix for each "livreur" as a 2D List of Etape objects
            List<List<Etape>> matrice = new ArrayList<>();
    
            int clusterSize = listesIndex.get(i).size() + 1;
    
            // Create rows for the matrix and initialize each element with null (Etape type)
            for (int j = 0; j < clusterSize; j++) {
                matrice.add(new ArrayList<>(Collections.nCopies(clusterSize, null)));
            }
    
            // Populate the matrix with values from the original adjacency matrix
            matrice.get(0).set(0, this.matriceAdjacence.get(0).get(0));
            // Initialize the first row and first column of the matrix
            for (int j = 1; j < clusterSize; j++) {
                // Set the first row (0, j) from the original adjacency matrix
                matrice.get(0).set(j, this.matriceAdjacence.get(0).get(listesIndex.get(i).get(j - 1)));
                
                // Set the first column (j, 0) from the original adjacency matrix
                matrice.get(j).set(0, this.matriceAdjacence.get(listesIndex.get(i).get(j - 1)).get(0));
            }
    
            for (int j = 1; j < clusterSize; j++) {
                
                for (int k = 1; k < clusterSize; k++) {
                    matrice.get(j).set(k, this.matriceAdjacence.get(listesIndex.get(i).get(j - 1)).get(listesIndex.get(i).get(k - 1)));
                }
            }
    
            // Add this cluster's matrix to the main list
            this.listeMatriceAdjacence.add(matrice);
        }
    
    }

    

}