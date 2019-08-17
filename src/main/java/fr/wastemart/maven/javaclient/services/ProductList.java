package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.List;

public class ProductList {
        // --- POST --- //

    // POST a new list
    public static Integer createProductList(fr.wastemart.maven.javaclient.models.ProductList productList) {
        String json = "{\n" +
                "\t\"libelle\" : \""+productList.getLibelle()+"\",\n" +
                "\t\"date\":\""+productList.getDate()+"\",\n" +
                "\t\"Utilisateur_id\":"+productList.getUserId()+",\n" +
                "\t\"estArchive\":"+productList.getEstArchive()+"\n" +
                "}";

        return Requester.sendPostRequest("list/", json);
    }


        // --- GET --- //

    // GET all lists of User
    public static JSONArray fetchProductLists(Integer idUser) {
        return Requester.sendGetRequest("list/?idUser=" + idUser);
    }

    // GET all lists
    public static JSONArray fetchAllProductLists() {
        return Requester.sendGetRequest("list/");
    }

    // GET all lists by user category
    public static JSONArray fetchAllProductListsByUserCategory(Integer idUserCategory) {
        return Requester.sendGetRequest("list/?idUserCategory="+idUserCategory);
    }


        // --- PUT --- //

    // PUT a list (update)
    public static Integer updateProductList(fr.wastemart.maven.javaclient.models.ProductList list) {
        String json = "{\n" +
                "\t\"id\": "+list.getId()+",\n" +
                "\t\"libelle\" : \""+list.getLibelle()+"\",\n" +
                "\t\"date\": \""+list.getDate()+"\",\n" +
                "\t\"Utilisateur_id\": "+list.getUserId()+",\n" +
                "\t\"estArchive\":"+list.getEstArchive()+"\n" +
                "}";

        return Requester.sendPutRequest("list/", json);
    }


        // --- DELETE ---//

    // DELETE a list of products
    public static Integer removeProductsList(Integer listId){
        Integer deleteProductsInList = fr.wastemart.maven.javaclient.services.Product.deleteProductsInList(listId);
        if(deleteProductsInList == 1){
            return Requester.sendDeleteRequest("list/?id=" + listId);
        }
        return 0;
    }


    // Affects a list of product to a Warehouse
    public static Integer affectProductListToWarehouse(List<fr.wastemart.maven.javaclient.models.Product> productList, String city) {
        Integer processed = 0;
        for (fr.wastemart.maven.javaclient.models.Product product : productList) {
            if (product.getEnRayon().equals(1) && product.getEntrepotwm() == null) {
                Integer affectRes = affectProductToWareHouse(product, city);

                processed += 1;

                if (affectRes > 200) {
                    return affectRes;
                }
            }
        }

        return affectProductToReceiver(productList, processed);
    }

    // Affect one product to a Warehouse
    public static Integer affectProductToWareHouse(fr.wastemart.maven.javaclient.models.Product product, String city) {
        JSONObject fetchedWareHouse = fr.wastemart.maven.javaclient.services.Warehouse.fetchWarehouseByCity(city);

        fr.wastemart.maven.javaclient.models.Warehouse cityWarehouse = null;
        if (!fetchedWareHouse.isEmpty()) {
            cityWarehouse = fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse(fetchedWareHouse);
        }

        if(cityWarehouse != null && cityWarehouse.getPlaceLibre() > 0){
            product.setEntrepotwm(cityWarehouse.getId());
            cityWarehouse.setPlaceLibre(cityWarehouse.getPlaceLibre()-1);
            return fr.wastemart.maven.javaclient.services.Warehouse.updateWarehouse(cityWarehouse);
        } else {
            JSONArray wareHouses = fr.wastemart.maven.javaclient.services.Warehouse.fetchAllWarehouse();

            for(int i = 0; i < wareHouses.length(); i++){
                fr.wastemart.maven.javaclient.models.Warehouse availableWareHouse = fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse(wareHouses.getJSONObject(i));
                if (availableWareHouse.getPlaceLibre() > 0){
                    product.setEntrepotwm(availableWareHouse.getId());
                    availableWareHouse.setPlaceLibre(availableWareHouse.getPlaceLibre()-1);
                    return fr.wastemart.maven.javaclient.services.Warehouse.updateWarehouse(availableWareHouse);
                }
            }
        }
        return 0;
    }

    // Affect one product to a Receiver
    public static Integer affectProductToReceiver(List<fr.wastemart.maven.javaclient.models.Product> productList, Integer size){
        Integer count = 0;
        Integer productUpdateRes = 600;
        for (fr.wastemart.maven.javaclient.models.Product product : productList) {
            if (product.getEnRayon().equals(1) && product.getEntrepotwm() != null) {
                count += 1;
                if (count > size / 2) {
                    product.setDestinataire(3);
                } else {
                    product.setDestinataire(1);
                }
                productUpdateRes = Product.updateProduct(product);
            }
        }

        return productUpdateRes;
    }

    public static fr.wastemart.maven.javaclient.models.ProductList jsonToProductList(JSONObject productList) {
        return new fr.wastemart.maven.javaclient.models.ProductList(
            productList.getInt("id"),
            productList.getString("libelle"),
            productList.isNull("date") ? null : ZonedDateTime.parse(productList.getString("date")).toLocalDate(),
            productList.getInt("Utilisateur_id"),
            productList.getInt("estArchive")
        );
    }


}
