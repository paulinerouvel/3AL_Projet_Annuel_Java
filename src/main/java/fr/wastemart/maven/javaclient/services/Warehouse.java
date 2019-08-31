package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

public class Warehouse {
        // --- POST --- //

        // --- GET --- //

    // GET all warehouses
    public static JSONArray fetchAllWarehouse() {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("warehouse/", null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET Warehouse by City name
    public static JSONObject fetchWarehouseByCity(String city) {
        JSONObject result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("warehouse/?city=" + city, null);
            result = response.getDataAsJSONObject();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
        // TODO Test if it works, supposed to return JSONObject
    }


        // --- PUT --- //

    // PUT a Warehouse (update)
    public static boolean updateWarehouse(fr.wastemart.maven.javaclient.models.Warehouse warehouse, String token) {
        String json =
                "{\n" +
                        "\t\"id\": "+warehouse.getId()+",\n" +
                        "\t\"libelle\" : \""+warehouse.getLibelle()+"\",\n" +
                        "\t\"adresse\": \""+warehouse.getAdresse()+"\",\n" +
                        "\t\"ville\": \""+warehouse.getVille()+"\",\n" +
                        "\t\"codePostal\":"+warehouse.getCodePostal()+",\n" +
                        "\t\"desc\":\""+warehouse.getDesc()+"\",\n" +
                        "\t\"photo\":\""+warehouse.getPhoto()+"\",\n" +
                        "\t\"placeTotal\":"+warehouse.getPlaceTotal()+",\n" +
                        "\t\"placeLibre\":"+warehouse.getPlaceLibre()+"\n" +
                        "}";

        Integer result = 299;
        try {
            result = Requester.sendPutRequest("warehouse/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


    // --- DELETE ---//


    public static fr.wastemart.maven.javaclient.models.Warehouse jsonToWarehouse(JSONObject warehouse) {
        try{
            return new fr.wastemart.maven.javaclient.models.Warehouse(
                    warehouse.getInt("id"),
                    warehouse.isNull("libelle") ? null : warehouse.getString("libelle"),
                    warehouse.getString("adresse"),
                    warehouse.getString("ville"),
                    warehouse.getString("codePostal"),
                    warehouse.isNull("desc") ? null : warehouse.getString("desc"),
                    warehouse.isNull("photo") ? null : warehouse.getString("photo"),
                    warehouse.isNull("placeTotal") ? null : warehouse.getInt("placeTotal"),
                    warehouse.isNull("placeLibre") ? null : warehouse.getInt("placeLibre")
            );

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return null;
        }
    }

}
