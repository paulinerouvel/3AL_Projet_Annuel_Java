package models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Product {
    private Integer id;
    private String libelle;
    private String desc;
    private String photo;
    private Float prix;
    private Float prixInitial;
    private Integer quantite;
    private LocalDate dlc;
    private String codeBarre;
    private Integer enRayon;
    private LocalDate dateMiseEnRayon;
    private Integer categorieProduit;
    private Integer listProduct;
    private Integer entrepotwm;
    private Integer destinataire;

    public Product(Integer id, String libelle, String desc, String photo, Float prix, Float prixInitial, Integer quantite, LocalDate dlc,
                   String codeBarre, Integer enRayon, String dateMiseEnRayon, Integer categorieProduit, Integer listProduct, Integer entrepotwm, Integer destinataire) {
        this.id = id;
        this.libelle = libelle;
        this.desc = desc;
        this.photo = photo;
        this.prix = prix;
        this.prixInitial = prixInitial;
        this.quantite = quantite;
        //this.dlc = LocalDate.from(OffsetDateTime.parse(dlc, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
        this.dlc = dlc;
        this.codeBarre = codeBarre;
        this.enRayon = enRayon;
        this.dateMiseEnRayon = dateMiseEnRayon.equals("") ? null : dateMiseEnRayon.length() > 16 ?
                ZonedDateTime.parse(dateMiseEnRayon, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate() :
                ZonedDateTime.parse(dateMiseEnRayon, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toLocalDate(); // LocalDate.from(OffsetDateTime.parse(dateMiseEnRayon, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));;/*ZonedDateTime.parse(dateMiseEnRayon).toLocalDate();*/ //TODO Ne pas mettre dlc
        this.categorieProduit = categorieProduit;
        this.listProduct = listProduct;
        this.entrepotwm = entrepotwm;
        this.destinataire = destinataire;
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

    public LocalDate getDlc() {
        return dlc;
    }

    public void setDlc(LocalDate dlc) {
        this.dlc = dlc;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Integer getEnRayon() {
        return enRayon;
    }

    public void setEnRayon(Integer enRayon) {
        this.enRayon = enRayon;
    }

    public LocalDate getDateMiseEnRayon() {
        return dateMiseEnRayon;
    }

    public void setDateMiseEnRayon(LocalDate dateMiseEnRayon) {
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
