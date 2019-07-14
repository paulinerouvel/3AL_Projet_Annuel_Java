package services;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Command {

    public static JSONArray fetchCommand(){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/order");
            // /list/products?id=1

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
            System.out.println("result"+ result);
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

            System.out.println("result2"+ result);

            return result;

        } catch (IOException e) {
            System.out.println("Error fetch warehouse");
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    public static JSONArray deleteProduct(Integer warehouseID){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/warehouse/?id=" + warehouseID);
            // /list/products?id=1

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

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

            return new JSONArray(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }



    public static Integer updateWarehouse(models.Warehouse warehouse) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/warehouse/");

            String jsonBody =
                    "{\n" +
                            "\t\"id\": "+warehouse.getId()+",\n" +
                            "\t\"libelle\" : \""+warehouse.getLibelle()+"\",\n" +
                            "\t\"adresse\": \""+warehouse.getDesc()+"\",\n" +
                            "\t\"ville\": \""+warehouse.getVille()+"\",\n" +
                            "\t\"codePostal\":"+warehouse.getCodePostal()+",\n" +
                            "\t\"desc\":"+warehouse.getDesc()+",\n" +
                            "\t\"photo\":"+warehouse.getPhoto()+",\n" +
                            "\t\"placeTotal\":\""+warehouse.getPlaceTotal()+"\",\n" +
                            "\t\"placeLibre\":\""+warehouse.getPlaceLibre()+"\",\n" +
                            "}";

            System.out.println(jsonBody);

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
}
