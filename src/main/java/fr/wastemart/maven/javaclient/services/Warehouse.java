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
            result = Requester.sendGetRequest("warehouse/", null);

            for(int i = 0; i < result.length(); i++) {
                if (result.getJSONObject(i).isNull("libelle")) {
                    result.getJSONObject(i).put("libelle", "");
                }
                if (result.getJSONObject(i).isNull("adresse")) {
                    result.getJSONObject(i).put("adresse", "Adresse non renseignée");
                }
                if (result.getJSONObject(i).isNull("ville")) {
                    result.getJSONObject(i).put("ville", "Ville non renseignée");
                }
                if (result.getJSONObject(i).isNull("codePostal")) {
                    result.getJSONObject(i).put("codePostal", "Code postal non renseigné");
                }
                if(result.getJSONObject(i).isNull("photo")) {
                    result.getJSONObject(i).put("photo", "");
                }
            }
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }

    // GET Warehouse by City name
    public static JSONObject fetchWarehouseByCity(String city) {
        JSONObject result;
        try {
            result = Requester.sendGetRequest("warehouse/?city=" + city, null).getJSONObject(0);
            // TODO Test if it works, supposed to return JSONObject
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }


        // --- PUT --- //

    // PUT a Warehouse (update)
    public static Integer updateWarehouse(fr.wastemart.maven.javaclient.models.Warehouse warehouse, String token) {
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

        Integer result;
        try {
            result = Requester.sendPutRequest("warehouse/", json, token);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }

    // --- DELETE ---//


    public static fr.wastemart.maven.javaclient.models.Warehouse jsonToWarehouse(JSONObject warehouse) {
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
    }
}
