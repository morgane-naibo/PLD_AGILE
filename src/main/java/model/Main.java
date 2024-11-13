package model;

import java.nio.file.Paths;
import java.nio.file.Path;

import util.XMLDemande;
import util.XMLPlan;
import tsp.RunTSP;
import java.lang.Exception;
import exceptions.*;

public class Main {

    public static void main(String[] args) throws Exception{
        String mapPathPlan = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\moyenPlan.xml";
        String mapPathDemande = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\demandeMoyen5.xml";
        XMLPlan planReader = new XMLPlan();
        Plan plan = planReader.parse(mapPathPlan);
        XMLDemande demandeReader = new XMLDemande();
        Demande demande = new Demande();
        demande = demandeReader.parse(mapPathDemande);
        demande.setPlan(plan);
        // System.out.println(demande);
        demande.initialiserListePointdeLivraisons();
        //System.out.println("Liste PDL : " + demande.getListePointDeLivraison().toString());
        // demande.initialiserMatriceAdjacence();
        // System.out.println("AAAAAAAAAAAAAAAAAA\r\n");
        // System.out.println(demande.matrixToString(demande.getMatriceAdjacence()));
        //System.out.println(demande.getMatriceAdjacence());
        // System.out.println(demande.getListePointDeLivraison());
        // System.out.println("AHHHHHHHH");
        // demande.supprimerPointDeLivraison(demande.getListePointDeLivraison().get(1));
        // System.out.println(demande.getListePointDeLivraison());
        // System.out.println("AHHHHHHHH");
        // System.out.println(demande.getMatriceAdjacence().toString());

        //System.out.print(plan);
        //System.out.print(demande);
        // demande.creerClusters();
        // demande.creerMatricesParClusters();

        // System.out.println("\nLes listes index par cluster");
        // System.out.println(demande.getListesIndex().get(0).toString());
        // System.out.println(demande.getListesIndex().get(1).toString());
        //demande.calculerTSP();

        //RunTSP run = new RunTSP();
        try {
            demande.calculerTSP();
            //System.out.println(demande.getLivraisons().get(0).getListeEtapes().toString());
            Intersection inter = plan.chercherIntersectionParId(1957527553);
            System.out.println("inter : "+inter.toString());
            PointDeLivraison pdl= new PointDeLivraison(inter);
            System.out.println("pdl : "+pdl.toString());
            demande.ajouterPDLaMatrice(0,pdl);
            //demande.recalculerTrajetApresAjoutPDL(0);
            System.out.println("Nouveau trajet : "+demande.getLivraisons().get(0).getListeEtapes().toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        //run.calculerTSP(demande.getListeMatriceAdjacence().get(0));
        
        // System.out.println("BBBBBBBBBBBBBBBB\r\n");
        // System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(0)));

        //run.calculerTSP(demande.getListeMatriceAdjacence().get(1));

        // System.out.println("CCCCCCCCCCCCCCCC\r\n");
        // System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(1)));
        // try{
        //     Intersection inter = plan.chercherIntersectionParId(7);
        //     PointDeLivraison newPDL = new PointDeLivraison(inter.getId(), null);
        //     newPDL.setLatitude(inter.getLatitude());
        //     newPDL.setLongitude(inter.getLongitude());
        //     newPDL.setNumero(inter.getNumero());
        //     demande.ajouterPointDeLivraison(newPDL);
        //     demande.ajouterPDLaMatrice(0);
        //     System.out.println(demande.matrixToString(demande.getMatriceAdjacence()));
        //     System.out.println("AAAAAHHHHHHHHHHHHH");
        //     System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(0)));
        //     System.out.println("IIIIIIIIIIHHHHHHHHHHHHH");
        //     System.out.println(demande.matrixToString(demande.getListeMatriceAdjacence().get(1)));
        // } catch (IDIntersectionException e){
        //     e.printStackTrace();
        // }
        

        // if (run.getTimeLimit()==1){
        //     System.out.println("Time Limit atteint");
        //     run.calculerTSPApresTimeLimit(demande.getListeMatriceAdjacence().get(0));
        // }

        
    }

}
