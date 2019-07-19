package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Warehouse {
    // GET all warehouses
    public static JSONArray fetchAllWarehouse() {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/warehouse/");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            JSONArray result = new JSONArray(content.toString());
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

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }
    // GET Warehouse by City name
    public static JSONObject fetchWarehouseByCity(String city) {
        URL url = null;
        try {

            url = new URL("https://wastemart-api.herokuapp.com/warehouse/?city=" + city);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;

            if(status == 408) {
                return new JSONObject();
            } else if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            return new JSONObject(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject("{null}");
        }
    }

    // PUT a warehouse
    public static Integer updateWarehouse(fr.wastemart.maven.javaclient.models.Warehouse warehouse) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/warehouse/");

            String jsonBody =
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


            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            ((HttpURLConnection) con).setRequestMethod("PUT");



            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
            return http.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (http != null) {
                    return http.getResponseCode();
                }
                return 299;
            } catch (IOException ex) {
                ex.printStackTrace();
                return 299;
            }
        }



    }
    public static Integer updateProductWarehouse(Integer idProduct, Integer idWarehouse) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/warehouse/");
            String jsonBody =
                    "{\n" +
                            "\t\"idProduct\": "+idProduct+",\n" +
                            "\t\"idWarehouse\" : "+idWarehouse+"\n" +
                            "}";


            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            ((HttpURLConnection) con).setRequestMethod("PUT");


            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
            return http.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (http != null) {
                    return http.getResponseCode();
                }
                return 299;
            } catch (IOException ex) {
                ex.printStackTrace();
                return 299;
            }
        }
    }

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
