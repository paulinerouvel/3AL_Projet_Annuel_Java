package fr.wastemart.maven.javaclient.models;

public class Warehouse {
    private Integer id;
    private String libelle;
    private String adresse;
    private String ville;
    private String codePostal;
    private String desc;
    private String photo;
    private Integer placeTotal;
    private Integer placeLibre;

    public Warehouse(Integer id, String libelle, String adresse, String ville, String codePostal, String desc, String photo, Integer placeTotal, Integer placeLibre) {
        this.id = id;
        this.libelle = libelle;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.desc = desc;
        this.photo = photo;
        this.placeTotal = placeTotal;
        this.placeLibre = placeLibre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getPlaceTotal() {
        return placeTotal;
    }

    public void setPlaceTotal(Integer placeTotal) {
        this.placeTotal = placeTotal;
    }

    public Integer getPlaceLibre() {
        return placeLibre;
    }

    public void setPlaceLibre(Integer placeLibre) {
        this.placeLibre = placeLibre;
    }
}
