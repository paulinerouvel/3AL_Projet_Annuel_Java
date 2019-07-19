package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

public class Order {

    public static JSONArray fetchOrder(){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/order");

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

            return result;

        } catch (IOException e) {
            System.out.println("Error fetching order");
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    public static JSONArray fetchProductsByOrder(Integer idOrder){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/warehouse?idOrder=" + idOrder);
            // /list/products?id=1

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            System.out.println(con.getResponseCode());

            in.close();
            con.disconnect();

            JSONArray result = new JSONArray(content.toString());
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

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    public static fr.wastemart.maven.javaclient.models.Order jsonToOrder(JSONObject order) {
        return new fr.wastemart.maven.javaclient.models.Order(
                order.getInt("id"),
                ZonedDateTime.parse(order.getString("date")).toLocalDate(),
                order.getInt("utilisateurID")
        );
    }
}
