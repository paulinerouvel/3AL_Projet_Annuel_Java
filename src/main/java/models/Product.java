package models;

import java.time.LocalDate;

public class Product {
    private Integer id;
    private String libelle;
    private String desc;
    private String photo;
    private Float prix;
    private Float reduction;
    private LocalDate dlc;
    private String codeBarre;
    private Integer enRayon;
    private LocalDate dateMiseEnRayon;

    public Product(Integer id, String libelle, String desc, String photo, Float prix, Float reduction, LocalDate dlc,
                   String codeBarre, Integer enRayon, LocalDate dateMiseEnRayon) {
        this.id = id;
        this.libelle = libelle;
        this.desc = desc;
        this.photo = photo;
        this.prix = prix;
        this.reduction = reduction;
        this.dlc = dlc;
        this.codeBarre = codeBarre;
        this.enRayon = enRayon;
        this.dateMiseEnRayon = dateMiseEnRayon;
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

    public Float getReduction() {
        return reduction;
    }

    public void setReduction(Float reduction) {
        this.reduction = reduction;
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
