package model;

import java.nio.file.Paths;
import java.nio.file.Path;

import util.XMLPlan;

public class Main {

    public static void main(String[] args) {
        /* TEST PLUS COURT CHEMIN 
        Plan plan = new Plan() ;
        Intersection i1 = new Intersection(1, 45.75406, 4.857418) ;
        Intersection i2 = new Intersection(2, 45.75343, 4.857465) ;
        Intersection i3 = new Intersection(3, 45.750404, 4.8744674) ;
        Intersection i4 = new Intersection(4, 45.75871, 4.8704023) ;
        Intersection i5 = new Intersection(5, 45.75171, 4.8718166);
        Intersection i6 = new Intersection(6, 45.750896, 4.859119);

        Troncon ta = new Troncon(i5, i1, 1, "5to2");
        Troncon tb = new Troncon(i1, i2, 2, "5to2 and 3to2");
        Troncon tc = new Troncon(i3, i1, 3, "3to2");
        Troncon td = new Troncon(i5, i3, 4, "5to3 and 3to5");
        Troncon te = new Troncon(i3, i5, 4, "5to3 and 3to5");
        Troncon tf = new Troncon(i4, i5, 5, "2to5 and 6to5");
        Troncon tg = new Troncon(i2, i4, 6, "2to5");
        Troncon th = new Troncon(i2, i3, 7, "2to3");
        Troncon ti = new Troncon(i3, i6, 8, "3to6 and 6to3");
        Troncon tj = new Troncon(i6, i3, 8, "3to6 and 6to3");
        Troncon tk = new Troncon(i6, i4, 9, "6to5");
        Troncon tl = new Troncon(i2, i6, 10, "2to6 and 6to2");
        Troncon tm = new Troncon(i6, i2, 10, "2to6 and 6to2");

        plan.ajouterIntersection(i1);
        plan.ajouterIntersection(i2);
        plan.ajouterIntersection(i3);
        plan.ajouterIntersection(i4);
        plan.ajouterIntersection(i5);
        plan.ajouterIntersection(i6);

        plan.ajouterTroncon(ta);
        plan.ajouterTroncon(tb);
        plan.ajouterTroncon(tc);
        plan.ajouterTroncon(td);
        plan.ajouterTroncon(te);
        plan.ajouterTroncon(tf);
        plan.ajouterTroncon(tg);
        plan.ajouterTroncon(th);
        plan.ajouterTroncon(ti);
        plan.ajouterTroncon(tj);
        plan.ajouterTroncon(tk);
        plan.ajouterTroncon(tl);
        plan.ajouterTroncon(tm);

        Etape plusCourtChemin2to3 = new Etape() ;
        plusCourtChemin2to3 = plan.chercherPlusCourtChemin(i2, i3);

        System.out.println(plusCourtChemin2to3.toString());
        */

        String mapPath = "resources\\fichiersXMLPickupDelivery\\fichiersXMLPickupDelivery\\testPlan.xml";
        XMLPlan planReader = new XMLPlan();
        Plan plan = planReader.parse(mapPath);
        System.out.print(plan);
        
    }

}
