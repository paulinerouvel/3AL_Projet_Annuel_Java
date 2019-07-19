package fr.wastemart.maven.javaclient.models;


import java.time.LocalDate;

public class  Order {
private java.lang.Integer id;
private java.time.LocalDate date;
private java.lang.Integer userId;

public Order(java.lang.Integer id, java.time.LocalDate date, java.lang.Integer userId) {
this.id = id;
this.date = date;
this.userId = userId;
}

public Integer getId() {
 return this.id;
}


public LocalDate getDate() {
 return this.date;
}


public Integer getUserId() {
 return this.userId;
}


}