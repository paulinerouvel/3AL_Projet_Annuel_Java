package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Product;
import models.ProductList;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserInstance;

import java.time.LocalDate;


public class ProductListController {
    private UserInstance instance;
    private JSONArray lists;
    private JSONArray products;

    @FXML
    TableView<ProductList> listsTable;
    @FXML
    TableColumn<Object, Object> productLists;
    @FXML
    TableColumn<Object, Object> numberInProductLists;

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Object, Object> productName;
    @FXML
    TableColumn<Object, Object> productDesc;
    @FXML
    TableColumn<Object, Object> productPrice;
    @FXML
    TableColumn<Object, Object> productDlc;
    @FXML
    TableColumn<Object, Object> productAvailable;
    @FXML
    TableColumn<Object, Object> productDate;

    public void init(){
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
    }

    private void displayProductLists() {
        lists = services.ProductList.fetchProductLists(instance.getUser().getId());

        System.out.println("Listdata =" +lists);

        productLists.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        numberInProductLists.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

        for(int i = 0; i < lists.length(); i++){
            JSONObject list = lists.getJSONObject(i);
            ProductList listElement = new ProductList(list.getInt("id"),
                    list.getString("libelle"),
                    LocalDate.now());
            listsTable.getItems().add(listElement);
        }
    }

    private void displayProducts(Integer id) {
        products = services.Product.fetchProducts(id);

        System.out.println("Productdata =" +products);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);

                Product productElement = new Product(product.getInt("id"),
                        product.getString("libelle"),
                        product.getString("desc"),
                        product.getString("photo"),
                        product.getFloat("prix"),
                        product.getFloat("prixInitial"),
                        product.getInt("quantite"),
                        product.getString("dlc"),
                        product.getString("codeBarre"),
                        product.getInt("enRayon"),
                        product.getString("dateMiseEnRayon"));

            productsTable.getItems().add(productElement);
        }

        /*for (Object productElement : products) {

            JsonElement product = new JsonParser().parse(productElement.toString());
            Product list = new Product(
                product.getAsJsonObject().get("id").getAsString().length() < 0 ? null : product.getAsJsonObject().get("id").getAsInt(),
                product.getAsJsonObject().get("libelle").getAsString().length() < 0 ? null : product.getAsJsonObject().get("libelle").getAsString(),
                product.getAsJsonObject().get("desc").getAsString().length() < 0 ? null : product.getAsJsonObject().get("desc").getAsString(),
                product.getAsJsonObject().get("photo").getAsString().length() < 0 ? null : product.getAsJsonObject().get("photo").getAsString(),
                product.getAsJsonObject().get("prix").getAsString().length() < 0 ? null : product.getAsJsonObject().get("prix").getAsFloat(),
                product.getAsJsonObject().get("prixInitial").getAsString().length() < 0 ? null : product.getAsJsonObject().get("prixInitial").getAsFloat(),
                product.getAsJsonObject().get("quantite").getAsString().length() < 0 ? null : product.getAsJsonObject().get("quantite").getAsInt(),
                product.getAsJsonObject().get("dlc").getAsString().length() < 0 ? null : product.getAsJsonObject().get("dlc").getAsString(),
                product.getAsJsonObject().get("").getAsString().length() < 0 ? null : product.getAsJsonObject().get("prix").getAsString(),
                product.getAsJsonObject().get("enRayon").getAsString().length() < 0 ? null : product.getAsJsonObject().get("enRayon").getAsInt(),
                product.getAsJsonObject().get("dateMiseEnRayon").getAsString().length() < 0 ? null : product.getAsJsonObject().get("dateMiseEnRayon").getAsString());

            productsTable.getItems().add(list);
        }*/
    }

    @FXML
    public void clickItem(MouseEvent event)
    {
        displayProducts(listsTable.getSelectionModel().getSelectedIndex());
            /*
            System.out.println(tableID.getSelectionModel().getSelectedItem().getBeer());
            System.out.println(productsTable.getSelectionModel().getSelectedItem().getBrewery());
            System.out.println(listsTable.getSelectionModel().getSelectedItem().getCountry());
            */

    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }
}
