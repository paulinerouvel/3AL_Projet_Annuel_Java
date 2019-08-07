package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.ProductList;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static fr.wastemart.maven.javaclient.services.Product.deleteProduct;
import static fr.wastemart.maven.javaclient.services.ProductList.jsonToProductList;


public class ProductListController extends GenericController {
    private UserInstance instance;
    private JSONArray lists;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;

    @FXML CheckBox listArchiveCheckBox;
    @FXML
    TableView<ProductList> listsTable;
    @FXML
    TableColumn<Object, Object> listId;
    @FXML
    TableColumn<Object, Object> listName;
    @FXML
    TableColumn<Object, Object> listEstArchive;

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
        listArchiveCheckBox.setSelected(false);
        displayProductLists();
        displayProducts(lists.getJSONObject(0).getInt("id"));
        listsTable.getSelectionModel().selectFirst();
    }

    @FXML
    private void displayProductLists() {
        listsTable.getItems().clear();
        lists = fr.wastemart.maven.javaclient.services.ProductList.fetchProductLists(instance.getUser().getId());

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listEstArchive.setCellValueFactory(new PropertyValueFactory<>("estArchive"));


        for(int i = 0; i < lists.length(); i++){
            if(listArchiveCheckBox.isSelected() || lists.getJSONObject(i).getInt("estArchive") != 1){
                JSONObject list = lists.getJSONObject(i).put("Utilisateur_id", instance.getUser().getId());
                ProductList listElement = jsonToProductList(list);
                listsTable.getItems().add(listElement);
            }

        }
    }

    private void displayProducts(Integer id) {
        productsTable.getItems().clear();
        products = fr.wastemart.maven.javaclient.services.Product.fetchProducts(id);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

        for(int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

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
            Integer listToRemoveId = lists.getJSONObject(indexOfListSelected).getInt("id");
            Integer removeProductListRes = fr.wastemart.maven.javaclient.services.ProductList.removeProductsList(listToRemoveId);
        }

        displayProducts(lists.getJSONObject(0).getInt("id"));
        displayProductLists();

    }

    public void createList(ActionEvent event) {
        refreshSelectedIndices();
        ProductList productList = new ProductList(-1,
                "Test",
                LocalDate.now(),
                instance.getUser().getId(),
                0);

        fr.wastemart.maven.javaclient.services.ProductList.createProductList(productList);
        displayProductLists();
    }

    public void removeProduct(ActionEvent event) {
        refreshSelectedIndices();

        if (indexOfProductSelected != -1){
            deleteProduct(products.getJSONObject(indexOfProductSelected).getInt("id"));
        }

        displayProducts(lists.getJSONObject(indexOfListSelected).getInt("id"));
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }

    public void displayAddProduct(ActionEvent actionEvent) throws Exception {
        refreshSelectedIndices();

        if(indexOfListSelected != -1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageProduct.fxml"));

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr.wastemart.maven.javaclient/views/ManageProduct.fxml"));

            Scene newScene;
            try {
                newScene = new Scene(loader.load());
            } catch (IOException ex) {
                // TODO: handle error
                return;
            }

            ManageProductController controller = loader.getController();
            JSONObject product = products.getJSONObject(indexOfProductSelected);
            Product productToModify = fr.wastemart.maven.javaclient.services.Product.jsonToProduct(product);

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
