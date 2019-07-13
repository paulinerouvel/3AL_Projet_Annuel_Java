package controllers;

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
import models.Product;
import models.ProductList;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserInstance;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static services.Product.deleteProduct;


public class ProductListController {
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

    public void init(){
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
        listsTable.getSelectionModel().selectFirst();
    }

    private void displayProductLists() {
        listsTable.getItems().clear();
        lists = services.ProductList.fetchProductLists(instance.getUser().getId());


        System.out.println("Listdata =" +lists);

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        for(int i = 0; i < lists.length(); i++){
            JSONObject list = lists.getJSONObject(i);
            ProductList listElement = new ProductList(list.getInt("id"),
                    list.getString("libelle"),
                    list.getString("date"),
                    instance.getUser().getId(),
                    list.getInt("estArchive"));
            listsTable.getItems().add(listElement);
        }
    }

    private void displayProducts(Integer id) {
        productsTable.getItems().clear();
        products = services.Product.fetchProducts(id);

        System.out.println("Productdata =" +products);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = services.Product.jsonToProduct(product);

            productsTable.getItems().add(productElement);
        }
    }

    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
        }
    }

    public void removeList(ActionEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            services.ProductList.removeProductList(indexOfListSelected);
        }
    }

    public void removeProduct(ActionEvent event) {
        refreshSelectedIndices();

        if (indexOfProductSelected != -1){
            deleteProduct(products.getJSONObject(indexOfProductSelected).getInt("id"));
        }

        displayProducts(lists.getJSONObject(0).getInt("id"));
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }

    public void displayAddProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
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

        }

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
            Product productToModify = services.Product.jsonToProduct(product);

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

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}
