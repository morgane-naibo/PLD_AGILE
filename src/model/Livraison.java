package model;

public class Livraison {
    private String destinataire;
    private String adresse;
    private PointDeLivraison point;

    // Constructeur
    public Livraison(String destinataire, String adresse, PointDeLivraison point) {
        this.destinataire = destinataire;
        this.adresse = adresse;
        this.point = point;
    }

}
