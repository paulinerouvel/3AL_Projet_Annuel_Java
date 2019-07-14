package fr.wastemart.maven.javaclient.services;


public class  Order implements IOrder {
private java.lang.String id;
private java.lang.String date;
private java.time.LocalDate userId;

public Order(java.lang.String id, java.lang.String date, java.time.LocalDate userId) {
this.id = id;
this.date = date;
this.userId = userId;
}

public java.lang.String getId() {
 return this.id;
}


public java.lang.String getDate() {
 return this.date;
}


public java.time.LocalDate getUserId() {
 return this.userId;
}


}