package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Order {
        // --- POST --- //

        // --- GET --- //

    // GET all orders
    public static JSONArray fetchOrder(String token) throws Exception {
        JSONArray result;

        HttpResponse response = Requester.sendGetRequest("order/", token);
        result = response.getDataAsJSONArray();

        return result;
    }


        // --- PUT --- //

     // --- DELETE ---//


    public static fr.wastemart.maven.javaclient.models.Order jsonToOrder(JSONObject order) {
        return new fr.wastemart.maven.javaclient.models.Order(
                order.getInt("id"),
                ZonedDateTime.parse(order.getString("date")).toLocalDate(),
                order.getInt("utilisateurID")
        );
    }
}
