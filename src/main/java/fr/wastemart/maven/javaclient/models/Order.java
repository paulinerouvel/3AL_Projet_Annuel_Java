package fr.wastemart.maven.javaclient.models;

public class  Order {
    private Integer id;
    private String date;
    private String adresse_livraison;
    private String cp_livraison;
    private String ville_livraison;
    private Integer utilisateur_id;

    public Order(Integer id, String date, String adresse_livraison, String cp_livraison, String ville_livraison, Integer utilisateur_id) {
        this.id = id;
        this.date = date;
        this.adresse_livraison = adresse_livraison;
        this.cp_livraison = cp_livraison;
        this.ville_livraison = ville_livraison;
        this.utilisateur_id = utilisateur_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdresse_livraison() {
        return adresse_livraison;
    }

    public void setAdresse_livraison(String adresse_livraison) {
        this.adresse_livraison = adresse_livraison;
    }

    public String getCp_livraison() {
        return cp_livraison;
    }

    public void setCp_livraison(String cp_livraison) {
        this.cp_livraison = cp_livraison;
    }

    public String getVille_livraison() {
        return ville_livraison;
    }

    public void setVille_livraison(String ville_livraison) {
        this.ville_livraison = ville_livraison;
    }

    public Integer getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(Integer utilisateur_id) {
        utilisateur_id = utilisateur_id;
    }
}