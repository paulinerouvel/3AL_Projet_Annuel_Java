package models;

import java.time.LocalDate;

public class List {
    private Integer id;
    private String libelle;
    private LocalDate dateMiseEnRayon;

    public List(Integer id, String libelle, LocalDate dateMiseEnRayon){
        this.id = id;
        this.libelle = libelle;
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

    public LocalDate getDateMiseEnRayon() {
        return dateMiseEnRayon;
    }

    public void setDateMiseEnRayon(LocalDate dateMiseEnRayon) {
        this.dateMiseEnRayon = dateMiseEnRayon;
    }
}
