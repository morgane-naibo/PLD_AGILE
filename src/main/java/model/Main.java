package model;

import java.nio.file.Paths;
import java.nio.file.Path;

import util.XMLDemande;
import util.XMLPlan;
import tsp.RunTSP;

public class Main {

    public static void main(String[] args) {
        String mapPathPlan = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";
        String mapPathDemande = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testDemande.xml";
        XMLPlan planReader = new XMLPlan();
        Plan plan = planReader.parse(mapPathPlan);
        XMLDemande demandeReader = new XMLDemande();
        Demande demande = new Demande();
        demande = demandeReader.parse(mapPathDemande);
        demande.setPlan(plan);
        // System.out.println(demande);

        demande.initialiserMatriceAdjacence();
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

        RunTSP run = new RunTSP();
        run.calculerTSP(demande.getListeMatriceAdjacence().get(0));
        
    }

}
