package model;

import java.nio.file.Paths;
import java.nio.file.Path;

import util.XMLDemande;
import util.XMLPlan;
import tsp.RunTSP;
import java.lang.Exception;
import exceptions.*;

public class Main {

    public static void main(String[] args) throws IDIntersectionException{
        String mapPathPlan = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";
        String mapPathDemande = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testDemande.xml";
        XMLPlan planReader = new XMLPlan();
        Plan plan = planReader.parse(mapPathPlan);
        XMLDemande demandeReader = new XMLDemande();
        Demande demande = new Demande();
        demande = demandeReader.parse(mapPathDemande);
        demande.setPlan(plan);
        // System.out.println(demande);
        demande.initialiserListePointdeLivraisons();
        demande.initialiserMatriceAdjacence();
        System.out.println("AAAAAAAAAAAAAAAAAA\r\n");
        System.out.println(demande.matrixToString(demande.getMatriceAdjacence()));
        //System.out.println(demande.getMatriceAdjacence());
        // System.out.println(demande.getListePointDeLivraison());
        // System.out.println("AHHHHHHHH");
        // demande.supprimerPointDeLivraison(demande.getListePointDeLivraison().get(1));
        // System.out.println(demande.getListePointDeLivraison());
        // System.out.println("AHHHHHHHH");
        // System.out.println(demande.getMatriceAdjacence().toString());

        //System.out.print(plan);
        //System.out.print(demande);
        demande.creerClusters();
        demande.creerMatricesParClusters();
        //demande.calculerTSP();

        RunTSP run = new RunTSP();
        run.calculerTSP(demande.getListeMatriceAdjacence().get(0));
        try{
            Intersection inter = plan.chercherIntersectionParId(7);
            PointDeLivraison newPDL = new PointDeLivraison(inter.getId(), null);
            newPDL.setLatitude(inter.getLatitude());
            newPDL.setLongitude(inter.getLongitude());
            newPDL.setNumero(inter.getNumero());
            demande.ajouterPointDeLivraison(newPDL);
            demande.ajouterPDLaMatrice(0);
            System.out.println(demande.matrixToString(demande.getMatriceAdjacence()));
            System.out.println("AAAAAHHHHHHHHHHHHH");
            System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(0)));
            System.out.println("IIIIIIIIIIHHHHHHHHHHHHH");
            System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(1)));
        } catch (IDIntersectionException e){
            e.printStackTrace();
        }
        

        // if (run.getTimeLimit()==1){
        //     System.out.println("Time Limit atteint");
        //     run.calculerTSPApresTimeLimit(demande.getListeMatriceAdjacence().get(0))
        // }

        
    }

}
