package model;

import util.XMLDemande;
import util.XMLPlan;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        /*TEST PLUS COURT CHEMIN 
        Plan plan = new Plan() ;
        Intersection i1 = new Intersection(1, 45.75406, 4.857418) ;
        Intersection i2 = new Intersection(2, 45.75343, 4.857465) ;
        Intersection i3 = new Intersection(3, 45.750404, 4.8744674) ;
        Intersection i4 = new Intersection(4, 45.75871, 4.8704023) ;
        Intersection i5 = new Intersection(5, 45.75171, 4.8718166);

        Troncon ta = new Troncon(i5, i1, 1, "5to2");
        Troncon tb = new Troncon(i1, i2, 2, "5to2 and 3to2");
        Troncon tc = new Troncon(i3, i1, 3, "3to2");
        Troncon td = new Troncon(i5, i3, 4, "5to3 and 3to5");
        Troncon te = new Troncon(i3, i5, 4, "5to3 and 3to5");
        Troncon tf = new Troncon(i4, i5, 5, "2to5 and 6to5");
        Troncon tg = new Troncon(i2, i4, 8, "2to5");
        Troncon th = new Troncon(i2, i3, 7, "2to3");

        List<Troncon> liste;
        liste.add()


        plan.ajouterIntersection(i1);
        plan.ajouterIntersection(i2);
        plan.ajouterIntersection(i3);
        plan.ajouterIntersection(i4);
        plan.ajouterIntersection(i5);

        plan.ajouterTroncon(ta);
        plan.ajouterTroncon(tb);
        plan.ajouterTroncon(tc);
        plan.ajouterTroncon(td);
        plan.ajouterTroncon(te);
        plan.ajouterTroncon(tf);
        plan.ajouterTroncon(tg);
        plan.ajouterTroncon(th);

        
        */
        
        String planPath = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";
        String demandePath = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\demandeMoyen5.xml";

        XMLPlan planReader = new XMLPlan();
        Plan plan = planReader.parse(planPath);
        //System.out.print(plan.getListeIntersections().getFirst().getListeTroncons().toString());
        
        Etape plusCourtChemin = new Etape() ;
        plusCourtChemin = plan.chercherPlusCourtChemin(plan.getListeIntersections().getFirst(), plan.getListeIntersections().get(1));

        System.out.println(plusCourtChemin.toString());

        // XMLDemande demandeReader= new XMLDemande();
        // Demande demande = demandeReader.parse(demandePath);
        // System.out.println(demande);
    
        
    }

}