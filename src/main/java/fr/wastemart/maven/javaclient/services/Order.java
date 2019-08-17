package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Order {
    public static JSONArray fetchOrder() {
        return Requester.sendGetRequest("order/");
    }

    public static JSONArray fetchProductsByOrder(Integer idOrder) {
        JSONArray result = Requester.sendGetRequest("product/warehouse?idOrder="+idOrder);

        for(int i = 0; i < result.length(); i++) {
            if (result.getJSONObject(i).isNull("dateMiseEnRayon")) {
                result.getJSONObject(i).put("dateMiseEnRayon", "");
            }
            if (result.getJSONObject(i).isNull("enRayon")) {
                result.getJSONObject(i).put("enRayon", "0");
            }
            if (result.getJSONObject(i).isNull("entrepotwm_id")) {
                result.getJSONObject(i).put("entrepotwm_id", "0");
            }
            if (result.getJSONObject(i).isNull("destinataire")) {
                result.getJSONObject(i).put("destinataire", "0");
            }
            if(result.getJSONObject(i).isNull("codeBarre")) {
                result.getJSONObject(i).put("codeBarre", "");
            }
        }

        return result;
    }

    public static fr.wastemart.maven.javaclient.models.Order jsonToOrder(JSONObject order) {
        return new fr.wastemart.maven.javaclient.models.Order(
                order.getInt("id"),
                ZonedDateTime.parse(order.getString("date")).toLocalDate(),
                order.getInt("utilisateurID")
        );
    }
}
