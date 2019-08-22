package fr.wastemart.maven.javaclient.models;

public class Product {
    private Integer id;
    private String libelle;
    private String desc;
    private String photo;
    private Float prix;
    private Float prixInitial;
    private Integer quantite;
    private String dlc;
    private String codeBarre;
    private Boolean enRayon;
    private String dateMiseEnRayon;
    private Integer categorieProduit;
    private Integer listProduct;
    private Integer entrepotwm;
    private Integer destinataire;

    public Product(Integer id, String libelle, String desc, String photo, Float prix, Float prixInitial, Integer quantite, String dlc,
                   String codeBarre, Boolean enRayon, String dateMiseEnRayon, Integer categorieProduit, Integer listProduct, Integer entrepotwm, Integer destinataire) {
        this.id = id;
        this.libelle = libelle;
        this.desc = desc;
        this.photo = photo;
        this.prix = prix;
        this.prixInitial = prixInitial;
        this.quantite = quantite;
        this.dlc = dlc; // Nullable
        this.codeBarre = codeBarre; // Nullable
        this.enRayon = enRayon;
        this.dateMiseEnRayon = dateMiseEnRayon; // Nullable
        this.categorieProduit = categorieProduit;
        this.listProduct = listProduct; // Nullable
        this.entrepotwm = entrepotwm; // Nullable
        this.destinataire = destinataire; // Nullable
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

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Float getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(Float prixInitial) {
        this.prixInitial = prixInitial;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getDlc() {
        return dlc;
    }

    public void setDlc(String dlc) {
        this.dlc = dlc;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Boolean getEnRayon() {
        return enRayon;
    }

    public void setEnRayon(Boolean enRayon) {
        this.enRayon = enRayon;
    }

    public String getDateMiseEnRayon() {
        return dateMiseEnRayon;
    }

    public void setDateMiseEnRayon(String dateMiseEnRayon) {
        this.dateMiseEnRayon = dateMiseEnRayon;
    }

    public Integer getCategorieProduit() {
        return categorieProduit;
    }

    public void setCategorieProduit(Integer categorieProduit) {
        this.categorieProduit = categorieProduit;
    }

    public Integer getListProduct() {
        return listProduct;
    }

    public void setListProduct(Integer listProduct) {
        this.listProduct = listProduct;
    }

    public Integer getEntrepotwm() {
        return entrepotwm;
    }

    public void setEntrepotwm(Integer entrepotwm) {
        this.entrepotwm = entrepotwm;
    }

    public Integer getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Integer destinataire) {
        this.destinataire = destinataire;
    }
}
