package fr.wastemart.maven.javaclient.services;

import fr.wastemart.maven.javaclient.models.User;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Product {

    public static JSONArray fetchProducts(Integer idList){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/list/products?id=" + idList);

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
                if (result.getJSONObject(i).isNull("enRayon")) {
                    result.getJSONObject(i).put("enRayon", "0");
                }

            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    public static JSONArray fetchProductsByWarehouse(Integer idWareHhouse){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/warehouse?id=" + idWareHhouse);
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

            System.out.println("result2"+ result);

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }


    public static Integer deleteProduct(Integer productId){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/?id=" + productId);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                return con.getResponseCode();
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

            return con.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
            return 503;
        }
    }


    public static Integer deleteProductsInList(Integer listId) {
        JSONArray products = fr.wastemart.maven.javaclient.services.Product.fetchProducts(listId);

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Integer deleteProductRes = deleteProduct(product.getInt("id"));
            if(deleteProductRes > 299){
                return 0;
            }
        }
        return 1;
    }


    public static Integer addProductToList(fr.wastemart.maven.javaclient.models.Product product){
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        URL url = null;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/");

            String dateMiseEnRayon = product.getDateMiseEnRayon() == null
                    ? null : "\""+product.getDateMiseEnRayon()+"\"";

            String jsonBody =
                    "{\n" +
                            "\t\"libelle\" : \""+product.getLibelle()+"\",\n" +
                            "\t\"desc\": \""+product.getDesc()+"\",\n" +
                            "\t\"photo\": \""+product.getPhoto()+"\",\n" +
                            "\t\"prix\":"+product.getPrix()+",\n" +
                            "\t\"prixInitial\":"+product.getPrixInitial()+",\n" +
                            "\t\"quantite\":"+product.getQuantite()+",\n" +
                            "\t\"dlc\":\""+product.getDlc()+"\",\n" +
                            "\t\"codeBarre\":\""+product.getCodeBarre()+"\",\n" +
                            "\t\"enRayon\":"+product.getEnRayon()+",\n" +
                            "\t\"dateMiseEnRayon\":"+dateMiseEnRayon+",\n" +
                            "\t\"categorieProduit_id\":"+product.getCategorieProduit()+",\n" +
                            "\t\"listProduct_id\":"+product.getListProduct()+",\n" +
                            "\t\"entrepotwm_id\":"+product.getEntrepotwm()+",\n" +
                            "\t\"destinataire\":"+product.getDestinataire()+"\n" +
                            "}";

            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            ((HttpURLConnection) con).setRequestMethod("POST");

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


    public static Integer updateProduct(fr.wastemart.maven.javaclient.models.Product product) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/product/");

            String dateMiseEnRayon = product.getDateMiseEnRayon() == null ? null : "\""+product.getDateMiseEnRayon()+"\"";
            String codeBarre = product.getCodeBarre() == null ? null : "\""+product.getCodeBarre()+"\"";

            String jsonBody =
                    "{\n" +
                            "\t\"id\": "+product.getId()+",\n" +
                            "\t\"libelle\" : \""+product.getLibelle()+"\",\n" +
                            "\t\"desc\": \""+product.getDesc()+"\",\n" +
                            "\t\"photo\": \""+product.getPhoto()+"\",\n" +
                            "\t\"prix\":"+product.getPrix()+",\n" +
                            "\t\"prixInitial\":"+product.getPrixInitial()+",\n" +
                            "\t\"quantite\":"+product.getQuantite()+",\n" +
                            "\t\"dlc\":\""+product.getDlc()+"\",\n" +
                            "\t\"codeBarre\":"+codeBarre+",\n" +
                            "\t\"enRayon\":"+product.getEnRayon()+",\n" +
                            "\t\"dateMiseEnRayon\":"+dateMiseEnRayon+",\n" +
                            "\t\"categorieProduit_id\":"+product.getCategorieProduit()+",\n" +
                            "\t\"listProduct_id\":"+product.getListProduct()+",\n" +
                            "\t\"entrepotwm_id\":"+product.getEntrepotwm()+",\n" +
                            "\t\"destinataire\":"+product.getDestinataire()+"\n" +
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
    public static fr.wastemart.maven.javaclient.models.Product jsonToProduct(JSONObject product) {
        return new fr.wastemart.maven.javaclient.models.Product(
                product.getInt("id"),
                product.getString("libelle"),
                product.getString("desc"),
                product.getString("photo"),
                product.getFloat("prix"),
                product.getFloat("prixInitial"),
                product.getInt("quantite"),
                product.isNull("dlc") ? null : ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                product.isNull("codeBarre") ? null : product.getString("codeBarre"),
                product.isNull("enRayon") ? 0 : product.getInt("enRayon"),
                product.isNull("dateMiseEnRayon") ? "" : product.getString("dateMiseEnRayon"),
                product.getInt("categorieProduit_id"),
                product.isNull("listProduct_id") ? null : product.getInt("listProduct_id"),
                product.isNull("entrepotwm_id") ? null : product.getInt("entrepotwm_id"),
                product.isNull("destinataire") ? null : product.getInt("destinataire")
        );
    }


}
