package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import static fr.wastemart.maven.javaclient.services.Product.fetchProducts;
import static fr.wastemart.maven.javaclient.services.Product.updateProduct;
import static fr.wastemart.maven.javaclient.services.ProductList.fetchAllProductListsByUserCategory;
import static fr.wastemart.maven.javaclient.services.ProductList.jsonToProductList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;


public class ConsSuggestionListController {
    private UserInstance instance;
    private JSONArray lists;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;

    @FXML
    TableView<ProductList> listsTable;
    @FXML
    TableColumn<Object, Object> listId;
    @FXML
    TableColumn<Object, Object> listName;
    @FXML
    TableColumn<Object, Object> listUser;

    @FXML
    TableView<Product> productsTable;
    @FXML
    TableColumn<Object, Object> productName;
    @FXML
    TableColumn<Object, Object> productDesc;
    @FXML
    TableColumn<Object, Object> productPrice;
    @FXML
    TableColumn<Object, Object> productInitialPrice;
    @FXML
    TableColumn<Object, Object> productDlc;
    @FXML
    TableColumn<Object, Object> productAvailable;
    @FXML
    TableColumn<Object, Object> productDate;
    @FXML
    TableColumn<Object, Object> productQuantity;

    public void init(){

        try {
            displayProductLists();
            displayProducts(lists.getJSONObject(0).getInt("id"));
            listsTable.getSelectionModel().selectFirst();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void displayProductLists() {
        listsTable.getItems().clear();
        //lists = services.ProductList.fetchAllProductLists();
        lists = fetchAllProductListsByUserCategory(3);

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listUser.setCellValueFactory(new PropertyValueFactory<>("userId"));


        for(int i = 0; i < lists.length(); i++){
            JSONObject list = lists.getJSONObject(i);
            ProductList listElement = jsonToProductList(list);

            listsTable.getItems().add(listElement);
        }
    }

    private void displayProducts(Integer id) {
        try {
            productsTable.getItems().clear();
            products = fetchProducts(id);

            productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
            productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
            productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
            productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
            productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));
            productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));


            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);

                Product productElement = new Product(product.getInt("id"),
                        product.getString("libelle"),
                        product.getString("desc"),
                        product.getString("photo"),
                        product.getFloat("prix"),
                        product.getFloat("prixInitial"),
                        product.getInt("quantite"),
                        ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                        product.getString("codeBarre"),
                        product.getInt("enRayon"),
                        product.getString("dateMiseEnRayon"),
                        product.getInt("categorieProduit_id"),
                        product.getInt("listProduct_id"),
                        product.getInt("entrepotwm_id"),
                        product.getInt("destinataire")
                );

                productsTable.getItems().add(productElement);
            }
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        }
    }

    @FXML
    public void validateProduct(ActionEvent event){
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = new Product(product.getInt("id"),
                        product.getString("libelle"),
                        product.getString("desc"),
                        product.getString("photo"),
                        product.getFloat("prix"),
                        product.getFloat("prixInitial"),
                        product.getInt("quantite"),
                        ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                        product.getString("codeBarre"),
                        1,
                        product.getString("dateMiseEnRayon"),
                        product.getInt("categorieProduit_id"),
                        product.getInt("listProduct_id"),
                        product.isNull("entrepotwm_id") ? -1 : product.getInt("entrepotwm_id"),
                        product.isNull("destinataire") ? -1 : product.getInt("destinataire")
                );

                updateProduct(productElement);

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void refuseProduct(ActionEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            try {
                JSONObject product = products.getJSONObject(indexOfProductSelected).put("enRayon", 0);
                Product productElement = new Product(product.getInt("id"),
                        product.getString("libelle"),
                        product.getString("desc"),
                        product.getString("photo"),
                        product.getFloat("prix"),
                        product.getFloat("prixInitial"),
                        product.getInt("quantite"),
                        ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                        product.getString("codeBarre"),
                        0,
                        product.getString("dateMiseEnRayon"),
                        product.getInt("categorieProduit_id"),
                        product.getInt("listProduct_id"),
                        product.isNull("entrepotwm_id") ? -1 : product.getInt("entrepotwm_id"),
                        product.isNull("destinataire") ? -1 : product.getInt("destinataire")
                );

                updateProduct(productElement);

                displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

            } catch (Exception e) {
                e.printStackTrace();
            }        }
    }

    public void submitList(ActionEvent event) {
        refreshSelectedIndices();

        if (indexOfListSelected != -1 && lists.getJSONObject(indexOfListSelected).getInt("estArchive") != 1){

            JSONObject list = lists.getJSONObject(indexOfListSelected);
            ProductList listElement = fr.wastemart.maven.javaclient.services.ProductList.jsonToProductList(list);

            ArrayList<Product> productList = new ArrayList<Product>();
            for(int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                productList.add(fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product));
            }

            // Affecte la liste de produits à un entrepot
            Integer affectProductListToWarehouseRes = fr.wastemart.maven.javaclient.services.ProductList.affectProductListToWarehouse(productList, instance.getUser().getVille());

            if(affectProductListToWarehouseRes != 0 && affectProductListToWarehouseRes < 299){ // Erreur
                listElement.setEstArchive(1);
                listElement.setDate(LocalDate.now());
                fr.wastemart.maven.javaclient.services.ProductList.updateProductList(listElement);
            }
        }
        displayProductLists();
    }

    public void displayAddProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManageProduct.fxml"));

        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            // TODO: handle error
            return;
        }

        ManageProductController controller = loader.getController();
        controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Add", null);

        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        Stage inputStage = new Stage();
        inputStage.initOwner(stageNodeRoot);
        inputStage.setScene(newScene);
        inputStage.showAndWait();

        displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
    }

    public void displayModifyProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfProductSelected != -1) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ManageProduct.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                // TODO: handle error
                return;
            }

            ManageProductController controller = loader.getController();
            JSONObject product = products.getJSONObject(indexOfProductSelected);

            Product productToModify = new Product(product.getInt("id"),
                    product.getString("libelle"),
                    product.getString("desc"),
                    product.getString("photo"),
                    product.getFloat("prix"),
                    product.getFloat("prixInitial"),
                    product.getInt("quantite"),
                    ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                    product.getString("codeBarre"),
                    product.getInt("enRayon"),
                    product.getString("dateMiseEnRayon"),
                    product.getInt("categorieProduit_id"),
                    product.getInt("listProduct_id"),
                    product.getInt("entrepotwm_id"),
                    product.getInt("destinataire")
            );

            controller.init(lists.getJSONObject(indexOfListSelected).getInt("id"), "Modify", productToModify);

            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Stage inputStage = new Stage();
            inputStage.initOwner(stageNodeRoot);
            inputStage.setScene(newScene);
            inputStage.showAndWait();
        }

        displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));

    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = listsTable.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}
