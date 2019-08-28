package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

public class Order {
        // --- POST --- //

        // --- GET --- //

    // GET all orders
    public static JSONArray fetchOrder(String token) {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("order/", token);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }


        // --- PUT --- //

     // --- DELETE ---//


    public static fr.wastemart.maven.javaclient.models.Order jsonToOrder(JSONObject order) {
        try {
            return new fr.wastemart.maven.javaclient.models.Order(
                    order.getInt("id"),
                    DateFormatter.dateToString(order.getString("date")),
                    order.getString("adresse_livraison"),
                    order.getString("cp_livraison"),
                    order.getString("ville_livraison"),
                    order.getInt("utilisateur_id"));
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return null;
        }

    }
}
