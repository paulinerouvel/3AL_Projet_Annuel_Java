package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductList {
    // GET all lists of User
    public static JSONArray fetchProductLists(Integer idUser) {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/list/?idUser=" + idUser);

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

            return new JSONArray(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    // GET all lists
    public static JSONArray fetchAllProductLists() {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/list/");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                System.out.println(con.getResponseCode());
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
            System.out.println(content.toString());
            return new JSONArray(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    public static void createList() {
        System.out.println("Create product List TODO"); //TODO

    }

    public static void removeProductList(Integer listId){
        System.out.println("Remove product List TODO");
    } //TODO

    // PUT a list
    public static Integer updateList(models.ProductList list) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/list/");

            String jsonBody =
                    "{\n" +
                            "\t\"id\": "+list.getId()+",\n" +
                            "\t\"libelle\" : \""+list.getLibelle()+"\",\n" +
                            "\t\"date\": \""+list.getDate()+"\",\n" +
                            "\t\"Utilisateur_id\": "+list.getUserId()+",\n" +
                            "\t\"estArchive\":"+list.getEstArchive()+"\n" +
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


    public static Integer affectProductListToWarehouse(List<models.Product> productList, String city) {
        Integer processed = 0;
        for(int i = 0; i < productList.size(); i++){
        //for (models.Product product : productList){
            models.Product product = productList.get(i);

            if(product.getEnRayon().equals(1) && product.getEntrepotwm() == null) {
                Integer affectRes = affectProductToWareHouse(product, city);

                System.out.println("DEBUG - affectProductListToWarehouse : "+affectRes);
                processed += 1;

                if(affectRes > 200){
                    return affectRes;
                }
            }
        }

        return affectProductToReceiver(productList, processed);
    }

    public static Integer affectProductToWareHouse(models.Product product, String city) {
        JSONObject fetchedWareHouse = services.Warehouse.fetchWarehouseByCity(city);

        models.Warehouse cityWarehouse = null;
        if (!fetchedWareHouse.isEmpty()) {
            cityWarehouse = services.Warehouse.jsonToWarehouse(fetchedWareHouse);
        }

        System.out.println("DEBUG - affectProductToWareHouse");
        if(cityWarehouse != null && cityWarehouse.getPlaceLibre() > 0){
            System.out.println("DEBUG - affectProductToWareHouse CITY WAREHOUSE!");
            product.setEntrepotwm(cityWarehouse.getId());
            cityWarehouse.setPlaceLibre(cityWarehouse.getPlaceLibre()-1);
            return services.Warehouse.updateWarehouse(cityWarehouse);
        } else {
            JSONArray wareHouses = services.Warehouse.fetchAllWarehouse();

            for(int i = 0; i < wareHouses.length(); i++){
                models.Warehouse availableWareHouse = services.Warehouse.jsonToWarehouse(wareHouses.getJSONObject(i));
                if (availableWareHouse.getPlaceLibre() > 0){
                    System.out.print("DEBUG - affectProductToWareHouse OTHER WAREHOUSE!");
                    product.setEntrepotwm(availableWareHouse.getId());
                    availableWareHouse.setPlaceLibre(availableWareHouse.getPlaceLibre()-1);
                    return services.Warehouse.updateWarehouse(availableWareHouse);
                }
            }
        }
        return 0;
    }

    public static Integer affectProductToReceiver(List<models.Product> productList, Integer size){
        Integer count = 0;
        Integer productUpdateRes = 600;
        for(int i = 0; i < productList.size(); i++){
            models.Product product = productList.get(i);
            System.out.println("DEBUG - affectProductToReceiver : "+ product);

            if(product.getEnRayon().equals(1) && product.getEntrepotwm() != null) {
                count += 1;
                if (count > size / 2) {
                    product.setDestinataire(3);
                } else {
                    product.setDestinataire(1);
                }
                productUpdateRes = services.Product.updateProduct(product);
            }
        }

        return productUpdateRes;
    }


    public static models.ProductList jsonToProductList(JSONObject productList) {
        System.out.println(productList);
        return new models.ProductList(
            productList.getInt("id"),
            productList.getString("libelle"),
            productList.getString("date"),
            productList.getInt("Utilisateur_id"),
            productList.getInt("estArchive")
        );
    }


}
