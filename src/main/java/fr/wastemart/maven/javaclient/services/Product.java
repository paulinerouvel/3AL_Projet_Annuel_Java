package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Product {
        // --- POST --- //

    // POST a new Product in a List
    public static boolean addProductToList(fr.wastemart.maven.javaclient.models.Product product, String token) {
        Integer result = 299;

        String photo = product.getPhoto() == null ? null : "\""+product.getPhoto()+"\"";
        String dlc = product.getDlc() == null ? null : "\""+product.getDlc()+"\"";
        String codeBarre = product.getCodeBarre() == null ? null : "\""+product.getCodeBarre()+"\"";
        String dateMiseEnRayon = product.getDateMiseEnRayon() == null ? null : "\""+product.getDateMiseEnRayon()+"\"";
        String listProduct = product.getListProduct() == null ? null : "\""+product.getListProduct()+"\"";
        String entrepotwm = product.getEntrepotwm() == null ? null : "\""+product.getEntrepotwm()+"\"";
        String destinataire = product.getDestinataire() == null ? null : "\""+product.getDestinataire()+"\"";


        String json =
                "{\n" +
                        "\t\"id\": "+product.getId()+",\n" +
                        "\t\"libelle\" : \""+product.getLibelle()+"\",\n" +
                        "\t\"desc\": \""+product.getDesc()+"\",\n" +
                        "\t\"photo\": "+photo+",\n" +
                        "\t\"prix\":"+product.getPrix()+",\n" +
                        "\t\"prixInitial\":1,\n" +
                        "\t\"quantite\":"+product.getQuantite()+",\n" +
                        "\t\"dlc\":"+dlc+",\n" +
                        "\t\"codeBarre\":"+codeBarre+",\n" +
                        "\t\"enRayon\":"+product.getEnRayon()+",\n" +
                        "\t\"dateMiseEnRayon\":"+dateMiseEnRayon+",\n" +
                        "\t\"categorieProduit_id\":"+product.getCategorieProduit()+",\n" +
                        "\t\"listProduct_id\":"+listProduct+",\n" +
                        "\t\"entrepotwm_id\":"+entrepotwm+",\n" +
                        "\t\"destinataire\":"+destinataire+"\n" +
                        "}";
        try {
            result = Requester.sendPostRequest("product/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


        // --- GET --- //

    // GET all Products
    public static JSONArray fetchAllProducts() {
        JSONArray result = null;

        try{
            HttpResponse response = Requester.sendGetRequest("product", null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET all Products of a Warehouse
    public static JSONArray fetchProductsByWarehouse(Integer idWareHhouse) {
        JSONArray result = null;

        try{
            HttpResponse response = Requester.sendGetRequest("product/warehouse?id=" + idWareHhouse, null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET all Products of an Order
    public static JSONArray fetchProductsByOrder(Integer idOrder) {
        JSONArray result = null;

        try{
            HttpResponse response = Requester.sendGetRequest("product/warehouse?idOrder="+idOrder, null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    public static JSONArray fetchProductCategories() {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("product/category", null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

        // --- PUT --- //

    // PUT a Product (update)
    public static boolean updateProduct(fr.wastemart.maven.javaclient.models.Product product, String token) {
        Integer result = 299;
        String photo = product.getPhoto() == null ? null : "\""+product.getPhoto()+"\"";
        String dlc = product.getDlc() == null ? null : "\""+product.getDlc()+"\"";
        String codeBarre = product.getCodeBarre() == null ? null : "\""+product.getCodeBarre()+"\"";
        String dateMiseEnRayon = product.getDateMiseEnRayon() == null ? null : "\""+product.getDateMiseEnRayon()+"\"";
        String listProduct = product.getListProduct() == null ? null : "\""+product.getListProduct()+"\"";
        String entrepotwm = product.getEntrepotwm() == null ? null : "\""+product.getEntrepotwm()+"\"";
        String destinataire = product.getDestinataire() == null ? null : "\""+product.getDestinataire()+"\"";


        String json =
                "{\n" +
                        "\t\"id\": "+product.getId()+",\n" +
                        "\t\"libelle\" : \""+product.getLibelle()+"\",\n" +
                        "\t\"desc\": \""+product.getDesc()+"\",\n" +
                        "\t\"photo\": "+photo+",\n" +
                        "\t\"prix\":"+product.getPrix()+",\n" +
                        "\t\"prixInitial\":1,\n" +
                        "\t\"quantite\":"+product.getQuantite()+",\n" +
                        "\t\"dlc\":"+dlc+",\n" +
                        "\t\"codeBarre\":"+codeBarre+",\n" +
                        "\t\"enRayon\":"+product.getEnRayon()+",\n" +
                        "\t\"dateMiseEnRayon\":"+dateMiseEnRayon+",\n" +
                        "\t\"categorieProduit_id\":"+product.getCategorieProduit()+",\n" +
                        "\t\"listProduct_id\":"+listProduct+",\n" +
                        "\t\"entrepotwm_id\":"+entrepotwm+",\n" +
                        "\t\"destinataire\":"+destinataire+"\n" +
                        "}";


        try {
            result = Requester.sendPutRequest("product/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }
        return result < 299;


    }


    // --- DELETE ---//

    // DELETE a Product
    public static boolean deleteProduct(Integer productId, String token) {
        Integer result = 299;

        try {
            result = Requester.sendDeleteRequest("product/?id=" + productId, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }

    // DELETE all the Products in a List
    static boolean deleteProductsInList(Integer listId, String token) {
        JSONArray products = null;
        try {
            products = ProductList.fetchProducts(listId, token);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            if(!deleteProduct(product.getInt("id"), token)){
                return false;
            }

        }

        return true;
    }


    public static fr.wastemart.maven.javaclient.models.Product jsonToProduct(JSONObject product) {
        try {
            return new fr.wastemart.maven.javaclient.models.Product(
                    product.getInt("id"),
                    product.getString("libelle"),
                    product.getString("desc"),
                    product.getString("photo"),
                    product.getFloat("prix"),
                    product.getInt("quantite"),
                    product.isNull("dlc") ? null : DateFormatter.dateToString(product.getString("dlc")),
                    product.isNull("codeBarre") ? null : product.getString("codeBarre"),
                    product.getInt("enRayon") == 1,
                    product.isNull("dateMiseEnRayon") ? null : DateFormatter.dateToString(product.getString("dateMiseEnRayon")),
                    product.getInt("categorieProduit_id"),
                    product.isNull("listProduct_id") ? null : product.getInt("listProduct_id"),
                    product.isNull("entrepotwm_id") ? null : product.getInt("entrepotwm_id"),
                    product.isNull("destinataire") ? null : product.getInt("destinataire")
            );
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return null;
        }
    }


}
