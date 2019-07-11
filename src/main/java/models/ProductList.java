package models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ProductList {
    private Integer id;
    private String libelle;
    private LocalDate dateMiseEnRayon;
    private Integer userId;
    private Integer estArchive;

    public ProductList(Integer id, String libelle, String dateMiseEnRayon, Integer userId, Integer estArchive){
        this.id = id;
        this.libelle = libelle;
        this.dateMiseEnRayon = LocalDate.from(OffsetDateTime.parse(dateMiseEnRayon, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
        this.userId = userId;
        this.estArchive = estArchive;
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

    public LocalDate getDateMiseEnRayon() {
        return dateMiseEnRayon;
    }

    public void setDateMiseEnRayon(LocalDate dateMiseEnRayon) {
        this.dateMiseEnRayon = dateMiseEnRayon;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEstArchive() {
        return estArchive;
    }

    public void setEstArchive(Integer estArchive) {
        this.estArchive = estArchive;
    }
}
