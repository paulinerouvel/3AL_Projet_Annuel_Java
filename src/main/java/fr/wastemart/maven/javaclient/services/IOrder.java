package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.annotation.AutoImplement;
import fr.wastemart.maven.javaclient.annotation.Mandatory;

import java.time.LocalDate;

@AutoImplement(as = "Order", builder = false)
public interface IOrder {

    @Mandatory
    String getId();

    @Mandatory
    String getDate();

    @Mandatory
    LocalDate getUserId();
}
