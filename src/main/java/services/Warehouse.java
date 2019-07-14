package services;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Warehouse {

    public static JSONArray fetchWarehouse(){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/warehouse");
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

}
