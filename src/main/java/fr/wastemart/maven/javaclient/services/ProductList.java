package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.List;

public class ProductList {
        // --- POST --- //

    // POST a new list
    public static boolean createProductList(fr.wastemart.maven.javaclient.models.ProductList productList, String token) {
        Integer result = 299;

        String json = "{\n" +
                "\t\"libelle\" : \""+productList.getLibelle()+"\",\n" +
                "\t\"date\":\""+productList.getDate()+"\",\n" +
                "\t\"Utilisateur_id\":"+productList.getUserId()+",\n" +
                "\t\"estArchive\":"+productList.getEstArchive()+"\n" +
                "}";

        try {
            result = Requester.sendPostRequest("list/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


        // --- GET --- //

    // GET all lists of User
    public static JSONArray fetchProductLists(Integer idUser, String token) throws Exception {
        JSONArray result;

        HttpResponse response = Requester.sendGetRequest("list/?idUser=" + idUser, token);
        result = response.getDataAsJSONArray();

        return result;
    }

    // GET all lists
    public static JSONArray fetchAllProductLists(String token) throws Exception {
        JSONArray result;

        HttpResponse response = Requester.sendGetRequest("list/", token);
        result = response.getDataAsJSONArray();

        return result;
    }

    // GET all lists by user category
    public static JSONArray fetchAllProductListsByUserCategory(Integer idUserCategory, String token) throws Exception {
        JSONArray result;


        HttpResponse response = Requester.sendGetRequest("list/?idUserCategory="+idUserCategory, token);
        result = response.getDataAsJSONArray();

        return result;
    }

    // GET all the products in a list
    public static JSONArray fetchProducts(Integer idList, String token) throws Exception {
        JSONArray result;

        HttpResponse response = Requester.sendGetRequest("list/products?id=" + idList, token);
        result = response.getDataAsJSONArray();

        for(int i = 0; i < result.length(); i++) {
            if (result.getJSONObject(i).isNull("enRayon")) {
                result.getJSONObject(i).put("enRayon", "0");
            }
        }

        return result;
    }


        // --- PUT --- //

    // PUT a list (update)
    public static boolean updateProductList(fr.wastemart.maven.javaclient.models.ProductList list, String token) {
        Integer result = 299;

        String json = "{\n" +
                "\t\"id\": "+list.getId()+",\n" +
                "\t\"libelle\" : \""+list.getLibelle()+"\",\n" +
                "\t\"date\": \""+list.getDate()+"\",\n" +
                "\t\"Utilisateur_id\": "+list.getUserId()+",\n" +
                "\t\"estArchive\":"+list.getEstArchive()+"\n" +
                "}";

        try {
            result = Requester.sendPutRequest("list/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


        // --- DELETE ---//

    // DELETE a list of products
    public static boolean removeProductsList(Integer listId, String token) {
        Integer result = 299;

        try {
            if(Product.deleteProductsInList(listId, token)){
                    result = Requester.sendDeleteRequest("list/?id=" + listId, token).getResponseCode();
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


    // Affects a list of product to a Warehouse
    public static boolean affectProductListToWarehouse(List<fr.wastemart.maven.javaclient.models.Product> productList, String city, String token) {
        try {
            Integer processed = 0;
            for (fr.wastemart.maven.javaclient.models.Product product : productList) {
                if (product.getEnRayon().equals(true) && product.getEntrepotwm() == null) {

                    boolean affectRes = affectProductToWareHouse(product, city, token);

                    processed += 1;

                    if (!affectRes) {
                        return false;
                    }
                }
            }

            return affectProductListToReceiver(productList, processed, token);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return false;
        }
    }

    // Affect one product to a Warehouse
    public static boolean affectProductToWareHouse(fr.wastemart.maven.javaclient.models.Product product, String city, String token) throws Exception {
        JSONObject fetchedWareHouse = fr.wastemart.maven.javaclient.services.Warehouse.fetchWarehouseByCity(city);

        fr.wastemart.maven.javaclient.models.Warehouse cityWarehouse = null;
        if (!fetchedWareHouse.isEmpty()) {
            cityWarehouse = fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse(fetchedWareHouse);
        }

        if(cityWarehouse != null && cityWarehouse.getPlaceLibre() > 0){
            product.setEntrepotwm(cityWarehouse.getId());
            cityWarehouse.setPlaceLibre(cityWarehouse.getPlaceLibre()-1);
            return fr.wastemart.maven.javaclient.services.Warehouse.updateWarehouse(cityWarehouse, token);
        } else {
            JSONArray wareHouses = fr.wastemart.maven.javaclient.services.Warehouse.fetchAllWarehouse();

            for(int i = 0; i < wareHouses.length(); i++){
                fr.wastemart.maven.javaclient.models.Warehouse availableWareHouse = fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse(wareHouses.getJSONObject(i));
                if (availableWareHouse.getPlaceLibre() > 0){
                    product.setEntrepotwm(availableWareHouse.getId());
                    availableWareHouse.setPlaceLibre(availableWareHouse.getPlaceLibre()-1);
                    return fr.wastemart.maven.javaclient.services.Warehouse.updateWarehouse(availableWareHouse, token);
                }
            }
        }
        return false;
    }

    // Affect a list of product to a Receiver
    public static boolean affectProductListToReceiver(List<fr.wastemart.maven.javaclient.models.Product> productList, Integer size, String token) throws Exception {
        Integer count = 0;
        Boolean productUpdateRes = false;
        for (fr.wastemart.maven.javaclient.models.Product product : productList) {
            if (product.getEnRayon().equals(1) && product.getEntrepotwm() != null) {
                count += 1;
                if (count > size / 2) {
                    product.setDestinataire(3);
                } else {
                    product.setDestinataire(1);
                }
                productUpdateRes = Product.updateProduct(product, token);
            }
        }

        return productUpdateRes;
    }

    public static fr.wastemart.maven.javaclient.models.ProductList jsonToProductList(JSONObject productList) {
        try {
            return new fr.wastemart.maven.javaclient.models.ProductList(
                productList.getInt("id"),
                productList.getString("libelle"),
                productList.isNull("date") ? null : ZonedDateTime.parse(productList.getString("date")).toLocalDate(),
                productList.getInt("Utilisateur_id"),
                productList.getInt("estArchive")
            );
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return null;
        }
    }


}
