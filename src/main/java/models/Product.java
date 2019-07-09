package models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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

    public Product(Integer id, String libelle, String desc, String photo, Float prix, Float prixInitial, Integer quantite, String dlc,
                   String codeBarre, Integer enRayon, String dateMiseEnRayon) {
        this.id = id;
        this.libelle = libelle;
        this.desc = desc;
        this.photo = photo;
        this.prix = prix;
        this.prixInitial = prixInitial;
        this.quantite = quantite;
        this.dlc = LocalDate.from(OffsetDateTime.parse(dlc, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
        this.codeBarre = codeBarre;
        this.enRayon = enRayon;
        this.dateMiseEnRayon = LocalDate.from(OffsetDateTime.parse(dateMiseEnRayon, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
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
}
