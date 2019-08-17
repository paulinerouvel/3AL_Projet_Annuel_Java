package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Product {
        // --- POST --- //

    // POST a new Product in a List
    public static Integer addProductToList(fr.wastemart.maven.javaclient.models.Product product){
        String dateMiseEnRayon = product.getDateMiseEnRayon() == null ? null : "\""+product.getDateMiseEnRayon()+"\"";

        String json =
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

        return Requester.sendPostRequest("product/", json);
    }


        // --- GET --- //

    // GET all the products of a list
    public static JSONArray fetchProducts(Integer idList){
        JSONArray result = Requester.sendGetRequest("list/products?id=" + idList);

        for(int i = 0; i < result.length(); i++) {
            if (result.getJSONObject(i).isNull("enRayon")) {
                result.getJSONObject(i).put("enRayon", "0");
            }
        }

        return result;
    }

    // GET all Products of a Warehouse
    public static JSONArray fetchProductsByWarehouse(Integer idWareHhouse){
        JSONArray result = Requester.sendGetRequest("product/warehouse?id=" + idWareHhouse);

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
    }


        // --- PUT --- //

    // PUT a Product (update)
    public static Integer updateProduct(fr.wastemart.maven.javaclient.models.Product product) {
        String dateMiseEnRayon = product.getDateMiseEnRayon() == null ? null : "\""+product.getDateMiseEnRayon()+"\"";
        String codeBarre = product.getCodeBarre() == null ? null : "\""+product.getCodeBarre()+"\"";

        String json =
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

        return Requester.sendPutRequest("product/", json);
    }


        // --- DELETE ---//

    // DELETE a Product
    public static Integer deleteProduct(Integer productId){
        return Requester.sendDeleteRequest("product/?id=" + productId);
    }

    // DELETE all the Products in a List
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
