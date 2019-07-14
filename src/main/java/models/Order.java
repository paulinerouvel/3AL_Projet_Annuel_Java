package models;

import java.time.LocalDate;

public class Order {
    private Integer id;
    private LocalDate date;
    private Integer idUser;

    public Order(Integer id, LocalDate date, Integer idUser) {
        this.id = id;
        this.date = date;
        this.idUser = idUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
